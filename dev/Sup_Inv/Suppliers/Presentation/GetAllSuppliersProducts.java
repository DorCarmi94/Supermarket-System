package Sup_Inv.Suppliers.Presentation;

import Sup_Inv.Suppliers.Service.SupplierManagment;
import Sup_Inv.Suppliers.Service.SupplierProductDTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class GetAllSuppliersProducts extends Menu_Option {


    private SupplierManagment supplierManagment;

    public GetAllSuppliersProducts(SupplierManagment supplierManagment) {
        this.supplierManagment = supplierManagment;
    }


    @Override
    public void apply() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int supId = readInt("Supplier ID", reader);

        List<SupplierProductDTO> simpleSupplierProductDTOS = supplierManagment.getAllSupplierProducts(supId);
        if(simpleSupplierProductDTOS != null) {
            for (SupplierProductDTO product : simpleSupplierProductDTOS) {
                System.out.println("bardoce: {"+product.barcode+"}");
            }
        } else {
            System.out.println("Invalid supplier id or no contract");
        }
    }
}
