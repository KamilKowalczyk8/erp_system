package kamil.kowalczyk.erp_system.client.domain;

import kamil.kowalczyk.erp_system.client.domain.dto.CreateClientDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Long createClient(CreateClientDto dto) {
        Client client = new Client(
                dto.firstName(),
                dto.lastName(),
                dto.email(),
                dto.phoneNumber()
        );
        return clientRepository.save(client).getId();
    }

    @Transactional(readOnly = true)
    public Client getClient(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Klient o id " + id + " nie istnieje!"));
    }



}
