package ModulesConntectionInterfaces;

import Sup_Inv.Suppliers.Structs.Days;

import java.util.Date;
import java.util.List;

public class PeriodicalOrderDTOforTransport extends RegularOrderDTOforTransport {

    public Date deliveryDate;

    public PeriodicalOrderDTOforTransport(int supplierId, int orderId, int shopID,
                                          List<Days> daysCanSupply,int areaID, Date date){
        super(supplierId, orderId, shopID, daysCanSupply,areaID);
        this.deliveryDate = date;
    }

    public Date getDate(){
        return deliveryDate;
    }

}