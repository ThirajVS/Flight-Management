package com.aerocadet.portal.auth;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(max = 120) String fullName,
        @NotBlank @Email @Size(max = 180) String email,
        @NotBlank @Pattern(regexp = "^[+]?[0-9 ()-]{7,20}$", message = "must be a valid phone number") String phone,
        @NotBlank @Size(min = 10, max = 72) String password,
        @NotNull @Past LocalDate dateOfBirth,
        @NotBlank @Size(max = 80) String nationality,
        @NotBlank @Size(max = 80) String city,
        @NotBlank @Size(max = 80) String state,
        @NotBlank @Size(max = 80) String country) {
}

