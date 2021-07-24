package Sup_Inv.Suppliers.Presentation;

import Sup_Inv.Suppliers.Service.SupplierManagment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class updatePaymentOptions extends Menu_Option {


    private SupplierManagment supplierManagment;

    public updatePaymentOptions(SupplierManagment supplierManagment) {
        this.supplierManagment = supplierManagment;
    }


    @Override
    public void apply() {
        /*
         * <update> <supplier id> <payment options>+
         */

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int supid = -1;

        System.out.println("1) Add\n" +
                "2) Remove");
        int op = readInt("Option",reader);

        if(!(op == 1 | op == 2)){
            System.out.println("Invalid input");
            return;
        }
        supid = readInt("Supplier ID", reader);

        String paymentOptionsStr = null;
        try {
            paymentOptionsStr = readString("Payment options", reader);
        } catch (IOException e) {
            System.out.println("Error at reading");
            return;
        }
        String paymentOptions = paymentOptionsStr;

        boolean update = false;
        if(op == 2){
            update = supplierManagment.removePaymentOptions(supid, paymentOptions);
        } else { // add
            update = supplierManagment.addPaymentOption(supid, paymentOptions);
        }

        if(update){
            System.out.println("Options was updated");
        } else {
            System.out.println("Options wasnt updated");
        }

    }
}
