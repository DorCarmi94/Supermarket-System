package Sup_Inv.Suppliers.Presentation;

import Sup_Inv.Suppliers.Service.SupplierManagment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class PurchaseHistoryFromSupplier extends Menu_Option {


    private SupplierManagment supplierManagment;

    public PurchaseHistoryFromSupplier(SupplierManagment supplierManagment) {
        this.supplierManagment = supplierManagment;
    }


    @Override
    public void apply() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int supId = readInt("Supplier ID", reader);

        List<String> catalogNumbers = supplierManagment.getPurchaseHistory(supId);

        if(catalogNumbers != null) {
            int index = 0;
            System.out.println("The catalog numbers ids:");
            for (String catalogN : catalogNumbers) {
                if(index != 10) {
                    System.out.print(catalogN + " ");
                } else {
                    System.out.println(catalogN);
                    index = 0;
                    continue;
                }
                index = index + 1;
            }
            System.out.println();
        } else {
            System.out.println("Invalid supplier ID");
        }
    }
}
