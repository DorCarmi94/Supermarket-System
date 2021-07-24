package Sup_Inv.Suppliers.Service;

import Sup_Inv.Result.Result;
import Sup_Inv.Suppliers.Structs.Days;
import Sup_Inv.Suppliers.Structs.OrderStatus;
import Sup_Inv.Suppliers.Supplier.*;
import Sup_Inv.Suppliers.Supplier.Order.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class OrderAndProductCtrl implements OrderAndProductManagement {
    private final ProductsManager productsManager;
    private final OrderManager orderManager;
    private final SupplierSystem supplierSystem;

    public OrderAndProductCtrl(){
        productsManager = ProductsManager.getInstance();
        orderManager = OrderManager.getInstance();
        supplierSystem = SupplierSystem.getInstance();
    }

    @Override
    public SystemProduct getProduct(int barcode) {
        Product product = productsManager.getProduct(barcode);

        if(product == null){
            return null;
        }

        return productToSystemProduct(product);
    }

    @Override
    public List<Integer> getAllProductBarcodes() {
        return productsManager.getAllBarcodes();
    }

    @Override
    public List<Integer> productsInTheSystem(List<Integer> barcodes) {
        List<Integer> barcds =  productsManager.getAllBarcodes();
        List<Integer> barcodesInSystem = new ArrayList<>();

        for(Integer barcode : barcodes){
            if(barcds.indexOf(barcode) > 0){
                barcodesInSystem.add(barcode.intValue());
            }
        }

        return  barcodesInSystem;

    }

    public SystemProduct productToSystemProduct(Product product){
        return new SystemProduct(product.getBarCode(), product.getManufacture(), product.getName(),
                product.getCategory(),product.getSubCategory(),product.getSize(),
                product.getFreqSupply(), product.getMinPrice());
    }

    @Override
    public Result<Integer> createNewSupplierOrder(int supplierId, List<ProductInOrderDTO> products, int shopNumber) {
        List<ProductInOrder> productInOrders = new LinkedList<>();

        for(ProductInOrderDTO productInOrderDTO : products){
            productInOrders.add(ProductInOrderDTOToPIO(productInOrderDTO));
        }

        return supplierSystem.createNewOrder(supplierId, productInOrders, shopNumber);
    }

    @Override
    public Result<Integer> createRegularOrder(List<ProductInOrderDTO> products, int shopNumber) {
        List<ProductInOrder> productInOrders = new LinkedList<>();

        for(ProductInOrderDTO productInOrderDTO : products){
            productInOrders.add(ProductInOrderDTOToPIO(productInOrderDTO));
        }

        return supplierSystem.createRegularOrder(productInOrders, shopNumber);
    }

    @Override
    public Result<List<Integer>> createPeriodicalOrder(List<ProductInOrderDTO> products, List<Days> days, int weekPeriod, int shopNumber) {
        List<ProductInOrder> productInOrders = new LinkedList<>();

        for(ProductInOrderDTO productInOrderDTO : products){
            productInOrders.add(ProductInOrderDTOToPIO(productInOrderDTO));
        }

        return supplierSystem.createPeriodicalOrder(productInOrders, days, weekPeriod, shopNumber);
    }

    @Override
    public Result<List<Integer>> addProductsToPeriodicalOrder(int orderId, List<ProductInOrderDTO> products) {
        List<ProductInOrder> productInOrders = new LinkedList<>();

        for(ProductInOrderDTO productInOrderDTO : products){
            productInOrders.add(ProductInOrderDTOToPIO(productInOrderDTO));
        }

        return supplierSystem.addProductsToPeriodicalOrder(orderId, productInOrders);
    }

    @Override
    public Result<List<Integer>> RemoveProductsFromPeriodicalOrder(int orderId, List<Integer> barcodes) {
        return supplierSystem.removeProductsFromOrder(orderId, barcodes);
    }

    /**
     * Return the products information of a order
     * @param orderId the order id
     * @return OrderDTO that contains the information abount the products, null if no such order id
     */
    public Result<OrderDTO> orderArrived(int orderId){
        List<ProductInOrderDTO> products = new ArrayList<>();
        Result<Order> order;
        order = orderManager.orderArrived(orderId);

        if(order.isFailure()){
            return Result.makeFailure(order.getMessage());
        }

        for(ProductInOrder product : order.getValue().getProducts()){
            products.add(ProductInOrderToDTO(product));
        }

        return Result.makeOk("Ok", new OrderDTO(order.getValue().getShopNumber(), products));
    }

    public OrderShipDetails orderDetails(int orderId){
        AllOrderDetails orderDetails = supplierSystem.getOrderDetails(orderId);
        if(orderDetails == null){
            return null;
        }

        Supplier supplier = orderDetails.supplier;
        ContactInfo contactInfo = supplier.getContacts().get(0);
        SupplierDetailsDTO supplierDetailsDTO = new SupplierDetailsDTO(supplier.getSupId(), supplier.getSupplierName(),
                supplier.getIncNum(),supplier.getAccountNumber(), supplier.getAddress(),
                contactInfo.getName(), contactInfo.getPhoneNumber(), contactInfo.getEmail(),
                supplier.getContract().getDailyInfo(), supplier.getArea(), supplier.isSelfDelivery());

        return new OrderShipDetails(orderDetails.orderId, orderDetails.shopNumber, orderDetails.deliveryDate,
                supplierDetailsDTO, orderDetails.details, orderDetails.isPeriodicalOrder);

    }

    @Override
    public boolean updateOrderArrivalTime(int orderId, Date date) {
        return orderManager.updateOrderDelivery(orderId, date);
    }

    @Override
    public boolean updateOrderStatus(int orderId, OrderStatus status) {
        return orderManager.updateOrderStatus(orderId, status);
    }

    @Override
    public List<Integer> getAllOpenOrderIdsByShop(int shopNumber) {
        return orderManager.getAllOpenOrderIdsByShop(shopNumber);
    }

    @Override
    public List<Integer> getAllOpenPeriodicalOrder(){
        return orderManager.getAllOpenPeriodicalOrder();
    }

    @Override
    public List<Integer> getAllOpenOrders() {
        return orderManager.getAllOpenOrders();
    }

    public List<Integer> getAllWaitingShippingRegularOrders() {
        return orderManager.getAllWaitingShippingRegularOrders();
    }

    public List<Integer> getAllWaitingShippingPeriodicalOrders() {
        return orderManager.getAllWaitingShippingPeriodicalOrders();
    }


    public static ProductInOrder ProductInOrderDTOToPIO(ProductInOrderDTO productInOrderDTO){
        return new ProductInOrder(
                productInOrderDTO.barcode,
                productInOrderDTO.amount);
    }

    public static ProductInOrderDTO ProductInOrderToDTO(ProductInOrder productInOrder){
        return new ProductInOrderDTO(
                productInOrder.getBarcode(),
                productInOrder.getAmount(),
                productInOrder.getPricePerUnit());
    }

    public static Product SystemProductToProduct(SystemProduct systemProduct)
    {
        return new Product(systemProduct);
    }
}
