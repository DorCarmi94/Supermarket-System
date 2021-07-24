//package Trans_HR.Data_Layer.DAOs;
//
//
//import Sup_Inv.DataAccess.SupInvDBConn;
//import Trans_HR.Data_Layer.Dummy_objects.dummy_Missing_items;
//import javafx.util.Pair;
//
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.LinkedList;
//import java.util.List;
//
//public class missing_items_DAO {
//
//    public void insert(dummy_Missing_items missing_items){
//        String query="INSERT INTO \"main\".\"Missing_Items\"\n" +
//                "(\"SN\", \"StoreSN\", \"SupplierSN\")\n" +
//                String.format("VALUES ('%d', '%d', '%d');", missing_items.getSN(),missing_items.getStore_id(), missing_items.getSupplier_id());
//
//        try {
//            PreparedStatement statement= SupInvDBConn.getInstance().getConn().prepareStatement(query);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        for(int i=0;i<missing_items.getItems().size();i++) {
//            String query_items = "INSERT INTO \"main\".\"Missing_Items_list\"\n" +
//                    "(\"SN\", \"Missing_ItemSN\", \"Amount\", \"ItemName\")\n" +
//                    String.format("VALUES ('%d', '%d', '%d', '%s');", i, missing_items.getSN(), missing_items.getItems().get(i).getValue(), missing_items.getItems().get(i).getKey());
//            try {
//                PreparedStatement statement = SupInvDBConn.getInstance().getConn().prepareStatement(query_items);
//                statement.executeUpdate();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public List<dummy_Missing_items> selectAll(){
//        List<dummy_Missing_items> output = new LinkedList<>();
//        String query="SELECT * FROM Missing_Items";
//        try {
//            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
//            ResultSet rs2  = stmt2.executeQuery(query);
//            while (rs2.next()) {
//                dummy_Missing_items missing_items = new dummy_Missing_items(rs2.getInt("SN"),
//                        rs2.getInt("StoreSN"),rs2.getInt("SupplierSN"));
//
//
//                String query2="SELECT * FROM Missing_Items_list\n"+
//                        String.format("WHERE Missing_ItemSN = '%d' ORDER BY SN ASC;", missing_items.getSN());
//                List<Pair<String,Integer>> Items = new LinkedList<>();
//                try {
//                    Statement stmt = SupInvDBConn.getInstance().getConn().createStatement();
//                    ResultSet rs  = stmt.executeQuery(query2);
//                    while (rs.next()) {
//                        Pair<String,Integer> pair = new Pair<>(rs.getString("ItemName"),rs.getInt("Amount"));
//                        Items.add(rs.getInt("SN"),pair);
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                    throw new NullPointerException();
//                }
//                missing_items.setItems(Items);
//                output.add(missing_items);
//            }
//            return output;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new NullPointerException();
//        }
//    }
//
//
//
//    public void delete(int SN){
//        String query="DELETE FROM \"main\".\"Missing_Items_list\"\n" +
//                String.format("WHERE Missing_ItemSN = '%d';",SN);
//        System.out.println(query);
//        try {
//            PreparedStatement statement= SupInvDBConn.getInstance().getConn().prepareStatement(query);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        String query2="DELETE FROM \"main\".\"Missing_Items\"\n" +
//                String.format("WHERE SN = '%d';",SN);
//        try {
//            PreparedStatement statement= SupInvDBConn.getInstance().getConn().prepareStatement(query2);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    public void update(){
//
//    }
//
//}
