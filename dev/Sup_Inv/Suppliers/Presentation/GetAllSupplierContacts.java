package Sup_Inv.Suppliers.Presentation;

import Sup_Inv.Suppliers.Service.ContactInfoDTO;
import Sup_Inv.Suppliers.Service.SupplierManagment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class GetAllSupplierContacts extends Menu_Option {

    private SupplierManagment supplierManagment;

    public GetAllSupplierContacts(SupplierManagment supplierManagment) {
        this.supplierManagment = supplierManagment;
    }

    @Override
    public void apply() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int supId = readInt("Supplier ID", reader);

        List<ContactInfoDTO> simpleSupplierContactsDTOS = supplierManagment.getSupplierContacts(supId);
        if(simpleSupplierContactsDTOS != null) {
            for (ContactInfoDTO contact : simpleSupplierContactsDTOS) {
                System.out.println(contact.toString());
            }
        } else {
            System.out.println("Invalid supplier id or no contract");
        }
    }
}
