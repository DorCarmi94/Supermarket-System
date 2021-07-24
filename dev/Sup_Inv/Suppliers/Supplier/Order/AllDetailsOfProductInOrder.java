package Sup_Inv.Suppliers.Supplier.Order;

public class AllDetailsOfProductInOrder extends ProductInOrder {
    public String name;
    public double originalPrice;
    public double discount;


    public AllDetailsOfProductInOrder(int barcode, int amount, String productCatalogNumber, double pricePerUnit,
                                      String name, double originalPrice, double discount) {
        super(barcode, amount, productCatalogNumber, pricePerUnit);
        this.name = name;
        this.originalPrice = originalPrice;
        this.discount = discount;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public String getName() {
        return name;
    }

    public double getDiscount() {
        return discount;
    }
}
