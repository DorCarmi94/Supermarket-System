package Sup_Inv.Suppliers.Supplier;

import Sup_Inv.Suppliers.Structs.Days;
import Sup_Inv.Suppliers.Structs.StructUtils;
import Sup_Inv.Suppliers.Supplier.Order.ProductInOrder;

import java.util.*;

public class Supplier {
    private static int SupplierIDsCounter=0;
    private int supId=-1;
    private String incNum;
    private String accountNumber;
    private List<String> paymentInfo;
    private String address;
    private int area;
    private boolean selfDelivery;

    //Sup_Inv.Suppliers.Supplier.Sup_Inv.Suppliers.Supplier Details
    private String name;

    private List<ContactInfo> contacts;
    private ContractWithSupplier contract;

    public Supplier(int supID,String name, String address, String incNum, String accountNumber, String paymentInfo,
                    String contactName, String phoneNumber,String email, boolean selfDelivery){
        this.name=name;
        this.incNum=incNum;
        this.accountNumber=accountNumber;
        this.paymentInfo=new LinkedList<>();
        this.paymentInfo.add(paymentInfo);
        this.supId=supID;
        this.address = address;
        setArea(address);
        this.selfDelivery = selfDelivery;


        this.contacts = new LinkedList<>();
        this.addContactInfo(contactName,phoneNumber,email);

        contract = null;
    }

    public Supplier(String name, String address, String incNum, String accountNumber, String paymentInfo,
                    String contactName, String phoneNumber,String email, boolean selfDelivery){
        this.name=name;
        this.incNum=incNum;
        this.accountNumber=accountNumber;
        this.paymentInfo=new LinkedList<>();
        this.paymentInfo.add(paymentInfo);
        this.address=address;
        this.contacts = new LinkedList<>();
        this.addContactInfo(contactName,phoneNumber,email);
        setArea(address);
        this.selfDelivery = selfDelivery;

        contract = null;
    }

    public Supplier(String name, String address, String incNum, String accountNumber, String paymentInfo, boolean selfDelivery){
        this.name=name;
        this.incNum=incNum;
        this.accountNumber=accountNumber;
        this.paymentInfo=new LinkedList<>();
        this.paymentInfo.add(paymentInfo);
        this.address=address;
        this.contacts = new LinkedList<>();
        contract = null;
        setArea(address);
        this.selfDelivery = selfDelivery;

    }

    public boolean addContactInfo(String name, String phone, String email){
        this.contacts.add(new ContactInfo(name, phone, email,this.supId));
        return true;
    }

    public boolean addContactInfo(ContactInfo contactInfo){
        this.contacts.add(contactInfo);
        return true;
    }

    /**
     *
     * @param contractDetails
     * @param days
     * @return List of products id with wasnt added to the supplier
     */
    public boolean addContractInfo(String contractDetails, List<Days> days){
        if(this.contract==null)
        {
            this.contract=new ContractWithSupplier(contractDetails,days);
            return true;
        }
        return false;
    }

    public void setContract(ContractWithSupplier contract) {
       this.contract=contract;
    }

