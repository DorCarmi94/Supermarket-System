package ModulesConntectionInterfaces;

import Sup_Inv.Suppliers.Structs.Days;
import Sup_Inv.Suppliers.Structs.StructUtils;

import java.util.Date;
import java.util.List;

public class RegularOrderDTOforTransport {

    protected int supplierId;
    protected int shopID;
    protected int orderId;
    protected List<Days> daysCanSupply;
    protected int supplierArea;


    public RegularOrderDTOforTransport(int supplierId, int orderId, int shopID, List<Days> daysCanSupply,int supplierArea){
        this.supplierId = supplierId;
        this.shopID = shopID;
        this.orderId = orderId;
        this.daysCanSupply = daysCanSupply;
        this.supplierArea=supplierArea;
    }

    public int getSupplierId(){
        return supplierId;
    }

    public int getOrderId(){
        return orderId;
    }

    public int getShopId(){
        return shopID;
    }

    public Date getDate(){
        return StructUtils.getTheNearestDate(daysCanSupply);
    }

    public int getSupplierArea() {
        return supplierArea;
    }
}