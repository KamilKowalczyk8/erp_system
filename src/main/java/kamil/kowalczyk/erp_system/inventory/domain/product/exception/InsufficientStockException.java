package kamil.kowalczyk.erp_system.inventory.domain.product.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String sku, int currentStock, int quantityToRemove){
            super("Niewystarczająca ilość towaru (SKU: " + sku + "). Obecnie: " + currentStock + ", Próba wydania: " + quantityToRemove);
    }
}