package Trans_HR.Interface_Layer.Workers;

import Trans_HR.Business_Layer.Controllers.Site_Controller;
import Trans_HR.Business_Layer.Service;
import Trans_HR.Business_Layer.Transportations.Utils.Buisness_Exception;
import Trans_HR.Business_Layer.Workers.Modules.Worker.Worker;
import Trans_HR.Business_Layer.Workers.Utils.InfoObject;
import Trans_HR.Data_Layer.Mapper;

public class SystemInterfaceWorkers {

    private static SystemInterfaceWorkers systemInterfaceWorkers = null;


    private SystemInterfaceWorkers() {
    }

    public static SystemInterfaceWorkers getInstance(){
        if(systemInterfaceWorkers == null){
            systemInterfaceWorkers = new SystemInterfaceWorkers();
        }
        return systemInterfaceWorkers;
    }

    public InfoObject printAllManagersAvailableInDates(String date, String shiftType) {
        return Service.getInstance().getWorkerController().printAllManagersAvailableInThisDate(date,shiftType);
    }
    public boolean isDriver(int SN){
        return Service.getInstance().isDriver(SN);

    }

    public void getStores(){
        Service.getInstance().site_controller.getStores();
    }

    public void getShifts(){
        Service.getInstance().getShiftController().getShifts();
    }
    public void getWorkers(){
        Service.getInstance().getWorkerController().getWorkers();
    }

    public void printAllWorkersFromThisShop(){
        Service.getInstance().getWorkerController().printAllWorkerForThisStore();
    }

    public void printAllShiftFromThisShop(){
        Service.getInstance().getShiftController().printAllShiftsForThisStore();
    }

    public InfoObject printAllWorkers(){
        return Service.getInstance().getWorkerController().printAllWorker();
    }

    public InfoObject printAllWorkersAvailableInDates(String date, String shiftType) {
        return Service.getInstance().getWorkerController().printAllWorkersAvailableInThisDate(date, shiftType);
    }

    public InfoObject createShift(String shiftType, int selectedManagerSn,String chosenWorkersSn,String date) {
        return Service.getInstance().getShiftController().createShift(shiftType, selectedManagerSn, chosenWorkersSn, date);
    }

    public InfoObject printShift(int shiftSn){
        return Service.getInstance().getShiftController().printShift(shiftSn);
    }

    public InfoObject addWorker(int workerId,String workerName,String workerPhoneNumber,int workerBankAccount,int workerSalary,String dateOfStart,String workerJobTitle,String constrains) {
        return Service.getInstance().getWorkerController().addWorker(workerId,workerName,workerPhoneNumber,workerBankAccount,workerSalary,dateOfStart,workerJobTitle,constrains);
    }

    public InfoObject addDriver(int workerId,String workerName,String workerPhoneNumber,int workerBankAccount,int workerSalary,String dateOfStart,String workerJobTitle,String constrains,String licenses) {
        return Service.getInstance().getWorkerController().addDriver(workerId,workerName,workerPhoneNumber,workerBankAccount,workerSalary,dateOfStart,workerJobTitle,constrains,licenses);
    }

    public InfoObject printWorkerBySn(int workerSn){
        return Service.getInstance().getWorkerController().printWorkerBySn(workerSn);
    }

    public InfoObject printWorkerConstrainsBySn(int workerSn){
        return Service.getInstance().getWorkerController().printWorkersConstrainsBySn(workerSn);
    }

    public InfoObject setNewSalaryBySn(int workerSn,int newSalary){
        return Service.getInstance().getWorkerController().setNewSalaryBySn(workerSn, newSalary);
    }

    public InfoObject removeWorkerBySn(int workerSn){
        return Service.getInstance().getWorkerController().removeWorkerBySn(workerSn);
    }

    public InfoObject resetWorkerConstrainsBySn(int workerSn){
        return Service.getInstance().getWorkerController().resetWorkerConstrainsBySn(workerSn);
    }

    public InfoObject printAllShifts(){
        return Service.getInstance().getShiftController().printAllShits();
    }

    public InfoObject editWorkerConstrainsBySn(int workerSn, String newConstrains){
        return Service.getInstance().getWorkerController().editWorkerConstrainsBySn(workerSn,newConstrains);
    }

    public InfoObject removeLaterShiftForFiredManagerByManagerSn(int workerSn) {
        return Service.getInstance().getShiftController().removeLaterShiftForFiredManagerByManagerSn(workerSn);
    }

    public boolean printAllStores() {
        if(!(Site_Controller.getInstance().printAllSites())){
            // empty
            System.out.println("No stores yet. add stores first");
            return false;
        }
        return true;
    }

    public boolean addNewStore( String name, String city, String street, String number, String name_of_contact, String phone, String site_area)  throws Buisness_Exception {
        return Site_Controller.getInstance().addsite("store",name,city,street,number,name_of_contact,phone,site_area);
    }

    public boolean setCurrentStore(int currentStore) {
        if(Site_Controller.getInstance().isStoreExcites(currentStore)) {
            Service.getInstance().getWorkerController().setCurrentStoreSN(currentStore);
            Service.getInstance().getShiftController().setCurrentStoreSN(currentStore);
            System.out.println("Welcome to " + Service.getInstance().getHashStoresMap().get(currentStore).getName());
        }else{
            System.out.println("There is no store with this SN");
            return false;
        }
        return true;
    }

    public InfoObject addNewLicense(int workerSn, String license) {

        return Service.getInstance().getWorkerController().addNewLicense(workerSn,license);
    }

    public void clearDB() {
        Service.getInstance().getWorkerController().clearDB();
    }

    public void getAllSN() {
        Service.getInstance().getWorkerController().getAllSN();
        Service.getInstance().getShiftController().getAllSN();
        Service.getInstance().site_controller.getAllSN();

    }

    public void initConstants() {
        Service.getInstance().initConstants();
    }

    public void transportationCheck(String date, String shiftType) {
        Service.getInstance().getShiftController().checkShiftWithDriverAndStoreKeeper(date,shiftType);
    }

}