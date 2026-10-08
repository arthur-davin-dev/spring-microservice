package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeactivateCustomerUseCase {
    private final CustomerRepository customerRepository;
    private final CustomerDomainService customerDomainService;

    @Transactional
    public Customer execute(UUID id) {
        Customer customer = customerRepository.findById(new CustomerId(id))
                .orElseThrow(() -> new UpdateCustomerUseCase.CustomerNotFoundException(id));
        customerDomainService.deactivateCustomer(customer);
        return customerRepository.save(customer);
    }
}
