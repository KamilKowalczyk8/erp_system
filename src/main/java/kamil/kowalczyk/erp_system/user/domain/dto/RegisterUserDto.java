package kamil.kowalczyk.erp_system.user.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterUserDto(

        @NotBlank(message = "Nazwa użytkownika jest wymagana")
        @Size(min = 3, max = 50, message = "Nazwa użytkownika musi mieć od 3 do 50 znaków")
        String username,

        @NotBlank(message = "Email jest wymagany")
        @Email(message = "Niepoprawny format adresu email")
        String email,

        @NotBlank(message = "Hasło jest wymagane")
        @Size(min = 8, message = "Hasło musi mieć co najmniej 8 znaków")
        String rawPassword

) {}
