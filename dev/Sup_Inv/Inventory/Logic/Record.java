package Sup_Inv.Inventory.Logic;

import Sup_Inv.Inventory.Interfaces.Observer;
import Sup_Inv.Inventory.Interfaces.myObservable;
import Sup_Inv.Inventory.Persistence.DTO.RecordDTO;
import java.util.Date;
import java.util.List;

public class Record implements myObservable {

    //region fields
    private String recId;
    private String itemId;
    private String shopNum;
    private double cost;
    private Date costChangeDate;
    private double price;
    private Date priceChangeDate;
    public final List<Observer> observers;


    public Record(List<Observer> observers, RecordDTO currDTORec) {
        this.recId = currDTORec.getRecId();
        this.itemId = currDTORec.getItemId();
        this.shopNum = currDTORec.getShopNum();
        this.cost = currDTORec.getCost();
        this.costChangeDate = currDTORec.getCostChangeDate();
        this.price = currDTORec.getPrice();
        this.priceChangeDate = currDTORec.getPriceChangeDate();
        this.observers = observers;
    }
    //endregion

    //region getters
    public Date getPriceChangeDate() {
        return priceChangeDate;
    }
    public Date getCostChangeDate() {
        return costChangeDate;
    }
    public double getCost() {
        return cost;
    }
    public String getRecId() {
        return recId;
    }
    public String getItemId() {
        return itemId;
    }
    public double getPrice() {
        return price;
    }
    public String getShopNum() { return shopNum; }
    //endregion

    public Record(List<Observer> observers, String id, String itemId, double cost, Date costChangeDate, Date priceChangeDate, String shopNum) {
        this.recId = id;
        this.itemId = itemId;
        this.cost = cost;
        this.costChangeDate = costChangeDate;
        this.price = cost*1.75; //default price
        this.priceChangeDate = priceChangeDate;
        this.observers = observers;
        this.shopNum = shopNum;
    }
    public Record(List<Observer> observers, String id, String itemId, double cost, Date costChangeDate, double newPrice, Date priceChangeDate, String shopNum) {
        this.recId = id;
        this.itemId = itemId;
        this.cost = cost;
        this.costChangeDate = costChangeDate;
        this.price = newPrice; //new price
        this.priceChangeDate = priceChangeDate;
        this.observers = observers;
        this.shopNum = shopNum;
    }

    public void recordItemStatus() {
        notifyObserver("|-------\n" +
                "| item id; " + itemId + "\n" +
                "| cost; " + cost +
                " \tChange Date; " + toPrint(costChangeDate) + "\n" +
                "| price; " + price +
                " \tChange Date; " + toPrint(priceChangeDate) + "\n" +
                "|-------\n");
    }

    private String toPrint(Date costChangeDate) {
        String cost = costChangeDate.toString();
        String[] splited = cost.split(" ");
        return splited[0] + " " + splited[1] + " " + splited[2] + " " + splited[5];
    }

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
