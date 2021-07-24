package Sup_Inv.Suppliers.Service;

public class ContactInfoDTO {

    private String phoneNumber;
    private String email;
    private String name;
    private int SupID;

    public ContactInfoDTO(String name, String phoneNumber, String email, int supID){
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

    @Override
    public String toString() {
        return "ContactInfo: " +
                "phoneNumber='" + phoneNumber +", email='" + email +
                ", name='" + name +
                ", SupID=" + SupID;
    }
}
