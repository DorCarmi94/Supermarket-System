package Sup_Inv.Suppliers.Service;

import Sup_Inv.Suppliers.Structs.Days;

import java.util.List;

public class SupplierDetailsDTO {

    public int supplierID;
    public String supplierName;
    public String incNum;
    public String accountNumber;
    public String address;
    public List<Days> supplyDays;
    public int area;
    public boolean selfDelivery;

    public String contactName;
    public String phoneNumber;
    public String email;

    public SupplierDetailsDTO(int supplierID, String supplierName) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
    }

    public SupplierDetailsDTO(int supplierID, String supplierName, String incNum, String accountNumber,
                              String address, String contactName, String phoneNumber, String email,
                              List<Days> supplyDays, int area, boolean selfDelivery) {
        this.supplierID = supplierID;
        this.supplierName = supplierName;
        this.incNum = incNum;
        this.accountNumber = accountNumber;
        this.address = address;
        this.contactName = contactName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.supplyDays = supplyDays;
        this.area = area;
        this.selfDelivery = selfDelivery;
    }

    public String toString(){
        return String.format("SupplierID : %d\nsupplier name : %s",
                supplierID, supplierName);
    }
    public String toStringFull(){
        return String.format("SupplierID : %d\nsupplier name : %s\nincorporation number: %s\naccount number : %s\naddress : %s",
                supplierID, supplierName, incNum, accountNumber, accountNumber);
    }
}
