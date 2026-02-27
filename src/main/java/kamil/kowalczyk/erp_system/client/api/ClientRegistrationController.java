package kamil.kowalczyk.erp_system.client.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kamil.kowalczyk.erp_system.client.application.ClientRegistrationFacade;
import kamil.kowalczyk.erp_system.client.application.dto.RegisterClientRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Rejestracja Klientów", description = "Publiczne Api do zakładania kont przez klientów")
public class ClientRegistrationController {

    private final ClientRegistrationFacade registrationFacade;

    @PostMapping("/register")
    @Operation(summary = "Zarejestruj nowego klienta wraz z kontem do logowania")
    public ResponseEntity<Long> registerClient(@RequestBody @Valid RegisterClientRequestDto dto) {

        Long newClientId = registrationFacade.registerFullClient(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(newClientId);
    }

}
