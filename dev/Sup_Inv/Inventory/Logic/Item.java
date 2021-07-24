package Sup_Inv.Inventory.Logic;
import Sup_Inv.Inventory.Interfaces.Observer;
import Sup_Inv.Inventory.Interfaces.myObservable;
import Sup_Inv.Inventory.Persistence.DTO.ItemDTO;

import java.util.ArrayList;
import java.util.List;


public class Item implements myObservable {

    //region fields
    //supplier item info
    private String id;
    private String name;
    private String shopNum;

    private String manufacturer;
    private String category;
    private String sub_category;
    private String size;
    private String freqBuySupply;

    //item info
    private int quanStrg;
    private int quantShop;
    private int totalQuantity;
    private int capacityShop = 50;
    private boolean minimum;//for alerts
    public final List<Observer> observers;
    //endregion

    public Item(Observer o, ItemDTO itemDTO) {
        this.observers = new ArrayList<>();
        this.register(o);
        this.quanStrg = Integer.parseInt(itemDTO.getQuantityStorage());
        this.quantShop = Integer.parseInt(itemDTO.getQuantityShop());
        this.totalQuantity = this.quanStrg + this.quantShop;
        this.id = itemDTO.getId();
        this.name = itemDTO.getName();
        this.manufacturer = itemDTO.getManufacturer();
        this.category = itemDTO.getCategory();
        this.sub_category = itemDTO.getSub_category();
        this.size = itemDTO.getSize();
        this.freqBuySupply = String.valueOf(itemDTO.getFreqBuySupply());
        this.shopNum = itemDTO.getShopNum();
        checkMinimum();
    }

    //region getters
    public String getCategory() { return this.category; }
    public boolean getIfQuantityMinimum() { return this.minimum; }
    public String getId() { return id; }
    public String getName() { return this.name; }
    public String getShopNum() {
        return shopNum;
    }
    public int getQuanStrg() {
        return quanStrg;
    }
    public int getQuantShop() {
        return quantShop;
    }
    //endregion

    //region item functions
    public OrderItem updateMyQuantities(int qstrg, int qshop, char c) {
        if(c == '-'){
            this.quanStrg -= qstrg;
            this.quantShop -= qshop;
        }
        else{
            this.quanStrg += qstrg;
            this.quantShop += qshop;
        }
        int missInShop = this.capacityShop - this.quantShop;
        if (missInShop > 0 & this.quanStrg > 0)
        {
            if( quanStrg >= missInShop) //enough in storge to fill the shop
            {
                this.quanStrg = quanStrg - missInShop;
                this.quantShop = capacityShop;
            }
            else //taking all storage to fill the shop
            {
                this.quantShop = quantShop + this.quanStrg;
                this.quanStrg = 0;
            }
        }
        if(quanStrg < 0) quanStrg = 0;
        if(quantShop < 0) quantShop = 0;
        totalQuantity = quanStrg+quantShop;

        if(this.checkMinimumQuant())
            return issueOrderForShortageItem();
        return null;
    }
    public Boolean checkMinimumQuant() {

        if((totalQuantity) < Integer.parseInt(freqBuySupply)*10)
        {
            minimum = true;
            notifyObserver("|--------------------------------------------------\n" +
                    "|Alert! this product ENDED. order more!");
        }
        else if((totalQuantity) < Integer.parseInt(freqBuySupply)*10 + 10)
        {
            minimum = true;
            notifyObserver("|--------------------------------------------------\n" +
                    "|Alert! this product is about to end, need to order more");
        }
        else if(quanStrg == 0)
        {
            minimum = false;
            notifyObserver("|--------------------------------------------------\n" +
                    "|Alert!! this product ENDED in the storage. order more!");
        }
        else
        {
            minimum = false;
        }
        itemStatusUpdated();
        return minimum;
    }
    private OrderItem issueOrderForShortageItem() {
        int quantityToOrder;
        if(quanStrg == 0)
            quantityToOrder = (Integer.parseInt(freqBuySupply)*10 + 10);
            else
        quantityToOrder = (Integer.parseInt(freqBuySupply)*10 + 10) - totalQuantity;
        return new OrderItem(Integer.parseInt(id), quantityToOrder);
    }
    private void checkMinimum() {
        if((totalQuantity) < Integer.parseInt(freqBuySupply)*10)
            minimum = true;
        else if((totalQuantity) < Integer.parseInt(freqBuySupply)*10 + 10)
            minimum = true;
        else if(quanStrg == 0)
            minimum = true;
        else
            minimum = false;
    }
    public void itemStatus() {
        notifyObserver("|--------------------------------------------------\n" +
                "| id; " + id + " name; " + name + "\n" +
                "| manufacturer; " + manufacturer + "\n" +
                "| quanStrg; " + quanStrg +
                " quantShop; " + quantShop + "\n" +
                "| totalQuantity; " + totalQuantity + "\n" +
                "| category; " + category +
                " sub_category; " + sub_category +
                " size; " + size + "\n" +
                "| freqBuySupply; " + freqBuySupply +
                " minimum; " + minimum + " \n" +
                "|--------------------------------------------------\n");
    }
    public void itemStatusUpdated() {
        notifyObserver("|--------------------------------------------------\n" +
                "| id; " + id + " name; " + name + "\n" +
                "| quanStrg; " + quanStrg +
                " quantShop; " + quantShop + "\n" +
                "| totalQuantity; " + totalQuantity +
                " minimum; " + minimum + " \n" +
                "|--------------------------------------------------\n");
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
