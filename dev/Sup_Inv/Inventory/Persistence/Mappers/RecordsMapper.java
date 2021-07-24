package Sup_Inv.Inventory.Persistence.Mappers;

import Sup_Inv.Inventory.Persistence.DTO.RecordDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class RecordsMapper extends AbstractMappers {

    private Connection conn;

    public RecordsMapper(Connection conn) {
        this.conn = conn;
    }

    public HashMap<String, List<RecordDTO>> load(String shopNum) {
        String query = "SELECT * " +
                        "FROM Records " +
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

    private HashMap<String, List<RecordDTO>> builtDTOfromRes(ResultSet res) throws SQLException, ParseException {

        String itemId, recId, shopNum;
        Double cost, price;
        Date costChangeDate, priceChangeDate;
        RecordDTO currRec;
        HashMap<String, List<RecordDTO>> RecsDTO = new HashMap<>();

        while(res.next()){
            List<RecordDTO> tmpRecLst = new ArrayList<>();
            itemId = res.getString(res.findColumn("itemId"));
            recId = res.getString(res.findColumn("recId"));
            shopNum = res.getString(res.findColumn("shopNum"));
            cost = res.getDouble(res.findColumn("cost"));
            price = res.getDouble(res.findColumn("price"));
            String costTmp = res.getString(res.findColumn("costChangeDate"));
            costChangeDate = new SimpleDateFormat("yyyy-MM-dd").parse(convertToFormat(costTmp));
            String priceTmp = res.getString(res.findColumn("priceChangeDate"));
            priceChangeDate = new SimpleDateFormat("yyyy-MM-dd").parse(convertToFormat(priceTmp));
            currRec = new RecordDTO(recId, itemId, shopNum, cost, costChangeDate, price ,priceChangeDate);
            if(RecsDTO.keySet().contains(itemId))
                RecsDTO.get(itemId).add(currRec);
            else{
                tmpRecLst.add(currRec);
                RecsDTO.put(currRec.getItemId(), tmpRecLst);
            }
        }
        return RecsDTO;
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
    public LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    @Override
    public void insert() {
    }
    public void insert(RecordDTO recDTO){
        try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Records" +
                " (recId, shopNum, itemId, cost, price, costChangeDate, priceChangeDate)" +
                " Values (?, ?, ?, ?, ?, ?, ?)")){
            pstmt.setString(1, recDTO.getRecId());
            pstmt.setString(2, recDTO.getShopNum());
            pstmt.setString(3, recDTO.getItemId());
            pstmt.setDouble(4, recDTO.getCost());
            pstmt.setDouble(5, recDTO.getPrice());
            pstmt.setString(6, recDTO.getCostChangeDate().toString());
            pstmt.setString(7,recDTO.getPriceChangeDate().toString());
            pstmt.executeUpdate();
        } catch (java.sql.SQLException e) { }
    }

    @Override
    public void update() {

    }
//    public void update(RecordDTO recDTO){
//
//        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE Records " +
//                                            "SET " +
//                                            "cost = ?, price = ?, costChangeDate = ?, " +
//                                            "priceChangeDate = ? WHERE recId = ? AND " +
//                                            "shopNum = ?" +
//                                            "Values (?, ?, ?, ?, ?, ?)")) {
//
//            pstmt.setDouble(1, recDTO.getCost());
//            pstmt.setDouble(2, recDTO.getPrice());
//            pstmt.setString(3, recDTO.getCostChangeDate().toString());
//            pstmt.setString(4, recDTO.getPriceChangeDate().toString());
//            pstmt.setString(5, recDTO.getRecId());
//            pstmt.setString(6, recDTO.getShopNum());
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
    @Override
    public void delete() {

    }
}
