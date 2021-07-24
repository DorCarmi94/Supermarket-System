package Trans_HR.Data_Layer.DAOs;


import Sup_Inv.DataAccess.SupInvDBConn;
import Trans_HR.Business_Layer.Transportations.Utils.Buisness_Exception;
import Trans_HR.Data_Layer.Dummy_objects.dummy_License;
import Trans_HR.Data_Layer.Dummy_objects.dummy_Truck;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class truck_DAO {
    public void insert(dummy_Truck truck){
        String query="INSERT INTO \"main\".\"Trucks\"\n" +
                "(\"SN\", \"LicenseNumber\", \"Model\", \"Weight\", \"MaxWeight\")\n" +
                String.format("VALUES ('%s', '%s', '%s', '%.2f', '%.2f');", truck.getSN(), truck.getLicense_number(), truck.getModel(), truck.getWeight(), truck.getMax_weight());

        try {
            PreparedStatement statement= SupInvDBConn.getInstance().getConn().prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(int SNLicense: truck.getLicense_type())
        {
            String query_type="INSERT INTO \"main\".\"Truck_License\"\n" +
                    "(\"TruckSN\", \"LicenseSN\")\n" +
                    String.format("VALUES ('%d', '%d');",truck.getSN(),SNLicense);
            try {
                PreparedStatement statement= SupInvDBConn.getInstance().getConn().prepareStatement(query_type);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertTruckLicense(List<Integer> license, int SN) throws Buisness_Exception {
        for(int SNLicense: license)
        {
            String query_type="INSERT INTO \"main\".\"Truck_License\"\n" +
                    "(\"TruckSN\", \"License`N\")\n" +
                    String.format("VALUES ('%d', '%d');",SN, SNLicense);
            try {
                PreparedStatement statement= SupInvDBConn.getInstance().getConn().prepareStatement(query_type);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    public void delete(Integer SN) throws Buisness_Exception {

        List<Integer> license = getTruckLicense(SN);
        String query="DELETE FROM \"main\".\"Truck_License\"\n" +
                String.format("WHERE TruckSN = '%d';",SN);
        try {
            PreparedStatement statement= SupInvDBConn.getInstance().getConn().prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query_type="DELETE FROM \"main\".\"Trucks\"\n" +
                String.format("WHERE SN = '%d';",SN);
        try {
            PreparedStatement statement= SupInvDBConn.getInstance().getConn().prepareStatement(query_type);
            statement.executeUpdate();
        } catch (SQLException e) {
            insertTruckLicense(license, SN);
            throw new Buisness_Exception("-Cannot remove a truck that is in another transport-");
//            e.printStackTrace();
        }
    }

    public List<Integer> getTruckLicense(int SN){
        String query2="SELECT * FROM Truck_License\n"+
                String.format("WHERE TruckSN = '%d';",SN);
        List<Integer> license = new LinkedList<>();
        try {
            Statement stmt = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs  = stmt.executeQuery(query2);
            while (rs.next()) {
                license.add(rs.getInt("LicenseSN"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new NullPointerException();
        }
        return license;

    }


    private int getLicense_SN(String type){
        String selectQuery = String.format("select * from License where LicenseType = '%s'",type);
        try {

            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(selectQuery);
            int SN= rs2.getInt("SN");
            return SN;


        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public List<dummy_License> selectAllLicense(){
        String selectQuery = "select * from License";
        List<dummy_License> list=new LinkedList<>();
        try {
            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(selectQuery);
            while (rs2.next()){
                dummy_License add= new dummy_License(rs2.getInt("SN"),
                        rs2.getString("LicenseType"));
                list.add(add);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public dummy_Truck select(int SN){
        List<dummy_Truck> output = new LinkedList<>();
        String query="SELECT * FROM Trucks\n" +
        String.format("WHERE SN = '%d';",SN);
        try {
            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(query);
            if(!rs2.next())
                return null;
            else {
                dummy_Truck dummy_truck = new dummy_Truck(rs2.getInt("SN"),rs2.getInt("LicenseNumber"),
                        rs2.getString("Model"),rs2.getInt("Weight"),rs2.getInt("MaxWeight"));


                String query2="SELECT * FROM Truck_License\n"+
                        String.format("WHERE TruckSN = '%d';",dummy_truck.getSN());
                List<Integer> license = new LinkedList<>();
                try {
                    Statement stmt = SupInvDBConn.getInstance().getConn().createStatement();
                    ResultSet rs  = stmt.executeQuery(query2);
                    while (rs.next()) {
                        license.add(rs.getInt("LicenseSN"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new NullPointerException();
                }
                dummy_truck.setLicense_type(license);

                return dummy_truck;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }




    public List<dummy_Truck> selectAll(){
        List<dummy_Truck> output = new LinkedList<>();
        String query="SELECT * FROM Trucks";
        try {
            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(query);
            while (rs2.next()) {
                dummy_Truck dummy_truck = new dummy_Truck(rs2.getInt("SN"),rs2.getInt("LicenseNumber"),
                        rs2.getString("Model"),rs2.getInt("Weight"),rs2.getInt("MaxWeight"));


                String query2="SELECT * FROM Truck_License\n"+
                        String.format("WHERE TruckSN = '%d';",dummy_truck.getSN());
                List<Integer> license = new LinkedList<>();
                try {
                    Statement stmt = SupInvDBConn.getInstance().getConn().createStatement();
                    ResultSet rs  = stmt.executeQuery(query2);
                    while (rs.next()) {
                        license.add(rs.getInt("LicenseSN"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new NullPointerException();
                }
                dummy_truck.setLicense_type(license);

                output.add(dummy_truck);
            }
            return output;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public Integer getNextSN(){
        String query="SELECT MAX(SN) FROM Trucks";
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


    public List<Integer> selectTransportation(int SN) {
        List<Integer> items=new LinkedList<>();
        String query = "SELECT TransportationSN FROM Transportation_Truck\n" +
                String.format("WHERE TruckSN = '%d';", SN);
        try {
            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2 = stmt2.executeQuery(query);
            while (rs2.next()){
                items.add(rs2.getInt("TransportationSN"));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return items;
    }





//    public dummy_Truck select_by_id(int id){
//
//        String selectQuery = String.format("select * from Trucks where Trucks.SN = '%d'",id);
//        try {
//
//            Statement stmt2 = Connection.getInstance().getConn().createStatement();
//            ResultSet rs2  = stmt2.executeQuery(selectQuery);
//             dummy_Truck result = new dummy_Truck(rs2.getInt("SN"),rs2.getInt("LicenseNumber"),rs2.getString("Model"),rs2.getInt("Weight"),rs2.getInt("MaxWeight"),get_type(rs2.getInt("SN")));
//             return result;
//
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        throw new NullPointerException();
//    }

    private String get_type(int id){
        String selectQuery = String.format("select * from Truck_License where TruckSN = '%d'",id);

        int sn;
        String result;
        try {

            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(selectQuery);
            sn=rs2.getInt("LicenseSN");
            String query1=String.format("SELECT LicenseType FROM License WHERE SN = '%d' ",sn);
            Statement stmt = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs  = stmt.executeQuery(query1);
            result=rs.getString("License_Type");
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }


}
