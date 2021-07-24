package Sup_Inv.Suppliers.Presentation;

import Sup_Inv.Suppliers.Service.SupplierManagment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class PaymentOptions extends Menu_Option {


    private SupplierManagment supplierManagment;

    public PaymentOptions(SupplierManagment supplierManagment) {
        this.supplierManagment = supplierManagment;
    }


    @Override
    public void apply() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("1) get system payment options\n" +
                            "2) get supplier payment options");
        int op = readInt("Option",reader);

        if(!(op == 1 | op == 2)){
            System.out.println("Invalid input");
            return;
        }

        if(op == 1){
            System.out.println("Payment options are: " +
                    supplierManagment.getPaymentOptions());
        } else {
            int supId = readInt("Supplier ID", reader);

            List<String> paymentOptions = supplierManagment.getPaymentOptions(supId);
            if(paymentOptions == null){
                System.out.println("There isnt such a supplier with id "+ supId);
            } else {
                System.out.println("Payment options are: \n");
                System.out.println(paymentOptions.toString());
            }
        }
    }
}
