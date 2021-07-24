//package Trans_HR.Data_Layer;
//
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//public class Connection {
//
//    public java.sql.Connection getConn() {
//        return conn;
//    }
//
//    private static java.sql.Connection conn;
//    private static Connection connection = null;
//
//
//    public static Connection getInstance(){
//        if(connection == null){
//            connection = new Connection();
//        }
//        return connection;
//    }
//
//    private Connection(){
//        try {
//            String url = "jdbc:sqlite:"+System.getProperty("user.dir")+"/DB/sup_inv.db";
//            // create a connection to the database
//            conn = DriverManager.getConnection(url);
////            System.out.println("Connection to SQLite has been established.");
//
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        } finally {
//            try {
//                if (connection != null) {
//                    conn.close();
//                }
//            } catch (SQLException ex) {
//                System.out.println(ex.getMessage());
//            }
//        }
//    }
//
//}
