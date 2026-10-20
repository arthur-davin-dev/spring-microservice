//package kh.edu.istad.platform.customer.domain.port.out;
//
//import kh.edu.istad.platform.customer.domain.entity.Customer;
//import kh.edu.istad.common.domain.valueobject.CustomerId;
//import reactor.core.publisher.Mono;
//
//import java.util.Optional;
//import java.util.List;
//
//public interface CustomerRepository {
//    //    Customer save(Customer customer);
//    Mono<Customer> save(Customer customer);
//
//    Optional<Customer> findById(CustomerId id);
//
//    List<Customer> findAll();
//
//    boolean existsByUsername(String username);
//
//    boolean existsByEmail(String email);
//}


package kh.edu.istad.platform.customer.domain.port.out;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.List;
import kh.edu.istad.platform.customer.domain.dto.CustomerStatistics;

public interface CustomerRepository {

    Mono<Customer> save(Customer customer);

    Mono<Customer> findById(CustomerId id);

    Flux<Customer> findAll();

    Mono<Void> deleteById(CustomerId id);

    Mono<Customer> findByUsername(String username);

    Flux<Customer> query();

    Flux<Customer> findAllFiltered(List<String> filters, String search);

    Flux<CustomerStatistics> statistics();
}
