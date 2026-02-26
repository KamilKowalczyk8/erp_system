package kamil.kowalczyk.erp_system.client.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import kamil.kowalczyk.erp_system.client.application.ClientRegistrationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Rejestracja Klientów", description = "Publiczne Api do zakładania kont przez klientów")
public class ClientRegistrationController {

    private final ClientRegistrationFacade registrationFacade;



}
