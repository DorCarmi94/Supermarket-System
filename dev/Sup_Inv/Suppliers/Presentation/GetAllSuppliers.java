package Sup_Inv.Suppliers.Presentation;

import Sup_Inv.Suppliers.Service.SupplierDetailsDTO;
import Sup_Inv.Suppliers.Service.SupplierManagment;

import java.util.List;

public class GetAllSuppliers extends Menu_Option {


    private SupplierManagment supplierManagment;

    public GetAllSuppliers(SupplierManagment supplierManagment) {
        this.supplierManagment = supplierManagment;
    }


    @Override
    public void apply() {
        List<SupplierDetailsDTO> supplierDetails = supplierManagment.getAllSuppliers();

        if(supplierDetails != null) {
            for (SupplierDetailsDTO supD : supplierDetails) {
                System.out.println(supD.toString());
            }
        } else {
            System.out.println("There are not any supplier in the system");
        }
    }
}
