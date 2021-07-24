package Sup_Inv.Suppliers.DataAccess;

import Sup_Inv.Suppliers.Supplier.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper extends AbstractMapper<Product> {

    public ProductMapper(Connection conn) {
        super(conn);
    }

    @Override
    protected String findStatement() {
        return "SELECT * " +
                "FROM Product " +
                "WHERE barcode = ?";
    }


    @Override
    protected Product buildTFromResultSet(ResultSet res) {
        try {
            if(res.next()) {
                return new Product(res.getInt(1),
                        res.getString(2), res.getString(3),res.getString(4), res.getString(5),
                        res.getString(6), res.getInt(7), res.getInt(8));
            }
        } catch (SQLException e) {
        }

        return null;
    }

    protected String insertStatement() {
        return "INSERT INTO Product (barcode, name, manufacture, category, subCategory, size, freqSupply, minPrice)  " +
                "Values (?, ?, ?, ?, ?, ?, ?, ?)";
    }

    /*
     * In this case the id is the barcode and we know it. if we dont have the id
     * we need to fill the id in the given object
     */
    @Override
    public int insert(Product product) {
        if (loadedMap.getOrDefault(product.getBarCode(), null) != null) {
            return product.getBarCode();
        }

        try(PreparedStatement pstmt = conn.prepareStatement(insertStatement())){

            pstmt.setInt(1, product.getBarCode());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getManufacture());
            pstmt.setString(4, product.getCategory());
            pstmt.setString(5, product.getSubCategory());
            pstmt.setString(6, product.getSize());
            pstmt.setInt(7, product.getFreqSupply());
            pstmt.setDouble(8, product.getMinPrice());

            pstmt.executeUpdate();

            loadedMap.put(product.getBarCode(), product);
            return product.getBarCode();

        } catch (java.sql.SQLException e) {
            return -1;
        }
    }

    @Override
    protected String deleteStatement() {
        return "DELETE FROM Product " +
                "WHERE barcode = ?";
    }

    protected String updateStatement() {
        return "UPDATE Product " +
                "SET name = ? ,manufacture = ?, category = ?, subCategory = ?, " +
                "size = ?, freqSupply = ?, minPrice = ? WHERE barcode = ?";
    }


    public boolean update(Product product) {
        int barcode = product.getBarCode();

        try(PreparedStatement pstmt = conn.prepareStatement(updateStatement())){

            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getManufacture());
            pstmt.setString(3, product.getCategory());
            pstmt.setString(4, product.getSubCategory());
            pstmt.setString(5, product.getSize());
            pstmt.setInt(6, product.getFreqSupply());
            pstmt.setDouble(7, product.getMinPrice());

            pstmt.setInt(8, barcode);

            pstmt.executeUpdate();

            loadedMap.remove(barcode);
            loadedMap.put(barcode, product);
            return true;

        } catch (java.sql.SQLException e) {
            return false;
        }
    }

    public String getAllIdsStatement(){
        return "SELECT barcode FROM Product";
    }

    public List<Integer> getAllBarcodes() {
        List<Integer> ids = new ArrayList<>();

        try(PreparedStatement pstmt = conn.prepareStatement(getAllIdsStatement())){
            ResultSet res = pstmt.executeQuery();

            while(res.next()){
                ids.add(res.getInt(1));
            }

        } catch (java.sql.SQLException e) {
            return null;
        }

        return ids;
    }
}
