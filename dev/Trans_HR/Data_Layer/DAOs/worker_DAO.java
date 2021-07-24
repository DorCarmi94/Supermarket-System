package Trans_HR.Data_Layer.DAOs;

import Sup_Inv.DataAccess.SupInvDBConn;
import Trans_HR.Business_Layer.Workers.Modules.Worker.Worker;
import Trans_HR.Data_Layer.Dummy_objects.dummy_Worker;
import javafx.util.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

public class worker_DAO {
    /*public void insert(Worker workerToInsert){
        String workerInsertQuery = "INSERT INTO \"main\".\"Workers\"\n" +
                "(\"SN\", \"ID\", \"Name\", \"PhoneNumber\", \"BankAccount\", \"Salary\", \"Date\", \"Worker_Type\", \"StoreSN\")\n" +
                String.format("VALUES ('%d', '%d', '%s', '%s', '%d', '%d', '?', '%s', '%d');",
                        workerToInsert.getWorkerSn(),workerToInsert.getWorkerId(),workerToInsert.getWorkerName(),
                        workerToInsert.getWorkerPhone(),workerToInsert.getWorkerBankAccount(),workerToInsert.getWorkerSalary(),
                        workerToInsert.getWorkerJobTitle(),workerToInsert.getStoreSN());

        java.sql.Date sqlDate = new java.sql.Date(workerToInsert.getWorkerStartingDate().getTime());

        try {
            PreparedStatement statement= Connection.getInstance().getConn().prepareStatement(workerInsertQuery);
            statement.setDate(7,sqlDate);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for(Map.Entry<Pair<enums,enums>,Boolean> constrains : workerToInsert.getWorkerConstrains().entrySet()){
            Pair<enums,enums> cons = constrains.getKey();

            String workersConstrainsQuery = "INSERT INTO \"Constrains\"" +
                    "(\"WorkerSN\", \"Shift_typeSN\", \"DayOfWeek\", \"CanWork\") " +
                    String.format("VALUES ('%d', '%d', '%d', '%s')", workerToInsert.getWorkerSn(),cons.getValue(),cons.getKey(),constrains.getValue().toString());
        }
    }*/

