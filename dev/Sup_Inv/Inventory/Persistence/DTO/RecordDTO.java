package Sup_Inv.Inventory.Persistence.DTO;


import Sup_Inv.Inventory.Logic.Record;

import java.util.Date;

public class RecordDTO {

    private String recId;
    private String itemId;
    private String shopNum;
    private double cost;
    private Date costChangeDate;
    private double price;
    private Date priceChangeDate;


    public RecordDTO(String recId, String itemId, String shopNum, double cost, Date costChangeDate, double price, Date priceChangeDate) {
        this.recId = recId;
        this.itemId = itemId;
        this.shopNum = shopNum;
        this.cost = cost;
        this.costChangeDate = costChangeDate;
        this.price = price;
        this.priceChangeDate = priceChangeDate;
    }

    public RecordDTO(Record rec) {
        this.recId = rec.getRecId();
        this.itemId = rec.getItemId();
        this.shopNum = rec.getShopNum();
        this.cost = rec.getCost();
        this.costChangeDate = rec.getCostChangeDate();
        this.price = rec.getPrice();
        this.priceChangeDate = rec.getPriceChangeDate();
    }

    // region getters&setters

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getShopNum() {
        return shopNum;
    }

    public void setShopNum(String shopNum) {
        this.shopNum = shopNum;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Date getCostChangeDate() {
        return costChangeDate;
    }

    public void setCostChangeDate(Date costChangeDate) {
        this.costChangeDate = costChangeDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getPriceChangeDate() {
        return priceChangeDate;
    }

    public void setPriceChangeDate(Date priceChangeDate) {
        this.priceChangeDate = priceChangeDate;
    }

    //endregion

}
