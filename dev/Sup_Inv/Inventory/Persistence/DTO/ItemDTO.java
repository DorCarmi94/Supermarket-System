package Sup_Inv.Inventory.Persistence.DTO;

import Sup_Inv.Inventory.Logic.Item;

public class ItemDTO {

    //item fields
    private String shopNum;
    private String id;
    private String quantityShop;
    private String quantityStorage;

    //products fields
    private String name;
    private String manufacturer;
    private String category;
    private String sub_category;
    private String size;
    private int freqBuySupply;
    private double minPrice;

    public ItemDTO(String shopNum, String id, String quantityShop, String quantityStorage, String name, String manufacturer, String category, String sub_category, String size, int freqBuySupply, double minPrice) {
        this.shopNum = shopNum;
        this.id = id;
        this.quantityShop = quantityShop;
        this.quantityStorage = quantityStorage;
        this.name = name;
        this.manufacturer = manufacturer;
        this.category = category;
        this.sub_category = sub_category;
        this.size = size;
        this.freqBuySupply = freqBuySupply;
        this.minPrice = minPrice;
    }

    public ItemDTO(Item item) {
        this.shopNum = item.getShopNum();
        this.id = item.getId();
        this.quantityShop = String.valueOf(item.getQuantShop());
        this.quantityStorage = String.valueOf(item.getQuanStrg());
    }


    //region getters&setters

    public String getShopNum() {
        return shopNum;
    }

    public void setShopNum(String shopNum) {
        this.shopNum = shopNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuantityShop() {
        return quantityShop;
    }

    public void setQuantityShop(String quantityShop) {
        this.quantityShop = quantityShop;
    }

    public String getQuantityStorage() {
        return quantityStorage;
    }

    public void setQuantityStorage(String quantityStorage) {
        this.quantityStorage = quantityStorage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getFreqBuySupply() {
        return freqBuySupply;
    }

    public void setFreqBuySupply(int freqBuySupply) {
        this.freqBuySupply = freqBuySupply;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }


    //endregion

}
