package Trans_HR.Business_Layer.Transportations.Modules;


import Trans_HR.Business_Layer.Modules.Store;
import Trans_HR.Business_Layer.Modules.Supplier;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ItemsFile {

    private static int idcounter = 0;
    private int transportationID=-1;
    private Store store;
    private Integer supplier;
    boolean from_missing_items=false;
    private int id;
    private int orderID;

    public ItemsFile(Store store,Integer supplier, int orderID)
    {
        this.id=idcounter++;
        this.orderID =orderID;
        this.store=store;
        this.supplier=supplier;
    }

    public ItemsFile(int id, Store store,Integer supplier, int orderID)
    {
        this.id=id;
        this.orderID =orderID;
        this.store=store;
        this.supplier=supplier;
    }

    public static int getIdCounter(){
        return idcounter;
    }
    public static void setIdCounter(int id){
        if(idcounter<id)
            idcounter = id;
    }


    public int getId(){
        return this.id;
    }
    public int getorderID(){
        return this.orderID;
    }
    public Integer getSupplier() {return this.supplier;}
    public Store getStore(){return this.store;}
    public void setTransportationID(int id)
    {
        this.transportationID=id;
    }
    public void setFrom_missing_items()
    {
        this.from_missing_items=true;
    }


}
