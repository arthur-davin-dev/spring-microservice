package kh.edu.istad.platform.customer.restapi.controller;

import kh.edu.istad.platform.customer.domain.dto.UpdateCustomer;
import kh.edu.istad.platform.customer.domain.dto.CustomerStatistics;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.usecase.CreateCustomerUseCase;
import kh.edu.istad.platform.customer.domain.usecase.DeleteCustomerUseCase;
import kh.edu.istad.platform.customer.domain.usecase.FindByIdUseCase;
import kh.edu.istad.platform.customer.domain.usecase.GetCustomersUseCase;
import kh.edu.istad.platform.customer.domain.usecase.UpdateCustomerUseCase;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateResponse;
import kh.edu.istad.platform.customer.restapi.dto.CustomerUpdateRequest;
import kh.edu.istad.platform.customer.restapi.dto.PaginatedResponse;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {
    private final CreateCustomerUseCase createCustomerUseCase;
    private final GetCustomersUseCase getCustomersUseCase;
    private final FindByIdUseCase findByIdUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;
    private final CustomerWebMapper customerWebMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerInitiateResponse> create(@Valid @RequestBody CustomerInitiateRequest request) {
        return createCustomerUseCase.create(customerWebMapper.toCustomer(request))
                .map(customerWebMapper::toResponse);
    }

    @GetMapping
    public Flux<CustomerInitiateResponse> findAll(
            @RequestParam(name = "filter", required = false) List<String> filter,
            @RequestParam(name = "search", required = false) String search
    ) {
        return getCustomersUseCase.execute(filter, search).map(customerWebMapper::toResponse);
    }

    @GetMapping("/statistics")
    public Flux<CustomerStatistics> statistics() {
        return getCustomersUseCase.statistics();
    }

    @GetMapping("/{id}")
    public Mono<CustomerInitiateResponse> findById(@PathVariable("id") UUID id) {
        return findByIdUseCase.findById(id).map(customerWebMapper::toResponse);
    }

    @PutMapping("/{id}")
    public Mono<CustomerInitiateResponse> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody CustomerUpdateRequest request
    ) {
        UpdateCustomer command = new UpdateCustomer(
                id,
                request.familyName(),
                request.givenName(),
                request.email(),
                request.phoneNumber()
        );

        return updateCustomerUseCase.update(command)
                .map(customerWebMapper::toResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable("id") UUID id) {
        return deleteCustomerUseCase.deleteById(id);
    }
}
