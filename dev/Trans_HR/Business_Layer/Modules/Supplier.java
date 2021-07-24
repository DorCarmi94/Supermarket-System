package Trans_HR.Business_Layer.Modules;

public class Supplier extends Site {

    private static int idcounter1=0;

    public Supplier(String name,String phone, String contact_name, Address address, Area area){
        super(idcounter1++, name,phone,contact_name, address, area);

    }

    public Supplier(int id, String name,String phone, String contact_name, Address address, Area area){
        super(id, name,phone,contact_name, address, area);
        if(id>idcounter1)
            idcounter1= id+1;
    }

    public static void setIdCounter(int id){
        if(idcounter1<id)
            idcounter1 = id;
    }

    public static int getIdCounter(){
        return idcounter1;
    }
}
