package Sup_Inv.Suppliers.Supplier;

import Sup_Inv.Suppliers.Service.SystemProduct;

public class Product {
    //Real product stored in system
    private final int barCode;
    private String manufacture;
    private String name;
    private String category;
    private String subCategory;
    private String size;
    private int freqSupply;
    private double minPrice;

    public Product(SystemProduct systemProduct) {
        this.barCode = systemProduct.barcode;
        this.manufacture = systemProduct.manufacture;
        this.name = systemProduct.name;
        this.category = systemProduct.category;
        this.subCategory = systemProduct.subCategory;
        this.size = systemProduct.size;
    }

    public Product(int barcode, String manufacture, String name, String category,
                         String subCategory, String size, int freqSupply, double minPrice) {
        this.barCode = barcode;
        this.manufacture = manufacture;
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.size = size;
        this.freqSupply = freqSupply;
        this.minPrice = minPrice;
    }

    public int getBarCode() {
        return barCode;
    }

    public String getManufacture() {
        return manufacture;
    }

    public String getName() {
        return name;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public void setName(String name) {
        this.name =  name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getFreqSupply() {
        return freqSupply;
    }

    public void setFreqSupply(int freqSupply) {
        this.freqSupply = freqSupply;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }
}
