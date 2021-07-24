package Sup_Inv.Inventory.Persistence.Mappers;

import Sup_Inv.Inventory.Persistence.DTO.InventoryDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class InventoriesMapper extends AbstractMappers {
    private Connection conn;
    //region singelton Constructor
    private static InventoriesMapper instance = null;
    private InventoriesMapper() {
        super();
        this.conn = getMyDB();
    }
    public static InventoriesMapper getInstance(){
        if(instance == null)
            instance = new InventoriesMapper();
        return instance;
    }
    //endregion

    public HashMap<String, InventoryDTO> load() {
        String query = "SELECT * " +
                        "FROM Inventory";
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(query);
            ResultSet resultSet  = statement.executeQuery();
            return buildInvsfromRes(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private HashMap<String, InventoryDTO> buildInvsfromRes(ResultSet res) {
        InventoryDTO currInv;
        HashMap<String, InventoryDTO> invs = new HashMap<>(); //<id, inventoryDTO>
        try {
            while(res.next()){
                currInv = new InventoryDTO(res.getString(1), res.getString(2));
                invs.put(currInv.getShopNum(), currInv);
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invs;
    }

    @Override
    public void insert() {}
    public void insert(InventoryDTO invDTO) {
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Sup_Inv.Inventory" +
                " (id, name) " +
                "Values (?, ?)")){
            pstmt.setString(1, invDTO.getShopNum());
            pstmt.setString(2, invDTO.getShopName());

            pstmt.executeUpdate();

        } catch (java.sql.SQLException e) { }
    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }

    public String loadNewStore(int shop_id) {
        try (PreparedStatement pstmt = conn.prepareStatement("" +
                "SELECT Name FROM Stores WHERE Stores.SN = ?")){
            pstmt.setInt(1, shop_id);
            ResultSet resultSet  = pstmt.executeQuery();
            return nameFromRes(resultSet);
        } catch (java.sql.SQLException e) { }
        return "ERROR loadNewStore";
    }

    private String nameFromRes(ResultSet res) throws SQLException {
        String name = "";
        while (res.next()) {
            name = res.getString(res.findColumn("Name"));
        }
        return name;
    }
}
