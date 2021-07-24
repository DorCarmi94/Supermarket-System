package Sup_Inv.Inventory.Logic;

import Sup_Inv.Inventory.Interfaces.Observer;
import Sup_Inv.Inventory.Interfaces.myObservable;
import Sup_Inv.Inventory.Persistence.DTO.DefectiveDTO;

import java.util.Date;
import java.util.List;

public class Defective implements myObservable {

    //region fields
    private String defId;
    private String itemId;
    private String shopNum;

    private int quantity;
    private Date updateDate;
    public final List<Observer> observers;
    private boolean expired = false;
    private boolean defective = false;
    //endregion

    //region constructors
    public Defective(List<Observer> observers, String defId, String itemId, int quantity, Date updateDate, Boolean expired, Boolean defective, String shopNum) {
        this.itemId = itemId;
        this.defId = defId;
        this.quantity = quantity;
        this.updateDate = updateDate;
        this.observers = observers;
        this.expired = expired;
        this.defective = defective;
        this.shopNum = shopNum;
    }

    public Defective(List<Observer> observers, DefectiveDTO DTO){
        this.defId = DTO.getDefId();
        this.itemId = DTO.getItemId();
        this.quantity = DTO.getQuantity();
        this.updateDate = DTO.getUpdateDate();
        this.expired = DTO.isExpired();
        this.defective = DTO.isDefective();
        this.shopNum = DTO.getShopNum();
        this.observers = observers;
    }

    //endregion

    //region getters
    public String getDefId() {
        return defId;
    }

    public String getItemId() {
        return itemId;
    }

    public String getShopNum() {
        return shopNum;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public boolean isExpired() {
        return expired;
    }

    public boolean isDefective() {
        return defective;
    }
    //endregion

    //region defective functions
    public void defectiveItemStatus() {
        String out = "|---------------\n" +
                "| itemId; " + itemId + "\n" +
                "| update Date; " + toPrint(updateDate) + "\t quantity = " + quantity + " ==>";
                if(expired && defective)
                    out += " expired AND has a defect";
                else
                {
                    if(expired)
                        out += " has expired";
                    else if(defective)
                        out += " has defect";
                }
                notifyObserver(out);
    }
    private String toPrint(Date costChangeDate) {
        String cost = costChangeDate.toString();
        String[] splited = cost.split(" ");
        return splited[0] + " " + splited[1] + " " + splited[2] + " " + splited[5];
    }
    public int getQuantity() {
        return quantity;
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
}
