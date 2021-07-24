package Trans_HR.Data_Layer;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import Trans_HR.Business_Layer.Modules.Address;
import Trans_HR.Business_Layer.Modules.Store;
import Trans_HR.Business_Layer.Service;
import Trans_HR.Business_Layer.Transportations.Utils.Buisness_Exception;
import Trans_HR.Business_Layer.Workers.Modules.Shift;
import Trans_HR.Business_Layer.Workers.Modules.Worker.Driver;
import Trans_HR.Business_Layer.Workers.Modules.Worker.Worker;
import Trans_HR.Business_Layer.Workers.Utils.enums;
import Trans_HR.Data_Layer.DAOs.*;
import Trans_HR.Data_Layer.Dummy_objects.*;
import javafx.util.Pair;


public class Mapper {
    private static Mapper instance;
    private worker_DAO worker_Mapper;
    private address_DAO address_Mapper;
    private area_DAO area_Mapper;
    private item_file_DAO itemFile_Mapper;
    private shift_DAO shift_Mapper;
    private store_DAO store_Mapper;
    private supplier_DAO supplier_Mapper;
    private transportation_DAO transportation_Mapper;
    private truck_DAO truck_Mapper;

    public static Mapper getInstance() {
        if(instance==null){
            instance = new Mapper();
        }
        return instance;
    }

    public Mapper() {
        worker_Mapper = new worker_DAO();
        address_Mapper = new address_DAO();
        area_Mapper = new area_DAO();
        itemFile_Mapper = new item_file_DAO();
        shift_Mapper = new shift_DAO();
        store_Mapper = new store_DAO();
        supplier_Mapper = new supplier_DAO();
        transportation_Mapper = new transportation_DAO();
        truck_Mapper = new truck_DAO();
    }

    public void insertWorker(int id, String name, String phoneNumber, int bankAccount, int salary, Date date, String jobTitle, int Sn, int storeSN){
        dummy_Worker toAdd = new dummy_Worker(Sn,id,name,phoneNumber,bankAccount,salary,storeSN,date,jobTitle);
        worker_Mapper.insert(toAdd);
    }

    public void insertDriver(int id, String name, String phoneNumber, int bankAccount, int salary, Date date, String jobTitle, int Sn,int storeSN){
        dummy_Worker toAdd = new dummy_Worker(Sn,id,name,phoneNumber,bankAccount,salary,storeSN,date,jobTitle);
        worker_Mapper.insert(toAdd);
/*        for(int x : licenses){
            worker_Mapper.insertLicense(toAdd.getSN(),x);
        }*/
    }

    public void deleteConstraints(int workerSN){
        worker_Mapper.deleteConstraints(workerSN);
    }

    public void updateSalary(int sn,int salary){
        worker_Mapper.updateSalary(sn,salary);
    }

    public void add_transport_driver(int Transport,int driver){
        transportation_Mapper.add_driver(Transport,driver);
    }

    public void add_transport_truck(int Transport, int truck){
        transportation_Mapper.add_Truck(Transport,truck);
    }

    public void remove_driver_transportatin(int Transport, int truck){
        transportation_Mapper.remove_Driver(Transport,truck);
    }

    public void remove_truck_transportatin(int Transport, int truck){
        transportation_Mapper.remove_Truck(Transport,truck);
    }

    public void addConstraints(int workerSn,int selectedDay,int sType){
        worker_Mapper.addConstraints(workerSn, selectedDay,sType);
    }

    public void insertLicense(int driverSN,int license){
        worker_Mapper.insertLicense(driverSN,license);
    }

    public void insert_Shift_Workers(int workerSn,int shiftSN){
        shift_Mapper.insert_Shift_Workers(workerSn,shiftSN);

    }

    public void insertShift(Date date, int shiftType, int manager,int SN, int Branch){
        dummy_Shift toAdd = new dummy_Shift(date,shiftType,manager,SN,Branch);
        shift_Mapper.insert(toAdd);
    }



    public void insertAddress(int Sn,String city, String street, int number){
        dummy_Address toAdd = new dummy_Address(Sn,city, street, number);
        address_Mapper.insert(toAdd);
    }


    public Integer getNextSNAddress(){
        return address_Mapper.getNextSN();
    }

    public List<dummy_Address> selectAllAddress(){
        return address_Mapper.selectAll();
    }

