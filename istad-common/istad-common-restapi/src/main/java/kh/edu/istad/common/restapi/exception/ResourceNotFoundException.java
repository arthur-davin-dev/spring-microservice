package kh.edu.istad.common.restapi.exception;

import kh.edu.istad.common.domain.exception.DomainException;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String message) { super(message); }
}
