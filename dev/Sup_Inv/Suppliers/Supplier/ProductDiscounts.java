package Sup_Inv.Suppliers.Supplier;

import java.util.HashMap;
import java.util.Map;

public class ProductDiscounts {
    public int barCode;
    public Map<Integer,Double> discountPerAmount;
    public double originalPrice;

    public ProductDiscounts(int barCode, Map<Integer, Double> discountPerAmont, Double originalPrice) {
        this.barCode = barCode;

        this.originalPrice = originalPrice;
        this.discountPerAmount = new HashMap<>();
        for (Integer amout: discountPerAmont.keySet()) {
            this.discountPerAmount.put(amout,discountPerAmont.get(amout));

        }
    }

    public ProductDiscounts(int barCode,Double originalPrice)
    {
        this.barCode=barCode;
        this.originalPrice=originalPrice;
        this.discountPerAmount=new HashMap<>();
    }

    public void addDiscountPerAmount(int amount,Double discount)
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
}
