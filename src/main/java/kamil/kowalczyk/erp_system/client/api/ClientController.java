package kamil.kowalczyk.erp_system.client.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kamil.kowalczyk.erp_system.client.domain.Client;
import kamil.kowalczyk.erp_system.client.domain.ClientService;
import kamil.kowalczyk.erp_system.client.domain.dto.CreateClientDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/clients/client")
@Tag(name = "Klienci", description = "Zarządzanie bazą klientów (CRM)")
class ClientController {

    private final ClientService clientService;

    ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @Operation(summary = "Dodaj nowego klienta", description = "Zwraca ID nowo utworzonego klienta.")
    ResponseEntity<Long> createClient(@RequestBody @Valid Long userId, CreateClientDto dto) {
        Long clientId = clientService.createClient(userId, dto);

        return ResponseEntity
                .created(URI.create("/api/clients/client" + clientId))
                .body(clientId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobranie danego klienta", description = "Pokazuje danego klienta")
    public ResponseEntity<Client> getClient(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClient(id));
    }
}
