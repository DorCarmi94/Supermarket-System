package Sup_Inv.Suppliers.Supplier;

import Sup_Inv.DataAccess.SupInvDBConn;
import Sup_Inv.Suppliers.DataAccess.SupplierMapper;
import Sup_Inv.Suppliers.Structs.Days;
import Sup_Inv.Suppliers.Structs.PaymentOptions;

import java.util.ArrayList;
import java.util.List;

public class SupplierManager {

    private static SupplierManager instance = null;

    private SupplierMapper supplierMapper;

    private SupplierManager(){
        supplierMapper = new SupplierMapper(SupInvDBConn.getInstance().getConn());
    }

    public static SupplierManager getInstance(){
        if(instance == null){
            instance = new SupplierManager();
        }
        return instance;
    }

    public Supplier getById(int supplierId){
        return supplierMapper.findById(supplierId);
    }

    //Functions
    public int insert(Supplier supplier){
        return supplierMapper.insert(supplier);
    }
    public boolean deleteSupplier(Supplier sup) {

        List<ContactInfo> contactInfos=supplierMapper.getAllSupplierContacts(sup.getSupId());
        boolean ans=true;
        for (ContactInfo contact:
             contactInfos) {
            ans=supplierMapper.deleteContactInfo(sup.getSupId(),contact.getEmail());
            if(!ans)
            {
                break;
            }
        }
        if(ans) {
            return supplierMapper.deleteById(sup.getSupId());
        }
        else
            return false;
    }
    public String insertNewContactInfo(ContactInfo contactInfo) {
        return supplierMapper.insertContactInfo(contactInfo);
    }
    public boolean removeContactFromSupplier(int supID, String email) {
        List<ContactInfo> contacts=supplierMapper.getAllSupplierContacts(supID);
        if(contacts.size()<2)
        {
            return false;
        }
        else {
            return supplierMapper.deleteContactInfo(supID, email);
        }

    }

    /**
     * Check if supplier have all the products
     * @param supplierId supplier id
     * @param barcodes list of barcodes
     * @return true if the supplier have all the barcodes
     */
    public boolean hasAllBarcodes(int supplierId, List<Integer> barcodes){
        // TODO not needed for now, can be implement for faster speed
        throw new UnsupportedOperationException();
    }

    /**
     * Return all the supplier ids which have all the barcodes
     * @param barcodes the barcodes to check
     * @return Return list with all the supplier ids which have all the barcodes
     */
    public List<Integer> getAllSupplierIdsWithBarcodes(List<Integer> barcodes){
        List<Integer> supplierIds = new ArrayList<>();

        for(SupplierDetails supplierDetails : supplierMapper.getAllSuppliers()){
            List<Integer> supplierBarcodes = supplierMapper.getAllBarcodesOfSupplier(supplierDetails.supplierId);
            if(supplierBarcodes.containsAll(barcodes)){
                supplierIds.add(supplierDetails.supplierId);
            }
        }

        return supplierIds;
    }

    /**
     * Return all the supplier ids which have all the barcodes
     * @param supplierIds the supplier ids to check with
     * @param barcodes the barcodes to check
     * @return Return list with all the supplier ids which have all the barcodes
     */
    public List<Integer> getAllSuppliersWithBarcodes(List<Integer> supplierIds, List<Integer> barcodes){
        List<Integer> suppliers = new ArrayList<>();
        List<Integer> allSupplierWithBarcodes = getAllSupplierIdsWithBarcodes(barcodes);

        for(Integer id : supplierIds){
            if(allSupplierWithBarcodes.contains(id)){
                suppliers.add(id);
            }
        }

        return suppliers;
    }

    /**
     * Return all the supplier ids which supply in the given days
     * @param days the days to check with
     * @return Return list with all the supplier ids which supply in the given days
     */
    public List<Integer> getAllSupplierWithSupplyDays(List<Days> days) {
        List<Integer> supplierIds = new ArrayList<>();

        for(SupplierDetails supplierDetails : supplierMapper.getAllSuppliers()){
            List<Days> supplierDays = supplierMapper.getSupplyDays(supplierDetails.supplierId);
            if(supplierDays.containsAll(days)){
                supplierIds.add(supplierDetails.supplierId);
            }
        }

        return supplierIds;
    }

    public boolean addPaymentOption(int supId, String paymentInfo) {
        List<PaymentOptions> supplierPaymentInfo= supplierMapper.getAllSupplierPaymentInfo(supId);
        for ( PaymentOptions info:
              supplierPaymentInfo) {
            if(info.equals(paymentInfo))
            {
                return false;
            }
        }
        return supplierMapper.addPaymentInfo(supId,paymentInfo);

    }

    public boolean removePaymentOption(int supId, String paymentInfo) {
        List<PaymentOptions> supplierPaymentInfo = this.supplierMapper.getAllSupplierPaymentInfo(supId);
        boolean ans = false;
        for (PaymentOptions info :
                supplierPaymentInfo) {
            if (info.name().equals(paymentInfo)) {
                ans = true;
                break;
            }
        }
        if (ans) {
            supplierMapper.removePaymentOption(supId, paymentInfo);
        }

        return ans;
    }

    public int getIdByContract(int contractId) {
        return supplierMapper.getSupplierIdByContractId(contractId);
    }

    public int addContractToSupplier(int supplierId, ContractWithSupplier contractInfo) {

        return this.supplierMapper.insertContractToSupplier(supplierId,contractInfo);
    }

    public boolean addProductToContract(int supplierID,AddProduct product) {
        ContractWithSupplier contractWithSupplier=supplierMapper.getContractBySupplier(supplierID);
        if(contractWithSupplier==null)
        {
            return false;
        }

        return supplierMapper.addProductToContract(contractWithSupplier.getContractID(),product);
    }

    public List<ContactInfo> getAllSupplierContacts(int supID) {
        return this.supplierMapper.getAllSupplierContacts(supID);
    }

    public ContractWithSupplier getSupplierContract(int supID) {
        ContractWithSupplier contractWithSupplier=this.supplierMapper.getContractBySupplier(supID);
        //System.out.println("SupplierID from contract id: "+getIdByContract(contractWithSupplier.getContractID()));
        return contractWithSupplier;
    }

    public List<Days> getSupplyingDaysBySupID(int supID) {
        return this.supplierMapper.getSupplyDays(supID);
    }

    public List<PaymentOptions> getSupplierPaymentOptions(int supId) {

        return this.supplierMapper.getAllSupplierPaymentInfo(supId);
    }

    public List<ContractProduct> getAllSupplierProductsBardoces(int supplierId) {
        return this.supplierMapper.getAllSupplierProducts(supplierId);
    }

    public List<String> getCatalogsFromBarcodes(int supplierId, List<Integer> barcodes) {
        return supplierMapper.getCatalogsFromBarcodes(supplierId, barcodes);
    }

    public List<Integer> getBarcodesFromCatalog(int supplierId, List<String> catalogNumbers) {
        return supplierMapper.getBarcodesFromCatalog(supplierId, catalogNumbers);
    }

    public List<SupplierDetails> getAllSuppliers() {
        return this.supplierMapper.getAllSuppliers();
    }

    public List<ProductDiscounts> getAmountDiscountReport(int supID) {
        return this.supplierMapper.getAmountDiscountReport(supID);
    }
}
