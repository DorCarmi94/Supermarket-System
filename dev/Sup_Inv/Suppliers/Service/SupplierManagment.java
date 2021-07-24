package Sup_Inv.Suppliers.Service;

import Sup_Inv.Suppliers.Structs.Days;

import java.util.List;

public interface SupplierManagment {

    /**
     * Create new supplier in the system
     * @param name The name of the supplier
     * @param incNum incupartion number
     * @param address the address of the supplier
     * @param accountNumber Bank account
     * @param paymentInfo Payment info
     * @param contactName Contact info
     * @param phoneNumber Contact info
     * @param email Contact info
     * @return -1 if cant create a new supplier otherwise return new supplier ID in the system.
     */
    public int createSupplierCard(String name, String incNum, String address, String accountNumber, String paymentInfo,
                                  String contactName, String phoneNumber,String email, boolean selfDelivery);

    public List<String> getPaymentOptions();

    /**
     * Return the payment information of specific supplier.
     * @param supId ID of the supplier
     * @return null if the supplier doesnt exist in the system, otherwise its payment information
     */
    public List<String> getPaymentOptions(int supId);

    /**
     * Add all or noting of the payments info to the supplier
     * @param supId supplier ID
     * @param paymentInfo list of payments options
     * @return true if added false otherwise
     */
    public boolean addPaymentOption(int supId, String paymentInfo);

    /**
     * Remove all or noting of the payments info from supplier
     * @param supId supplier ID
     * @param paymentInfo list of payments options
     * @return true if added false otherwise
     */
    public boolean removePaymentOptions(int supId, String paymentInfo);

    /**
     * Return the details for each supplier in the system.
     * @return List<Sup_Inv.Suppliers.InvService.SupplierDetails> for each supplier in the system.
     */
    public List<SupplierDetailsDTO> getAllSuppliers();

    /**
     * Add person contact information to specific supplier
     * @param supplierId ID for the supplier
     * @param contactPersonName Person name
     * @param phoneNumber Phone number
     * @param email Email
     * @return True if the contact as been added.
     */
    public boolean addContactInfo(int supplierId, String contactPersonName, String phoneNumber, String email);

    /**
     * Remove contact person by email if there are more then 1 in the system for the
     * specifed supplier
     * @param supplierId supplier ID
     * @param email the email of the contact person
     * @return true if the contact has been removed false otherwise.
     */
    public boolean removeContactPerson(int supplierId,String email);
    /**
     * Add contract with the supplier, only one contract can exist.
     * @param supplierId Sup_Inv.Suppliers.Supplier id
     * @param contractInfo Contract details
     * @param days List of days he can supply items.
     * @param products List of product he supply, need to contain at least one
     * @return List of products id that wasnt added to the system, null if the contract wasnt added
     */
    public List<Integer> addContractToSupplier(int supplierId, String contractInfo, List<Days> days, List<SupplierProductDTO> products);

    /**
     * Add a product to the supplier contract
     * @param supplierId Sup_Inv.Suppliers.Supplier ID
     * @param product Data of the product
     * @return -1 if the product wasnt added, otherwise the product id in the system
     */
    public boolean addProductToContract(int supplierId, SupplierProductDTO product);

    /**
     * Return to each product the given supplier his discount per amount
     * @param supplierId Sup_Inv.Suppliers.Supplier ID
     * @return List of ProductDiscount.
     */
    public List<ProductDiscountsDTO> getAmountDiscountReport(int supplierId);

    /**
     * Return all the supplier products
     * @param supplierId supplier ID
     * @return List with all the supplier product info
     */
    public List<SupplierProductDTO> getAllSupplierProducts(int supplierId);

    /**
     * Return all the information that was added to the system about a product
     * @param supplierId supplier ID
     * @param barcode the product's barcode
     * @return All the information, encapsulated in an AddProductInfoDTO object,
     *          return null if there isnt such barcode in the supplier products.
     */
    public SupplierProductDTO getAllInformationAboutSuppliersProduct(int supplierId, int barcode);

    /**
     * Return all the catalog ID from a given suppler
     * @param supplierId Sup_Inv.Suppliers.Supplier ID
     * @return List with all the products catalog numbers that the system has order which
     * contains them
     */
    public List<String> getPurchaseHistory(int supplierId);


    /**
     * Return all the supplierInfo by a given supplier id
     * @param supID Sup_Inv.Suppliers.Supplier ID
     * @return DTO with all info
     */
    public SupplierDetailsDTO getSupplierInfo(int supID);

    /**
     * Return all the contact of a supplier by a given supplier id
     * @param supID Sup_Inv.Suppliers.Supplier ID
     * @return contactInfoDTO with all info
     */
    public List<ContactInfoDTO> getSupplierContacts(int supID);

    /**
     * Return the contract of a supplier by a given supplier id days and details text
     * @param supID Sup_Inv.Suppliers.Supplier ID
     * @return contractWithSupplierDTO with all info
     */
    public ContractWithSupplierDTO getSupplierContractInfo(int supID);

}
