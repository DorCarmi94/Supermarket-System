package Sup_Inv.Suppliers.Supplier;

public class DiscountOfProduct {
    private int amount;
    private double discount;

    public DiscountOfProduct(int amount, double discount){
        this.amount=amount;
        this.discount=discount;
    }

    public int getAmount() {
        return amount;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}


