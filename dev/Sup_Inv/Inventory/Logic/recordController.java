package Sup_Inv.Inventory.Logic;

import Sup_Inv.Inventory.Interfaces.Observer;
import Sup_Inv.Inventory.Interfaces.myObservable;
import Sup_Inv.Inventory.Persistence.DTO.RecordDTO;
import Sup_Inv.Inventory.Persistence.Mappers.RecordsMapper;
import Sup_Inv.Inventory.View.InvService;
import Sup_Inv.DataAccess.SupInvDBConn;
import Sup_Inv.Suppliers.Service.OrderDTO;
import Sup_Inv.Suppliers.Service.ProductInOrderDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class recordController implements myObservable {

    //region fields
    private HashMap<String, List<Record>> records; //item id, records(price&cost)
    public final List<Observer> observers;
    private String shopNum;
    private RecordsMapper myRecordMapper;
    private int recIdCounter = 0;

    //endregion

    //region constructor
    public recordController(Observer o, String shopNum) {
        this.records = new HashMap<>();
        observers = new ArrayList<>();
        this.register(o);
        this.shopNum = shopNum;
        this.myRecordMapper = new RecordsMapper(SupInvDBConn.getInstance().getConn());
    }
    public HashMap<String, List<Record>> getRecords() {
        return records;
    }
    //endregion

    //region record update
    public void updateRecordsSuppliers(OrderDTO order, Inventory inv, InvService invService) {
        Record newRecord;
        double newPrice;
        for (ProductInOrderDTO prod : order.productInOrderDTOList) {
            String id = String.valueOf(prod.barcode);
            if(records.containsKey(id)){
                List<Record> currList = records.get(id);
                Record lastRecord = currList.get(currList.size()-1);
                double oldCost = lastRecord.getCost();
                double newCost = prod.price;
                if(newCost != oldCost) {
                    String[] lastRecordInfo = new String[4];
                    lastRecordInfo[0] = lastRecord.getRecId();
                    lastRecordInfo[1] = lastRecord.getItemId();
                    lastRecordInfo[2] = String.valueOf(lastRecord.getPrice());
                    lastRecordInfo[3] = String.valueOf(lastRecord.getPriceChangeDate());
                    newPrice = inv.askUserPrice(newCost, oldCost, lastRecordInfo, invService);
                    if(newPrice != lastRecord.getPrice())
                        newRecord = new Record(observers, String.valueOf(recIdCounter++), lastRecordInfo[1],
                                                newCost, new Date(System.currentTimeMillis()),
                                                newPrice, new Date(System.currentTimeMillis()), shopNum);
                    else
                        newRecord = new Record(observers, String.valueOf(recIdCounter++), lastRecordInfo[1],
                                                newCost, new Date(System.currentTimeMillis()),
                                                newPrice, lastRecord.getPriceChangeDate(), shopNum);
                    records.get(id).add(newRecord);
                    myRecordMapper.insert(new RecordDTO(newRecord));
                }
            }
            else {
                newRecord = new Record(observers, String.valueOf(recIdCounter++), id, prod.price, new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()), shopNum);
                records.put(id, new ArrayList<>());
                records.get(id).add(newRecord);
                myRecordMapper.insert(new RecordDTO(newRecord));
            }
        }
    }

    public String[] getLastRecInfo(String id) {
        String[] lastRecInfo = new String[2];
        Record lastRec;
        List<Record> recList = records.get(id);
        if (recList != null && !recList.isEmpty()){
            lastRec = recList.get(recList.size() - 1);
        lastRecInfo[0] = lastRec.getItemId();
        lastRecInfo[1] = String.valueOf(lastRec.getPrice());
        return lastRecInfo;
    }
        System.out.println("wrong id");
        return null;
    }

    public void setNewPrice(String id, String newPrice, String nameLast, String priceLast) {

        double dNewPrice = Double.parseDouble(newPrice);
        double oldPrice = Double.parseDouble(priceLast);
        List<Record> recList = records.get(id);
        Record lastRec = recList.get(recList.size()-1);
        Record newRecord = new Record(observers, id, nameLast, lastRec.getCost(), new Date(System.currentTimeMillis()),
                dNewPrice , new Date(System.currentTimeMillis()), shopNum);
        records.get(id).add(newRecord);
        myRecordMapper.insert(new RecordDTO(newRecord));
        if(dNewPrice  < oldPrice)
            notifyObserver("(~:\tNew Sale\t:~)");
        else
            notifyObserver(")~:\tExpensive you mtfkr\t:~(");
        newRecord.recordItemStatus();
    }
    //endregion

    //region record reports
    public void getRecordsReport(String id) {
        if (!records.containsKey(id))
            notifyObserver("this id doesnt exist in the shop");
        else {
            List<Record> recordLst = records.get(id);
            for (Record r : recordLst)
                r.recordItemStatus();
        }
    }
    public void getGeneralRecordsReport() {
        for(String id : records.keySet())
            for (Record r : records.get(id))
                r.recordItemStatus();
    }
    //endregion

    //region observer
    @Override
    public void register(Observer o) {
        observers.add(o);
    }
    @Override
    public void notifyObserver(String msg) {
        observers.forEach(o -> o.onEvent(msg));
    }



    //endregion

    public void loadRecordsFromDB(String shopNum) {
        HashMap<String, List<RecordDTO>> recordsDTO = null;
        recordsDTO = myRecordMapper.load(shopNum);
        for (String id : recordsDTO.keySet()) {
            List <Record> currRecords = new ArrayList<>();
            for (RecordDTO currDTORec : recordsDTO.get(id)) {
                Record rec = new Record(observers, currDTORec);
                currRecords.add(rec);
            }
            records.put(id, currRecords);
            recIdCounter++;
        }
    }
}