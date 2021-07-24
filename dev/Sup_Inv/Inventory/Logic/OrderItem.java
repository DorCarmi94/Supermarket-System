package Sup_Inv.Inventory.Logic;

public class OrderItem {

    private Integer id;
    private Integer quantity; //quantity to order

    public OrderItem(Integer id, Integer quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    //region getter setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    //endregion
}
