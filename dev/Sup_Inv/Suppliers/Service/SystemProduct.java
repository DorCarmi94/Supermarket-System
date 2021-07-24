package Sup_Inv.Suppliers.Service;

public class SystemProduct {

    public int barcode;
    public String name;
    public String manufacture;
    public String category;
    public String subCategory;
    public String size;
    public int freqSupply;
    public double minPrice;

    public SystemProduct(int barcode, String manufacture, String name, String category,
                         String subCategory, String size) {
        this.barcode = barcode;
        this.manufacture = manufacture;
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.size = size;
        this.freqSupply = -1;
        this.minPrice = -1;
    }

    public SystemProduct(int barcode, String manufacture, String name, String category,
                         String subCategory, String size, int freqSupply, double minPrice) {
        this.barcode        = barcode;
        this.manufacture    = manufacture;
        this.name           = name;
        this.category       = category;
        this.subCategory     = subCategory;
        this.size              = size;
        this.freqSupply     = freqSupply;
        this.minPrice        = minPrice;
    }
    @Override
    public String toString()
    {
        return      "barcode: "+barcode+" \n"+
                    "manufacture: "+manufacture+" \n"+
                    "name: "+name+" \n"+
                    "category: "+category+" \n"+
                    "subCategory: "+subCategory+" \n"+
                    "size: "+size+" \n"+
                    "freqSupply: "+freqSupply+" \n"+
                    "minPrice: "+minPrice+" \n";
    }
}
