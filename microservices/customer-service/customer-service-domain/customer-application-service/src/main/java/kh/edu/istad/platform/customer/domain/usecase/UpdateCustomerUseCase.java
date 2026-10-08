package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.service.CustomerDomainService;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateCustomerUseCase {
    private final CustomerRepository customerRepository;
    private final CustomerDomainService customerDomainService;

    @Transactional
    public Customer execute(UUID id, String familyName, String givenName, String email, String phoneNumber) {
        Customer customer = customerRepository.findById(new CustomerId(id))
                .orElseThrow(() -> new CustomerNotFoundException(id));
        customerDomainService.updateCustomer(customer, familyName, givenName, new Email(email), new PhoneNumber(phoneNumber));
        return customerRepository.save(customer);
    }

    public static class CustomerNotFoundException extends ResourceNotFoundException {
        public CustomerNotFoundException(UUID id) { super("Customer not found: " + id); }
    }
}
