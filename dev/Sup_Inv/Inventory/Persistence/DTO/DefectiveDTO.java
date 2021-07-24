package Sup_Inv.Inventory.Persistence.DTO;


import Sup_Inv.Inventory.Logic.Defective;

import java.util.Date;

public class DefectiveDTO {

    private String defId;
    private String itemId;
    private int quantity;
    private Date updateDate;
    private boolean expired;
    private boolean defective;
    private String shopNum;

    public DefectiveDTO(String defId, String itemId, String shopNum, int quantity, Date updateDate, boolean expired, boolean defective) {
        this.defId = defId;
        this.itemId = itemId;
        this.shopNum = shopNum;
        this.quantity = quantity;
        this.updateDate = updateDate;
        this.expired = expired;
        this.defective = defective;
    }

    public DefectiveDTO(Defective def) {
        this.defId = def.getDefId();
        this.itemId = def.getItemId();
        this.shopNum = def.getShopNum();
        this.quantity = def.getQuantity();
        this.updateDate = def.getUpdateDate();
        this.expired = def.isExpired();
        this.defective = def.isDefective();
    }

    //region getters&setters

    public String getDefId() {
        return defId;
    }

    public String getItemId() {
        return itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public boolean isExpired() {
        return expired;
    }

    public boolean isDefective() {
        return defective;
    }

    public String getShopNum() {
        return shopNum;
    }


    //endregion

}
