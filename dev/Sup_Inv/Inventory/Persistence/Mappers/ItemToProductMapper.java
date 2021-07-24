package Sup_Inv.Inventory.Persistence.Mappers;

import Sup_Inv.Inventory.Persistence.DTO.ItemDTO;
import Sup_Inv.Suppliers.DataAccess.ProductMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class ItemToProductMapper {
    private Connection conn;
    private ItemsMapper itemsMapper;
    private ProductMapper productMapper;


public ItemToProductMapper(Connection conn){
    this.conn = conn;
}
    //region getters & setters

    public Connection getConn() {
        return conn;
    }

    public void setConn(Connection conn) {
        this.conn = conn;
    }

    public ItemsMapper getItemsMapper() {
        return itemsMapper;
    }

    public void setItemsMapper(ItemsMapper itemsMapper) {
        this.itemsMapper = itemsMapper;
    }

    public ProductMapper getProductMapper() {
        return productMapper;
    }

    public void setProductMapper(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }


    //endregion

    public HashMap<String, ItemDTO> loadInvFromItemsAndProducts(String shopNum) {
        String query = "SELECT * " +
                        "FROM Items2 " +
                            "JOIN Product " +
                                "ON Items2.id = Product.barcode " +
                        "WHERE shopNum = ?";
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(query);
            statement.setString(1, shopNum);
            ResultSet resultSet  = statement.executeQuery();
            return builtDTOfromRes(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ItemDTO loadById(String barcode, String shopNum) {
            String query = "SELECT * " +
                    "FROM Product " +
                    "WHERE barcode = ?  ";

        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(query);
            statement.setString(1, barcode);
          //  statement.setString(2, barcode);
            ResultSet resultSet  = statement.executeQuery();
            return builtOneDTOfromRes(resultSet, shopNum);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HashMap<String, ItemDTO> builtDTOfromRes(ResultSet res) throws SQLException {

        String shopNum, id, quantityShop, quantityStorage;
        String name, manufacturer, category, sub_category, size; int freqBuySupply; double minPrice;

        ItemDTO currItem;
        HashMap<String, ItemDTO> itemsDTO = new HashMap<>();

        while(res.next()){
            shopNum = res.getString(res.findColumn("shopNum"));
            id = res.getString(res.findColumn("id"));
            quantityShop = res.getString(res.findColumn("qShop"));
            quantityStorage = res.getString(res.findColumn("qStorage"));
            name = res.getString(res.findColumn("name"));
            manufacturer = res.getString(res.findColumn("manufacture"));
            category = res.getString(res.findColumn("category"));
            sub_category = res.getString(res.findColumn("subCategory"));
            size = res.getString(res.findColumn("size"));
            freqBuySupply = res.getInt(res.findColumn("freqSupply"));
            minPrice = res.getDouble(res.findColumn("minPrice"));

            currItem = new ItemDTO(shopNum, id, quantityShop, quantityStorage,
                                    name, manufacturer, category, sub_category, size, freqBuySupply, minPrice);
            itemsDTO.put(currItem.getId(), currItem);
        }
        return itemsDTO;
    }

    private ItemDTO builtOneDTOfromRes(ResultSet res, String shopNumCurr) throws SQLException {

        String shopNum, id, quantityShop, quantityStorage;
        String name, manufacturer, category, sub_category, size; int freqBuySupply; double minPrice;

        ItemDTO currItem = null;

        while(res.next()){
            shopNum = shopNumCurr;
            id = res.getString(res.findColumn("barcode"));
            quantityShop = "0";
            quantityStorage = "0";
            name = res.getString(res.findColumn("name"));
            manufacturer = res.getString(res.findColumn("manufacture"));
            category = res.getString(res.findColumn("category"));
            sub_category = res.getString(res.findColumn("subCategory"));
            size = res.getString(res.findColumn("size"));
            freqBuySupply = res.getInt(res.findColumn("freqSupply"));
            minPrice = res.getDouble(res.findColumn("minPrice"));

            currItem = new ItemDTO(shopNum, id, quantityShop, quantityStorage,
                    name, manufacturer, category, sub_category, size, freqBuySupply, minPrice);
        }
        return currItem;
    }



}
