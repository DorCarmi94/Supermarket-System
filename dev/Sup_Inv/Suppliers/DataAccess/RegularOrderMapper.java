package Sup_Inv.Suppliers.DataAccess;

import Sup_Inv.Suppliers.Structs.OrderStatus;
import Sup_Inv.Suppliers.Structs.StructUtils;
import Sup_Inv.Suppliers.Supplier.Order.AllDetailsOfProductInOrder;
import Sup_Inv.Suppliers.Supplier.Order.Order;
import Sup_Inv.Suppliers.Supplier.Order.ProductInOrder;
import Sup_Inv.Suppliers.Supplier.Order.RegularOrder;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class RegularOrderMapper extends AbstractMapper<RegularOrder> {

    public RegularOrderMapper(Connection conn) {
        super(conn);
    }

    @Override
    protected String findStatement() {
        //TODO: problem with regular supplier order - the order isnt exist there!
        return "SELECT S.*, PIC.barcode, P.contract_id, P.catalog_number, P.amount, P.price_per_unit\n" +
                "FROM Supplier_order AS S JOIN Product_in_order AS P\n" +
                "JOIN Product_in_contract as PIC\n" +
                "ON S.id = P.order_id AND P.catalog_number =  PIC.catalog_number\n" +
                "WHERE S.id IN Regular_supplier_order AND S.id = ?";

        //SELECT S.*, PIC.barcode, P.contract_id, P.catalog_number, P.amount, P.price_per_unit
        // FROM Supplier_order AS S JOIN Product_in_order AS P JOIN Product_in_contract as PIC
        // ON S.id = P.order_id AND P.catalog_number =  PIC.catalog_number
        // WHERE S.id IN Regular_supplier_order AND S.id = 2
    }

    @Override
    protected String deleteStatement() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected RegularOrder buildTFromResultSet(ResultSet res) {
        RegularOrder regularOrder = null;
        int orderId = -1, shopNumber = -1;
        OrderStatus status = null;
        String deliveryDay = "";
        List<ProductInOrder> products = new ArrayList<>();

        try {
            if(res.next()) {
                orderId = res.getInt(1);
                shopNumber = res.getInt(2);
                status = StructUtils.getOrderStatus(res.getInt(3));
                deliveryDay = res.getString(4);
                products.add(new ProductInOrder(res.getInt(5), res.getInt(8), res.getString(7), res.getDouble(9)));
            } else {
                return null;
            }

            while (res.next()) {
                products.add(new ProductInOrder(res.getInt(5), res.getInt(8), res.getString(7), res.getDouble(9)));
            }

            regularOrder = RegularOrder.CreateRegularOrder(orderId, products, shopNumber);
            regularOrder.setStatus(status);

            DateFormat dateFormat = new SimpleDateFormat(StructUtils.dateFormat());
            regularOrder.setDeliveryDay(dateFormat.parse(deliveryDay));

        } catch (SQLException | ParseException e) {
        }

        return regularOrder;
    }

    private String insertIntoOrderStatement(){
        return "INSERT INTO Supplier_order (shop_number, status, delivery_day) " +
                "VALUES (?, ?, ?)";
    }

    private String insertIntoProductInOrderStatement(){
        return "INSERT INTO Product_in_order (order_id, contract_id, catalog_number, amount, price_per_unit) " +
                "VALUES (?, ?, ?, ?, ?)";
    }

    private String insertIdToRegularOrderStatement(){
        return "INSERT INTO Regular_supplier_order (id) " +
                "VALUES (?)";
    }

    private String isValidShopStatement(){
        return "SELECT SN from Stores  " +
                "WHERE SN = (?)";
    }

    public boolean isValidShopNumber(int shopNumber){
        try(PreparedStatement pstmt = conn.prepareStatement(isValidShopStatement())){
            pstmt.setString(1,""+shopNumber);
            ResultSet res = pstmt.executeQuery();

            if(res.next()){
                return true;
            }

        } catch (SQLException throwables) {
        }
        return false;
    }

    @Override
    public int insert(RegularOrder product) {

        int orderId = -1, contractId = product.getContractId();
        int rowAffected;
        boolean rollback = false;
        ResultSet res = null;

        if(!isValidShopNumber(product.getShopNumber())){
            return -2;
        }

        try{
            conn.setAutoCommit(false);
        } catch (SQLException throwables) {
        }

        try(PreparedStatement insertPstmt = conn.prepareStatement(insertIntoOrderStatement(), Statement.RETURN_GENERATED_KEYS);
            PreparedStatement productInsertPstmt = conn.prepareStatement(insertIntoProductInOrderStatement());
            PreparedStatement insertRegularIdPstmt = conn.prepareStatement(insertIdToRegularOrderStatement(), Statement.RETURN_GENERATED_KEYS)){
            conn.setAutoCommit(false);

            insertPstmt.setInt(1,product.getShopNumber());
            insertPstmt.setInt(2, StructUtils.getOrderStatusInt(product.getStatus()));

            String strDate = StructUtils.dateToForamt(product.getDeliveryDay());
            insertPstmt.setString(3, strDate);

            rowAffected = insertPstmt.executeUpdate();

            // get the material id
            res = insertPstmt.getGeneratedKeys();
            if (res.next()) {
                orderId = res.getInt(1);
            }

            if (rowAffected != 0) {
                for (ProductInOrder productInOrder : product.getProducts()) {
                    productInsertPstmt.clearParameters();
                    productInsertPstmt.setInt(1, orderId);
                    productInsertPstmt.setInt(2, contractId);
                    productInsertPstmt.setString(3, productInOrder.getProductCatalogNumber());
                    productInsertPstmt.setInt(4, productInOrder.getAmount());
                    productInsertPstmt.setDouble(5, productInOrder.getPricePerUnit());
                    productInsertPstmt.addBatch();
                }
                productInsertPstmt.executeBatch();

                insertRegularIdPstmt.setInt(1, orderId);
                rowAffected = insertRegularIdPstmt.executeUpdate();
                if (rowAffected == 0) {
                    rollback = true;
                }
            } else {
                rollback = true;
            }


        } catch (SQLException throwables) {
            rollback = true;
        }

        try{
            if(!rollback) {
                conn.commit();
            } else {
                conn.rollback();
            }
            conn.setAutoCommit(true);
        } catch (SQLException throwables) {
        }

        return orderId;
    }


    private String updateDeliveryDateStatement(){
        return "UPDATE Supplier_order\n" +
                "SET delivery_day = ?\n" +
                "WHERE id = ?";
    }

    public boolean updateDeliveryDate(int orderId, Date date){
        try(PreparedStatement ptsmt = conn.prepareStatement(updateDeliveryDateStatement())){

            ptsmt.setString(1,StructUtils.dateToForamt(date));
            ptsmt.setInt(2, orderId);

            ptsmt.executeUpdate();
            return true;


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    private String allShopOpenOrdersStatement(){
        return "SELECT id\n" +
                "FROM Supplier_order\n" +
                "WHERE shop_number = ? AND status = ?";
    }

    public List<Integer> getAllOpenOrderIdsByShop(int shopNumber) {
        ResultSet rs;
        List<Integer> orderIds = new ArrayList<>();

        try(PreparedStatement ptsmt = conn.prepareStatement(allShopOpenOrdersStatement())){

            ptsmt.setInt(1, shopNumber);
            ptsmt.setInt(2, StructUtils.getOrderStatusInt(OrderStatus.Open));

            rs = ptsmt.executeQuery();
            while(rs.next()){
                orderIds.add(rs.getInt(1));
            }
        } catch (SQLException throwables) {
        }

        return orderIds;
    }

    private String updateDeliveryStatusStatement(){
        return "UPDATE Supplier_order\n" +
                "SET status = ?\n" +
                "WHERE id = ?";
    }

    public boolean updateOrderStatus(int orderId, OrderStatus status){
        try(PreparedStatement ptsmt = conn.prepareStatement(updateDeliveryStatusStatement())){

            int statusInt = StructUtils.getOrderStatusInt(status);
            ptsmt.setInt(1,statusInt);
            ptsmt.setInt(2, orderId);

            ptsmt.executeUpdate();
            return true;


        } catch (SQLException throwables) {
        }

        return false;
    }

    private String loadBasicDetailsStatement(){
        return "SELECT *\n" +
                "FROM Supplier_order\n" +
                "WHERE id = ?";
    }

    public Order loadBasicDetails(int orderId) {

        try(PreparedStatement ptsmt = conn.prepareStatement(loadBasicDetailsStatement())){

            ptsmt.setInt(1, orderId);

            ResultSet res = ptsmt.executeQuery();

            if(res.next()){
                List<ProductInOrder> products = new ArrayList<>();
                products.add(null);
                RegularOrder order = RegularOrder.CreateRegularOrder(orderId, products, res.getInt(2));
                order.setStatus(StructUtils.getOrderStatus(res.getInt(3)));
                DateFormat dateFormat = new SimpleDateFormat(StructUtils.dateFormat());
                order.setDeliveryDay(dateFormat.parse(res.getString(4)));
                order.setContractId(getTheOrderContract(orderId));

                return order;
            }

        } catch (SQLException | ParseException e) {
        }
        return null;
    }

    private String getAllProductDetailsStatement(){
        return "SELECT PO.*, PIC.original_price, (1 - PO.price_per_unit / PIC.original_price) AS discount, P.name, P.barcode\n" +
                "FROM Product_in_order as PO JOIN Product_in_contract as PIC\n" +
                "\tJOIN Product as P\n" +
                "\t\tON PO.contract_id = PIC.contract_id AND PO.catalog_number = PIC.catalog_number\n" +
                "\t\tAND PIC.barcode = P.barcode WHERE PO.order_id = ?";
    }

    public List<AllDetailsOfProductInOrder> getAllProductDetails(int orderId) {
        List<AllDetailsOfProductInOrder> details = new ArrayList<>();

        try(PreparedStatement ptsmt = conn.prepareStatement(getAllProductDetailsStatement())){

            ptsmt.setInt(1, orderId);

            ResultSet res = ptsmt.executeQuery();

            while(res.next()){
                details.add(new AllDetailsOfProductInOrder(res.getInt(9), res.getInt(4),
                        res.getString(3), res.getDouble(5), res.getString(8),
                        res.getDouble(6), res.getDouble(7)));
            }

        } catch (SQLException e) {
        }

        return details;
    }

    private String getTheSupplierOfOrderStatement(){
        return "SELECT C.supplier_id\n" +
                "FROM Product_in_order AS P JOIN Contract AS C\n" +
                "ON P.contract_id = C.id\n" +
                "WHERE P.order_id = ?";
    }

    public int getTheSupplierOfOrder(int orderId) {
        try(PreparedStatement ptsmt = conn.prepareStatement(getTheSupplierOfOrderStatement())){

            ptsmt.setInt(1, orderId);

            ResultSet res = ptsmt.executeQuery();

            if(res.next()){
                return res.getInt(1);
            }

        } catch (SQLException e) {
        }
        return -1;
    }

    private String getTheOrderContractStatement(){
        return "SELECT DISTINCT(contract_id)\n" +
                "FROM Product_in_order\n" +
                "WHERE order_id = ?";
    }

    public int getTheOrderContract(int orderId) {
        try(PreparedStatement ptsmt = conn.prepareStatement(getTheOrderContractStatement())){

            ptsmt.setInt(1, orderId);

            ResultSet res = ptsmt.executeQuery();

            if(res.next()){
                return res.getInt(1);
            }

        } catch (SQLException e) {
        }
        return -1;
    }

    private String removeProductsFromOrderStatement(){
        return "DELETE FROM Product_in_order\n" +
                "WHERE order_id = ? AND catalog_number = ?";
    }

    public List<String> removeProductsFromOrder(int orderId, List<String> catalogs) {
        List<String> wasntDeleted = new ArrayList<>();

        for(String catalog : catalogs) {
            try (PreparedStatement ptsmt = conn.prepareStatement(removeProductsFromOrderStatement())) {

                ptsmt.setInt(1, orderId);
                ptsmt.setString(2, catalog);

                ptsmt.executeUpdate();

            } catch (SQLException e) {
                wasntDeleted.add(catalog);
            }
        }
        return wasntDeleted;
    }

    private String allOpenOrdersStatement(){
        return "SELECT id\n" +
                "FROM Supplier_order\n" +
                "WHERE status = ?";
    }

    public List<Integer> getAllOpenOrders() {
        ResultSet rs;
        List<Integer> orderIds = new ArrayList<>();

        try(PreparedStatement ptsmt = conn.prepareStatement(allOpenOrdersStatement())){
            ptsmt.setInt(1, StructUtils.getOrderStatusInt(OrderStatus.Open));

            rs = ptsmt.executeQuery();
            while(rs.next()){
                orderIds.add(rs.getInt(1));
            }
        } catch (SQLException throwables) {
        }

        return orderIds;
    }

    private String getAllWaitingShippingRegularOrdersStatement(){
        return "SELECT S.id\n" +
                "FROM Supplier_order AS S JOIN Regular_supplier_order AS R\n" +
                "ON S.id = R.id\n" +
                "WHERE S.status = ?";
    }

    public List<Integer> getAllWaitingShippingRegularOrders() {
        ResultSet rs;
        List<Integer> orderIds = new ArrayList<>();

        try(PreparedStatement ptsmt = conn.prepareStatement(getAllWaitingShippingRegularOrdersStatement())){
            ptsmt.setInt(1, StructUtils.getOrderStatusInt(OrderStatus.WaitingForShipping));

            rs = ptsmt.executeQuery();
            while(rs.next()){
                orderIds.add(rs.getInt(1));
            }
        } catch (SQLException throwables) {
        }

        return orderIds;
    }

    public String getPurchaseHistoryOfSupplierStatement() {
        return "SELECT DISTINCT(P.catalog_number)\n" +
                "FROM Product_in_order AS P JOIN CONTRACT AS C\n" +
                "WHERE P.contract_id = c.id AND C.supplier_id = ?";
    }

    public List<String> getPurchaseHistoryOfSupplier(int supplierId) {
        ResultSet rs;
        List<String> catalogs = new ArrayList<>();

        try(PreparedStatement ptsmt = conn.prepareStatement(getPurchaseHistoryOfSupplierStatement())){
            ptsmt.setInt(1, supplierId);

            rs = ptsmt.executeQuery();
            while(rs.next()){
                catalogs.add(rs.getString(1));
            }
        } catch (SQLException throwables) {
        }

        return catalogs;
    }

    public String getAllOrderCatalogsStatement() {
        return "SELECT DISTINCT(catalog_number)\n" +
                "FROM Product_in_order \n" +
                "WHERE order_id = ?";
    }

    public List<String> getAllOrderCatalogs(int orderId) {
        ResultSet rs;
        List<String> catalogs = new ArrayList<>();

        try(PreparedStatement ptsmt = conn.prepareStatement(getAllOrderCatalogsStatement())){
            ptsmt.setInt(1, orderId);

            rs = ptsmt.executeQuery();
            while(rs.next()){
                catalogs.add(rs.getString(1));
            }
        } catch (SQLException throwables) {
        }

        return catalogs;
    }
}
