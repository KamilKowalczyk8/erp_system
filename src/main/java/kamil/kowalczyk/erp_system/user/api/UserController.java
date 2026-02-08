package kamil.kowalczyk.erp_system.user.api;


import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import kamil.kowalczyk.erp_system.user.domain.UserService;
import kamil.kowalczyk.erp_system.user.domain.dto.LoginUserDto;
import kamil.kowalczyk.erp_system.user.domain.dto.RegisterUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
class UserController {
    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Rejestracja użytkownika", description = "Rejestruje nowego użytkownika")
    ResponseEntity<Long> registerUser(@RequestBody @Valid RegisterUserDto dto) {
        Long newUserId = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUserId);
    }

    @PostMapping("/login")
    @Operation(summary = "Logowanie użytkownika", description = "Logujemy użytkownika do systemu")
    ResponseEntity<String> loginUser(@RequestBody @Valid LoginUserDto dto) {
        String token = userService.loginUser(dto);
        return ResponseEntity.ok(token);
    }
}