    public void insert(dummy_Worker worker) {
        String query = MessageFormat.format("INSERT INTO \"main\".\"Workers\"\n(\"SN\",\"ID\",\"Name\",\"PhoneNumber\",\"BankAccount\", \"Salary\", \"StoreSN\", \"date\", \"Job_Title\")\n{0}",
                                                                                                               String.format("VALUES ('%d','%d','%s','%s','%d','%d','%d', %s , '%s');",
                                    worker.getSN(),worker.getId(), worker.getName(), worker.getPhone(), worker.getBankAccount(), worker.getSalary(), worker.getStoreSN(), "?", worker.getJob_title()));

        try {
            java.sql.Date sqlDate = new java.sql.Date(worker.getStart_Date().getTime());
            PreparedStatement statement= SupInvDBConn.getInstance().getConn().prepareStatement(query);
            statement.setDate(1,sqlDate);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteManager(int Manager){
        deleteConstraints(Manager);
        String removeShiftManager = "DELETE FROM Shift_Worker WHERE SNWorker = " + Manager + "; DELETE FROM Shift where ManagerSN = " +Manager+ "; DELETE FROM Shift where ManagerSN =" +Manager +";";
        executeQuery(removeShiftManager);
    }

    public void deleteEmployee(int workerSN){
        String sql = "DELETE FROM Workers WHERE SN = " + workerSN;
        executeQuery(sql);
    }

/*    public void update(Shift shiftToInsert,Worker worker){
        String shiftWorkersQuery = "INSERT INTO \"main\".\"Shift_Worker\"\n" +
                "(\"SNShift\", \"SNWorker\")\n" +
                String.format("VALUES ('%d', '%d');", shiftToInsert.getShiftSn(),worker.getWorkerSn());
    }*/

    public dummy_Worker selectWorkerBySN(int workerSN){
        Worker workerToReturn = null;
        String selectQuery = String.format("select * from Workers where Workers.SN = '%d'",workerSN);
        try {
            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(selectQuery);
            rs2.next();
            dummy_Worker toADD = new dummy_Worker(rs2.getInt("SN"),rs2.getInt
                    ("ID"),rs2.getString("Name"),rs2.getString("PhoneNumber"),
                    rs2.getInt("BankAccount"),rs2.getInt("Salary"),rs2.getInt("StoreSN"),rs2.getDate("Date"),rs2.getString("Job_Title"));
            return toADD;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public List<dummy_Worker> selectAllDrivers(){
        Worker workerToReturn = null;
        List<dummy_Worker> listToReturn = new LinkedList<>();
        String selectQuery = "select * from Workers where Workers.Job_Title = 'Driver' Or Workers.Job_Title = 'driver' ";
        try {
            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(selectQuery);
            while(rs2.next()) {
                dummy_Worker toADD = new dummy_Worker(rs2.getInt("SN"), rs2.getInt
                        ("ID"), rs2.getString("Name"), rs2.getString("PhoneNumber"),
                        rs2.getInt("BankAccount"), rs2.getInt("Salary"), rs2.getInt("StoreSN"), rs2.getDate("Date"), rs2.getString("Job_Title"));
//            List<Integer> Licenses = selectDriverLicenseByWorkerSN(toADD.getSN());
//            List<Pair<Integer,Integer>> Constrains = selectConstrainsByWorkerSN(toADD.getSN());
                listToReturn.add(toADD);
            }
            return listToReturn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public List<dummy_Worker> selectWorkersByStoreSN(int storeSN){
        List<dummy_Worker> workerToReturn = new LinkedList<>();
        String selectQuery = String.format("select * from Workers where StoreSN = '%d'", storeSN);
        try {
            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(selectQuery);
            while (rs2.next()) {
               dummy_Worker toADD = new dummy_Worker(rs2.getInt("SN"),rs2.getInt
                       ("ID"),rs2.getString("Name"),rs2.getString("PhoneNumber"),
                       rs2.getInt("BankAccount"),rs2.getInt("Salary"),rs2.getInt("StoreSN"),rs2.getDate("Date"),rs2.getString("Job_Title"));
               workerToReturn.add(toADD);
            }
            return workerToReturn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public List<Pair<Integer,Integer>> selectConstrainsByWorkerSN(int workerSN){
        String constrainsQuery = String.format("select * from Constrains where WorkerSN = %d", workerSN);
        List<Pair<Integer,Integer>> constraints = new LinkedList<>();
        try {
            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(constrainsQuery);
            while (rs2.next()) {
                Pair<Integer,Integer> toADD =new Pair<>(rs2.getInt("DayOfWeek"),rs2.getInt
                        ("ShiftType"));
                constraints.add(toADD);
            }
            return constraints;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public List<Integer> selectDriverLicenseByWorkerSN(int workerSN){
        String constrainsQuery = String.format("select * from Driver_License where DriverSN = %d", workerSN);
        List<Integer> license = new LinkedList<>();
        try {
            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(constrainsQuery);
            while (rs2.next()) {
                int toADD =rs2.getInt("LicenseSN");
                license.add(toADD);
            }
            return license;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }
/*

    public void insertLicense(int sn, int license){
        String query = "INSERT INTO \"main\".\"Driver_License\"\n" +
                "(\"DriverSN\",\"LicenseSN\")\n" +
                String.format("VALUES ('%d','%d');",sn, license);

    }

        try {

            Statement stmt2 = Connection.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(selectQuery);
            return new dummy_Worker(rs2.getString("SN"),rs2.getString("ID"),rs2.getString("Name"),rs2.getInt("PhoneNumber"),rs2.getInt("BankAccount"),rs2.getInt("Salary"),rs2.getInt("StoreSN"),AddressSn,rs2.getInt("AreaSN"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    } */

    public void updateSalary(int sn,int salary){
        String query_Salary = "UPDATE \"main\".\"Workers\"\n" + "SET Salary = " + salary +" WHERE SN = "+sn;

        executeQuery(query_Salary);
    }

    public void deleteConstraints(int workerSN){
        String sql = "DELETE FROM \"main\".\"Constrains\"\n WHERE WorkerSN = " + workerSN;

        executeQuery(sql);
    }
    public void addConstraints(int workerSn,int selectedDay,int sType){
        String query_constraints = "INSERT INTO \"main\".\"Constrains\"\n" +
                "(\"WorkerSN\",\"DayOfWeek\",\"ShiftType\",\"CanWork\")\n" +
                String.format("VALUES ('%d','%d','%d','%b');",workerSn,selectedDay, sType,false);

        executeQuery(query_constraints);
    }


    public void insertLicense(int sn, int license){
        String query = "INSERT INTO \"main\".\"Driver_License\"\n" +
                "(\"DriverSN\",\"LicenseSN\")\n" +
                String.format("VALUES ('%d','%d');",sn, license);
        executeQuery(query);
    }
    public void initShiftType(){

        String query = "INSERT INTO \"main\".\"Shift_Type\"\n" +
                "(\"SN\",\"DayType\")\n" +
                String.format("VALUES ('%d','%s');",1, "MORNING");
        String que = "INSERT INTO \"main\".\"Shift_Type\"\n" +
                "(\"SN\",\"DayType\")\n" +
                String.format("VALUES ('%d','%s');",2, "NIGHT");

        executeNOexception(query);
        executeNOexception(que);
    }





    public void initLicense(){

        String query = "INSERT INTO \"main\".\"License\"\n" +
                "(\"SN\",\"LicenseType\")\n" +
                String.format("VALUES ('%d','%s');",1, "C");
        String que = "INSERT INTO \"main\".\"License\"\n" +
                "(\"SN\",\"LicenseType\")\n" +
                String.format("VALUES ('%d','%s');",2, "C1");

        executeNOexception(query);
        executeNOexception(que);
    }
    private void executeNOexception(String query){
        try {
            // java.sql.Date sqlDate = new java.sql.Date(worker.getStart_Date().getTime());
            PreparedStatement statement= SupInvDBConn.getInstance().getConn().prepareStatement(query);
            //statement.setDate(7,sqlDate);
            statement.executeUpdate();
        } catch (SQLException e) {
         //  e.printStackTrace();
        }
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

    public void deleteAllDriverLicense() {
        String delete = "DELETE from Driver_License;";
        executeQuery(delete);
    }

    public void deleteAllConstraints() {
        String delete = "DELETE from Constrains;";
        executeQuery(delete);
    }

    public void deleteAllWorkers() {
        String delete = "DELETE from Workers;";
        executeQuery(delete);
    }

    public void deleteLicenseType() {
        String delete = "DELETE from License;";
        executeQuery(delete);
    }

    public void deleteShiftType() {
        String delete = "DELETE from Shift_Type;";
        executeQuery(delete);
    }

    public int getWorkerSn() {
        String query = "select SN from Workers\n" +
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
/*
    public void insert(dummy_Worker worker) {
        String query = "INSERT INTO \"main\".\"Workers\"\n" +
                "(\"SN\",\"ID\",\"Name\",\"PhoneNumber\",\"BankAccount\", \"Salary\", \"StoreSN\", \"date\", \"jobTitle\")\n" +
                String.format("VALUES ('%d','%d','%s','%d','%d','%d','%d', '%date', '%s');", worker.getId(), worker.getName(), worker.getPhone(), worker.getBankAccount(), worker.getSalary(), worker.getStoreSN(), worker.getStart_Date(), worker.getJob_title());

        for(Pair x : worker.getConstrains().keySet()){
            boolean canWork =  worker.getConstrains().get(x);
            String query_constraints = "INSERT INTO \"main\".\"Workers_Constraints\"\n" +
                    "(\"WorkerSN\",\"Shift_type\",\"DayOfWeek\",\"CanWork\")\n" +
                    String.format("VALUES ('%d','%d','%d','%b');",worker.getSN(), x.getValue(), x.getKey(),canWork);

        }

    }





    public void deleteEmployee(int sn) {
        String sql = "DELETE FROM \"main\".\"Workers\"\n WHERE SN = " + sn;
    } */

}
