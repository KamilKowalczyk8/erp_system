package kamil.kowalczyk.erp_system.client.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateClientDto(
        @NotBlank(message = "Imię jest wymagane")
        String firstName,

        @NotBlank(message = "Nazwisko jest wymagane")
        String lastName,

        @NotBlank(message = "Email jest wymagany")
        String email,

        String phoneNumber

) {}
