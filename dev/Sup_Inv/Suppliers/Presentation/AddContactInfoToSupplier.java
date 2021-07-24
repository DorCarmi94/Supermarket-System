package Sup_Inv.Suppliers.Presentation;

import Sup_Inv.Suppliers.Service.SupplierManagment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AddContactInfoToSupplier extends Menu_Option {


    private SupplierManagment supplierManagment;

    public AddContactInfoToSupplier(SupplierManagment supplierManagment) {
        this.supplierManagment = supplierManagment;
    }

    public String readString(String info, BufferedReader bufferedReader){
        System.out.print(info + ": ");
        try{
          return bufferedReader.readLine();
        } catch (IOException e) {
            System.out.print("Error at reading");
        }

        return null;
    }

    public int readInt(String info, BufferedReader bufferedReader){
        System.out.print(info + ": ");
        try{
            String input = bufferedReader.readLine();
            return Integer.parseInt(input);
        } catch (IOException e) {
            System.out.println("Error at reading");
        } catch (NumberFormatException e){
            System.out.print("Need to be a number");
        }

        return -1;
    }

    @Override
    public void apply() {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        int supID = -1;
        String name, phoneNumber, email;

        supID = readInt("Supplier ID", reader);
        if(supID < -1){
            return;
        }
        name = readString("Name", reader);
        if(name == null){
            return;
        }

        phoneNumber = readString("Phone number", reader);
        if(phoneNumber == null){
            return;
        }

        email = readString("Email", reader);
        if(email == null){
            return;
        }

        if(supplierManagment.addContactInfo(supID, name, phoneNumber, email)){
            System.out.println("The contact has been added");
        } else {
            System.out.println("The contact wasnt added, maybe exists already? check that and check if the information is valid");
        }
    }
}
