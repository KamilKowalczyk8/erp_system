package kamil.kowalczyk.erp_system.client.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterClientRequestDto(
        @NotBlank(message = "Email jest wymagany")
        @Email(message = "Niepoprawny format emailu")
        String email,

        @NotBlank(message = "Hasło jest wymagane")
        @Size(min = 8, message = "Hasło musi mieć co najmniej 8 znaków")
        String password,

        @NotBlank(message = "Imię jest wymagane")
        @Size(max = 50, message = "Imię może mieć maksymalnie 50 znaków")
        String firstName,

        @NotBlank(message = "Nazwisko jest wymagane")
        String lastName,

        @Pattern(regexp = "\\d{9}", message = "Telefon musi składać się z 9 cyfr (np. 123456789)")
        String phoneNumber
) {}
