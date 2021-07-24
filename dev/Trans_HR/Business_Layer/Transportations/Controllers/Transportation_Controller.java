package Trans_HR.Business_Layer.Transportations.Controllers;

import ModulesConntectionInterfaces.PeriodicalOrderDTOforTransport;
import ModulesConntectionInterfaces.RegularOrderDTOforTransport;
import ModulesConntectionInterfaces.TranspirationToSupplier;
import Sup_Inv.Inventory.Logic.Item;
import Trans_HR.Business_Layer.Modules.Site;
import Trans_HR.Business_Layer.Modules.Store;
import Trans_HR.Business_Layer.Modules.Supplier;
import Trans_HR.Business_Layer.Service;
import Trans_HR.Business_Layer.Transportations.Modules.ItemsFile;
import Trans_HR.Business_Layer.Transportations.Modules.Transportation;
import Trans_HR.Business_Layer.Transportations.Utils.Buisness_Exception;
import Trans_HR.Business_Layer.Workers.Utils.ShiftType;
import javafx.util.Pair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Transportation_Controller {

    private static class Singelton_Transport {
        private static Transportation_Controller instance = new Transportation_Controller();
    }

    private Transportation_Controller() {
        // initialization code..
    }

    public static Transportation_Controller getInstance() {
        return Singelton_Transport.instance;
    }

    List<ItemsFile> current = new LinkedList<ItemsFile>();
//    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");


    public List<String> Show_shiftTypeList() throws Buisness_Exception {
        Service service = Service.getInstance();
        List<String> output = new LinkedList<>();
        for(ShiftType shiftType : service.getshiftTypeList().values())
        {
            output.add(shiftType.getShiftTypeSN()+". "+shiftType.getShiftType());
        }
        if (output.isEmpty())
            throw new Buisness_Exception("There are no shiftType in the system\n");
        return output;
    }
    public String getShiftNameByID(int id) throws Buisness_Exception {
        Service service = Service.getInstance();
        if(!service.getshiftTypeList().containsKey(id))
            throw new Buisness_Exception("Error\n");
        return service.getshiftTypeList().get(id).getShiftType();
    }


    public String RemoveSites(int transportationID,Integer [] storesToRemove,Integer [] suppliersToRemove)
            throws Buisness_Exception{
        try{
            Service service = Service.getInstance();
            Transportation transportation = service.getHashTransportation().get(transportationID);
            List<Integer> suppliers = transportation.getSuppliers();
            List<Store> stores= transportation.getStores();
            if(suppliers.size()==suppliersToRemove.length)
            {
                return "cant remove all the suppliers";
            }
            else if(stores.size()==storesToRemove.length)
            {
                return "cant remove all the stores";
            }
            else {
                List<ItemsFile> itemsFiles= transportation.getItemsFiles();
                for (int id : storesToRemove)
                {
                    Store store= service.getHashStoresMap().get(id);
                    stores.remove(stores.indexOf(store));
                    service.removeStoreFromTransport(transportationID,id);
                    for (ItemsFile itemsFile:itemsFiles)
                    {
                        if(itemsFile.getStore().getId()==id)
                        {
                            itemsFiles.remove(itemsFiles.indexOf(itemsFile));
                            service.removeItemFileFromTransport(itemsFile.getId());
                        }
                    }
                }
                for (int id : suppliersToRemove)
                {
                    Supplier supplier= service.getSuppliersMap().get(id);
                    suppliers.remove(suppliers.indexOf(supplier));
                    service.removeSupplierFromTransport(transportationID,id);
                    for (ItemsFile itemsFile:itemsFiles)
                    {
                        if(itemsFile.getSupplier()==id)
                        {
                            itemsFiles.remove(itemsFiles.indexOf(itemsFile));
                            service.removeItemFileFromTransport(itemsFile.getId());
                        }
                    }
                }
                return "Ok";
            }
        }
        catch (Exception e)
        {
            throw new Buisness_Exception("-Error-\n");
        }

    }

    public List<String> supplier_list_by_transportationID(int transportationID) throws Buisness_Exception
    {
        try{
            Service service = Service.getInstance();
            TranspirationToSupplier transpirationToSupplier = TranspirationToSupplier.getInstance();
            service.upload_All_Supplier();
            service.upload_Transportation(transportationID);
            List<String> output= new LinkedList<>();
            Transportation transportation = service.getHashTransportation().get(transportationID);
            output.add("Suppliers:");
//            TODO: Check how to get the name
            for(Integer supplier:transportation.getSuppliers())
            {
                String line =supplier+". "+transpirationToSupplier.getSupplierInfo(supplier).supplierName+".";
                output.add(line);
            }
            for(Integer supplier:transportation.getSuppliers())
            {
                String line =supplier+". ";
                output.add(line);
            }
            return output;
        }
        catch (Exception e)
        {
            throw new Buisness_Exception("-Error-\n");
        }

    }


    public List<String> store_list_by_transportationID(int transportationID) throws Buisness_Exception
    {
        try{
            Service service = Service.getInstance();
            service.upload_Transportation(transportationID);
            service.upload_All_Supplier();
            List<String> output= new LinkedList<>();
            Transportation transportation = service.getHashTransportation().get(transportationID);
            output.add("Stores:");
            for(Store store:transportation.getStores())
            {
                String line = store.getId()+". "+store.getName()+".";
                output.add(line);
            }
            return output;
        }
        catch (Exception e)
        {
            throw new Buisness_Exception("-Error-\n");
        }
    }

    public Date gettransportationDate(int transportationID)throws Buisness_Exception {
        Service service = Service.getInstance();
        try {
            Transportation transportation = service.getHashTransportation().get(transportationID);
            return transportation.getDate();
        }
        catch (Exception e){
            throw new Buisness_Exception("-Error couldn't find Transportation-\n");
        }
    }


    public Pair<Date,Integer> Free_truck_and_driver(int transportationID) {
        Service service = Service.getInstance();
        try {
//            service.upload_Transportation(transportationID);
            Transportation transportation = service.getHashTransportation().get(transportationID);
            int driverID = transportation.getDriveId();
            int truckID = transportation.getTruck().getId();
//            service.remove_driver_transportatin(transportation.getId(),driverID);
            service.getDrivers().get(driverID).Remove_date(transportationID);
//            service.remove_truck_transportatin(transportation.getId(),driverID);
            service.getHashTrucks().get(truckID).Remove_date(transportationID);
            return new Pair<>(transportation.getDate(), transportation.getDepartureTime());
        }
        catch (Exception e){return null;}
    }

    public void SetTruckWeight(int transportationId,double truckWeight) throws Buisness_Exception
    {
        Service service = Service.getInstance();
        try {
            Transportation transportation = service.getHashTransportation().get(transportationId);
            if (transportation.getTruck().getMax_weight() > truckWeight) {
                service.setWeight_truck(transportationId, truckWeight);
                for(ItemsFile itemsFile : transportation.getItemsFiles())
                {
                    TranspirationToSupplier.getInstance().setOrderStatusAsScheduled(itemsFile.getorderID(),transportation.getDate());
                }

            }

            else
                throw new Buisness_Exception("-Truck weight exceeds the maximum allowed-\n");
        }
        catch (Exception e)
        {
            throw new Buisness_Exception("-Couldn't Set Truck Weight-\n");
        }

    }


    public boolean Change_truck_and_driver(int transportationID,int driver_id, int truck_id) {
        Service service = Service.getInstance();
        Transportation transportation = service.getHashTransportation().get(transportationID);
        int oldDriver = transportation.getDriveId();
        int oldTruck = transportation.getTruck().getId();
        try {

            service.Change_truck_and_driver( transportationID, driver_id,  truck_id);

//            transportation.setTruck(service.getHashTrucks().get(truck_id));
//            transportation.setDriver(service.getDrivers().get(driver_id));
//            service.getDrivers().get(driver_id).addDate(transportation);
//            service.add_transport_driver(transportation.getId(), transportation.getDriveId());
//            service.getHashTrucks().get(truck_id).addDate(transportation);
//            service.add_transport_Truck(transportation.getId(), transportation.getTruck().getId());
            return true;
        }
        catch (Exception e)
        {
            transportation.setTruck(service.getHashTrucks().get(oldTruck));
            transportation.setDriver(service.getDrivers().get(oldDriver));
            service.getDrivers().get(transportation.getDriveId()).addDate(transportation);
            service.add_transport_driver(transportation.getId(), transportation.getDriveId());
            service.getHashTrucks().get(transportation.getTruck().getId()).addDate(transportation);
            service.add_transport_Truck(transportation.getId(), transportation.getTruck().getId());
            return false;
        }

    }
    public boolean Change_Back_truck_and_driver(int transportationID) {
        Service service = Service.getInstance();
        try {
            Transportation transportation = service.getHashTransportation().get(transportationID);
            service.getDrivers().get(transportation.getDriveId()).addDate(transportation);
            service.getHashTrucks().get(transportation.getTruck().getId()).addDate(transportation);
            service.add_transport_driver(transportation.getId(), transportation.getDriveId());
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }


    public void createTransportation_Periodical_Order(Date date, int DepartureTime, int driver_id,
                                        int truck_id, List<Integer> suppliers, List<Integer> stores)  throws Buisness_Exception  {
        try {
            Service service = Service.getInstance();
            if(Transportation.getIdCounter()==0)
            {
                service.set_Transportation_idCouter();
            }
            if(ItemsFile.getIdCounter()==0)
            {
                service.set_ItemFile_idCouter();
            }

            List<Store> stores1 = new LinkedList<Store>();
            for (Store site : service.getHashStoresMap().values()) {
                if (stores.contains(site.getId()))
                    stores1.add(site);
            }
            Transportation transportation =
                    new Transportation(date, DepartureTime, service.getDrivers().get(driver_id),
                            service.getHashTrucks().get(truck_id), suppliers, stores1);

            TranspirationToSupplier transpirationToSupplier = TranspirationToSupplier.getInstance();
            List<PeriodicalOrderDTOforTransport> orderList = transpirationToSupplier.getPeriodicalOpenOrders();

            if (orderList.size() == 0) {
                throw new Buisness_Exception("There are no Periodical Orders");
            } else {
                for (PeriodicalOrderDTOforTransport order : orderList) {

                    if (stores.contains(order.getShopId()) && suppliers.contains(order.getSupplierId()) &&
                            formatter.format(date).equals(formatter.format(order.getDate()))) {

                        ItemsFile itemFile = new ItemsFile(
                                service.getHashStoresMap().get(order.getShopId()),
                                order.getSupplierId(),order.getOrderId());

                        itemFile.setTransportationID(transportation.getId());
                        itemFile.setFrom_missing_items();

                        service.add_ItemFile(itemFile);
                        transportation.addItemFile(itemFile);
                    }

                }
            }
            service.getHashTrucks().get(truck_id).addDate(transportation);
            service.getDrivers().get(driver_id).addDate(transportation);
            service.add_Transportation(transportation);
            for(ItemsFile itemsFile : transportation.getItemsFiles())
            {
                transpirationToSupplier.setOrderStatusAsShipped(itemsFile.getorderID(),transportation.getDate());
            }

        }
        catch (Exception e)
        {
            System.out.println(e);
            throw new Buisness_Exception("-Error couldn't create Transportation-\n");
        }


    }


    public void createTransportation_Regular_Open_Order(Date date, int DepartureTime, int driver_id,
                                     int truck_id, List<Integer> suppliers, List<Integer> stores)  throws Buisness_Exception  {
        try {
            Service service = Service.getInstance();
            if(Transportation.getIdCounter()==0)
            {
                service.set_Transportation_idCouter();
            }
            if(ItemsFile.getIdCounter()==0)
            {
                service.set_ItemFile_idCouter();
            }

            List<Store> stores1 = new LinkedList<Store>();
            for (Store site : service.getHashStoresMap().values()) {
                if (stores.contains(site.getId()))
                    stores1.add(site);
            }
            Transportation transportation =
                    new Transportation(date, DepartureTime, service.getDrivers().get(driver_id),
                            service.getHashTrucks().get(truck_id), suppliers, stores1);

            TranspirationToSupplier transpirationToSupplier = TranspirationToSupplier.getInstance();
            List<RegularOrderDTOforTransport> orderList = transpirationToSupplier.getRegularOpenOrders();

            if (orderList.size() == 0) {
                throw new Buisness_Exception("There are no Periodical Orders");
            } else {
                for (RegularOrderDTOforTransport order : orderList) {

                    if (stores.contains(order.getShopId()) && suppliers.contains(order.getSupplierId()) &&
                            formatter.format(date).equals(formatter.format(order.getDate()))) {
                        ItemsFile itemFile = new ItemsFile(
                                service.getHashStoresMap().get(order.getShopId()),
                                order.getSupplierId(),order.getOrderId());

                        itemFile.setTransportationID(transportation.getId());
                        itemFile.setFrom_missing_items();

                        service.add_ItemFile(itemFile);
                        transportation.addItemFile(itemFile);
                    }

                }
            }
            service.getHashTrucks().get(truck_id).addDate(transportation);
            service.getDrivers().get(driver_id).addDate(transportation);
            service.add_Transportation(transportation);
            for(ItemsFile itemsFile : transportation.getItemsFiles())
            {
                transpirationToSupplier.setOrderStatusAsShipped(itemsFile.getorderID(),transportation.getDate());
            }
        }
        catch (Exception e)
        {
            throw new Buisness_Exception("-Error couldn't create Transportation-\n");
        }


    }

//    public void createRegularTransportation(Date date, int DepartureTime, int driver_id,
//                                            int truck_license_number, List<Integer> suppliers, List<Integer> stores)
//            throws Buisness_Exception{
//        try {
//            Service service = Service.getInstance();
//            service.set_Transportation_idCouter();
//            service.set_ItemFile_idCouter();
//
//            List<Supplier> suppliers1 = new LinkedList<Supplier>();
//            List<Store> stores1 = new LinkedList<Store>();
//            for (Supplier site : service.getSuppliersMap().values()) {
//                if (suppliers.contains(site.getId()))
//                    suppliers1.add(site);
//            }
//
//            for (Store site : service.getHashStoresMap().values()) {
//                if (stores.contains(site.getId()))
//                    stores1.add(site);
//            }
//
//            Transportation transportation =
//                    new Transportation(date, DepartureTime, service.getDrivers().get(driver_id), service.getHashTrucks().get(truck_license_number), suppliers1, stores1);
//
//            for (ItemsFile itemsFile : current) {
//                itemsFile.setTransportationID(transportation.getId());
//                transportation.addItemFile(itemsFile);
//
//                service.add_ItemFile(itemsFile);
//            }
//            this.current = new LinkedList<>();
//            service.getDrivers().get(driver_id).addDate(transportation);
//            service.getHashTrucks().get(truck_license_number).addDate(transportation);
//
//            service.add_Transportation(transportation);
//
//        }
//        catch (Exception e)
//        {
//            throw new Buisness_Exception("-Error couldn't create Transportation-\n");
//        }
//    }


    public void delete_Transport(int transport_id) throws Buisness_Exception {
        try
        {
            Service service = Service.getInstance();
            TranspirationToSupplier transpirationToSupplier = TranspirationToSupplier.getInstance();
            service.upload_Transportation(transport_id);
            if (service.getHashTransportation().size() == 0) {
                throw new Buisness_Exception("There are no transportations to delete");
            }
//
//            if (!service.getHashTransportation().containsKey(transport_id)) {
//                throw new Buisness_Exception("The transport id doesnt exist");            }

            for (Map.Entry<Integer, Transportation> transport : service.getHashTransportation().entrySet()) {
                if (transport.getValue().getId() == transport_id) {
                    int driver = transport.getValue().getDriveId();
                    int truck = transport.getValue().getId();
                    service.remove_driver_from_transport(transport_id,driver);
//                    service.getDrivers().get(driver).Remove_date(transport_id);
                    service.remove_truck_from_transport(transport_id,truck);
//                    service.getHashTrucks().get(truck).Remove_date(transport_id);
//                    service.getHashTransportation().remove(transport.getKey());
                    List<ItemsFile> itemsFilesList =transport.getValue().getItemsFiles();
                    service.remove_transport(transport_id);
                    for(ItemsFile itemsFile : itemsFilesList)
                    {
                        transpirationToSupplier.setOrderStatusBackToOpen(itemsFile.getorderID());
                    }
                }
            }

        }
        catch (Exception e)
        {
            throw new Buisness_Exception("-Error couldn't delete Transportation-\n");
        }

    }

    public List<String> getTransport_id() throws Buisness_Exception {
        Service service = Service.getInstance();
        service.upload__all_Transportation();
        if (service.getHashTransportation().size() == 0) {
            throw new Buisness_Exception("There are no transportation's to delete" + "\n");
        } else {
            List<String> result = new LinkedList<>();
            for (Transportation transportation : service.getHashTransportation().values()) {
                if(!transportation.getStatus().toLowerCase().equals("canceled") &&
                        !transportation.getStatus().toLowerCase().equals("completed"))
                         result.add(transportation.getId().toString());
            }
            return result;
        }
    }

//    public void addItemFiletotransport(int store, int supplier, int orderID)throws Buisness_Exception {
//        try {
//            Service service = Service.getInstance();
//            service.set_ItemFile_idCouter();
//            ItemsFile itemsFile = new ItemsFile(service.getHashStoresMap().get(store), service.getSuppliersMap().get(supplier), orderID);
//            current.add(itemsFile);
//        }
//        catch (Exception e)
//        {
//            throw new Buisness_Exception("could not create item file");
//        }
//
//    }

    public List<String> Show_transports() throws Buisness_Exception  {
        Service service = Service.getInstance();
        List<String> output = new LinkedList<>();
        service.upload__all_Transportation();
        for (Transportation transportation : service.getHashTransportation().values()) {
            if(!transportation.getStatus().equals("Canceled"))
                output.add(transportation.toString());
        }
        if(output.isEmpty())
            throw new Buisness_Exception("There are no transports to show\n");
        return output;
    }

    public List<String> get_area_for_suppliers() throws Buisness_Exception {
        Service service = Service.getInstance();
        List<String> areas = new LinkedList<String>();
        service.upload_All_Supplier();
        if (service.getSuppliersMap().size() == 0) {
            throw new Buisness_Exception("There are no suppliers to make a transport");
        }
        for (Supplier supplier : service.getSuppliersMap().values()) {
            if (!areas.contains(supplier.getArea().getAreaName())) {
                areas.add(supplier.getArea().getAreaName());
            }
        }
        return areas;
    }

    public List<String> get_area_for_stores() throws Buisness_Exception {
        Service service = Service.getInstance();
        List<String> areas = new LinkedList<>();
        service.getAllStores();
        if (service.getHashStoresMap().size() == 0) {
            throw new Buisness_Exception("There are no stores to supply to");
        } else {
            for (Store store : service.getHashStoresMap().values()) {
                if (!areas.contains(store.getArea().getAreaName())) {
                    areas.add(store.getArea().getAreaName());
                }
            }
            return areas;
        }
    }

    //TODO:ASS 4

    public List<String> get_Dates_Periodical_Order() throws Buisness_Exception {

        TranspirationToSupplier transpirationToSupplier = TranspirationToSupplier.getInstance();
        List<String> output = new LinkedList<>();

        List<PeriodicalOrderDTOforTransport> orderList = transpirationToSupplier.getPeriodicalOpenOrders();
        if (orderList.size() == 0) {
            throw new Buisness_Exception("There are no Orders");
        } else {
            for (PeriodicalOrderDTOforTransport order : orderList) {
//                System.out.println(formatter.format(order.getDate()));
                if (!output.contains(formatter.format(order.getDate()))) {
                    output.add(formatter.format(order.getDate()));
                }
            }
            output.sort(String.CASE_INSENSITIVE_ORDER);
            return output;
        }
    }

    public List<String> get_Dates_Regular_Open_Order() throws Buisness_Exception {

        TranspirationToSupplier transpirationToSupplier = TranspirationToSupplier.getInstance();
        List<String> output = new LinkedList<>();

        List<RegularOrderDTOforTransport> orderList = transpirationToSupplier.getRegularOpenOrders();
        System.out.println("-----------"+orderList.size()+"-----------");
        if (orderList.size() == 0) {
            throw new Buisness_Exception("There are no Orders");
        } else {
            for (RegularOrderDTOforTransport order : orderList) {
                if (!output.contains(formatter.format(order.getDate()))) {
                    output.add(formatter.format(order.getDate()));
                }
            }
            output.sort(String.CASE_INSENSITIVE_ORDER);
            return output;
        }
    }

    public List<String> get_stores_by_date_Periodical_Order(Date date) throws Buisness_Exception {
        try
        {
            Service service = Service.getInstance();
            TranspirationToSupplier transpirationToSupplier = TranspirationToSupplier.getInstance();
            List<String> output = new LinkedList<>();
            List<PeriodicalOrderDTOforTransport> orderList = transpirationToSupplier.getPeriodicalOpenOrders();

            if (orderList.size() == 0) {
                throw new Buisness_Exception("There are no Orders");
            } else {
                service.getAllStores();
                if (service.getHashStoresMap().size() == 0) {
                    throw new Buisness_Exception("There are no stores to supply to");
                }
                for (PeriodicalOrderDTOforTransport order : orderList) {
                    if(formatter.format(date).equals(formatter.format(order.getDate())))
                    {
                        String store = service.getHashStoresMap().get(order.getShopId()).getName();
                        String line = order.getShopId() + ". " + "Name: " + store + ".";

                        if (!output.contains(line)) {
                            output.add(line);
                        }
                    }
                }
                output.sort(String.CASE_INSENSITIVE_ORDER);
                return output;
            }
        }
        catch (Exception e)
        {
            throw new Buisness_Exception("Store error");
        }
    }


    public List<String> get_stores_by_date_Regular_Open_Order(Date date) throws Buisness_Exception {
        try
        {
            Service service = Service.getInstance();
            TranspirationToSupplier transpirationToSupplier = TranspirationToSupplier.getInstance();
            List<String> output = new LinkedList<>();
            List<RegularOrderDTOforTransport> orderList = transpirationToSupplier.getRegularOpenOrders();

            if (orderList.size() == 0) {
                throw new Buisness_Exception("There are no Orders");
            } else {
                service.getAllStores();
                if (service.getHashStoresMap().size() == 0) {
                    throw new Buisness_Exception("There are no stores to supply to");
                }
                for (RegularOrderDTOforTransport order : orderList) {
                    if(formatter.format(date).equals(formatter.format(order.getDate())))
                    {
                        String store = service.getHashStoresMap().get(order.getShopId()).getName();
                        String line = order.getShopId() + ". " + "Name: " + store + ".";

                        if (!output.contains(line)) {
                            output.add(line);
                        }
                    }
                }
                output.sort(String.CASE_INSENSITIVE_ORDER);
                return output;
            }
        }
        catch (Exception e)
        {
            throw new Buisness_Exception("Store error");
        }
    }



    public List<String> get_area_for_suppliers_by_date_store_Periodical_Order(Date date, Integer storeID) throws Buisness_Exception {
        try
        {
            Service service = Service.getInstance();
            TranspirationToSupplier transpirationToSupplier = TranspirationToSupplier.getInstance();
            List<String> output = new LinkedList<>();
            List<PeriodicalOrderDTOforTransport> orderList = transpirationToSupplier.getPeriodicalOpenOrders();

            if (orderList.size() == 0) {
                throw new Buisness_Exception("There are no Orders");
            } else {
                for (PeriodicalOrderDTOforTransport order : orderList) {
                    if(storeID==order.getShopId() && formatter.format(date).equals(formatter.format(order.getDate())))
                    {
                        if (!output.contains(service.getArea_list().get(order.getSupplierArea()).getAreaName())) {
                            output.add(service.getArea_list().get(order.getSupplierArea()).getAreaName());
                        }
                    }
                }
                output.sort(String.CASE_INSENSITIVE_ORDER);
                return output;
            }
        }
        catch (Exception e)
        {
            throw new Buisness_Exception("Area error");
        }
    }

    public List<String> get_area_for_suppliers_by_date_store_Regular_Open_Order(Date date, Integer storeID) throws Buisness_Exception {
        try
        {
            Service service = Service.getInstance();
            TranspirationToSupplier transpirationToSupplier = TranspirationToSupplier.getInstance();
            List<String> output = new LinkedList<>();
            List<RegularOrderDTOforTransport> orderList = transpirationToSupplier.getRegularOpenOrders();

            if (orderList.size() == 0) {
                throw new Buisness_Exception("There are no Orders");
            } else {
                for (RegularOrderDTOforTransport order : orderList) {
                    if(storeID==order.getShopId() && formatter.format(date).equals(formatter.format(order.getDate())))
                    {
                        if (!output.contains(service.getArea_list().get(order.getSupplierArea()).getAreaName())) {
                            output.add(service.getArea_list().get(order.getSupplierArea()).getAreaName());
                        }
                    }
                }
                output.sort(String.CASE_INSENSITIVE_ORDER);
                return output;
            }
        }
        catch (Exception e)
        {
            throw new Buisness_Exception("Area error");
        }
    }

    public List<String> get_Suppliers_by_area_Periodical_Order(Date date, Integer storeID, String area) throws Buisness_Exception {
        try
        {
            Service service = Service.getInstance();
            TranspirationToSupplier transpirationToSupplier = TranspirationToSupplier.getInstance();
            List<String> output = new LinkedList<>();
            List<PeriodicalOrderDTOforTransport> orderList = transpirationToSupplier.getPeriodicalOpenOrders();

            if (orderList.size() == 0) {
                throw new Buisness_Exception("There are no Orders");
            } else {
                for (PeriodicalOrderDTOforTransport order : orderList) {
                    if(storeID==order.getShopId() && formatter.format(date).equals(formatter.format(order.getDate()))&&
                            area.equals(service.getArea_list().get(order.getSupplierArea()).getAreaName()))
                    {
                        String line = order.getSupplierId() + ". " + "Name: " +
                                transpirationToSupplier.getSupplierInfo(order.getSupplierId()).supplierName + ".";
                        if (!output.contains(line)) {
                            output.add(line);
                        }
                    }
                }
                output.sort(String.CASE_INSENSITIVE_ORDER);
                return output;
            }
        }
        catch (Exception e)
        {
            throw new Buisness_Exception("Area error");
        }
    }

    public List<String> get_Suppliers_by_area_Regular_Open_Order(Date date, Integer storeID, String area) throws Buisness_Exception {
        try
        {
            Service service = Service.getInstance();
            TranspirationToSupplier transpirationToSupplier = TranspirationToSupplier.getInstance();
            List<String> output = new LinkedList<>();
            List<RegularOrderDTOforTransport> orderList = transpirationToSupplier.getRegularOpenOrders();

            if (orderList.size() == 0) {
                throw new Buisness_Exception("There are no Orders");
            } else {
                for (RegularOrderDTOforTransport order : orderList) {
                    if(storeID==order.getShopId() && formatter.format(date).equals(formatter.format(order.getDate()))&&
                            area.equals(service.getArea_list().get(order.getSupplierArea()).getAreaName()))
                    {
                        String line = order.getSupplierId() + ". " + "Name: " +
                                transpirationToSupplier.getSupplierInfo(order.getSupplierId()).supplierName + ".";
                        if (!output.contains(line)) {
                            output.add(line);
                        }
                    }
                }
                output.sort(String.CASE_INSENSITIVE_ORDER);
                return output;
            }
        }
        catch (Exception e)
        {
            throw new Buisness_Exception("Area error");
        }
    }


}


