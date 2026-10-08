package kh.edu.istad.platform.customer.restapi.controller;

import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.domain.usecase.InitiateCustomerUseCase;
import kh.edu.istad.platform.customer.domain.usecase.UpdateCustomerUseCase;
import kh.edu.istad.platform.customer.domain.usecase.DeactivateCustomerUseCase;
import kh.edu.istad.platform.customer.restapi.dto.CustomerUpdateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateResponse;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.UUID;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final InitiateCustomerUseCase initiateCustomerUseCase;
    private final CustomerWebMapper customerWebMapper;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeactivateCustomerUseCase deactivateCustomerUseCase;
    private final kh.edu.istad.platform.customer.domain.usecase.GetCustomersUseCase getCustomersUseCase;

    @GetMapping
    public List<CustomerInitiateResponse> getCustomers() {
        return getCustomersUseCase.execute().stream()
                .map(customer -> new CustomerInitiateResponse(customer.getId().value(), customer.getUsername(), customer.getFamilyName(), customer.getGivenName(), customer.getEmail().value(), customer.getPhoneNumber().value()))
                .toList();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CustomerInitiateResponse initiateCustomer(
            @Valid @RequestBody CustomerInitiateRequest customerInitiateRequest
    ) {

        InitiateCustomerResult result = initiateCustomerUseCase.execute(
                customerWebMapper.toCommand(customerInitiateRequest)
        );
        return customerWebMapper.toResponse(result);
    }

    @PutMapping("/{id}")
    public CustomerInitiateResponse updateCustomer(@PathVariable("id") UUID id, @Valid @RequestBody CustomerUpdateRequest request) {
        var customer = updateCustomerUseCase.execute(id, request.familyName(), request.givenName(), request.email(), request.phoneNumber());
        return new CustomerInitiateResponse(customer.getId().value(), customer.getUsername(), customer.getFamilyName(), customer.getGivenName(), customer.getEmail().value(), customer.getPhoneNumber().value());
    }

    @PatchMapping("/{id}/deactivate")
    public CustomerInitiateResponse deactivateCustomer(@PathVariable("id") UUID id) {
        var customer = deactivateCustomerUseCase.execute(id);
        return new CustomerInitiateResponse(customer.getId().value(), customer.getUsername(), customer.getFamilyName(), customer.getGivenName(), customer.getEmail().value(), customer.getPhoneNumber().value());
    }

}
