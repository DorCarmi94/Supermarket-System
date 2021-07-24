package Trans_HR.Data_Layer.DAOs;

import Sup_Inv.DataAccess.SupInvDBConn;
//import Trans_HR.Data_Layer.Connection;
import Trans_HR.Data_Layer.Dummy_objects.dummy_Address;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class address_DAO {

    public void insert(dummy_Address dummy_address){

        String address_query="INSERT INTO \"main\".\"Address\"\n" +
                "(\"SN\", \"City\", \"Street\", \"Number\")\n" +
                String.format("VALUES ('%d', '%s', '%s', '%d');", dummy_address.getSN(), dummy_address.getCity(), dummy_address.getStreet(), dummy_address.getNumber());
        try {
            PreparedStatement statement= SupInvDBConn.getInstance().getConn().prepareStatement(address_query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Integer SN){
        String address_query="DELETE FROM \"main\".\"Address\"\n" +
                String.format("WHERE SN = '%d';",SN);
        try {
            PreparedStatement statement= SupInvDBConn.getInstance().getConn().prepareStatement(address_query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(){

    }

    public void delete(){

    }

    public void update(){

    }

    public dummy_Address select(int Sn){
        String query="SELECT * FROM Address";
        try {
            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(query);
            return new dummy_Address(rs2.getString("City"),rs2.getString("Street"),rs2.getInt("Number"),rs2.getInt("SN"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    private void executeQuery(String query){
        try {
            // java.sql.Date sqlDate = new java.sql.Date(worker.getStart_Date().getTime());
            PreparedStatement statement= SupInvDBConn.getInstance().getConn().prepareStatement(query);
            //statement.setDate(7,sqlDate);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        String delete = "DELETE from Address;";
        executeQuery(delete);
    }

    public int getAddressSn() {
        String query = "select SN from Address\n" +
                " ORDER BY SN DESC\n" +
                " LIMIT 1;";
        try {
            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(query);
            rs2.next();
            return rs2.getInt("SN");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public Integer getNextSN(){
        String query="SELECT MAX(SN) FROM Address";
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

    public List<dummy_Address> selectAll(){
        List<dummy_Address> output = new LinkedList<>();
        String query="SELECT * FROM Address";
        try {
            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(query);
            while (rs2.next()) {
                dummy_Address dummy_address = new dummy_Address(rs2.getInt("SN"),rs2.getString("City"),rs2.getString("Street"),rs2.getInt("Number"));
                output.add(dummy_address);
            }
            return output;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }
}
