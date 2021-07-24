package Sup_Inv.Suppliers.Service;

public class ProductInOrderDTO {

    public int barcode;
    public int amount;
    public double price;

    public ProductInOrderDTO(int barcode, int amount){
        this.barcode = barcode;
        this.amount = amount;
    }

    public ProductInOrderDTO(int barcode, int amount, double price){
        this.barcode = barcode;
        this.amount = amount;
        this.price = price;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
