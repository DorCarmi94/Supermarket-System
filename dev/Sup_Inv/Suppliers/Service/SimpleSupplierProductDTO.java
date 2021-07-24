package Sup_Inv.Suppliers.Service;

public class SimpleSupplierProductDTO {

    public String productCatalogNumber;
    public int barcode;

    public SimpleSupplierProductDTO(int barcode, String productCatalogNumber) {
        this.productCatalogNumber = productCatalogNumber;
        this.barcode = barcode;
    }

    public String toString(){
        return String.format("Barcode : %d, Catalog number : %s", barcode, productCatalogNumber);
    }
}
