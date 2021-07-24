package Trans_HR.Interface_Layer.Transportations;

import Trans_HR.Business_Layer.Service;
import Trans_HR.Business_Layer.Transportations.Utils.Buisness_Exception;
import Trans_HR.Interface_Layer.Workers.SystemInterfaceWorkers;
import javafx.util.Pair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class SystemInterfaceTransportations {


    private Service service = Service.getInstance();
//    private Trucks_Controller trucks_controller = Trucks_Controller.getInstance();
//    private Site_Controller site_controller = Site_Controller.getInstance();
//    private Transportation_Controller transportation_controller = Transportation_Controller.getInstance();
//    private Missing_items_Controller missing_items_controller = Missing_items_Controller.getInstance();
//    private Drivers_Controller drivers_controller = Drivers_Controller.getInstance();
    private Scanner scan = new Scanner(System.in);

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static class SingletonControler {
        private static SystemInterfaceTransportations instance = new SystemInterfaceTransportations();
    }

    private SystemInterfaceTransportations() {
       service.upload_Area();
        service.upload_license();
    }

    public static SystemInterfaceTransportations getInstance() {
        return SystemInterfaceTransportations.SingletonControler.instance;
    }

 /*   public void uploadData() {
        service.uploadData();
    } */

    static String Print_error(Exception e) {
        Throwable current = e;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current.getMessage();
    }
    public List<String> get_Dates_Periodical_Order() throws Buisness_Exception {
        return service.transportation_controller.get_Dates_Periodical_Order();
    }

    public List<String> get_Dates_Regular_Open_Order() throws Buisness_Exception {
        return service.transportation_controller.get_Dates_Regular_Open_Order();
    }

    public List<String> get_stores_by_date_Periodical_Order(Date date) throws Buisness_Exception {
        return service.transportation_controller.get_stores_by_date_Periodical_Order(date);

    }

    public List<String> get_stores_by_date_Regular_Open_Order(Date date) throws Buisness_Exception {
        return service.transportation_controller.get_stores_by_date_Regular_Open_Order(date);

    }

    public List<String> get_area_for_suppliers_by_date_store_Periodical_Order(Date date, Integer storeID) throws Buisness_Exception {
        return service.transportation_controller.get_area_for_suppliers_by_date_store_Periodical_Order(date,storeID);

    }

    public List<String> get_area_for_suppliers_by_date_store_Regular_Open_Order(Date date, Integer storeID) throws Buisness_Exception {
        return service.transportation_controller.get_area_for_suppliers_by_date_store_Regular_Open_Order(date,storeID);

    }

    public List<String> get_Suppliers_by_area_Periodical_Order(Date date, Integer storeID, String area) throws Buisness_Exception {
        return service.transportation_controller.get_Suppliers_by_area_Periodical_Order(date,storeID,area);

    }

    public List<String> get_Suppliers_by_area_Regular_Open_Order(Date date, Integer storeID, String area) throws Buisness_Exception {
        return service.transportation_controller.get_Suppliers_by_area_Regular_Open_Order(date,storeID,area);

    }

//    public List<String> getMissingItemsStores() throws Buisness_Exception {
//        return service.missing_items_controller.getMissingItemsStores();
//    }

//    public List<String> getSupplierAreaByStore(int id) {
//        return service.site_controller.getSupplierAreaByStore(id);
//    }
//
//    public List<String> getSupplierByStoreArea(int id, String area) {
//        return service.site_controller.getSupplierByStoreArea(id, area);
//    }

    public List<String> getFreeTrucks(Date date, int departureTime, boolean lastCheck) throws Buisness_Exception {
        List<String> out = service.trucks_controller.getFreeTrucks(date, departureTime);
        if(out.isEmpty() && lastCheck )
        {
            throw new Buisness_Exception("There are no Trucks available, cancel transport");
        }
        return out;
    }

    public List<String> getDriverToTrucks(int truckId, Date date){
        return service.drivers_controller.getDriverToTrucks(truckId, date);
    }

    public void createTransportation_Periodical_Order(Date date, int DepartureTime, int driver_id,
                                        int truck_id, List<Integer> suppliers, List<Integer> stores) throws Buisness_Exception  {
        service.transportation_controller.createTransportation_Periodical_Order(date, DepartureTime, driver_id,
                truck_id, suppliers, stores);
    }

    public void createTransportation_Regular_Open_Order(Date date, int DepartureTime, int driver_id,
                                     int truck_id, List<Integer> suppliers, List<Integer> stores) throws Buisness_Exception  {
        service.transportation_controller.createTransportation_Regular_Open_Order(date, DepartureTime, driver_id,
                truck_id, suppliers, stores);
    }

    public List<String>  get_area_for_suppliers() throws Buisness_Exception {
        return service.transportation_controller.get_area_for_suppliers();
    }

    public List<String> getSuppliersbyarea(String area) {
        return service.site_controller.getSuppliersbyarea(area);
    }

    public List<String> get_area_for_stores() throws Buisness_Exception {
        return service.transportation_controller.get_area_for_stores();
    }

    public List<String> get_Stores_By_specific_area(String area) {
        return service.site_controller.get_Stores_By_specific_area(area);
    }

//    public void addItemFiletotransport(List<Pair<String, Integer>> items, int store, int supplier) throws Buisness_Exception {
//        service.transportation_controller.addItemFiletotransport(items, store, supplier);
//    }

//    public void createRegularTransportation(Date date, int DepartureTime, int driver_id,
//                                            int truck_license_number, List<Integer> suppliers, List<Integer> stores)
//            throws Buisness_Exception{
//        service.transportation_controller.createRegularTransportation(date, DepartureTime, driver_id,
//                truck_license_number, suppliers, stores);
//    }

    public List<String> Show_transports() throws Buisness_Exception {
        return service.transportation_controller.Show_transports();
    }

    public List<String> getTransport_id() throws Buisness_Exception {
        return service.transportation_controller.getTransport_id();
    }

    public void delete_Transport(int id) throws Buisness_Exception {
        service.transportation_controller.delete_Transport(id);
    }

    public Pair<Date,Integer> Free_truck_and_driver(int transportationID) throws Buisness_Exception {
        return service.transportation_controller.Free_truck_and_driver(transportationID);
    }
    public Date gettransportationDate(int transportationID) throws Buisness_Exception {
        return service.transportation_controller.gettransportationDate(transportationID);
    }

    public boolean Change_truck_and_driver(int transportationID,int driver_id, int truck_id) {
        return service.transportation_controller.Change_truck_and_driver(transportationID, driver_id,truck_id);
    }

    public boolean Change_Back_truck_and_driver(int transportationID) {
        return service.transportation_controller.Change_Back_truck_and_driver(transportationID);
    }

    public List<String> store_list_by_transportationID(int transportationID) throws Buisness_Exception{
        return service.transportation_controller.store_list_by_transportationID(transportationID);
    }
    public List<String> supplier_list_by_transportationID(int transportationID) throws Buisness_Exception{
        return service.transportation_controller.supplier_list_by_transportationID(transportationID);
    }

    public String RemoveSites(int transportationID,Integer[] storesToRemove,Integer[] suppliersToRemove)
            throws Buisness_Exception{
        return  service.transportation_controller.RemoveSites(transportationID,storesToRemove,suppliersToRemove);
    }

    public boolean addsupplier(String name, String city, String street, Integer number,
                               String name_of_contact, String phone, String supplier_area) throws Buisness_Exception{
        return service.site_controller.addsupplier(name, city, street, number, name_of_contact, phone, supplier_area);
    }

    public void SetTruckWeight(int transportationId,double truckWeight) throws Buisness_Exception{
        service.transportation_controller.SetTruckWeight(transportationId,truckWeight);

    }

    public List<String> Show_supplier() throws Buisness_Exception {
        return service.site_controller.Show_supplier();
    }

    public boolean addTruck(int license_number, List<String> licenses_types,
                            String model, double weight, double max_weight) throws Buisness_Exception {
        return service.trucks_controller.addTruck(license_number, licenses_types, model, weight, max_weight);
    }

    public List<String> showtrucks() throws Buisness_Exception{
        return service.trucks_controller.showtrucks();
    }

    public void removeTruck(int id) throws Buisness_Exception{
         service.trucks_controller.removeTruck(id);
    }

    public List<String> Show_AreaList() throws Buisness_Exception {
        service.set_supplier_idCouter();
        return service.site_controller.Show_AreaList();
    }

    public List<String> Show_LicenseList() throws Buisness_Exception {
        service.set_truck_idCouter();
        return service.trucks_controller.Show_LicenseList();
    }

    public List<String> getAllDrivers(Date date,String shiftType,List<String> licenses, boolean lastCheck) throws Buisness_Exception{
        List<String> out = service.getWorkerController().getAllDrivers(date, shiftType, licenses);
        if(out.isEmpty() && lastCheck )
        {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String _date = formatter.format(date);
            SystemInterfaceWorkers.getInstance().transportationCheck(_date,shiftType);
            throw new Buisness_Exception("There are no Drivers available message send to HR Manager");
        }
        else return out;

    }

    public boolean isStoreKeeperAvailable(Date date,String shiftType, int storeSN, boolean lastCheck) throws Buisness_Exception{
        boolean out = service.getWorkerController().isStoreKeeperAvailable(date, shiftType, storeSN);
        if(!out && lastCheck )
        {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            String _date = formatter.format(date);
            SystemInterfaceWorkers.getInstance().transportationCheck(_date,shiftType);
            throw new Buisness_Exception("There are no Store Keeper available message send to HR Manager");
        }
        else return out;

    }

    public List<String> Show_shiftTypeList() throws Buisness_Exception {
        return service.transportation_controller.Show_shiftTypeList();
    }

    public String getShiftNameByID(int id) throws Buisness_Exception {
        return service.transportation_controller.getShiftNameByID(id);
    }

    public List<String> getTruckLicenseList(int truckID) throws Buisness_Exception {
        return service.trucks_controller.getTruckLicenseList(truckID);
    }

}
