package Trans_HR.Business_Layer.Modules;

public abstract class Site {

    private static int idcounter=1;
    private int id;

    @Override
    public String toString() {
        return
                id + ". " +
                " Store name: " + name;
    }

    private String name;
    private String phone;
    private String contact_name;
    private Address address;
    private Area area;

    Site (String name, String phone, String contact_name, Address address,Area area){
        this.id=idcounter++;
        this.name=name;
        this.contact_name=contact_name;
        this.phone=phone;
        this.address=address;
        this.area=area;
    }


    Site (String name, String phone, String contact_name, Address address,Area area, int id){
        this.id=id;
        this.name=name;
        this.contact_name=contact_name;
        this.phone=phone;
        this.address=address;
        this.area=area;
    }
    Site (int id, String name, String phone, String contact_name, Address address,Area area){
        this.id=id;
        this.name=name;
        this.contact_name=contact_name;
        this.phone=phone;
        this.address=address;
        this.area=area;
    }


    public static void setSn(int SN){idcounter = ++SN; }
    public String getPhone(){ return phone;}
    public String getContact_name(){return  contact_name;}
    public Address getAddress() {
        return address;
    }
    public Area getArea() {
        return area;
    }
    public String getName() {
        return name;
    }
    public int getId(){return id;}



}
