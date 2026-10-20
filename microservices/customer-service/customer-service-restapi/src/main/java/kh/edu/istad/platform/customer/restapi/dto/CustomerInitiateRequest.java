package kh.edu.istad.platform.customer.restapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CustomerInitiateRequest(
        @NotBlank
        String username,
        @NotBlank
        String familyName,
        @NotBlank
        String givenName,
        @NotBlank
        String email,
        @NotBlank
        String phoneNumber,
        @NotBlank
        @Pattern(regexp = "(?i)Silver|Gold|Premium", message = "segment must be Silver, Gold, or Premium")
        String segment
) {
}
