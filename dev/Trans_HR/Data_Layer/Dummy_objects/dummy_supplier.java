package Trans_HR.Data_Layer.Dummy_objects;

public class dummy_supplier {

    private int SN;
    private String name;
    private String Phone;
    private String ContactName;
    private int AddressSN;
    private int AreaSN;
    private dummy_Address dummy_address;

    public dummy_supplier(int SN,String name, String Phone, String ContactName, int AddressSN, int AreaSN ,String city, String street, int number){
        this.Phone =Phone;
        this.ContactName = ContactName;
        this.name=name;
        this.SN = SN;
        this.AddressSN=AddressSN;
        this.AreaSN=AreaSN;
        this.dummy_address = new dummy_Address(AddressSN,city,street,number);
    }

    public dummy_supplier(int SN, String name, String Phone, String ContactName, int AddressSN, int AreaSN){
        this.Phone =Phone;
        this.ContactName = ContactName;
        this.name=name;
        this.SN = SN;
        this.AddressSN=AddressSN;
        this.AreaSN=AreaSN;
        this.dummy_address = null;
    }

    public dummy_supplier(int SN,String name, String Phone, String ContactName, int AddressSN, int AreaSN,dummy_Address dummy_address){
        this.Phone =Phone;
        this.ContactName = ContactName;
        this.name=name;
        this.SN = SN;
        this.AddressSN=AddressSN;
        this.AreaSN=AreaSN;
        this.dummy_address = dummy_address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        this.ContactName = contactName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSN() {
        return SN;
    }

    public void setSN(int SN) {
        this.SN = SN;
    }

    public int getAddress_Sn() {
        return AddressSN;
    }

    public void setAddressSN(int address_Sn) {
        AddressSN = address_Sn;
    }

    public int getAreaSn() {
        return AreaSN;
    }

    public void setAreaSn(int areaSn) {
        this.AreaSN = areaSn;
    }

    public dummy_Address getDummy_address() {
        return dummy_address;
    }

    public void setDummy_address(dummy_Address dummy_address) {
        this.dummy_address= dummy_address;
    }
}
