package Sup_Inv.Suppliers.Supplier;

import Sup_Inv.Suppliers.Structs.Days;
import Sup_Inv.Suppliers.Supplier.Order.ProductInOrder;

import java.util.LinkedList;
import java.util.List;


public class ContractWithSupplier {

    private List<Days> dailyInfo;
    private String contractDetails;
    //TODO save it as hashmap for faster search
    private List<ContractProduct> products;
    private int contractID;
    private int supID;

    public int getSupID() {
        return supID;
    }

    public void setSupID(int supID) {
        this.supID = supID;
    }

    public void setContractID(int contractID) {
        this.contractID = contractID;
    }

    public int getContractID() {
        return contractID;
    }

    public ContractWithSupplier(String contractDetails, List<Days> days){
        this.dailyInfo=days;
        this.contractDetails=contractDetails;
        this.products=new LinkedList<ContractProduct>();
    }
    //int produceId, String manufacture, String name,  int barcode, int productCatalogNumber, double originalPrice
    public boolean addProduct(AddProduct product){
        if(this.products.stream()
                .filter(x->x.getProductCatalogNumber().equals(product.productCatalogNumber))
                .findAny()
                .orElse(null)==null)
        {
            products.add(new ContractProduct(product.barCode,product.productCatalogNumber,product.productDiscounts));
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean addDiscountToProduct(int barcode, int amount, double discount){
        ContractProduct productToChange=this.products.stream()
                .filter(p->p.getBarCode()==barcode)
                .findFirst()
                .orElse(null);
        if(productToChange==null)
        {
            return false;
        }
        else
        {
            return productToChange.addDiscountToProduct(amount,discount);
        }
    }

    public List<Days> getDailyInfo() {
        return dailyInfo;
    }

    public String getContractDetails() {
        return contractDetails;
    }

    public List<ContractProduct> getProducts() {
        return products;
    }

    public List<ProductDiscounts> getAmountDiscountReport() {
        List<ProductDiscounts> barCodeToProductDiscounts= new LinkedList<ProductDiscounts>();
        for (ContractProduct cp:
             this.products) {
            barCodeToProductDiscounts.add(cp.getDiscounts());
        }
        return barCodeToProductDiscounts;
    }

    /**
     * Check if there is a product id in this contract
     * @param barcode barcode
     * @return true if the there is barcode
     */
    public boolean hasProduct(int barcode) {
        for(ContractProduct product : products){
            if(product.getBarCode() == barcode){
                return true;
            }
        }
        return false;

    }

    /**
     * fill the catalogNumber filed according to the product id
     * @param products products
     */
    public void fillWithCatalogNumber(List<ProductInOrder> products) {
        for(ContractProduct contractProduct : this.products){
            for(ProductInOrder product : products){
                if(product.getBarcode() == contractProduct.getBarCode()){
                    product.setProductCatalogNumber(contractProduct.getProductCatalogNumber());
                    break;
                }
            }
        }
    }

    public double calculateOrderPrice(List<ProductInOrder> products) {
        double total = 0;

        for(ProductInOrder product : products){
            for(ContractProduct cp : this.products){
                if(cp.getBarCode() == product.getBarcode()){
                    total = total + cp.getPricePerAmount(product.getAmount());
                    break;
                }
            }
        }

        return total;
    }

    public void addSupplyDayToContract(Days day)
    {
        this.dailyInfo.add(day);
    }

    public void setProducts(List<ContractProduct> allContractProducts) {
        this.products=allContractProducts;
    }

    public void setPricePerUnit(List<ProductInOrder> products) {
        for(ContractProduct contractProduct : this.products){
            for(ProductInOrder product : products){
                if(product.getBarcode() == contractProduct.getBarCode()){
                    product.setPricePerUnit(contractProduct.getPricePerAmount(product.getAmount()));
                    break;
                }
            }
        }
    }
}