    public dummy_Address selectAddress(int SN){
        return address_Mapper.select(SN);
    }
    //Area
    public List<dummy_Area> selectAllArea(){
        return area_Mapper.selectAll();
    }
    //Supplier
    public void insertSupplier(int SN,String name, String Phone, String ContactName, int AddressSN, int AreaSN ,String city, String street, int number){
        insertAddress(AddressSN,city, street, number);
        dummy_supplier toAdd = new dummy_supplier(SN, name, Phone, ContactName, AddressSN, AreaSN, city, street, number);
         supplier_Mapper.insert(toAdd);
    }

    public List<dummy_supplier> selectAllSuppliers(){
        List<dummy_supplier> output = supplier_Mapper.selectAll();
        for (dummy_supplier s : output)
        {
            s.setDummy_address(selectAddress(s.getAddress_Sn()));

        }
        return output;
    }

    public dummy_supplier selectSupplier(int SN){
        dummy_supplier supplier = supplier_Mapper.select(SN);
        if(supplier!=null)
            supplier.setDummy_address(selectAddress(supplier.getAddress_Sn()));
        return supplier;
    }

    public Integer getNextSNSupplier(){
        return supplier_Mapper.getNextSN();
    }
    //Truck
    public void insertTruck(int SN,int license_number, String model, double weight, double max_weight, List<Integer> license_type){
        dummy_Truck toAdd = new dummy_Truck(SN,license_number, model, weight, max_weight, license_type);
        truck_Mapper.insert(toAdd);
    }

    public void deleteTruck(int SN) throws Buisness_Exception {
        truck_Mapper.delete(SN);
    }

    public List<dummy_Truck> selectAllTrucks(){
        return truck_Mapper.selectAll();
    }

    public dummy_Truck selectTruck(int SN){
        return truck_Mapper.select(SN);
    }

    public List<Integer> selectTransportationTrucks(int SN) {
        return truck_Mapper.selectTransportation(SN);
    }

    public Integer getNextSNTruck(){
        return truck_Mapper.getNextSN();
    }
    //License
    public List<dummy_License> selectAllLicense(){
        return truck_Mapper.selectAllLicense();
    }

    public void insertTransportation(int Sn, Date date, int leaving_time, double truck_weight,
                                     int trucksn, int Driver, List<Integer> suppliers,
                                     List<Integer> stores, List<Integer> itemsFile,String status){
        dummy_Transportation toAdd = new dummy_Transportation( Sn,date, leaving_time, truck_weight,
                trucksn,Driver,suppliers, stores,itemsFile,status);
        transportation_Mapper.insert(toAdd);
    }

    public dummy_Transportation selectTransportation(int SN){
        return transportation_Mapper.select(SN);
    }

    public Integer getNextSNTransportation(){
        return transportation_Mapper.getNextSN();
    }

    public List<dummy_Transportation> select_all_Transportation(){
        return transportation_Mapper.selectAll();
    }

    public void deleteDriverTransportation(int sn) {
        transportation_Mapper.deleteDriver(sn);
    }
    public void deleteTruckTransportation(int sn) {
        transportation_Mapper.deleteTruck(sn);
    }

    public void updateTruckWeightTransportation(int SN, double wieght){
        transportation_Mapper.updateTruckWeight(SN, wieght);
    }

    public void remove_transport(int sn){
        List<Integer> fileToRemove = transportation_Mapper.select_items_files(sn);
        transportation_Mapper.delete(sn);
        for (int id:fileToRemove)
        {
            deleteItemfile(id);
        }
    }

    public void remove_StoreFromTransport(int transportationID,int store){
        transportation_Mapper.deleteStore( transportationID, store);

    }
    public void remove_SupplierFromTransport(int transportationID,int Supplier){
        transportation_Mapper.deleteSupplier( transportationID, Supplier);

    }

  /*  //Worker
    public List<dummy_Worker> get_drivers(){ //TODO implement this
        return worker_Mapper.
    } */

    //Itemfile
    public void insertItemfile(int Sn, int supplier_id,int store_id, int orderID){
        dummy_Items_File toAdd = new dummy_Items_File(Sn,store_id,supplier_id, orderID);
        itemFile_Mapper.insert(toAdd);
    }

    public void deleteItemfile(int SN){
        itemFile_Mapper.delete(SN);
    }

