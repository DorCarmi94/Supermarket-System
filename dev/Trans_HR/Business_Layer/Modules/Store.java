package Trans_HR.Business_Layer.Modules;

public class Store extends Site {

    public Store(String name, String phone, String contact_name, Address address, Area area) {
        super(name, phone, contact_name, address, area);
    }


    public Store(String name, String phone, String contact_name, Address address, Area area, int id) {
        super(name, phone, contact_name, address, area, id);
    }

    public Store(int id, String name, String phone, String contact_name, Address address, Area area) {
        super(id, name, phone, contact_name, address, area);
    }
}

