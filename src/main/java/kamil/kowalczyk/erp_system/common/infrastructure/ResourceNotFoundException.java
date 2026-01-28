package kamil.kowalczyk.erp_system.common.infrastructure;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
