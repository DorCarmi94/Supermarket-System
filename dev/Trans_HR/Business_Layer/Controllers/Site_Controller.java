package Trans_HR.Business_Layer.Controllers;


import Trans_HR.Business_Layer.Modules.*;
import Trans_HR.Business_Layer.Service;
import Trans_HR.Business_Layer.Transportations.Utils.Buisness_Exception;
import Trans_HR.Data_Layer.Mapper;

import java.util.LinkedList;
import java.util.List;

public class Site_Controller {


    public void getAllSN() {
        int Sn = Mapper.getInstance().getAddressSn();
        this.AddresssnFactory = ++Sn;
        int storeSN = Mapper.getInstance().getstoreSN();
        Site.setSn(storeSN);
    }

    private static class SingletonService {
        private static Site_Controller instance = new Site_Controller();
    }
    private int AddresssnFactory;
    private Site_Controller() {
        this.AddresssnFactory = 0;
    }
    private int getAddressSnFactory(){
        return ++this.AddresssnFactory;
    }

    public static Site_Controller getInstance() {
        return SingletonService.instance;
    }

    public boolean addsupplier(String name, String city, String street, Integer number,
                               String name_of_contact, String phone, String supplier_area)
            throws Buisness_Exception {

        Service service = Service.getInstance();
        service.set_supplier_idCouter();
        service.set_address_idCounter();
        Supplier supplier = new Supplier(name, phone, name_of_contact,
                new Address(city, street, number), service.getAreaByName(supplier_area));

        service.add_new_supplier(supplier);
        return true;
    }

    public void getStores(){
        Mapper.getInstance().getAllStores();
    }


    public boolean addsite(String site_type, String name, String city,
                           String street, String number, String name_of_contact,
                           String phone, String site_area)
            throws Buisness_Exception{
        Service service = Service.getInstance();
        if (site_type.toLowerCase().equals("store")) {
            Address address = new Address(city, street, Integer.parseInt(number),getAddressSnFactory());
            Store store = new Store(name, phone, name_of_contact, address, service.getAreaByName(site_area));
            service.getHashStoresMap().put(store.getId(), store);

            Mapper.getInstance().insertAddress(city,street,address.getNumber(),address.getSn());

            Mapper.getInstance().insertStore(phone,name_of_contact,name,store.getId(),city,street,Integer.parseInt(number),store.getAddress().getSn(),store.getArea().getAreaSN());
            return true;

        } else if (site_type.equals("supplier")) {
            Supplier supplier = new Supplier(name, phone, name_of_contact,
                    new Address(city, street, Integer.parseInt(number),getAddressSnFactory()), service.getAreaByName(site_area));
            service.getSuppliersMap().put(supplier.getId(), supplier);
            return true;
        } else {
            return false;
        }
    }


    public List<String> Show_AreaList() throws Buisness_Exception {
        Service service = Service.getInstance();
        List<String> output = new LinkedList<>();
        for(Area area : service.getArea_list().values())
        {
            output.add(area.getAreaName());
        }
        if (output.isEmpty())
            throw new Buisness_Exception("There are no Areas in the system\n");
        return output;
    }

    public List<String> Show_supplier() throws Buisness_Exception {
        Service service = Service.getInstance();
        service.upload_All_Supplier();
        if (service.getSuppliersMap().size() == 0)
            throw new Buisness_Exception("There are no sites in the system" + "\n");
        else {
            List<String> result = new LinkedList<>();
            for (Supplier supplier : service.getSuppliersMap().values())
            {
                String line = supplier.getId()+ ". Name :" + supplier.getName() + ", Type: Supplier" + ".";
                result.add(line);
            }
            return result;
        }
    }

    public List<String> getSuppliersbyarea(String area) {
        Service service = Service.getInstance();
        service.upload_All_Supplier();
        List<Site> sites = new LinkedList<Site>();
        List<String> output = new LinkedList<String>();
        for (Site site : service.getSuppliersMap().values()) {
            if ((!sites.contains(site) & (site.getArea().getAreaName().equals(area)))) {
                String line = site.getId() + "." + "Name: " + site.getName() + ".";
                output.add(line);
                sites.add(site);
            }
        }
        output.sort(String.CASE_INSENSITIVE_ORDER);
        return output;
    }

    //print store and id
    public List<String> get_Stores_By_specific_area(String area) {
        Service service = Service.getInstance();
        List<String> output = new LinkedList<String>();
        for (Store sites : service.getHashStoresMap().values()) {
            if (sites.getArea().getAreaName().equals(area)) {
                String line = sites.getId() + ". " + sites.getName() + ", ";
                output.add(line);
            }
        }
        output.sort(String.CASE_INSENSITIVE_ORDER);
        return output;
    }

//    public List<String> getSupplierByStoreArea(int storeId, String area) {
//        Service service = Service.getInstance();
//        List<String> output = new LinkedList<String>();
//        for (MissingItems missingItems : service.getMissing().values()) {
//            int supplierId = missingItems.getSupplierId();
//            Area supplierArea = service.getSuppliersMap().get(supplierId).getArea();
//            if (storeId == missingItems.getStoreId() && area.equals(supplierArea.getAreaName())) {
//                String line = supplierId + ". " + service.getSuppliersMap().get(supplierId).getName() + ".";
//                if(!output.contains(line))
//                    output.add(line);
//            }
//        }
//        output.sort(String.CASE_INSENSITIVE_ORDER);
//        return output;
//    }
//
//
//    public List<String> getSupplierAreaByStore(int storeId) {
//        Service service = Service.getInstance();
//        List<Area> area_list = new LinkedList<Area>();
//        List<String> output = new LinkedList<String>();
//        for (MissingItems missingItems : service.getMissing().values()) {
//            if (storeId == missingItems.getStoreId()) {
//                int supplierId = missingItems.getSupplierId();
//                Area area = service.getSuppliersMap().get(supplierId).getArea();
//                if (!area_list.contains(area)) {
//                    area_list.add(area);
//                    output.add(area.getAreaName());
//                }
//            }
//        }
//
//        output.sort(String.CASE_INSENSITIVE_ORDER);
//        return output;
//    }

    public String get_Store_id(String site) {
        Service service = Service.getInstance();
        String output = "";
        for (Site sites : service.getHashStoresMap().values()) {
            if (sites.getName().equals(site)) {
                output = Integer.toString(sites.getId());
            }
        }
        return output;
    }

    public boolean printAllSites() {
        if(Service.getInstance().getHashStoresMap().isEmpty()){
            return false;
        }
        for(Store storeToPrint : Service.getInstance().getHashStoresMap().values()){
            System.out.println(storeToPrint);
        }
        return true;
    }

    public boolean isStoreExcites(int currentStore) {
        for(int storeSN : Service.getInstance().getHashStoresMap().keySet()){
            if(currentStore == storeSN){
                return true;
            }
        }
        return false;
    }

}
