package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.List;
import kh.edu.istad.platform.customer.domain.dto.CustomerStatistics;

@Component
@RequiredArgsConstructor
public class GetCustomersUseCase {
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public Flux<Customer> execute() {
        return customerRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Flux<Customer> execute(List<String> filters) {
        return execute(filters, null);
    }

    @Transactional(readOnly = true)
    public Flux<Customer> execute(List<String> filters, String search) {
        return (filters == null || filters.isEmpty()) && (search == null || search.isBlank())
                ? customerRepository.findAll()
                : customerRepository.findAllFiltered(filters, search);
    }

    @Transactional(readOnly = true)
    public Flux<CustomerStatistics> statistics() {
        return customerRepository.statistics();
    }
}