    /**
     *
     * @param product
     * @return true if the product have been added
     */
    public boolean addProduct(AddProduct product){
        try {
            this.contract.addProduct(product);
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    public List<ProductDiscounts> getAmountDiscountReport(){
        if(contract != null) {
            return contract.getAmountDiscountReport();
        }
        return  null;
    }

    public List<SupplierProductInfo> getAllProducts(){
        if(contract == null){
            return null;
        }

        List<SupplierProductInfo> supplierProductInfos= new LinkedList<SupplierProductInfo>();
        for (ContractProduct product:
                this.contract.getProducts()) {
            supplierProductInfos.add(new SupplierProductInfo(product.getBarCode(),product.getProductCatalogNumber()));
        }
        return supplierProductInfos;
    }
    public int getSupId(){
        return supId;
    }

    public List<String> getPaymentInfo(){
        return paymentInfo;
    }

    public String getSupplierName(){
        return name;
    }

    public String getIncNum() {
        return incNum;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAddress() {
        return address;
    }

    public boolean addDiscountToProduct(int barcode, int amount, double discount)
    {
        return contract.addDiscountToProduct(barcode,amount,discount);
    }

    /**
     * Add all paymentInfo if there are not already exist
     * @param paymentInfo Array of payments info
     * @return true
     */
    public boolean addPaymentOptions(String paymentInfo) {

            if(!this.paymentInfo.contains(paymentInfo.toUpperCase())){
                this.paymentInfo.add(paymentInfo);
            }

        return true;
    }

    /**
     * Remove all of payment info if there will be left at least one
     * payment info
     * @param paymentInfo Array of payment info
     * @return true if all of the payment are removed, false otherwise
     */
    public boolean RemovePaymentOptions(String paymentInfo) {
        boolean removed=false;
        for(String option : this.paymentInfo){
            if(option.equals(paymentInfo))
            {
                removed=true;
                this.paymentInfo.remove(option);
            }
        }
        return removed;
    }

    public boolean RemoveContactFromSupplier(String email)
    {
        ContactInfo contact=this.contacts.stream().filter(x->x.getEmail()==email).findFirst().orElse(null);
        if (contact!=null && this.contacts.size()>1)
        {
            this.contacts.remove(contact);
            return true;
        }
        else
        {
            return false;
        }
    }

    public ContractProduct getAllInformationAboutSuppliersProduct( int barcode) {
        return this.contract.getProducts().stream().filter(x->x.getBarCode()==barcode).findFirst().orElse(null);
    }

    /**
     * Check if the supplier supply this barcode
     * @param barcode barcode
     * @return true if the supplier supply this barcode
     */
    public boolean hasProduct(int barcode) {
        if(!hasContract()){
            return false;
        }
        return contract.hasProduct(barcode);
    }

    /**
     * fill the catalogNumber filed according to the product id
     * @param products products
     */
    public void fillWithCatalogNumber(List<ProductInOrder> products) {
        if(contract != null){
            contract.fillWithCatalogNumber(products);
        }
    }

    public List<ContactInfo> getContacts(){
        return new ArrayList<ContactInfo>(contacts);
    }

    public void setId(int aId) {
        this.supId=aId;
    }

    /**
     * Calculate the order price with the list of products
     * @param products the products
     * @return order price
     */
    public double calculateOrderPrice(List<ProductInOrder> products) {
        return contract.calculateOrderPrice(products);
    }

    /**
     * Calculate and set the price per unit for every product
     * @param products the products to set
     */
    public void setPricePerUnit(List<ProductInOrder> products) {
        if(contract != null){
            contract.setPricePerUnit(products);
        }
    }

    /**
     * Return the next delivery date
     * @return next delivery date
     */
    public Date getNextDeliveryDate(){
        List<Days> days = contract.getDailyInfo();
        if(!days.isEmpty()){
            return StructUtils.getTheNearestDate(days);
        }

        return null;
    }

    public void addContract(ContractWithSupplier contract) {
        this.contract=contract;
    }

    public boolean hasContract() {
        return this.contract!=null;
    }

    public ContractWithSupplier getContract()
    {
        return contract;
    }

    /**
     * Check if the supplier has all the barcodes
     * @param barcodes the barcodes to check
     * @return true if the supplier have all the barcodes
     */
    public boolean hasAllBarcodes(List<Integer> barcodes) {
        List<ContractProduct> products;
        boolean hasBarcode = false;
        if(!hasContract()){
            return false;
        }

        products = contract.getProducts();

        for(Integer barcode : barcodes) {
            for (ContractProduct contractProduct : products) {
                if (contractProduct.getBarCode() == barcode){
                    hasBarcode = true;
                    break;
                }
            }
            if(!hasBarcode) {
                return false;
            }
        }

        return true;
    }

    public void setContacts(List<ContactInfo> contacts) {
        this.contacts=contacts;
    }

    public int getArea(){
        return area;
    }

    private void setArea(String area){
        this.area = area.length() % 4;
    }

    public boolean isSelfDelivery() {
        return selfDelivery;
    }
}
