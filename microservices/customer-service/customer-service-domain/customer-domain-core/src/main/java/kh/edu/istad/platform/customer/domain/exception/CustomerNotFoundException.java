package kh.edu.istad.platform.customer.domain.exception;

import kh.edu.istad.common.domain.exception.ResourceNotFoundException;

import java.util.UUID;

public class CustomerNotFoundException extends ResourceNotFoundException {

    public CustomerNotFoundException(UUID id) {
        super("Customer not found: " + id);
    }
}