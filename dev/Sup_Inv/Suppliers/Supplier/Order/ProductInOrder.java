package Sup_Inv.Suppliers.Supplier.Order;

public class ProductInOrder {

    private int barcode;
    private int amount;
    private String productCatalogNumber;
    private double pricePerUnit;

    public ProductInOrder(int barcode, int amount){
        this.barcode = barcode;
        this.amount = amount;
        productCatalogNumber = null;
    }

    public ProductInOrder(int barcode, int amount, String productCatalogNumber, double pricePerUnit){
        this.barcode = barcode;
        this.amount = amount;
        this.productCatalogNumber = productCatalogNumber;
        this.pricePerUnit = pricePerUnit;
    }

    public int getBarcode() {
        return barcode;
    }

    public int getAmount() {
        return amount;
    }

    public String getProductCatalogNumber() {
        return productCatalogNumber;
    }

    public void setProductCatalogNumber(String productCatalogNumber) {
        this.productCatalogNumber = productCatalogNumber;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }
}
