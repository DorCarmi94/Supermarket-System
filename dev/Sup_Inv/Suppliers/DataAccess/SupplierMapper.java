package Sup_Inv.Suppliers.DataAccess;

import Sup_Inv.Suppliers.Structs.Days;
import Sup_Inv.Suppliers.Structs.PaymentOptions;
import Sup_Inv.Suppliers.Structs.StructUtils;
import Sup_Inv.Suppliers.Supplier.*;


import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SupplierMapper extends AbstractMapper<Supplier> {
    protected WeakValueHashMap<String, ContactInfo> loadedContacts;
    protected WeakValueHashMap<Integer, ContractWithSupplier> loadedContracts;
    protected WeakValueHashMap<Integer, ContractProduct> loadedProductsInContract;
    protected WeakValueHashMap<Integer, DiscountOfProduct> loadedDiscountsOfProducts;


    public SupplierMapper(Connection conn) {
        super(conn);
        loadedContacts            = new WeakValueHashMap<>();
        loadedContracts           = new WeakValueHashMap<>();
        loadedProductsInContract  = new WeakValueHashMap<>();
        loadedDiscountsOfProducts = new WeakValueHashMap<>();
    }



    //Inserts statements
    protected String insertSupplierStatement() {
        return "INSERT INTO Supplier (sup_name,address,account_number,paymentInfo,inc_number, area, self_delivery)  " +
                "Values (?,?,?,?,?,?,?)";
    }
    protected String insertContactStatement() {
        return "INSERT INTO Contact_info (name,phone_number,email,supplier_id)  " +
                "Values (?,?,?,?)";
    }
    private String addPaymentInfoStatement() {
        return "INSERT INTO Supplier_paymentOptions (sup_id,paymentOption)  " +
                "Values (?,?)";
    }
    private String insertContractStatement() {
        return "INSERT INTO Contract (contract_info,supplier_id) " +
                "Values (?,?)";
    }

    private String addDayToContractStatement() {
        return "INSERT INTO supplay_days (supplay_day,supplier_id)  " +
                "Values (?,?)";
    }

    private String insertProductToContract() {
        return "INSERT INTO Product_in_contract (contract_id,catalog_number,barcode,original_price)  " +
                "Values (?,?,?,?)";
    }
    private String addDiscountOfProductToContractStatement() {
        return "INSERT INTO Discount_of_product_in_contract (amount,discount,catalog_number,contract_id)  " +
                "Values (?,?,?,?)";

    }


    //Find Statements


    @Override
    protected String findStatement() {
        return "SELECT * " +
                "FROM Supplier join Contact_info on Supplier.id=Contact_info.supplier_id " +
                "WHERE id = ? ";
    }

    protected String findContactStatement()
    {
        return "SELECT * " +
                "FROM Contact_info" +
                "WHERE email = ? " +
                "AND supplier_id= ?";
    }

    protected String findContractBySupplierIDStatement()
    {
        return "SELECT * " +
                "FROM Contract " +
                "WHERE supplier_id = ?";
    }

    private String findContractByContractIDStatement() {
        return "SELECT * " +
                "FROM Contract " +
                "WHERE id = ? ";

    }

    protected String getAllSuppliersStatement()
    {
        return "SELECT * " +
                "FROM Supplier ";
    }
    protected String getAllSupplierContactsStatement()
    {
        return "SELECT * " +
                "FROM Contact_info " +
                "WHERE supplier_id = ?";
    }

    protected String getAllSupplierProductsStatement()
    {
        return "SELECT * " +
                "FROM Product_in_contract " +
                "WHERE contract_id = ?";
    }

    private String getAllSupplierPaymentInfoStatement() {
        return "SELECT * " +
                "FROM Supplier_paymentOptions " +
                "WHERE  sup_id = ? ";
    }

    private String getAllSupplierDaysStatement() {
        return "SELECT * " +
                "FROM supplay_days " +
                "WHERE supplier_id = ? ";
    }

    private String getAllProductDiscountsStatement() {
        return "SELECT * " +
                "FROM Discount_of_product_in_contract " +
                "WHERE catalog_number = ? " +
                "AND contract_id = ?";
    }


    //Update Statements
    protected String updateStatement() {
        return "UPDATE Supplier " +
                "SET name = ? " +
                "address= ?, inc_number = ? " +
                "WHERE id = ?";
    }

    //DeleteStatements
    @Override
    protected String deleteStatement() {
        return "DELETE FROM Supplier " +
                "WHERE id = ?";
    }

    protected String deleteContactStatement() {
        return "DELETE FROM Contact_info " +
                "WHERE email = ?";
    }

    private String deletePaymentOptionStatement() {
        return "DELETE FROM Supplier_paymentOptions " +
                "WHERE sup_id = ?"+
                "AND paymentOption=?";
    }


    //buildFunctions

    @Override
    //Build Supplier
    protected Supplier buildTFromResultSet(ResultSet res) {
        try {
            if(res.next()) {
                //int supID,String name, String address, String incNum, String accountNumber, String paymentInfo,
                //                    String contactName, String phoneNumber,String email
                boolean selfDelivery = res.getInt("self_delivery") == 1;
                Supplier myNewSupplier=new Supplier(res.getInt("id"),res.getString(2),
                        res.getString(3), res.getString("inc_number"),res.getString("account_number"), res.getString("paymentInfo"),
                        res.getString(5), res.getString("phone_number"), res.getString("email"),
                        selfDelivery);
                List<ContactInfo> contacts = getAllSupplierContacts(myNewSupplier.getSupId());
                myNewSupplier.setContacts(contacts);
                ContractWithSupplier new_contractWithSupplier = this.getContractBySupplier(myNewSupplier.getSupId());
                if(new_contractWithSupplier != null) {
                    myNewSupplier.setContract(new_contractWithSupplier);
                    new_contractWithSupplier.setProducts(this.getAllContractProducts(new_contractWithSupplier.getContractID()));
                    for (ContractProduct product : new_contractWithSupplier.getProducts()) {
                        product.setDiscounts(this.getProductsDiscounts(product));
                    }
                }
                return myNewSupplier;
            }
        } catch (SQLException e) {
        }

        return null;
    }
    //Build Contact
    private ContactInfo buildContactFromResultSet(ResultSet res)
    {
        try {
            if(res.next()) {
                //String name, String phoneNumber, String email, int supID
                return new ContactInfo(res.getString("name"),res.getString("phone_number"),
                        res.getString("email"), res.getInt("supplier_id"));
            }
        } catch (SQLException e) {
        }

        return null;
    }

    //Build Contract
    private ContractWithSupplier buildContractFromResultSet(ResultSet res,List<Days> theDays)
    {
        try {
            if(res.next()) {
                //String name, String phoneNumber, String email, int supID
                ContractWithSupplier theContract=new ContractWithSupplier(res.getString("contract_info"),theDays);
                theContract.setContractID(res.getInt("id"));
                theContract.setSupID(res.getInt("supplier_id"));
                return theContract;

            }
        } catch (SQLException e) {
        }

        return null;
    }

    private ContractProduct buildProductFromResultSet(ResultSet res) {
        try {
            if(res.next()) {
                //int barCode, String productCatalogNumber, ProductDiscounts discounts
                ContractProduct theProduct=new ContractProduct(res.getInt("barcode"),res.getString("catalog_number"),res.getDouble("original_price"));
                theProduct.setContractID(res.getInt("contract_id"));

                theProduct.setDiscounts(getProductsDiscounts(theProduct));

                return theProduct;
            }
        } catch (SQLException e) {
        }
        return null;
    }

    private SupplierDetails buildSupplierDetailsFromResultSet(ResultSet res) {
        try {
                //int barCode, String productCatalogNumber, ProductDiscounts discounts
                SupplierDetails theSup=new SupplierDetails(res.getInt("id"),res.getString("sup_name"));
                return theSup;
        }
        catch (SQLException e) {
        }
        return null;
    }

    private ProductDiscounts getProductsDiscounts(ContractProduct contractProduct) {
        ProductDiscounts productDiscounts=new ProductDiscounts(contractProduct.getBarCode(),contractProduct.getOriginalPrice());
        List<ContractProduct> theProducts = new ArrayList<>();

        try(PreparedStatement pstmt = conn.prepareStatement(getAllProductDiscountsStatement())){
            pstmt.setString(1,contractProduct.getProductCatalogNumber());
            pstmt.setInt(2,contractProduct.getContractID());
            ResultSet res = pstmt.executeQuery();

            while(res.next()){
                productDiscounts.addDiscountPerAmount(res.getInt("amount"),res.getDouble("discount"));
            }

        } catch (java.sql.SQLException e) {
            return productDiscounts;
        }

        return productDiscounts;

    }



    //InsertFunctions
    @Override
    public int insert(Supplier supplier) {
        if (loadedMap.getOrDefault(supplier.getSupId(), null) != null) {
            return supplier.getSupId();
        }

        int selfDelivery = 0;
        if(supplier.isSelfDelivery()){
            selfDelivery = 1;
        }

        try(PreparedStatement pstmt = conn.prepareStatement(insertSupplierStatement(), Statement.RETURN_GENERATED_KEYS)){

            pstmt.setString(1, supplier.getSupplierName());
            pstmt.setString(2, supplier.getAddress());
            pstmt.setString(3, supplier.getAccountNumber());
            pstmt.setString(4, supplier.getPaymentInfo().get(0));
            pstmt.setString(5, supplier.getIncNum());
            pstmt.setInt(6, supplier.getArea());
            pstmt.setInt(7, selfDelivery);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                return -1;
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    supplier.setId(generatedKeys.getInt(1));
                }
                else {
                    return -1;
                }
            }


            loadedMap.put(supplier.getSupId(), supplier);
            return supplier.getSupId();

        } catch (java.sql.SQLException e) {
            return -1;
        }
    }

    public String insertContactInfo(ContactInfo newContact)
    {
        try(PreparedStatement pstmt = conn.prepareStatement(insertContactStatement())){

            pstmt.setString(1, newContact.getName());
            pstmt.setString(2, newContact.getPhoneNumber());
            pstmt.setString(3, newContact.getEmail());
            pstmt.setInt(4, newContact.getSupID());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                return null;
            }

            this.loadedContacts.put(newContact.getEmail(), newContact);
            Supplier sup=this.loadedMap.getOrDefault(newContact.getSupID(), null);
            if(sup != null)
            {
                sup.addContactInfo(newContact);
            }
            return newContact.getEmail();

        } catch (java.sql.SQLException e) {
            return null;
        }
    }

    public boolean addPaymentInfo(int supId, String paymentInfo) {
        try(PreparedStatement pstmt = conn.prepareStatement(addPaymentInfoStatement())){

            pstmt.setInt(1, supId);
            pstmt.setString(2, paymentInfo);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }

            if (loadedMap.getOrDefault(supId, null) != null) {
                loadedMap.get(supId).addPaymentOptions(paymentInfo);
            }
            return true;

        } catch (java.sql.SQLException e) {
            return false;
        }
    }

    public boolean addProductToContract(int contractID,AddProduct product) {
        try (PreparedStatement pstmt = conn.prepareStatement(insertProductToContract())){
            pstmt.setInt(1, contractID);
            pstmt.setString(2, product.productCatalogNumber);
            pstmt.setInt(3,product.barCode);
            pstmt.setDouble(4,product.originalPrice);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }
            try (PreparedStatement pstmtDiscoutns = conn.prepareStatement(addDiscountOfProductToContractStatement())) {

                for (Integer amount:
                        product.productDiscounts.discountPerAmount.keySet()) {
                    pstmtDiscoutns.clearParameters();
                    pstmtDiscoutns.setInt(1, amount);
                    pstmtDiscoutns.setDouble(2, product.productDiscounts.discountPerAmount.get(amount));
                    pstmtDiscoutns.setString(3, product.productCatalogNumber);
                    pstmtDiscoutns.setInt(4, contractID);

                    pstmtDiscoutns.addBatch();
                }
                pstmtDiscoutns.executeBatch();
            }


            if(this.loadedContracts.getOrDefault(contractID,null)!=null)
            {
                loadedContracts.get(contractID).addProduct(product);
            }
            return true;
        }
        catch (java.sql.SQLException e) {
            return false;
        }


    }
    public int findIfSupplierExists(int supID)
    {
        try (PreparedStatement pstmt = conn.prepareStatement(findStatement())){
            pstmt.setInt(1, supID);

            ResultSet res=pstmt.executeQuery();

            if(res.next())
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
        catch (java.sql.SQLException e) {
            return -1;
        }
    }

    public int findIfSupplierHasContract(int supID)
    {
        try (PreparedStatement pstmt = conn.prepareStatement(findContractBySupplierIDStatement())){
            pstmt.setInt(1, supID);

            ResultSet res=pstmt.executeQuery();

            if(res.next())
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
        catch (java.sql.SQLException e) {
            return -1;
        }
    }




    public int insertContractToSupplier(int supID, ContractWithSupplier contract)
    {
        int number=findIfSupplierExists(supID);
        if(number<0 || findIfSupplierHasContract(supID) >0 || (loadedMap.keySet().size()>0 && loadedMap.get(supID).hasContract()))
        {
            return -1;
        }
        if(contract==null|| contract.getDailyInfo()==null||contract.getProducts()==null||contract.getContractDetails()==null||contract.getContractDetails().length()==0)
        {
            return -1;
        }
        String statement=insertContractStatement();
        try(PreparedStatement pstmt = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){

            pstmt.setString(1, contract.getContractDetails());
            pstmt.setInt(2, supID);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                return -1;
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contract.setContractID(generatedKeys.getInt(1));
                }
                else {
                    return -1;
                }
            }
            try (PreparedStatement pstmtDay = conn.prepareStatement(addDayToContractStatement())) {
                for (Days day:
                        contract.getDailyInfo()) {
                    pstmtDay.clearParameters();

                    pstmtDay.setInt(1, StructUtils.getDayInt(day));
                    pstmtDay.setInt(2, supID);

                    pstmtDay.addBatch();
                }
                pstmtDay.executeBatch();

            }

            if(loadedMap.getOrDefault(supID,null)!=null)
            {
                loadedMap.get(supID).addContract(contract);
            }
            loadedContracts.put(contract.getContractID(),contract);
            return contract.getContractID();

        } catch (java.sql.SQLException e) {
            return -1;
        }
    }
    //remove and delete Functions

    public boolean deleteContactInfo(int supID, String email) {

        try(PreparedStatement pstmt = conn.prepareStatement(deleteContactStatement())){

            pstmt.setString(1, email);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }
            if (loadedContacts.getOrDefault(email, null) != null) {
                loadedContacts.remove(email);
            }
            return true;

        } catch (java.sql.SQLException e) {
            return false;
        }
    }
    public boolean removePaymentOption(int supId, String paymentInfo) {
        try(PreparedStatement pstmt = conn.prepareStatement(deletePaymentOptionStatement())){

            pstmt.setInt(1, supId);
            pstmt.setString(2, paymentInfo);


            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                return false;
            }
            if (loadedMap.getOrDefault(supId, null) != null) {
                loadedMap.get(supId).RemovePaymentOptions(paymentInfo);
            }
            return true;

        } catch (java.sql.SQLException e) {
            return false;
        }

    }

    //find and get functions
    public ContactInfo findContactBySupplierAndEmail(int supplierID,String email){
        ContactInfo res = loadedContacts.getOrDefault(email, null);

        if(res != null){
            return res;
        }

        try(PreparedStatement pstmt = conn.prepareStatement(findStatement())){

            pstmt.setString(1,email);
            pstmt.setInt(2,supplierID);
            ResultSet rs  = pstmt.executeQuery();

            res = buildContactFromResultSet(rs);
            loadedContacts.put(email, res);
            return res;

        } catch (java.sql.SQLException e) {
        }

        return  null;
    }

    public List<ContactInfo> getAllSupplierContacts(int supplierID)
    {
        List<ContactInfo> contacts = new ArrayList<>();
        String statement=getAllSupplierContactsStatement();
        try(PreparedStatement pstmt = conn.prepareStatement(statement)){
            pstmt.setInt(1,supplierID);
            ResultSet res = pstmt.executeQuery();


                ContactInfo contactInfo=buildContactFromResultSet(res);
                while(contactInfo!=null) {
                    contacts.add(contactInfo);
                    this.loadedContacts.put(contactInfo.getEmail(), contactInfo);
                    contactInfo=buildContactFromResultSet(res);
                }


        } catch (java.sql.SQLException e) {
            return new LinkedList<>();
        }

        return contacts;
    }

    public List<Days> getSupplyDays(int supplierID) {

        List<Days> daysList = new LinkedList<>();
        try (PreparedStatement pstmtDays = conn.prepareStatement(getAllSupplierDaysStatement())) {
            pstmtDays.setInt(1, supplierID);
            ResultSet res = pstmtDays.executeQuery();
            while (res.next()) {
                daysList.add(StructUtils.getDayWithInt(res.getInt("supplay_day")));
            }
        } catch (java.sql.SQLException e) {
            return null;
        }
        return daysList;

    }



    public List<PaymentOptions> getAllSupplierPaymentInfo(int supplierID) {
        List<PaymentOptions> paymentInfosOfSup = new ArrayList<>();

        try(PreparedStatement pstmt = conn.prepareStatement(getAllSupplierPaymentInfoStatement())){
            pstmt.setInt(1,supplierID);
            ResultSet res = pstmt.executeQuery();

            while(res.next()){
                paymentInfosOfSup.add(PaymentOptions.valueOf(res.getString("paymentOption")));
            }

        } catch (java.sql.SQLException e) {
            return new LinkedList<>();
        }

        return paymentInfosOfSup;
    }

    public ContractWithSupplier getContractBySupplier(int supplierID) {
        List<Days> supplyDays = getSupplyDays(supplierID);
        try (PreparedStatement pstmtContract = conn.prepareStatement(findContractBySupplierIDStatement())) {
            pstmtContract.setInt(1, supplierID);
            ResultSet res = pstmtContract.executeQuery();
            return buildContractFromResultSet(res,supplyDays);
        }
        catch (java.sql.SQLException e) {
            return null;
        }

    }

    public ContractWithSupplier getContractByContractID(int contractId) {
        if(this.loadedContracts.containsKey(contractId))
        {
            return this.loadedContracts.get(contractId);
        }

        List<Days> supplyDays;
        try (PreparedStatement pstmtContract = conn.prepareStatement(findContractByContractIDStatement())) {
            pstmtContract.setInt(1, contractId);
            ResultSet res = pstmtContract.executeQuery();
            ContractWithSupplier theContract=buildContractFromResultSet(res,new LinkedList<>());
            supplyDays=this.getSupplyDays(theContract.getSupID());
            for (Days day:supplyDays
                 ) {
                theContract.addSupplyDayToContract(day);
            }
            return theContract;

        }
        catch (java.sql.SQLException e) {
            return null;
        }
    }


    public List<ContractProduct> getAllSupplierProducts(int supplierId) {
        ContractWithSupplier contractWithSupplier=this.getContractBySupplier(supplierId);
        List<ContractProduct> theProducts = new ArrayList<>();
        if(contractWithSupplier==null)
        {
            return theProducts;
        }
        try(PreparedStatement pstmt = conn.prepareStatement(getAllSupplierProductsStatement())){
            pstmt.setInt(1,contractWithSupplier.getContractID());
            ResultSet res = pstmt.executeQuery();
            ContractProduct product;
            while((product=buildProductFromResultSet(res))!=null){
                theProducts.add(product);
            }

        } catch (java.sql.SQLException e) {
            return new LinkedList<>();
        }

        return theProducts;
    }

    public List<ContractProduct> getAllContractProducts(int contractID) {

        List<ContractProduct> theProducts = new ArrayList<>();

        try(PreparedStatement pstmt = conn.prepareStatement(getAllSupplierProductsStatement())){
            pstmt.setInt(1,contractID);
            ResultSet res = pstmt.executeQuery();
            ContractProduct product;
            while((product=buildProductFromResultSet(res))!=null){
                theProducts.add(product);
            }

        } catch (java.sql.SQLException e) {
            return new LinkedList<>();
        }

        return theProducts;
    }

    private String getCatalogsFromBarcodesStatement() {
        return "SELECT catalog_number\n" +
                "FROM Suppliers_products\n" +
                "WHERE barcode = ?";
    }

    /**
     * Return all the catalog numbers of the product that the supplier has
     * @param supplierId the supplier id
     * @param barcodes the barcodes of the catalog numbers
     * @return list of catalogs
     */
    public List<String> getCatalogsFromBarcodes(int supplierId, List<Integer> barcodes) {
        List<String> catalogs = new ArrayList<>();

        for(Integer barcode : barcodes) {
            try (PreparedStatement ptsmt = conn.prepareStatement(getCatalogsFromBarcodesStatement())) {

                ptsmt.setInt(1, barcode);

                ResultSet res = ptsmt.executeQuery();
                if(res.next()){
                    catalogs.add(res.getString(1));
                }

            } catch (SQLException e) {
            }
        }
        return catalogs;
    }

    private String getBarcodesFromCatalogStatement() {
        return "SELECT barcode\n" +
                "FROM Suppliers_products\n" +
                "WHERE catalog_number = ?";
    }

    /**
     * Return all the barcodes of the product that the supplier has
     * @param supplierId the supplier id
     * @param catalogs the barcodes of the catalog numbers
     * @return list of barcodes
     */
    public List<Integer> getBarcodesFromCatalog(int supplierId, List<String> catalogs) {
        List<Integer> barcodes = new ArrayList<>();

        for (String catalog : catalogs) {
            try (PreparedStatement ptsmt = conn.prepareStatement(getBarcodesFromCatalogStatement())) {

                ptsmt.setString(1, catalog);

                ResultSet res = ptsmt.executeQuery();
                if (res.next()) {
                    barcodes.add(res.getInt(1));
                }

            } catch (SQLException e) {
            }
        }
        return barcodes;
    }

    public List<SupplierDetails> getAllSuppliers() {
        List<SupplierDetails> sups = new ArrayList<>();
        String statement=getAllSuppliersStatement();
        try(PreparedStatement pstmt = conn.prepareStatement(statement)){
            ResultSet res = pstmt.executeQuery();

            while(res.next()){
                sups.add(this.buildSupplierDetailsFromResultSet(res));
            }

        } catch (java.sql.SQLException e) {
            return new LinkedList<>();
        }

        return sups;
    }

    public List<ProductDiscounts> getAmountDiscountReport(int supID) {
        List<ContractProduct> products= this.getAllSupplierProducts(supID);

        List<ProductDiscounts> productDiscounts= new LinkedList<>();
        for (ContractProduct product: products) {
            productDiscounts.add(this.getProductsDiscounts(product));
        }
        return productDiscounts;
    }

    private String getAllBarcodesOfSupplierStatement() {
        return "SELECT barcode\n" +
                "FROM Suppliers_products\n" +
                "WHERE supplier_id = ?";
    }

    public List<Integer> getAllBarcodesOfSupplier(int supplierId) {
        List<Integer> barcodes = new ArrayList<>();

        try (PreparedStatement ptsmt = conn.prepareStatement(getAllBarcodesOfSupplierStatement())) {

            ptsmt.setInt(1, supplierId);

            ResultSet res = ptsmt.executeQuery();
            while(res.next()){
                barcodes.add(res.getInt(1));
            }

        } catch (SQLException e) {
        }

        return barcodes;
    }

    private String getSupplierIdByContractIdStatement() {
        return "SELECT supplier_id\n" +
                "FROM Contract\n" +
                "WHERE id = ?";
    }

    public int getSupplierIdByContractId(int contractId) {

        try (PreparedStatement ptsmt = conn.prepareStatement(getSupplierIdByContractIdStatement())) {

            ptsmt.setInt(1, contractId);

            ResultSet res = ptsmt.executeQuery();
            if(res.next()){
                return res.getInt(1);
            }

        } catch (SQLException e) {
        }

        return -1;
    }
}
