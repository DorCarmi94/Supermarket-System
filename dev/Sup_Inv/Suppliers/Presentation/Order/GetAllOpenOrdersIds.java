package Sup_Inv.Suppliers.Presentation.Order;

import Sup_Inv.Suppliers.Presentation.Menu_Option;
import Sup_Inv.Suppliers.Service.OrderAndProductManagement;

import java.util.List;

public class GetAllOpenOrdersIds extends Menu_Option {


    private OrderAndProductManagement OrderAndProductManagement;

    public GetAllOpenOrdersIds(OrderAndProductManagement OrderAndProductManagement) {
        this.OrderAndProductManagement = OrderAndProductManagement;
    }


    @Override
    public void apply() {
        List<Integer> orderIds = OrderAndProductManagement.getAllOpenOrders();

        if(orderIds.isEmpty()) {
            System.out.println("There isnt any open order in the system");
        } else {
            System.out.println("Order Ids: "+orderIds.toString());
        }
    }
}
