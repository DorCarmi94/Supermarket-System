package Sup_Inv.Suppliers.Presentation;

import Sup_Inv.Result.Result;
import Sup_Inv.Suppliers.Service.OrderAndProductManagement;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RemoveProductsPeriodicalOrder extends Menu_Option {


    private OrderAndProductManagement orderAndProductManagement;

    public RemoveProductsPeriodicalOrder(OrderAndProductManagement orderAndProductManagement) {
        this.orderAndProductManagement = orderAndProductManagement;
    }

    @Override
    public void apply() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<Integer> barcodes = new ArrayList<>();
        int index = 0, barcode;

        int orderId = readIntPos("Order ID", "Order ID need to be bigger than -1", reader);
        if(orderId == -1) {
            return;
        }

        int numberOfBarcodes = readIntPos("Number of barcodes", "Number of barcodes need to be bigger than -1", reader);
        if(numberOfBarcodes == -1) {
            return;
        }

        while(index < numberOfBarcodes){
            barcode = readIntPos("Barcode", "Order ID need to be bigger than -1", reader);
            if(barcode > -1){
                barcodes.add(barcode);
                index = index + 1;
            }
        }

        Result<List<Integer>> res = orderAndProductManagement.RemoveProductsFromPeriodicalOrder(orderId, barcodes);

        if(res.isOk()){
            if(res.getValue().size() > 0) {
                System.out.println("Those products was deleted " + res.getValue().toString());
            }
        } else {
            System.out.println(res.getMessage());
        }

    }
}
