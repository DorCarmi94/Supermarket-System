package Sup_Inv.Suppliers.Service;

public class SupplierProductDTO {

    public int barcode;
    public String productCatalogNumber;
    public double originalPrice;
    public ProductDiscountsDTO discounts;

    public SystemProduct systemProduct;

    public SupplierProductDTO(int barcode, String productCatalogNumber, double originalPrice, ProductDiscountsDTO discountPerAmount) {
        this.barcode = barcode;
        this.productCatalogNumber = productCatalogNumber;
        this.originalPrice = originalPrice;

        this.discounts = discountPerAmount;
        systemProduct = null;

    }

    public SupplierProductDTO(int barcode, String productCatalogNumber, double originalPrice, ProductDiscountsDTO discountPerAmount, SystemProduct systemProduct) {
        this(barcode, productCatalogNumber, originalPrice, discountPerAmount);
        this.systemProduct = systemProduct;

    }



    public String toString(){
        if(systemProduct==null) {
            return String.format("barcode: %d\ncatalog number : %s\noriginal price : %f\n" +
                    discounts.toString() + "\n", barcode, productCatalogNumber, originalPrice);
        }
        else
        {
            return String.format("barcode: %d\ncatalog number : %s\noriginal price : %f\n" +
                    discounts.toString() + "\n%s", barcode, productCatalogNumber, originalPrice, systemProduct.toString());
        }
    }

    public String shallow_toString() {

        String discountStr = "";
        for(Integer amount : discounts.discountPerAmount.keySet()){
            discountStr = discountStr + String.format("\t%d : %f\n", amount, discounts.discountPerAmount.get(amount));
        }

        return String.format("barcode: %d\ncatalog number : %s\noriginal price : %f\n" +
                discountStr + "\n%s", barcode, productCatalogNumber, originalPrice, systemProduct.toString());
    }
}