    public dummy_Items_File selectItemfile(int SN){
        return itemFile_Mapper.select(SN);
    }

    public Integer getNextSNItemfile(){
        return itemFile_Mapper.getNextSN();
    }

    //MissingItem
//    public void insertMissingItem(int id, int store_id, int supplier_id,List<Pair<String,Integer>> missing){
//        dummy_Missing_items toAdd = new dummy_Missing_items(id,store_id,supplier_id,missing);
//        missingItems_Mapper.insert(toAdd);
//    }

//    public List<dummy_Missing_items> selectAllMissing_items(){
//        return missingItems_Mapper.selectAll();
//    }


    public void deleteAddress(int SN){
        address_Mapper.delete(SN);
    }

    public void insertAddress(String city, String street, int number,int SN){
        dummy_Address toAdd = new dummy_Address(city, street, number,SN);
        address_Mapper.insert(toAdd);
    }

    public void insertStore(String phone, String contact_name, String name, int id, String city, String street, int number, int Adrress_Sn, int AreaSn){
        dummy_store toAdd = new dummy_store(phone,contact_name, name, id,city,street,number,Adrress_Sn,AreaSn);
        store_Mapper.insert(toAdd);
    }

    public Address selectAddressBySN(int Address){
        dummy_Address address = address_Mapper.select(Address);
        return new Address(address.getCity(),address.getStreet(),address.getNumber(),address.getSN());
    }

    public dummy_store selectStore(int SN){
        dummy_store store = store_Mapper.select_by_storeId(SN);
        return store;
    }

//    public List<dummy_store> select_all_stores(){
//        dummy_store store=new dummy_store("054","Reut","store11",5,"alon","alona",5,1,1);
//        store_Mapper.insert(store);
//        return store_Mapper.select();
//    }

    public void getAllStores(){
       List<dummy_store> dummy_Stores = store_Mapper.select();

       for(dummy_store store: dummy_Stores){
//           System.out.println("-------------------");
           Address address = selectAddressBySN(store.getAddress_Sn());
           Store toADD = new Store(store.getName(),store.getPhone(),store.getContact_name(),address, Service.getInstance().getArea_list().get(store.getAreaSn()),store.getId());
           Service.getInstance().getHashStoresMap().putIfAbsent(toADD.getId(),toADD);
       }
    }

    private enums shiftTypeToEnum(int shiftType) {
        String DBshiftType="";
        switch (shiftType) {
            case 1:
                DBshiftType = "MORNING";
                break;

            case 2:
                DBshiftType = "NIGHT";
                break;
        }
        return enums.valueOf(DBshiftType);
    }

    private enums  dayToEnum(Integer day) {

        String DBday="";
        switch (day){
            case 1:
                DBday ="SUNDAY";
                break;

            case 2:
                DBday ="MONDAY";
                break;

            case 3:
                DBday ="TUESDAY";
                break;

            case 4:
                DBday ="WEDNESDAY";
                break;

            case 5:
                DBday ="THURSDAY";
                break;

            case 6:
                DBday ="FRIDAY";
                break;

            case 7:
                DBday ="SATURDAY";
                break;

        }

        return enums.valueOf(DBday);
    }

    public void getAllDrivers(){
        List<dummy_Worker> drivers = worker_Mapper.selectAllDrivers();
        List<Worker> workersToAdd = new LinkedList<>();

        for(dummy_Worker driverToAdd : drivers){
            workersToAdd.add(getWorker(driverToAdd.getStoreSN(),driverToAdd.getSN()));
        }

        for(Worker driverToAdds : workersToAdd){
            Service.getInstance().getDrivers().putIfAbsent(driverToAdds.getWorkerSn(), (Driver) driverToAdds);
        }

    }

