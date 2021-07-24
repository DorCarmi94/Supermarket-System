package Sup_Inv.Suppliers.Supplier;

public class AddProduct {

    public int barCode;
    public String productCatalogNumber;
    public double originalPrice;
    public ProductDiscounts productDiscounts;
    public Product product;

    public AddProduct(int barCode, String productCatalogNumber, double originalPrice ,ProductDiscounts productDiscounts) {
        this.barCode=barCode;
        this.productCatalogNumber = productCatalogNumber;
        this.originalPrice = originalPrice;
        this.productDiscounts = productDiscounts;
        this.product = null;
    }

    public AddProduct(int barCode, String productCatalogNumber, double originalPrice ,ProductDiscounts productDiscounts, Product product) {
        this.barCode=barCode;
        this.productCatalogNumber = productCatalogNumber;
        this.originalPrice = originalPrice;
        this.productDiscounts = productDiscounts;
        this.product = product;
    }
}
