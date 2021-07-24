package Sup_Inv.Suppliers.Service;


import Sup_Inv.Result.Result;
import Sup_Inv.Suppliers.Structs.Days;
import Sup_Inv.Suppliers.Structs.OrderStatus;

import java.util.Date;
import java.util.List;

public interface OrderAndProductManagement {

    /**
     * Return all the details of the product
     * @param barcode The barcode
     * @return SystemProduct or null if there isnt such a product
     */
    SystemProduct getProduct(int barcode);

    /**
     * A list of all the product barocdes in the system
     * @return list of all the product barocdes in the system, empty list if there isnt any product or null
     */
    List<Integer> getAllProductBarcodes();

    /**
     * Return a new list with the barcode from the given barcodes list
     * that exist in the system
     * @param barcodes list of barcodes to check
     * @return list of barcode that in the system
     */
    List<Integer> productsInTheSystem(List<Integer> barcodes);

    /**
     * Create a new order in the system
     * @param supplierId The supplier ID who need to supply the order
     * @param products The product to order
     * @return -1 if cant create the order, otherwise return the order id
     */
    Result<Integer> createNewSupplierOrder(int supplierId, List<ProductInOrderDTO> products, int shopNumber);

    Result<Integer> createRegularOrder(List<ProductInOrderDTO> products, int shopNumber);

    Result<List<Integer>> createPeriodicalOrder(List<ProductInOrderDTO> products, List<Days> days, int weekPeriod, int shopNumber);

    /**
     * Add products to the order. The proucts can be added up to day before the order
     * delivery day. If the product already exist in the order do noting with it.
     * @param orderId the order id
     * @param products the products to add
     * @return the list of product that wasnt added
     */
    public Result<List<Integer>> addProductsToPeriodicalOrder(int orderId, List<ProductInOrderDTO> products);

    /**
     * Remove products in the order. If the product doesnt exist, do noting.
     * @param orderId the order id
     * @param barcodes the barcodes to remove
     * @return the list of product that wasnt updated
     */
    public Result<List<Integer>> RemoveProductsFromPeriodicalOrder(int orderId,  List<Integer> barcodes);

    /**
     * Update the day of order arrival
     * @param orderId The order id
     * @param date The arrival day
     * @return true if it was updated.
     */
    public boolean updateOrderArrivalTime(int orderId, Date date);

    /**
     * Update the status of the given order id
     * @param orderId Order id
     * @param status Status
     * @return True if the update was successful
     */
    public boolean updateOrderStatus(int orderId, OrderStatus status);

    /**
     * Return a list of all the open orders of a shop
     * @param shopNumber the shop number
     * @return list with all the orders id that are open.
     */
    public List<Integer> getAllOpenOrderIdsByShop(int shopNumber);

    /**
     * Return a list of all the open orders
     * @return list with all the orders id that are open.
     */
    public List<Integer> getAllOpenOrders();

    /**
     * Return a list of all the open orders that are periodical
     * @return list with all the orders id that are open.
     */
    public List<Integer> getAllOpenPeriodicalOrder();

    /**
     * Return a list of all the waiting for shipping regular orders
     * @return list of all the waiting for shipping regular orders.
     */
    public List<Integer> getAllWaitingShippingRegularOrders();
    /**
     * Return a list of all the waiting for shipping periodical orders
     * @return list of all the waiting for shipping periodical orders.
     */
    public List<Integer> getAllWaitingShippingPeriodicalOrders();

    /**
     * Return all the details of order as discribed in the docs
     * @param orderId the order id
     * @return all the details of order as discribed in the docs
     */
    public OrderShipDetails orderDetails(int orderId);


    /**
     * Notify the supplier management system that an order has arrived to the store
     * @param orderId the order id
     * @return all the details of order as described in the docs
     */
    public Result<OrderDTO> orderArrived(int orderId);

}

