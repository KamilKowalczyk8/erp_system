package kamil.kowalczyk.erp_system.user.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserDto(
        @NotBlank(message = "Email jest wymagany")
        @Email(message = "Zły format email")
        String email,

        @NotBlank(message = "Hasło jest wymagane")
        String password
) {}
