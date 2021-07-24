package Sup_Inv.Suppliers.DataAccess;

import Sup_Inv.Suppliers.Structs.Days;
import Sup_Inv.Suppliers.Structs.OrderStatus;
import Sup_Inv.Suppliers.Structs.StructUtils;
import Sup_Inv.Suppliers.Supplier.Order.PeriodicalOrder;
import Sup_Inv.Suppliers.Supplier.Order.ProductInOrder;
import Sup_Inv.Suppliers.Supplier.Order.RegularOrder;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class PeriodicalOrderMapper extends AbstractMapper<PeriodicalOrder> {

    public PeriodicalOrderMapper(Connection conn) {
        super(conn);
    }

    protected String findStatement() {

        return "SELECT S.*, PIC.barcode, P.contract_id, P.catalog_number, P.amount, P.price_per_unit\n" +
                "FROM Supplier_order AS S JOIN Product_in_order AS P\n" +
                "JOIN Product_in_contract as PIC\n" +
                "ON S.id = P.order_id AND P.catalog_number =  PIC.catalog_number\n" +
                "WHERE S.id IN Periodical_supplier_order AND S.id = ?";
    }

    @Override
    protected String deleteStatement() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected PeriodicalOrder buildTFromResultSet(ResultSet res) {
        PeriodicalOrder periodicalOrder = null;
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

            periodicalOrder = PeriodicalOrder.CreatePeriodicalOrder(orderId, products, shopNumber);
            periodicalOrder.setStatus(status);

            DateFormat dateFormat = new SimpleDateFormat(StructUtils.dateFormat());
            periodicalOrder.setDeliveryDay(dateFormat.parse(deliveryDay));

        } catch (SQLException | ParseException e) {
        }

        return periodicalOrder;
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
        return "INSERT INTO Periodical_supplier_order (id) " +
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
    public int insert(PeriodicalOrder product) {

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

    private Date getNextDate(Date date, List<Days> days, int weekP){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        while(c.getTime().compareTo(date) > 0){
            int nearestDayInDays = StructUtils.nearestDayInDays(days);

            if(nearestDayInDays != 8){

                if(c.get(Calendar.DAY_OF_WEEK) + nearestDayInDays < 8){
                    weekP = 0;
                } else{
                    weekP = weekP - 1;
                }

                c.add(Calendar.DATE, nearestDayInDays + (7 * weekP));
                return c.getTime();
            }
        }

        return c.getTime();

    }

    private String isPeriodicalOrder(){
        return "SELECT *\n" +
                "FROM Periodical_supplier_order\n" +
                "WHERE id = ?";
    }

    public boolean isPeriodicalOrder(int orderId) {
        try(PreparedStatement ptsmt = conn.prepareStatement(isPeriodicalOrder())){

            ptsmt.setInt(1,orderId);

            ResultSet rs = ptsmt.executeQuery();
            if(rs.next()) {
                return true;
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }

    private String getAllWaitingShippingPeriodicalOrdersStatement(){
        return "SELECT S.id\n" +
                "FROM Supplier_order AS S JOIN Periodical_supplier_order AS P\n" +
                "ON S.id = P.id\n" +
                "WHERE S.status = ?";
    }

    public List<Integer> getAllWaitingShippingPeriodicalOrders() {
        ResultSet rs;
        List<Integer> orderIds = new ArrayList<>();

        try(PreparedStatement ptsmt = conn.prepareStatement(getAllWaitingShippingPeriodicalOrdersStatement())){
            ptsmt.setInt(1, StructUtils.getOrderStatusInt(OrderStatus.WaitingForShipping));

            rs = ptsmt.executeQuery();
            while(rs.next()){
                orderIds.add(rs.getInt(1));
            }
        } catch (SQLException throwables) {
        }

        return orderIds;
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

    public List<Integer> addProductsToPeriodicalOrder(int orderId, List<ProductInOrder> productInOrders) {
        int contractId = getTheOrderContract(orderId);
        List<Integer> wasntAdded = new ArrayList<>();

        for(ProductInOrder product : productInOrders) {
            try (PreparedStatement ptsmt = conn.prepareStatement(insertIntoProductInOrderStatement())) {

                ptsmt.setInt(1, orderId);
                ptsmt.setInt(2, contractId);
                ptsmt.setString(3, product.getProductCatalogNumber());
                ptsmt.setInt(4, product.getAmount());
                ptsmt.setDouble(5, product.getPricePerUnit());

                ptsmt.executeUpdate();

            } catch (SQLException e) {
                wasntAdded.add(product.getBarcode());
            }
        }
        return wasntAdded;
    }

    private String allOpenOrdersStatement(){
        return "SELECT S.id\n" +
                "FROM Supplier_order AS S JOIN Periodical_supplier_order AS P\n" +
                "WHERE status = ? AND S.id = P.id";
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
}
