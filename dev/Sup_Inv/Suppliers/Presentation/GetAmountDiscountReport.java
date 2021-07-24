package Sup_Inv.Suppliers.Presentation;

import Sup_Inv.Suppliers.Service.ProductDiscountsDTO;
import Sup_Inv.Suppliers.Service.SupplierManagment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class GetAmountDiscountReport extends Menu_Option {


    private SupplierManagment supplierManagment;

    public GetAmountDiscountReport(SupplierManagment supplierManagment) {
        this.supplierManagment = supplierManagment;
    }


    @Override
    public void apply() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int supId = readInt("Supplier ID", reader);

        List<ProductDiscountsDTO> discountDTOList = supplierManagment.getAmountDiscountReport(supId);
        if(discountDTOList == null || discountDTOList.size()==0){
            System.out.println("No such supplier ID or there is no contract with the supplier");
            return;
        }

        for (ProductDiscountsDTO discountDTO : discountDTOList){
            System.out.println(discountDTO.toString());
        }


    }
}
