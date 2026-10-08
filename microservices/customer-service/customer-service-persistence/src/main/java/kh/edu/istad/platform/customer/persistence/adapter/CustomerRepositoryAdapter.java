package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.persistence.entity.CustomerEntity;
import kh.edu.istad.platform.customer.persistence.repository.CustomerJpaRepository;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import java.util.Optional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepository {
    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = customerJpaRepository.findById(customer.getId().value()).orElseGet(CustomerEntity::new);
        entity.setCustomerId(customer.getId().value());
        entity.setUsername(customer.getUsername());
        entity.setFamilyName(customer.getFamilyName());
        entity.setGivenName(customer.getGivenName());
        entity.setEmail(customer.getEmail().value());
        entity.setPhoneNumber(customer.getPhoneNumber().value());
        entity.setStatus(customer.getStatus());
        return toDomain(customerJpaRepository.save(entity));
    }

    @Override
    public Optional<Customer> findById(CustomerId id) {
        return customerJpaRepository.findById(id.value()).map(this::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return customerJpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsByUsername(String username) {
        return customerJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerJpaRepository.existsByEmail(email);
    }

    private Customer toDomain(CustomerEntity entity) {
        return Customer.builder().id(new CustomerId(entity.getCustomerId())).username(entity.getUsername())
                .familyName(entity.getFamilyName()).givenName(entity.getGivenName())
                .email(new Email(entity.getEmail())).phoneNumber(new PhoneNumber(entity.getPhoneNumber()))
                .status(entity.getStatus()).build();
    }
}
