package Sup_Inv.Suppliers.Presentation;

import Sup_Inv.Suppliers.Service.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class GetAllSuppliersProductsDetalis extends Menu_Option {


    private SupplierManagment supplierManagment;
    private OrderAndProductManagement orderAndProductManagement;

    public GetAllSuppliersProductsDetalis(SupplierManagment supplierManagment,OrderAndProductManagement orderAndProductManagement) {
        this.supplierManagment = supplierManagment;
        this.orderAndProductManagement=orderAndProductManagement;
    }


    @Override
    public void apply() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int supId = readInt("Supplier ID", reader);


        List<SupplierProductDTO> simpleSupplierProductDTOS = supplierManagment.getAllSupplierProducts(supId);
        System.out.println("");
        if(simpleSupplierProductDTOS != null) {
            for(SupplierProductDTO dto : simpleSupplierProductDTOS){
                SystemProduct systemProduct=this.orderAndProductManagement.getProduct(dto.barcode);
                System.out.println(systemProduct.toString());
                System.out.println("");
            }
        } else {
            System.out.println("Invalid supplier id or no contract");
        }
    }
}
