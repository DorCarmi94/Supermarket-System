package Sup_Inv.Suppliers.Supplier.Order;

import java.util.List;

public class PeriodicalOrder extends Order {

    protected PeriodicalOrder(int orderId, List<ProductInOrder> products, int shopNumber){
        super(orderId, products, shopNumber);
    }

    public static PeriodicalOrder CreatePeriodicalOrder(int orderId, List<ProductInOrder> productsInOrder, int shopNumber){
        if(productsInOrder != null && productsInOrder.isEmpty()){
            return null;
        }

        return new PeriodicalOrder(orderId, productsInOrder, shopNumber);
    }
}
