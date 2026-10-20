package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.UpdateCustomer;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.exception.CustomerNotFoundException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.valueobject.Email;
import kh.edu.istad.platform.customer.domain.valueobject.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
@Service
public class UpdateCustomerUseCase {
    private final CustomerRepository customerRepository;

    public Mono<Customer> update(UpdateCustomer updateCustomer) {
        CustomerId customerId = new CustomerId(updateCustomer.id());
        return customerRepository.findById(customerId)
                .switchIfEmpty(Mono.error(new CustomerNotFoundException(updateCustomer.id())))
                .flatMap(customer -> {
                    customer.updateCustomer(updateCustomer.familyName(), updateCustomer.givenName(), new Email(updateCustomer.email()), new PhoneNumber(updateCustomer.phoneNumber()));
                    return customerRepository.save(customer);
                });
    }
}
