package Sup_Inv.Suppliers.Supplier.Order;

import Sup_Inv.Suppliers.Supplier.Supplier;

import java.util.List;

public class AllOrderDetails {
    public int orderId;
    public int shopNumber;
    public String  deliveryDate;
    public List<AllDetailsOfProductInOrder> details;
    public boolean isPeriodicalOrder;
    public Supplier supplier;

    public AllOrderDetails(int orderId, int shopNumber, String deliveryDate, Supplier supplier,
                           List<AllDetailsOfProductInOrder> details, boolean isPeriodicalOrder){
        this.orderId = orderId;
        this.shopNumber = shopNumber;
        this.supplier = supplier;
        this.details = details;
        this.deliveryDate = deliveryDate;
        this.isPeriodicalOrder = isPeriodicalOrder;
    }
}
