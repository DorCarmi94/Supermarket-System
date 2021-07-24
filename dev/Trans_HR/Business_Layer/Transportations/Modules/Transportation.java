package Trans_HR.Business_Layer.Transportations.Modules;

import Trans_HR.Business_Layer.Modules.Store;
import Trans_HR.Business_Layer.Modules.Supplier;
import Trans_HR.Business_Layer.Workers.Modules.Worker.Driver;
import javafx.util.Pair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;


public class Transportation {


    private static int idcounter = 0;

    private int id;
    private Date date;
    private int DepartureTime;
    private Truck truck;
    private List<Integer> suppliers;
    private List<Store> stores;
    private double weight_truck = -1;
    private Driver driver;
    private List<ItemsFile> itemsFiles;
//    TODO: status
//    Canceled
//    InProcess
//    Completed
    private String status;

    public Transportation(Date date, int DepartureTime, Driver drivers, Truck truck, List<Integer> suppliers, List<Store> stores) {
        this.id = idcounter++;
        this.date = date;
        this.DepartureTime = DepartureTime;
        this.driver = drivers;
        this.truck = truck;
        this.suppliers = suppliers;
        this.stores = stores;
        this.itemsFiles = new LinkedList<>();
        this.status= "InProcess";
    }

    public Transportation(int id, Date date, int DepartureTime, Driver drivers, Truck truck,String status) {
        this.id = id;
        this.date = date;
        this.DepartureTime = DepartureTime;
        this.driver = drivers;
        this.truck = truck;
        this.suppliers =new LinkedList<>();
        this.stores = new LinkedList<>();
        this.itemsFiles = new LinkedList<>();
        if(id>idcounter)
            idcounter=id+1;
        this.status = status;
    }

    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String dariverName = "";
        String truck = "";
        if (this.driver!=null)
            dariverName= "\n\tdriver: " + this.driver.getWorkerName();
        if (this.truck!=null)
            truck= "\n\ttruck- license_number:" + this.truck.getlicense_number()
                    + ", Model:" + this.truck.getModel();
        String output = "";
        output += this.id + ".\n\tdate: " + dateFormat.format(this.date) + "\n\tleaving_time: " + this.DepartureTime
                + dariverName + truck + "\n";
        output += "\tstores: ";
        for (Store sites : stores) {

            output = output + sites.getName() + ", ";
        }
        output += "\n\tsuppliers: ";
        for (Integer sites : suppliers) {
            output = output + sites + ", ";
        }
        output += "\n\titemsFiles: ";
        for (ItemsFile itemsFile: itemsFiles) {
            output = output +"\n\t"+itemsFile.getId()+". "+itemsFile.getSupplier()+"->"+itemsFile.getStore().getName()+":";
        }
        if(weight_truck!=-1)
        {
            output+="\ntruck weight= "+weight_truck;
        }
        output+="\nStatus= "+this.status;

        return output;

    }
    public static int getIdCounter(){
        return idcounter;
    }
    public static void setIdCounter(int id){
        if(idcounter<id)
            idcounter = id;
    }
    public String getStatus()
    {
        return this.status;
    }
    public void setStatusToCanceled()
    {
        this.status="Canceled";
    }
    public void setStatusToInProcess()
    {
        this.status="InProcess";
    }
    public void setStatusToInCompleted()
    {
        this.status="Completed";
    }

    public void setDriver(Driver driver)
    {
        this.driver=driver;
    }
    public void setTruck(Truck truck)
    {
        this.truck=truck;
    }

    public void setWeight_truck(double weight_truck)
    {
        this.weight_truck=weight_truck;
    }

    public Date getDate() {
        return date;
    }

    public List<Integer> getSuppliers() {
        return suppliers;
    }

    public int getDepartureTime() {
        return DepartureTime;
    }

    public Truck getTruck() {
        return truck;
    }


    public List<Store> getStores() {
        return stores;
    }

    public double getWeight_truck() {
        return weight_truck;
    }

    public Driver getDriver() {
        return driver;
    }

    public List<ItemsFile> getItemsFiles() {
        return itemsFiles;
    }

    public Integer getId() {
        return id;
    }

    public void addItemFile(ItemsFile file) {
        itemsFiles.add(file);
    }

    public int getDriveId() {
        return driver.getWorkerSn();
    }

    public int getTrucklicense() {
        return truck.getlicense_number();
    }
}
