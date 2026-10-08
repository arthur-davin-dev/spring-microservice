package kh.edu.istad.platform.customer.domain.port.out;

import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import java.util.Optional;
import java.util.List;

public interface CustomerRepository {
    Customer save(Customer customer);
    Optional<Customer> findById(CustomerId id);
    List<Customer> findAll();
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
