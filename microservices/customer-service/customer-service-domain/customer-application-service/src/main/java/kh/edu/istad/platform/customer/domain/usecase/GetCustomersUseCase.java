package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetCustomersUseCase {
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<Customer> execute() {
        return customerRepository.findAll();
    }
}
