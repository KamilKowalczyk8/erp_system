package kamil.kowalczyk.erp_system.inventory.domain.product.exception;

public class ProductAlreadyExistsException extends RuntimeException{

    public ProductAlreadyExistsException(String skuCode) {
        super("Produkt o podanym kodzie SKU: " + skuCode + " już istnieje w tej bazie!");
    }
}
