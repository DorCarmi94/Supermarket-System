package Sup_Inv.Suppliers.Presentation.Order;

import Sup_Inv.Suppliers.Presentation.Menu_Option;
import Sup_Inv.Suppliers.Service.OrderAndProductManagement;

import java.util.List;

public class GetAllOpenPeriodicalOrders extends Menu_Option {


    private OrderAndProductManagement OrderAndProductManagement;

    public GetAllOpenPeriodicalOrders(OrderAndProductManagement OrderAndProductManagement) {
        this.OrderAndProductManagement = OrderAndProductManagement;
    }


    @Override
    public void apply() {
        List<Integer> orderIds = OrderAndProductManagement.getAllOpenPeriodicalOrder();

        if(orderIds.isEmpty()) {
            System.out.println("There isnt any open order in the system");
        } else {
            System.out.println("Order Ids: "+orderIds.toString());
        }
    }
}
