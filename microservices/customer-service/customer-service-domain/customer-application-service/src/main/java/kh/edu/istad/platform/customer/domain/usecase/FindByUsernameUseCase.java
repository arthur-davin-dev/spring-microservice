package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FindByUsernameUseCase {
    private final CustomerRepository customerRepository;
    public Mono<Customer> findByUsername(String username){
        return customerRepository.findByUsername(username);
    }
}
