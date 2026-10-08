package kh.edu.istad.platform.customer.restapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CustomerUpdateRequest(
        @NotBlank @Size(max = 100) String familyName,
        @NotBlank @Size(max = 100) String givenName,
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Pattern(regexp = "^[+0-9()\\- .]{6,30}$") String phoneNumber
) {}
