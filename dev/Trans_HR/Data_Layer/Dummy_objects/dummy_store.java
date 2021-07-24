package Trans_HR.Data_Layer.Dummy_objects;

public class dummy_store {

    private String phone;
    private String contact_name;
    private String name;
    private int id;
    private String city;
    private String street;
    private int number;
    private int Address_Sn;
    private int areaSn;

    public dummy_store(String phone, String contact_name, String name, int id, String city, String street, int number, int Adrress_Sn, int AreaSn){
        this.phone=phone;
        this.contact_name=contact_name;
        this.name=name;
        this.id=id;
        this.city=city;
        this.street=street;
        this.number=number;
        this.Address_Sn=Adrress_Sn;
        this.areaSn=AreaSn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getAddress_Sn() {
        return Address_Sn;
    }

    public void setAddress_Sn(int address_Sn) {
        Address_Sn = address_Sn;
    }

    public int getAreaSn() {
        return areaSn;
    }

    public void setAreaSn(int areaSn) {
        this.areaSn = areaSn;
    }
}
