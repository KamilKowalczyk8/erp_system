package kamil.kowalczyk.erp_system.client.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateClientDto(


        @NotBlank(message = "Imię jest wymagane")
        @Size(max = 50, message = "Imie może mieć maksymalnie 50 znaków")
        String firstName,

        @NotBlank(message = "Nazwisko jest wymagane")
        String lastName,

        @Pattern(regexp = "\\d{9}", message = "Telefon musi składać się z 9 cyfr (np. 123456789")
        String phoneNumber

) {}
