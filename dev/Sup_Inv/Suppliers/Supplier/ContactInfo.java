package Sup_Inv.Suppliers.Supplier;

/**
 * Data class
 */
public class ContactInfo {
    private String phoneNumber;
    private String email;
    private String name;
    private int SupID;

    public ContactInfo(String name, String phoneNumber, String email, int supID){
        this.email=email;
        this.name=name;
        this.phoneNumber=phoneNumber;
        this.SupID=supID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public void setSupID(int supID) {
        SupID = supID;
    }

    public int getSupID() {
        return SupID;
    }
}
