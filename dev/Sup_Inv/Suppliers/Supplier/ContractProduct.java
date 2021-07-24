package Sup_Inv.Suppliers.Supplier;

import java.util.HashMap;
import java.util.Map;

public class ContractProduct {
    private int barCode;
    private String productCatalogNumber;
    private Map<Integer,DiscountOfProduct> discounts;
    private double originalPrice;
    private int contractID;

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setContractID(int contractID) {
        this.contractID = contractID;
    }

    public int getContractID() {
        return contractID;
    }

    public ContractProduct(int barCode, String productCatalogNumber, ProductDiscounts discounts) {
        this.barCode = barCode;
        this.productCatalogNumber = productCatalogNumber;
        this.discounts=new HashMap<Integer, DiscountOfProduct>();
        for (Integer amount:discounts.discountPerAmount.keySet()) {
            this.discounts.put(amount,new DiscountOfProduct(amount,discounts.discountPerAmount.get(amount)));
        }
        this.originalPrice=discounts.originalPrice;
    }
    public ContractProduct(int barCode, String productCatalogNumber, double originalPrice) {
        this.barCode = barCode;
        this.productCatalogNumber = productCatalogNumber;
        this.discounts=new HashMap<Integer, DiscountOfProduct>();
        this.originalPrice=originalPrice;
    }

    public void setDiscounts(ProductDiscounts discounts)
    {
        for (Integer amount:
             discounts.discountPerAmount.keySet()) {
            this.discounts.put(amount,new DiscountOfProduct(amount,discounts.discountPerAmount.get(amount)));
        }
    }



    public int getBarCode() {
        return barCode;
    }

    public String getProductCatalogNumber() {
        return productCatalogNumber;
    }

    public ProductDiscounts getDiscounts() {
        Map<Integer,Double> discountsSummary = new HashMap<>();
        for (Integer amount:this.discounts.keySet()) {
            discountsSummary.put(amount,discounts.get(amount).getDiscount());
        }
        return new ProductDiscounts(this.barCode,discountsSummary,originalPrice);
    }

    public boolean addDiscountToProduct(int amount, double discount) {
        if(this.discounts.containsKey(amount))
        {
            this.discounts.get(amount).setDiscount(discount);
        }
        else
        {
            this.discounts.put(amount,new DiscountOfProduct(amount,discount));
        }
        return true;
    }

    public double getPricePerAmount(int amount) {
        int biggestDiscountAmount = 0;
        for(Integer pAmount : discounts.keySet()){
            if(amount < pAmount){
                continue;
            }

            if(biggestDiscountAmount < pAmount){
                biggestDiscountAmount = pAmount;
            }
        }

        if(biggestDiscountAmount == 0){
            return originalPrice;
        }

        return originalPrice * (1 - discounts.get(biggestDiscountAmount).getDiscount());
    }
}
