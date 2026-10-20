package kh.edu.istad.platform.customer.persistence.mapper;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerSegment;
import kh.edu.istad.platform.customer.persistence.document.CustomerDocument;
import kh.edu.istad.platform.customer.persistence.document.SegmentDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerPersistenceMapper {

    @Mapping(source = "id.value", target = "id")
    @Mapping(source = "email.value", target = "email")
    @Mapping(source = "phoneNumber.value", target = "phoneNumber")
    @Mapping(source = "segment", target = "segment", qualifiedByName = "segmentReference")
    CustomerDocument toDocument(Customer customer);

    @Mapping(source = "email", target = "email.value")
    @Mapping(source = "phoneNumber", target = "phoneNumber.value")
    @Mapping(source = "segment.id", target = "segment", qualifiedByName = "customerSegmentFromId")
    Customer toDomain(CustomerDocument document);

    @org.mapstruct.Named("segmentReference")
    default SegmentDocument segmentReference(CustomerSegment segment) {
        return segment == null ? null : new SegmentDocument(segment.name(), segment.label());
    }

    @org.mapstruct.Named("customerSegmentFromId")
    default CustomerSegment customerSegmentFromId(String id) {
        return id == null ? null : CustomerSegment.from(id);
    }

//     Mongo stores the ID as a String; the domain uses a UUID-backed CustomerId.
    default CustomerId stringToCustomerId(String id) {
        if (id == null) {
            return null;
        }

        try {
            return new CustomerId(UUID.fromString(id));
        } catch (IllegalArgumentException exception) {
            // Preserve compatibility with legacy ObjectId-backed documents.
            return new CustomerId(UUID.nameUUIDFromBytes(id.getBytes(StandardCharsets.UTF_8)));
        }
    }
}
