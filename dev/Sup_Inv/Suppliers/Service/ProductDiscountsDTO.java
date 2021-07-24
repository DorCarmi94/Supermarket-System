package Sup_Inv.Suppliers.Service;

import java.util.HashMap;
import java.util.Map;

public class ProductDiscountsDTO {
    public int barcode;
    public Map<Integer,Double> discountPerAmount;
    public double originalPrice;

    public ProductDiscountsDTO(int barcode, Map<Integer, Double> discountPerAmont, Double originalPrice) {
        this.barcode = barcode;
        this.discountPerAmount = discountPerAmont;
        this.originalPrice = originalPrice;
    }

    public ProductDiscountsDTO(int barcode,Double originalPrice)
    {
        this.originalPrice=originalPrice;
        this.barcode=barcode;
        this.discountPerAmount=new HashMap<>();
    }

    public void addDiscount(int amount, double discount)
    {
        if(!this.discountPerAmount.containsKey(amount))
        {
            this.discountPerAmount.put(amount,discount);
        }
        else
        {
            this.discountPerAmount.replace(amount,discount);
        }
    }

    public String toString(){
        String discountStr = "";
        for(Integer amount : discountPerAmount.keySet()){
            discountStr = discountStr + String.format("\t%d : %f\n", amount, discountPerAmount.get(amount));
        }

        return String.format("Product ID: %d\nDiscounts :\n%sOriginal price : %f", barcode, discountStr, originalPrice);
    }


}
