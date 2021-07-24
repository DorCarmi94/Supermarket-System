package Sup_Inv.Inventory.Persistence.DTO;

public class InventoryDTO {
    private String shopNum;
    private String shopName;

    public InventoryDTO(String shopNum, String shopName) {
        this.shopNum = shopNum;
        this.shopName = shopName;
    }

    public String getShopNum() {
        return shopNum;
    }

    public void setShopNum(String shopNum) {
        this.shopNum = shopNum;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
