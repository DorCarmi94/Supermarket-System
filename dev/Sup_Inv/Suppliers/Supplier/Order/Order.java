package Sup_Inv.Suppliers.Supplier.Order;

import Sup_Inv.Suppliers.Structs.OrderStatus;

import java.util.*;

public abstract class Order {

    protected int orderId;
    protected int contractId;
    private int shopNumber;
    private Map<String, ProductInOrder> products;
    private OrderStatus status;
    private Date deliveryDay;

    protected Order(int orderId, List<ProductInOrder> products , int shopNumber){
        this.products = new HashMap<>();
        for (ProductInOrder product : products) {
            if(product != null) {
                this.products.put(product.getProductCatalogNumber(), product);
            }
        }

        this.orderId = orderId;
        status = OrderStatus.Open;
        deliveryDay = null;
        this.shopNumber = shopNumber;

    }

    /**
     * Sup_Inv.Suppliers.Supplier.Order.Order cant be created without at least one product.
     * @return new order
     */
    /*public static Order CreateOrder(int orderId, List<ProductInOrder> productsInOrder, int shopNumber){
        if(productsInOrder.isEmpty()){
            return null;
        }

        return new Order(orderId, productsInOrder, shopNumber);
    }*/

    public int getOrderId(){
        return  orderId;
    }

    public boolean setStatus(OrderStatus status){
        this.status=status;
        return true;
    }

    public boolean updateDeliveryDay(Date date){
        this.deliveryDay=date;
        return true;
    }

    public List<String> retrunProductsCatalogNumbers() {
        return new LinkedList<>(this.products.keySet());
    }

    public List<ProductInOrder> getProducts() {
        return new LinkedList<>(this.products.values());
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Integer getShopNumber() {
        return shopNumber;
    }

    public void setShopNumber(Integer shopNumber) {
        this.shopNumber = shopNumber;
    }

    public Date getDeliveryDay() {
        return deliveryDay;
    }

    public void setDeliveryDay(Date deliveryDay) {
        this.deliveryDay = deliveryDay;
    }

    public int getContractId(){
        return contractId;
    }

    public void setContractId(int contractId){
        this.contractId =  contractId;
    }

    public void setOrderId(int orderId){
        this.orderId = orderId;
    }
}
