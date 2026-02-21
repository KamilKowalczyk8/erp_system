package kamil.kowalczyk.erp_system.user.api;

import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kamil.kowalczyk.erp_system.user.domain.User;
import kamil.kowalczyk.erp_system.user.domain.UserService;
import kamil.kowalczyk.erp_system.user.domain.dto.LoginUserDto;
import kamil.kowalczyk.erp_system.user.domain.dto.RegisterUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    @Value("${app.security.jwt.expiration}")
    private long jwtExpiration;

    @PostMapping("/register")
    @Operation(summary = "Rejestracja użytkownika", description = "Rejestruje nowego użytkownika")
    ResponseEntity<Long> registerUser(@RequestBody @Valid RegisterUserDto dto) {
        Long newUserId = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserId);
    }

    @PostMapping("/login")
    @Operation(summary = "Logowanie użytkownika", description = "Logujemy użytkownika do systemu oraz zwaracamy jego token")
    ResponseEntity<?> loginUser(@RequestBody @Valid LoginUserDto dto) {
        String token = userService.loginUser(dto);

        ResponseCookie jwtCookie = ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(jwtExpiration / 1000)
                //.sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body("Zalogowano pomyślnie");

    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error","Niezalogowany"));
        }

        String email = authentication.getName();

        User user = userService.getUserByEmail(email);

        return ResponseEntity.ok(Map.of(
                "email", email,
                "username", user.getUsername()
        ));
    }
}
