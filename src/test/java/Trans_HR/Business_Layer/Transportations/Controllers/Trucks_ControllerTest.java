package Trans_HR.Business_Layer.Transportations.Controllers;

import Sup_Inv.Suppliers.Supplier.SupplierSystem;
import Trans_HR.Business_Layer.Service;
import Trans_HR.Business_Layer.Transportations.Modules.Truck;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class Trucks_ControllerTest {

    public Service service = Service.getInstance();
    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @Test
    public void addTruck() {
        int size_before = service.getHashTrucks().size();
        List<String> list = new LinkedList<>();
        list.add("C");
        try {
            Trucks_Controller.getInstance().addTruck(212,list,"volvo",100,150);
            assertEquals(size_before + 1,  service.getHashTrucks().size());
        }
        catch (Exception e)
        {

        }
    }

    @Test
    public void removeTruck() {
        service.set_truck_idCouter();
        int truckID= Truck.getIdCounter() -1;
        int size_before = service.getHashTrucks().size();
        try {
            Trucks_Controller.getInstance().removeTruck(truckID);
            assertEquals(size_before - 1,  service.getHashTrucks().size());
        }
        catch (Exception e)
        {

        }
    }
}