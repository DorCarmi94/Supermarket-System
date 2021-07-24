package Sup_Inv.Suppliers.Presentation.Order;

import Sup_Inv.Suppliers.Presentation.Menu_Option;
import Sup_Inv.Suppliers.Service.OrderAndProductManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateOrderArrivalDay extends Menu_Option {


    private OrderAndProductManagement orderAndProductManagement;

    public UpdateOrderArrivalDay(OrderAndProductManagement orderAndProductManagement) {
        this.orderAndProductManagement = orderAndProductManagement;
    }


    @Override
    public void apply() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int orderId = readInt("Order ID", reader);
        Date deliveryDate;

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            String dayinput = readString("Delivery day (dd/MM/yyyy)",reader);
            deliveryDate  = df.parse(dayinput);
        } catch (ParseException e) {
            System.out.println("Invalid date format");
            return;
        } catch (IOException e){
            System.out.println("Error at reading");
            return;
        }

        if(orderAndProductManagement.updateOrderArrivalTime(orderId, deliveryDate)){
            System.out.println("Order was updated");
        } else {
            System.out.println("Order was not updated");
        }
    }
}
