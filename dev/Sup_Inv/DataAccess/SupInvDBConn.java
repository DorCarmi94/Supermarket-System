package Sup_Inv.DataAccess;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SupInvDBConn {

    private static java.sql.Connection conn;
    private static SupInvDBConn SupInvDBConn = null;

    public static SupInvDBConn getInstance(){
        if(SupInvDBConn == null){
            SupInvDBConn = new SupInvDBConn();
        }
        return SupInvDBConn;
    }

    private SupInvDBConn(){
        try{
            //for the build project
            //String url = "jdbc:sqlite::resource:DB/db.db";
            // for easy use
            String url = "jdbc:sqlite:DB/db.db";
            conn = DriverManager.getConnection(url);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            /*try {
                if (SupInvDBConn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }*/
        }
    }


    public java.sql.Connection getConn() {
        return conn;
    }
}
