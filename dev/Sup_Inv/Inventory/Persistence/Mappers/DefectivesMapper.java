package Sup_Inv.Inventory.Persistence.Mappers;
import Sup_Inv.Inventory.Persistence.DTO.DefectiveDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DefectivesMapper extends AbstractMappers {

    private Connection conn;

    public DefectivesMapper(Connection conn) {
        this.conn = conn;
    }


    public HashMap<String, List<DefectiveDTO>> load(String shopNum) {
        String query = "SELECT * " +
                "FROM Defectives " +
                "WHERE shopNum = ?";
        PreparedStatement statement = null;
        try {
            statement = conn.prepareStatement(query);
            statement.setString(1, shopNum);
            ResultSet resultSet  = statement.executeQuery();
            return builtDTOfromRes(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    private HashMap<String, List<DefectiveDTO>> builtDTOfromRes(ResultSet res) throws SQLException, ParseException {

        String defId, itemId, shopNum;
        Date updateDate;
        int quantity;
        boolean expired, defective;

        DefectiveDTO currDef;
        HashMap<String, List<DefectiveDTO>> DefsDTO = new HashMap<>();

        while(res.next()){
            List<DefectiveDTO> tmpDefLst = new ArrayList<>();
            defId = res.getString(res.findColumn("defId"));
            itemId = res.getString(res.findColumn("itemId"));
            shopNum = res.getString(res.findColumn("shopNum"));
            quantity = res.getInt(res.findColumn("quantity"));
            expired = res.getBoolean(res.findColumn("expired"));
            defective = res.getBoolean(res.findColumn("defective"));
            String updateDateTmp = res.getString(res.findColumn("updateDate"));
            updateDate =  new SimpleDateFormat("yyyy-MM-dd").parse(convertToFormat(updateDateTmp));
            currDef = new DefectiveDTO(defId, itemId, shopNum, quantity, updateDate, expired, defective);

            if(DefsDTO.keySet().contains(itemId))
                DefsDTO.get(itemId).add(currDef);
            else{
                tmpDefLst.add(currDef);
                DefsDTO.put(itemId, tmpDefLst);
            }
        }
        return DefsDTO;
    }


    @Override
    public void insert() {

    }
    public void insert(DefectiveDTO defDTO){


        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Defectives" +
                " (defId, itemId, quantity, updateDate, expired, defective, shopNum) " +
                "Values (?, ?, ?, ?, ?, ?, ?)")){
            pstmt.setString(1, defDTO.getDefId());
            pstmt.setString(2, defDTO.getItemId());
            pstmt.setInt(3, defDTO.getQuantity());
            pstmt.setString(4, defDTO.getUpdateDate().toString());
            pstmt.setBoolean(5, defDTO.isExpired());
            pstmt.setBoolean(6, defDTO.isDefective());
            pstmt.setString(7, defDTO.getShopNum());
            pstmt.executeUpdate();

        } catch (java.sql.SQLException e) { }
    }

    private String convertToFormat(String costTmp) {
        String[] splited = costTmp.split(" ");
        String output = splited[5] + "-";
        switch (splited[1]){
            case "Jan": output += "01"; break;
            case "Feb": output += "02"; break;
            case "Mar": output += "03"; break;
            case "Apr": output += "04"; break;
            case "May": output += "05"; break;
            case "Jun": output += "06"; break;
            case "Jul": output += "07"; break;
            case "Aug": output += "08"; break;
            case "Sep": output += "09"; break;
            case "Oct": output += "10"; break;
            case "Nov": output += "11"; break;
            case "Dec": output += "12"; break;
            default: //error!!!
        }
        output = output + "-" + splited[2];
        return output;
    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }
}