    public Worker getWorker(int StoreSN, int workerSN){
        Worker worker = null;
        if(Service.getInstance().getWorkerList(StoreSN).containsKey(workerSN)){ //worker already exists
            return Service.getInstance().getWorkerList(StoreSN).get(workerSN);
        } //
        dummy_Worker toADD = worker_Mapper.selectWorkerBySN(workerSN); //get the worker from db
        worker = new Worker(toADD.getId(),toADD.getName(),toADD.getPhone(),toADD.getBankAccount(),toADD.getSalary(),
                toADD.getStart_Date(),toADD.getJob_title(),workerSN,StoreSN);
        if( toADD.getJob_title().toUpperCase().equals("DRIVER")){ //if driver add licenses
            String newLicense = "";
            List<Integer> licenses = worker_Mapper.selectDriverLicenseByWorkerSN(workerSN);
            if(licenses.contains(1)){
                newLicense = "C";
                if(licenses.contains(2)){
                    newLicense = "C,C1";
                }
            }else if(licenses.contains(2)){
                newLicense="C1";
            }
            Driver driver = new Driver(toADD.getId(),toADD.getName(),toADD.getPhone(),toADD.getBankAccount(),toADD.getSalary(),
                    toADD.getStart_Date(),toADD.getJob_title(),workerSN,StoreSN,newLicense);
            worker = driver;
            Service.getInstance().getDrivers().putIfAbsent(workerSN,driver);
        } //add constraints
        List<Pair<Integer,Integer>> constraints = worker_Mapper.selectConstrainsByWorkerSN(workerSN);
        for(Pair x: constraints){
            worker.addConstrainsToWorker(dayToEnum((int)x.getKey()),shiftTypeToEnum((int)x.getValue()));
        }
        Service.getInstance().getWorkerList().putIfAbsent(workerSN,worker);
        return worker;
    }

    public List<Worker> getShiftsWorker(int StoreSN,List<Integer> workers){
        List<Worker> workers1 = new LinkedList<>();
        for(int worker: workers){
            workers1.add(getWorker(StoreSN,worker));
        }
        return workers1;
    }

    public void getAllShifts(int storeSn){
        List<dummy_Shift> dummy_Shifts = shift_Mapper.selectShiftByStoreSN(storeSn);
        for(dummy_Shift shift: dummy_Shifts){
//            Date shiftDate = shift.getDate();
//            enums shiftType = shiftTypeToEnum(shift.getShift_type());
//            Worker manager = getWorker(storeSn,shift.getManager());
//            List<Worker> listOfWorkers = getShiftsWorker(storeSn,shift.getShift_workers());
//            int shiftSN = shift.getSn();
//            int storeSN = storeSn;

            Shift shift1 = new Shift(shift.getDate(),shiftTypeToEnum(shift.getShift_type()),getWorker(storeSn,shift.getManager()),
                    getShiftsWorker(storeSn,shift.getShift_workers()),shift.getSn(),storeSn);
            Service.getInstance().getShiftHistory().putIfAbsent(shift1.getShiftSn(),shift1);
        }
    }

    public void deleteWorker( int workerSn){
        worker_Mapper.deleteEmployee(workerSn);
    }

    public void deleteManager( int workerSn){
        worker_Mapper.deleteManager(workerSn);
    }
    // area license shifttype
    public void init(){
        area_Mapper.insert(1,"A");
        area_Mapper.insert(2,"B");
        area_Mapper.insert(3,"C");
        area_Mapper.insert(4,"D");
        worker_Mapper.initLicense();
        worker_Mapper.initShiftType();
    }

    public void getAllWorkersByStore(int StoreSN) {
        List<dummy_Worker> workers = worker_Mapper.selectWorkersByStoreSN(StoreSN);
        for(dummy_Worker worker: workers){
            Worker worker1 = getWorker(StoreSN,worker.getSN());
            Service.getInstance().getWorkerList().putIfAbsent(worker1.getWorkerSn(),worker1);
        }
    }
    //in area license shift_type
    public void clearDB() {
        //DELETE EVERYTHING
        shift_Mapper.deleteAllShift_Worker();
        worker_Mapper.deleteAllDriverLicense();
        worker_Mapper.deleteAllConstraints();
        worker_Mapper.deleteAllWorkers();
        shift_Mapper.deleteAllShifts();
        store_Mapper.deleteAllStores();
        address_Mapper.deleteAll();
        area_Mapper.deleteAll();
        worker_Mapper.deleteLicenseType();
        worker_Mapper.deleteShiftType();
    }

    public int getstoreSN() {
        return store_Mapper.getstoreSN();
    }

    public int getAddressSn() {
        return address_Mapper.getAddressSn();
    }

    public int getWorkerSn() {
        return worker_Mapper.getWorkerSn();
    }

    public int getShiftSn() {
        return shift_Mapper.getShiftSn();
    }
}
