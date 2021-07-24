package Sup_Inv.Inventory.Logic;

import java.util.HashMap;

public class ShortageOrder {

    private Integer shopNum;
    private HashMap<Integer, Integer> order;

    public int getLength() {
        return length;
    }

    private int length;

    public ShortageOrder(int shopNum) {
        this.shopNum = shopNum;
        this.order = new HashMap<>();
        this.length = 0;
    }

    public void addItemToOrder(OrderItem orderItem){
        order.put(orderItem.getId(), orderItem.getQuantity());
        length++;
    }

    public String toString()
    {
        String ans = "";
        for (int id : order.keySet()) {
            ans += "id: " + id + " quant: " + order.get(id) + "\n";
        }
        return ans;
    }

    public Integer getShopNum() {
        return shopNum;
    }

    public HashMap<Integer, Integer> getOrder() {
        return order;
    }
}
