package Trans_HR.Data_Layer.DAOs;

import Sup_Inv.DataAccess.SupInvDBConn;
import Trans_HR.Data_Layer.Dummy_objects.dummy_Items_File;
import javafx.util.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class item_file_DAO {

    public void insert(dummy_Items_File items_file){
        String query="INSERT INTO \"main\".\"ItemFiles\"\n" +
                "(\"SN\", \"StoreSN\", \"SupplierSN\", \"OrderSN\")\n" +
                String.format("VALUES ('%d', '%d', '%d', '%d');", items_file.getSn(),items_file.getStore_id(), items_file.getSupplier_id(), items_file.getorderID());

        try {
            PreparedStatement statement= SupInvDBConn.getInstance().getConn().prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public dummy_Items_File select(int SN){
        String query="SELECT * FROM ItemFiles\n"+
                String.format("WHERE SN = '%d';", SN);;
        try {
            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(query);
            if(!rs2.next())
                return null;
            else
            {
                dummy_Items_File items_file = new dummy_Items_File(rs2.getInt("SN"),
                        rs2.getInt("StoreSN"),rs2.getInt("SupplierSN"),rs2.getInt("OrderSN"));
                return items_file;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
    }

    public List<dummy_Items_File> selectAll(){
        List<dummy_Items_File> output = new LinkedList<>();
        String query="SELECT * FROM ItemFiles";
        try {
            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(query);
            while (rs2.next()) {
                dummy_Items_File items_file = new dummy_Items_File(rs2.getInt("SN"),
                        rs2.getInt("StoreSN"),rs2.getInt("SupplierSN"),rs2.getInt("OrderSN"));
                output.add(items_file);
            }
            return output;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
    }

    public Integer getNextSN(){
        String query="SELECT MAX(SN) FROM ItemFiles";
        try {
            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(query);
            if(!rs2.next())
                return 1;
            else
                return rs2.getInt("MAX(SN)") + 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }



    public void delete(int SN){

        String query2="DELETE FROM \"main\".\"ItemFiles\"\n" +
                String.format("WHERE SN = '%d';",SN);
        try {
            PreparedStatement statement= SupInvDBConn.getInstance().getConn().prepareStatement(query2);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
