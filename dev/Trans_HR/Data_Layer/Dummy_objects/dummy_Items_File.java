package Trans_HR.Data_Layer.Dummy_objects;

import javafx.util.Pair;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class dummy_Items_File {

    private int Sn;
    private int supplier_id;
    private int store_id;
    private int orderID;

    public dummy_Items_File(int Sn, int store_id, int supplier_id,int orderID){
        this.Sn=Sn;
        this.supplier_id=supplier_id;
        this.store_id=store_id;
        this.orderID =orderID;
    }

//    public dummy_Items_File(int SN, int store_id, int supplier_id){
//        this.Sn = SN;
//        this.store_id=store_id;
//        this.supplier_id=supplier_id;
//        this.orderID =orderID;
//
//    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public int getSn() {
        return Sn;
    }

    public void setSn(int sn) {
        Sn = sn;
    }

    public int getorderID() {
        return orderID;
    }

    public void setorderID(int orderID) {
        this.orderID = orderID;
    }
}
