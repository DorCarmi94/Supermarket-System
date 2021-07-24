package Sup_Inv.Suppliers.Supplier;

import Sup_Inv.Result.Result;
import Sup_Inv.Suppliers.Service.*;
import Sup_Inv.Suppliers.Structs.Days;
import Sup_Inv.Suppliers.Structs.OrderStatus;
import Sup_Inv.Suppliers.Structs.PaymentOptions;
import Sup_Inv.Suppliers.Structs.StructUtils;
import Sup_Inv.Suppliers.Supplier.Order.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class SupplierSystem {

    private static SupplierSystem instance = null;

    private Map<Integer, Supplier> suppliers;
    private Map<Integer,List<Order>> orders;

    private Map<Integer, Order> orderIdToOrder;

    private ProductsManager productsManager;
    private SupplierManager supplierManager;
    private OrderManager orderManager;

    private SupplierSystem() {
        suppliers = new HashMap<>();
        orders = new HashMap<>();
        orderIdToOrder = new HashMap<>();

        supplierManager = SupplierManager.getInstance();
        productsManager = ProductsManager.getInstance();
        orderManager = OrderManager.getInstance();
    }

    public static SupplierSystem getInstance(){
        if(instance == null){
            instance = new SupplierSystem();
        }
        return instance;
    }

    //TODO payment option upper case
    // in update payment option send usefull message, code the Remove option for supplier
    // supplier payment options print list, and not with /n
    // getAllSupplier
    // init with list of categorys and subcategoty for all the system
    // the same for product size
    //
    // TODO in order update display status and when order is close do not update delivery day and check the delivery date
    /**
     * Create new supplier in the system
     * @param name The name of the supplier
     * @param incNum incupartion number
     * @param accountNumber Bank account
     * @param paymentInfo payment options by string separated by ,
     * @return -1 if cant create a new supplier otherwise return new supplier ID in the system.
     */
    public int createSupplierCard(String name, String incNum, String address, String accountNumber, String paymentInfo,
                                  String contactName, String phoneNumber,String email, boolean selfDelivery) {
        String[] arr= paymentInfo.split(",");

        if(arr.length<1)
        {
            return -1;
        }
        for (int i = 0; i < arr.length; i++) {
            String modified=arr[i].trim();
            modified=modified.toUpperCase();
            String finalModified = modified;
            arr[i]=finalModified;
            if (Arrays.stream(PaymentOptions.values()).filter(x->x.name().equals(finalModified)).findFirst().orElse(null)==null) {
                return -1;
            }
        }



        Supplier sup = new Supplier(name,address,incNum,accountNumber, arr[0], selfDelivery);

        int returnedId=supplierManager.insert(sup);
        if(returnedId!=-1) {

            String returnedEmail = supplierManager.insertNewContactInfo(new ContactInfo(contactName, phoneNumber, email, sup.getSupId()));
            if (returnedEmail == null) {

                boolean ans = supplierManager.deleteSupplier(sup);
                return -1;
            }
            for (String info: arr) {

                if(!this.addPaymentOptions(sup.getSupId(),info))
                {
                    boolean ans = supplierManager.deleteSupplier(sup);
                    return -1;
                }
            }
            return returnedId;
        }
        if(sup.getSupId() < 0){
            return -1;
        }

        orders.put(sup.getSupId(), new LinkedList<>());
        suppliers.put(sup.getSupId(),sup);
        return sup.getSupId();
    }

    /**
     * Return the payment information of specific supplier.
     * @param supId ID of the supplier
     * @return null if the supplier doesnt exist in the system, otherwise its payment information
     */
    public List<String> getPaymentOptions(int supId) {
        List<String> options = new LinkedList<>();
        for (PaymentOptions theOpt : this.supplierManager.getSupplierPaymentOptions(supId)) {
            options.add(theOpt.name());

        }
        return options;
    }

    /**
     * Add payment options to supplier
     * @param supId the supplier ID
     * @param paymentInfo Array of payment options
     * @return true if the payment options was added or already exist, false otherwise
     */
    public boolean addPaymentOptions(int supId, String paymentInfo) {
        String modified=paymentInfo.trim();
        modified=modified.toUpperCase();
        String finalModified = modified;
        if (Arrays.stream(PaymentOptions.values()).filter(x->x.name().equals(finalModified)).findFirst().orElse(null)==null) {
                return false;
        }
        return supplierManager.addPaymentOption(supId,finalModified);
    }

    /**
     * Remove the payment options from the supplier
     * After the removal there is need to be at least one payment option for the
     * supplier otherwise the method wont remove any option
     * @param supId supplier ID
     * @param paymentInfo Array of payment options to remove
     * @return true if the all the payment are removed, false otherwise
     */
    public boolean removePaymentOptions(int supId, String paymentInfo) {
        String modified=paymentInfo.trim();
        modified=modified.toUpperCase();
        String finalModified = modified;
        return supplierManager.removePaymentOption(supId,finalModified);

    }


    /**
     * Return the details for each supplier in the system.
     * @return List<Sup_Inv.Suppliers.InvService.SupplierDetails> for each supplier in the system.
     */
    public List<SupplierDetails> getAllSuppliers() {
        return this.supplierManager.getAllSuppliers();

    }
    /**
     * Add person contact information to specific supplier
     * @param supplierId ID for the supplier
     * @param contactPersonName Person name
     * @param phoneNumber Phone number
     * @param email Email
     * @return True if the contact as been added.
     */
    public boolean addContactInfo(int supplierId, String contactPersonName, String phoneNumber, String email) {
        String returnedEmail=supplierManager.insertNewContactInfo(new ContactInfo(contactPersonName,phoneNumber,email,supplierId));
        if(returnedEmail==null)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean RemoveContactFromSupplier(int supID,String email)
    {
        return this.supplierManager.removeContactFromSupplier(supID,email);
    }

    /**
     * Add contract with the supplier, only one contract can exist.
     * @param supplierId Sup_Inv.Suppliers.Supplier id
     * @param contractInfo Contract details
     * @param days List of days he can supply items.
     * @param products List of product he supply
     * @return List of products id that cant be add to the system, or return null if cant add the contract to the
     *          supplier already have one.
     */
    public List<Integer> addContractToSupplier(int supplierId, String contractInfo, List<Days> days, List<AddProduct> products) {
        List<Integer> badProducts = new LinkedList<>();
        int freqSupply;

        int contractID = supplierManager.addContractToSupplier(supplierId,new ContractWithSupplier(contractInfo,days));
        if(contractID < 0) {
            return null;
        }

        freqSupply = getFreqSupply(days);

        boolean ans = true;
        for (AddProduct product : products) {

            if(product.product != null){
                product.product.setFreqSupply(freqSupply);
                product.product.setMinPrice(product.originalPrice);
            }

            ans = addProductToContract(supplierId, freqSupply, product);
            if(!ans) {
                badProducts.add(product.barCode);
            }
        }

        return badProducts;

    }

    private int getFreqSupply(List<Days> days) {
        int freqSupply;
        if(days.size() == 1){
            freqSupply = 1;
        } else {
            freqSupply = days.size() / 2;
        }
        return freqSupply;
    }

    /**
     * Add a product to the supplier contract
     * @param supplierID Sup_Inv.Suppliers.Supplier ID
     * @param product Data of the product
     * @return true if the product have been added
     */
    public boolean addProductToContract(int supplierID, int freqSupply, AddProduct product) {
        boolean ans;

        if(ans = supplierManager.addProductToContract(supplierID,product))
        {
            if(product.product != null) {
                product.product.setFreqSupply(freqSupply);
                product.product.setMinPrice(product.originalPrice);
                productsManager.addIfAbsent(product.product);
            } else {
                productsManager.updateIfNeededFreqSupplyAndMinPrice(product.barCode, freqSupply, product.originalPrice);
            }

        }
        return ans;
    }

    /**
     * Add a product to the supplier contract
     * @param supplierID Sup_Inv.Suppliers.Supplier ID
     * @param product Data of the product
     * @return true if the product have been added
     */
    public boolean addProductToContract(int supplierID, AddProduct product) {
        List<Days> days = supplierManager.getSupplyingDaysBySupID(supplierID);
        if(days == null){
            return false;
        }
        return addProductToContract(supplierID, getFreqSupply(days), product);
    }




    /**
     * Return for each product his discount per amount
     * @param supplierId Sup_Inv.Suppliers.Supplier ID
     * @return List of ProductDiscount.
     */
    public List<ProductDiscounts> getAmountDiscountReport(int supplierId) {

        return this.supplierManager.getAmountDiscountReport(supplierId);
    }


    //TODO for suppliers how dont have supply days the delivery date is null
    // in the integration edit it, to send this order of suppliers to vec
    /**
     * Create a new order in the system
     * @param supplierId The supplier ID who need to supply the order
     * @param products The product to order
     * @return -1 if cant create the order, otherwise return the order id
     */
    public Result<Integer> createNewOrder(int supplierId, List<ProductInOrder> products, int shopNumber) {
        Supplier supplier = supplierManager.getById(supplierId);
        List<Integer> barcodes = new ArrayList<>();
        int contractId;

        if(supplier == null){
            return Result.makeFailure("Supplier doesnt exist");
        }

        if(!supplier.hasContract()){
            return Result.makeFailure("The supplier doesnt have a contract");
        }
        contractId = supplier.getContract().getContractID();

        for(ProductInOrder product : products){
            barcodes.add(product.getBarcode());
        }

        //TODO can be faster if we use the db
        if(!supplier.hasAllBarcodes(barcodes)){
            return Result.makeFailure("The supplier do not supply some of this products");
        }

        supplier.setPricePerUnit(products);
        supplier.fillWithCatalogNumber(products);

        RegularOrder regularOrder = RegularOrder.CreateRegularOrder(-1, products, shopNumber);
        if(regularOrder == null){
            return Result.makeFailure("Need to have at least one product");
        }
        regularOrder.setDeliveryDay(supplier.getNextDeliveryDate());
        regularOrder.setContractId(contractId);

        if(!supplier.isSelfDelivery()){
            regularOrder.setStatus(OrderStatus.WaitingForShipping);
        }

        orderManager.createRegularOrder(regularOrder);
        if(regularOrder.getOrderId() < 0){
            if(regularOrder.getOrderId() == -2){
                return Result.makeFailure("No such shop id");
            }
            return Result.makeFailure("Order wasnt created");
        }

        return Result.makeOk("Order was created", regularOrder.getOrderId());
    }

    /**
     * Create a regular order if a supplier have all the products with the cheapest one
     * @param products products
     * @param shopNumber shop number
     * @return orderId if it was created otherwise -1
     */
    public Result<Integer> createRegularOrder(List<ProductInOrder> products, int shopNumber) {
        List<Integer> barcodes = new ArrayList<>();
        List<Integer> suppliersId;
        Supplier sup;

        for(ProductInOrder product : products){
            barcodes.add(product.getBarcode());
        }
        suppliersId = supplierManager.getAllSupplierIdsWithBarcodes(barcodes);
        if(suppliersId.isEmpty()){
            return Result.makeFailure("There isnt one supplier with all of this products");
        }

        int cheapestSupplierId = getCheapestSupplierId(suppliersId, products);

        sup = supplierManager.getById(cheapestSupplierId);
        sup.setPricePerUnit(products);
        sup.fillWithCatalogNumber(products);

        RegularOrder regularOrder = RegularOrder.CreateRegularOrder(-1,products, shopNumber);
        regularOrder.setDeliveryDay(sup.getNextDeliveryDate());
        regularOrder.setContractId(sup.getContract().getContractID());

        if(!sup.isSelfDelivery()){
            regularOrder.setStatus(OrderStatus.WaitingForShipping);
        }

        orderManager.createRegularOrder(regularOrder);
        if(regularOrder.getOrderId() < 0){
            if(regularOrder.getOrderId() == -2){
                return Result.makeFailure("No such shop id");
            }
            return Result.makeFailure("Order wasnt created");
        }

        return Result.makeOk("Order was created", regularOrder.getOrderId());

    }

    /**
     * Create periodical order
     * @param products the product to order
     * @param days the days to supply
     * @param weekPeriod week
     * @param shopNumber to shop number
     * @return the id of the order
     */
    public Result<List<Integer>> createPeriodicalOrder(List<ProductInOrder> products, List<Days> days, int weekPeriod, int shopNumber) {
        List<Integer> barcodes = new ArrayList<>();
        List<Integer> suppliersId;
        List<Integer> ordersId = new LinkedList<>();
        Supplier sup;

        suppliersId = supplierManager.getAllSupplierWithSupplyDays(days);
        if(suppliersId.isEmpty()){
            return Result.makeFailure("There isnt one supplier with the given supply days");
        }

        for(ProductInOrder product : products){
            barcodes.add(product.getBarcode());
        }
        suppliersId = supplierManager.getAllSuppliersWithBarcodes(suppliersId, barcodes);
        if(suppliersId.isEmpty()){
            return Result.makeFailure("There isnt one supplier with all of this products");
        }

        int cheapestSupplierId = getCheapestSupplierId(suppliersId, products);

        sup = supplierManager.getById(cheapestSupplierId);
        sup.setPricePerUnit(products);
        sup.fillWithCatalogNumber(products);

        ordersId = orderManager.createPeriodicalOrder(products, days, weekPeriod, shopNumber,
                            sup.getContract().getContractID(), sup.isSelfDelivery());
        if(ordersId.isEmpty()){
            return Result.makeFailure("Orders wasnt created, week period can be 1 or 2");
        }

        return Result.makeOk("Orders was created", ordersId);
    }

    public Result<List<Integer>> addProductsToPeriodicalOrder(int orderId, List<ProductInOrder> products) {
        Supplier sup;
        if(!orderManager.isPeriodicalOrder(orderId)){
            return Result.makeFailure("The order doesnt exist or this is not periodical order");
        }

        int supplierId = orderManager.getTheSupplierOfOrder(orderId);
        if(supplierId == -1){
            //not need to get here
            return Result.makeFailure("The supplier of this order desnt exist");
        }

        Order order = orderManager.getOrderBasicDetails(orderId);
        long diff = order.getDeliveryDay().getTime() - Calendar.getInstance().getTime().getTime();
        long days =  TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        if(days < 2){
            return Result.makeFailure("Cant edit order day before delivery");
        }

        sup = supplierManager.getById(supplierId);
        if(sup == null){
            //not need to get here
            return Result.makeFailure("The supplier of this order desnt exist");
        }
        sup.fillWithCatalogNumber(products);
        sup.setPricePerUnit(products);

        return Result.makeOk("Products was inserted",orderManager.addProductsToPeriodicalOrder(orderId, products));
    }

    /**
     * Return all the supplier products
     * @param supplierId supplier ID
     * @return List with all the supplier product info
     */
    public List<SupplierProductDTO> getAllSupplierProducts(int supplierId) {
        List<SupplierProductDTO> supplierProductDTOs=new LinkedList<>();
        //int barcode, String productCatalogNumber, double originalPrice, ProductDiscountsDTO discountPerAmount
        for (ContractProduct product:
                this.supplierManager.getAllSupplierProductsBardoces(supplierId)) {
            ProductDiscountsDTO productDiscountsDTO= new ProductDiscountsDTO(product.getBarCode(),product.getOriginalPrice());
            for (int amount:
                 product.getDiscounts().discountPerAmount.keySet()) {
                productDiscountsDTO.addDiscount(amount,product.getDiscounts().discountPerAmount.get(amount));
            }
            supplierProductDTOs.add(new SupplierProductDTO(product.getBarCode(),
                    product.getProductCatalogNumber(),product.getOriginalPrice(), productDiscountsDTO));

        }
        return supplierProductDTOs;
    }


    /**
     * Return all the orders ID from a given suppler
     * @param supplierId Sup_Inv.Suppliers.Supplier ID
     * @return List with all the orders for the specific supplier
     */
    public List<String> getPurchaseHistory(int supplierId) {
        return orderManager.getPurchaseHistoryOfSupplier(supplierId);
    }

    public AddProduct getAllInformationAboutSuppliersProduct(int supplierId, int barcode) {
        Supplier supplier = suppliers.getOrDefault(supplierId, null);

        if(supplier == null){
            return null;
        }
        ContractProduct cp=supplier.getAllInformationAboutSuppliersProduct(barcode);
        Product product=this.productsManager.getProduct(barcode);
        if(cp!=null && product!=null) {
            return new AddProduct(cp.getBarCode(), cp.getProductCatalogNumber(), cp.getDiscounts().originalPrice, cp.getDiscounts(), product);
        }
        else
        {
            return null;
        }

    }

    public OrderStatus getOrderStatus(int orderID)
    {
        Order order=this.orderIdToOrder.get(orderID);
        if(order!=null) {
            return order.getStatus();
        }
        else
        {
            return null;
        }
    }

    private int getCheapestSupplierId(List<Integer> suppliersId, List<ProductInOrder> products) {
        Supplier sup;
        int cheapestSupplierId = -1;
        double totalPrice = Double.MAX_VALUE;
        for(int id : suppliersId){
            sup = supplierManager.getById(id);
            double orderPrice = sup.calculateOrderPrice(products);
            if(orderPrice < totalPrice){
                totalPrice = orderPrice;
                cheapestSupplierId = id;
            }
        }
        return cheapestSupplierId;
    }

    public List<String> getOfferedPaymentOptions() {
        List<String> options = new LinkedList<>();
        for (Sup_Inv.Suppliers.Structs.PaymentOptions option : Sup_Inv.Suppliers.Structs.PaymentOptions.values()) {
            options.add(option.name());
        }
        return options;
    }

    public AllOrderDetails getOrderDetails(int orderId) {
        Order order = orderManager.getOrderBasicDetails(orderId);
        boolean isPeriodicalOrder = false;
        if(order == null){
            return null;
        }

        if(orderManager.isPeriodicalOrder(orderId)){
            isPeriodicalOrder = true;
        }

        List<AllDetailsOfProductInOrder> details = orderManager.getAllProductDetails(orderId);

        int supplierId = supplierManager.getIdByContract(order.getContractId());
        Supplier supplier = supplierManager.getById(supplierId);


        //supplier.setContract(null);

        return new AllOrderDetails(orderId, order.getShopNumber(), StructUtils.dateToForamt(order.getDeliveryDay()), supplier, details, isPeriodicalOrder);
    }

    public SupplierDetailsDTO getSupplierInformation(int supID) {
        Supplier supplier=supplierManager.getById(supID);
        //int supplierID, String supplierName, String incNum, String accountNumber,
        //                              String address, String contactName, String phoneNumber, String email) {
        if(supplier!=null) {
            SupplierDetailsDTO supplierDetails = new SupplierDetailsDTO(supplier.getSupId(), supplier.getSupplierName(),
                    supplier.getIncNum(), supplier.getAccountNumber(), supplier.getAddress(),
                    supplier.getContacts().get(0).getName(), supplier.getContacts().get(0).getPhoneNumber(), supplier.getContacts().get(0).getEmail(),
                    supplier.getContract().getDailyInfo(), supplier.getArea(), supplier.isSelfDelivery());
            return supplierDetails;
        }
        return null;
    }

    public List<ContactInfoDTO> getSupplierContacts(int supID) {
        List<ContactInfoDTO> contactInfoDTOS= new LinkedList<>();
        for (ContactInfo contact:
             supplierManager.getAllSupplierContacts(supID)) {
                contactInfoDTOS.add(new ContactInfoDTO(contact.getName(),contact.getPhoneNumber(),contact.getEmail(),contact.getSupID()));
        }
        return contactInfoDTOS;
    }

    public ContractWithSupplierDTO getSupplierContractInfo(int supID) {
        ContractWithSupplier contractWithSupplier=this.supplierManager.getSupplierContract(supID);
        if(contractWithSupplier!=null)
        {
            List<String> days=this.getSupplyingDaysNamesBySupID(supID);
            return new ContractWithSupplierDTO(days,contractWithSupplier.getContractDetails());
        }
        return null;
    }

    public List<String> getSupplyingDaysNamesBySupID(int supID) {
        List<String> days = new LinkedList<>();
        for (Days day:
                this.supplierManager.getSupplyingDaysBySupID(supID)) {
            days.add(day.name());

        }
        return days;
    }

    public List<Integer> getSupplyingDaysNumbersBySupID(int supID) {
        List<Integer> days = new LinkedList<>();
        for (Days day:
                this.supplierManager.getSupplyingDaysBySupID(supID)) {
            Integer dayInt=StructUtils.getDayInt(day);
            days.add(dayInt);

        }
        return days;
    }


    public Result<List<Integer>> removeProductsFromOrder(int orderId, List<Integer> barcodes) {
        List<String> catalogNumbers;
        int supplierId;
        if((supplierId = orderManager.getTheSupplierOfOrder(orderId)) == -1){
            return Result.makeFailure("No such order ID");
        }

        if(!orderManager.isPeriodicalOrder(orderId)){
            return Result.makeFailure("This is not periodical order");
        }

        Order order = orderManager.getOrderBasicDetails(orderId);
        long diff = order.getDeliveryDay().getTime() - Calendar.getInstance().getTime().getTime();
        long days =  TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

        if(days < 2){
            return Result.makeFailure("Cant edit order day before delivery");
        }

        catalogNumbers = supplierManager.getCatalogsFromBarcodes(supplierId, barcodes);
        Result<List<String>> resCatalogs = orderManager.removeProductsFromOrder(orderId, catalogNumbers);
        if(!resCatalogs.isOk()){
            return Result.makeFailure(resCatalogs.getMessage());
        }

        return Result.makeOk("Remove product",supplierManager.getBarcodesFromCatalog(supplierId, catalogNumbers));
    }
}
