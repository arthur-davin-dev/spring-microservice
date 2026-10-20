package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.dto.CustomerStatistics;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.persistence.document.CustomerDocument;
import kh.edu.istad.platform.customer.persistence.mapper.CustomerPersistenceMapper;
import kh.edu.istad.platform.customer.persistence.repository.CustomerMongoRepository;
import kh.edu.istad.platform.customer.persistence.document.SegmentDocument;
import kh.edu.istad.platform.customer.persistence.repository.FilteringFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.bson.Document;
import com.mongodb.DBRef;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import java.time.Instant;
import java.util.Date;

@RequiredArgsConstructor
@Repository
public class CustomerReactiveRepositoryAdapter implements CustomerRepository {
    private final CustomerMongoRepository customerMongoRepository;
    private final CustomerPersistenceMapper customerPersistenceMapper;
    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Mono<Customer> save(Customer customer) {
        CustomerDocument document = customerPersistenceMapper.toDocument(customer);
        Mono<CustomerDocument> save = customerMongoRepository.findById(document.getId())
                .flatMap(existing -> {
                    // Preserve creation audit fields so Persistable.isNew() selects an update.
                    document.setCreatedAt(existing.getCreatedAt());
                    document.setCreatedBy(existing.getCreatedBy());
                    return customerMongoRepository.save(document);
                })
                .switchIfEmpty(Mono.defer(() -> customerMongoRepository.insert(document)))
                ;
        if (document.getSegment() != null) {
            return mongoTemplate.save(document.getSegment(), "customer_segments")
                    .then(save)
                    .map(customerPersistenceMapper::toDomain);
        }
        return save.map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Mono<Customer> findById(CustomerId id) {
        return mongoTemplate.findById(id.value().toString(), Document.class,
                        mongoTemplate.getCollectionName(CustomerDocument.class))
                .flatMap(this::readCustomer);
    }

    @Override
    public Flux<Customer> findAll() {
        return mongoTemplate.findAll(Document.class, mongoTemplate.getCollectionName(CustomerDocument.class))
                .flatMap(this::readCustomer);
    }

    @Override
    public Mono<Void> deleteById(CustomerId id) {
        return customerMongoRepository.deleteById(id.value().toString());
    }

    @Override
    public Mono<Customer> findByUsername(String username) {
        return mongoTemplate.findOne(new Query(Criteria.where("username").is(username)), Document.class,
                        mongoTemplate.getCollectionName(CustomerDocument.class))
                .flatMap(this::readCustomer);
    }

    @Override
    public Flux<Customer> query() {
        return findAll();
    }

    @Override
    public Flux<Customer> findAllFiltered(List<String> filters, String search) {
        Query query = FilteringFactory.parseCustomerFilters(filters, search);
        return mongoTemplate.find(query, Document.class, mongoTemplate.getCollectionName(CustomerDocument.class))
                .flatMap(this::readCustomer);
    }

    /** ReactiveMongoTemplate does not resolve @DocumentReference fields. Read the stored reference id,
     * then resolve the segment explicitly with a reactive lookup. */
    private Mono<Customer> readCustomer(Document raw) {
        CustomerDocument customer = new CustomerDocument();
        customer.setId(stringValue(raw.get("_id")));
        customer.setUsername(stringValue(raw.get("username")));
        customer.setFamilyName(stringValue(raw.get("familyName")));
        customer.setGivenName(stringValue(raw.get("givenName")));
        customer.setEmail(stringValue(raw.get("email")));
        customer.setPhoneNumber(stringValue(raw.get("phoneNumber")));
        String status = stringValue(raw.get("status"));
        if (status != null) customer.setStatus(CustomerStatus.valueOf(status));
        customer.setCreatedBy(stringValue(raw.get("createdBy")));
        customer.setUpdateBy(stringValue(raw.get("updateBy")));
        customer.setCreatedAt(instantValue(raw.get("createdAt")));
        customer.setUpdateAt(instantValue(raw.get("updateAt")));

        String segmentId = segmentId(raw.get("segment"));
        if (segmentId == null) return Mono.just(customerPersistenceMapper.toDomain(customer));

        return mongoTemplate.findById(segmentId, SegmentDocument.class, "customer_segments")
                .map(segment -> {
                    customer.setSegment(segment);
                    return customerPersistenceMapper.toDomain(customer);
                })
                .defaultIfEmpty(customerPersistenceMapper.toDomain(customer));
    }

    private String segmentId(Object reference) {
        if (reference instanceof DBRef dbRef) return stringValue(dbRef.getId());
        if (reference instanceof Document document) return stringValue(document.get("$id"));
        return stringValue(reference);
    }

    private String stringValue(Object value) {
        return value == null ? null : value.toString();
    }

    private Instant instantValue(Object value) {
        if (value instanceof Date date) return date.toInstant();
        if (value instanceof Instant instant) return instant;
        return null;
    }

    @Override
    public Flux<CustomerStatistics> statistics() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("status", "segment").count().as("count"),
                Aggregation.project("count")
                        .and("_id.status").as("status")
                        .and("_id.segment").as("segment")
                        .andExclude("_id")
        );
        return mongoTemplate.aggregate(aggregation, CustomerDocument.class, CustomerStatistics.class);
    }

}
