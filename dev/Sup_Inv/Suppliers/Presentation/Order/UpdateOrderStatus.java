package Sup_Inv.Suppliers.Presentation.Order;

import Sup_Inv.Suppliers.Presentation.Menu_Option;
import Sup_Inv.Suppliers.Service.OrderAndProductManagement;
import Sup_Inv.Suppliers.Structs.OrderStatus;
import Sup_Inv.Suppliers.Structs.StructUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class UpdateOrderStatus extends Menu_Option {


    private OrderAndProductManagement orderAndProductManagement;

    public UpdateOrderStatus(OrderAndProductManagement orderAndProductManagement) {
        this.orderAndProductManagement = orderAndProductManagement;
    }

    @Override
    public void apply() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int orderId = readInt("Order ID", reader);

        String statusStr = null;
        try {
            statusStr = readString("Enter order status:[OPEN/CLOSE/WAITING_FOR_SHIPPING]", reader);
        } catch (IOException e) {
            System.out.println("Error at reading");
        }

        OrderStatus status = StructUtils.getOrderStatus(statusStr);
        if(status == null){
            System.out.println("Invalid order status");
            return;
        }

        if(orderAndProductManagement.updateOrderStatus(orderId, status)){
            System.out.println("Order was updated");
        } else {
            System.out.println("Order was not updated");
        }
    }
}
