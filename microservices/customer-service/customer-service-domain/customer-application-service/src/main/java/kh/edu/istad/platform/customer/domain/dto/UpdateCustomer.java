package kh.edu.istad.platform.customer.domain.dto;

import java.util.UUID;

public record UpdateCustomer(
        UUID id,
        String familyName,
        String givenName,
        String email,
        String phoneNumber
) {
}
