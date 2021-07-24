package Trans_HR.Data_Layer.DAOs;


import Sup_Inv.DataAccess.SupInvDBConn;
import Trans_HR.Data_Layer.Dummy_objects.dummy_Area;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

public class area_DAO {
    public void insert(int SN, String area) {
        String query = MessageFormat.format(
                "INSERT INTO \"main\".\"Area\"\n(\"SN\",\"AreaName\")\n{0}", String.format("VALUES ('%d','%s');",
                        SN, area));

        executeQuery(query);

    }

    private void executeQuery(String query) {
        try {
            // java.sql.Date sqlDate = new java.sql.Date(worker.getStart_Date().getTime());
            PreparedStatement statement = SupInvDBConn.getInstance().getConn().prepareStatement(query);
            //statement.setDate(7,sqlDate);
            statement.executeQuery();
        } catch (SQLException e) {
            //e.printStackTrace();
        }
    }

    public void delete() {

    }

    public void update() {

    }

    public List<dummy_Area> selectAll(){
        List<dummy_Area> output = new LinkedList<>();
        String query="SELECT * FROM Area";
        try {
            Statement stmt2 = SupInvDBConn.getInstance().getConn().createStatement();
            ResultSet rs2  = stmt2.executeQuery(query);
            while (rs2.next()) {
                dummy_Area dummy_area = new dummy_Area(rs2.getInt("SN"),rs2.getString("AreaName"));
                output.add(dummy_area);
            }
            return output;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new NullPointerException();
    }

    public void deleteAll() {
        String delete = "DELETE from Area;";
        executeQuery(delete);
    }
}

