package Sup_Inv.Suppliers.Supplier.Order;

import Sup_Inv.DataAccess.SupInvDBConn;
import Sup_Inv.Result.Result;
import Sup_Inv.Suppliers.DataAccess.PeriodicalOrderMapper;
import Sup_Inv.Suppliers.DataAccess.RegularOrderMapper;
import Sup_Inv.Suppliers.Structs.Days;
import Sup_Inv.Suppliers.Structs.OrderStatus;
import Sup_Inv.Suppliers.Structs.StructUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class OrderManager {

    private static OrderManager instance = null;

    private RegularOrderMapper regularOrderMapper;
    private PeriodicalOrderMapper periodicalOrderMapper;

    private OrderManager(){
        regularOrderMapper = new RegularOrderMapper(SupInvDBConn.getInstance().getConn());
        periodicalOrderMapper = new PeriodicalOrderMapper(SupInvDBConn.getInstance().getConn());
    }

    public static OrderManager getInstance(){
        if(instance == null){
            instance = new OrderManager();
        }
        return instance;
    }

    public RegularOrder getRegularOrder(int orderId){
        return regularOrderMapper.findById(orderId);
    }

    public PeriodicalOrder getPeriodicalOrder(int orderId){
        return periodicalOrderMapper.findById(orderId);
    }


    public void createRegularOrder(RegularOrder regularOrder){
        regularOrder.setOrderId(regularOrderMapper.insert(regularOrder));
    }

    public List<Integer> createPeriodicalOrder(List<ProductInOrder> products, List<Days> days, int weekPeriod, int shopNumber,
                                               int contractID, boolean isSupplierSelfDeliver){
        List<Integer> orderIds = new LinkedList<>();
        Date deliveryDate = StructUtils.getTheNearestDate(days);
        int orderToMake = days.size();

        if(weekPeriod == 1){
            orderToMake *= 4;
        } else if(weekPeriod == 2) {
            orderToMake *= 2;
        } else {
            return orderIds;
        }

        for(int j=0; j < orderToMake; j++){
            PeriodicalOrder periodicalOrder = PeriodicalOrder.CreatePeriodicalOrder(-1, products, shopNumber);
            periodicalOrder.setDeliveryDay(deliveryDate);
            periodicalOrder.setContractId(contractID);
            if(!isSupplierSelfDeliver){
                periodicalOrder.setStatus(OrderStatus.WaitingForShipping);
            }
            int orderId = periodicalOrderMapper.insert(periodicalOrder);
            if(orderId > -1){
                orderIds.add(orderId);
            }

            deliveryDate = StructUtils.getTheNearestDateWithWeekPeriod(deliveryDate, days, weekPeriod);

        }

        return orderIds;
    }

    public boolean updateOrderDelivery(int orderId, Date date){
        Date dateNow = Calendar.getInstance().getTime();
        if(dateNow.compareTo(date) > 0){
            return false;
        }

        Order order = regularOrderMapper.loadBasicDetails(orderId);
        if(order == null || order.getStatus() == OrderStatus.Close){
            return false;
        }

        if(periodicalOrderMapper.isPeriodicalOrder(orderId)){
            return false;
        }

        return regularOrderMapper.updateDeliveryDate(orderId, date);
    }

    public boolean updateOrderStatus(int orderId, OrderStatus status){
        Order order = regularOrderMapper.loadBasicDetails(orderId);
        if(order == null || order.getStatus() == OrderStatus.Close){
            return false;
        }

        return regularOrderMapper.updateOrderStatus(orderId, status);
    }

    public List<Integer> getAllOpenOrderIdsByShop(int shopNumber){
        return regularOrderMapper.getAllOpenOrderIdsByShop(shopNumber);
    }

    public Order getOrderBasicDetails(int orderId) {
        return regularOrderMapper.loadBasicDetails(orderId);
    }

    public List<AllDetailsOfProductInOrder> getAllProductDetails(int orderId) {
        return regularOrderMapper.getAllProductDetails(orderId);
    }

    public boolean isPeriodicalOrder(int orderId){
        return periodicalOrderMapper.isPeriodicalOrder(orderId);
    }

    public List<Integer> addProductsToPeriodicalOrder(int orderId, List<ProductInOrder> productInOrders) {
        return periodicalOrderMapper.addProductsToPeriodicalOrder(orderId, productInOrders);
    }

    public int getTheSupplierOfOrder(int orderId) {
        return regularOrderMapper.getTheSupplierOfOrder(orderId);
    }

    public Result<List<String>> removeProductsFromOrder(int orderId, List<String> catalog) {
        List<String> catalogs = regularOrderMapper.getAllOrderCatalogs(orderId);
        if(catalogs == null){
            return Result.makeFailure("No such order id");
        }

        Set<String> depdupeCatalogs = new LinkedHashSet<>(catalog);
        catalog.clear();
        catalog.addAll(depdupeCatalogs);

        if(catalogs.size() == catalog.size()){
            return Result.makeFailure("Cant remove all the products");
        }

        return Result.makeOk("Remove products", regularOrderMapper.removeProductsFromOrder(orderId, catalog));
    }

    public Result<Order> orderArrived(int orderId) {
        Order order = getRegularOrder(orderId);
        if(order == null) {
            order = getPeriodicalOrder(orderId);
            if(order == null) {
                return Result.makeFailure("Invalid order id");
            }
        }

        if(order.getStatus() == OrderStatus.Close){
            return Result.makeFailure("The order is alerady closed");
        }
        updateOrderStatus(orderId, OrderStatus.Close);

        return Result.makeOk("Ok",order);
    }

    public List<Integer> getAllOpenOrders() {
        return regularOrderMapper.getAllOpenOrders();
    }

    public List<Integer> getAllOpenPeriodicalOrder() {
        return periodicalOrderMapper.getAllOpenOrders();
    }

    public List<Integer> getAllWaitingShippingRegularOrders() {
        return regularOrderMapper.getAllWaitingShippingRegularOrders();
    }


    public List<Integer> getAllWaitingShippingPeriodicalOrders() {
        return periodicalOrderMapper.getAllWaitingShippingPeriodicalOrders();
    }

    /**
     * Return all of the catalog number that exist is some order of the supplier
     * @param supplierId the supplier id
     * @return list of all of the catalog number that exist is some order of the supplier
     */
    public List<String> getPurchaseHistoryOfSupplier(int supplierId){
        return  regularOrderMapper.getPurchaseHistoryOfSupplier(supplierId);
    }
}
