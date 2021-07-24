package Sup_Inv.Suppliers.Service;

import Sup_Inv.Suppliers.Structs.Days;
import Sup_Inv.Suppliers.Supplier.*;

import java.util.LinkedList;
import java.util.List;

public class SupplierCtrl implements SupplierManagment {

    private SupplierSystem supplierSystem;

    public SupplierCtrl(){
        supplierSystem = SupplierSystem.getInstance();
    }

    @Override
    public int createSupplierCard(String name, String incNum, String address, String accountNumber, String paymentInfo,
                                  String contactName, String phoneNumber,String email, boolean selfDelivery) {
        return supplierSystem.createSupplierCard(name, incNum, address, accountNumber, paymentInfo, contactName, phoneNumber, email, selfDelivery);
    }

    @Override
    public List<String> getPaymentOptions() {
        return supplierSystem.getOfferedPaymentOptions();
    }

    @Override
    public List<String> getPaymentOptions(int supId) {
        return supplierSystem.getPaymentOptions(supId);
    }

    @Override
    public boolean addPaymentOption(int supId, String paymentInfo) {
        return supplierSystem.addPaymentOptions(supId, paymentInfo);
    }

    @Override
    public boolean removePaymentOptions(int supId, String paymentInfo) {
        return supplierSystem.removePaymentOptions(supId, paymentInfo);
    }

    @Override
    public List<SupplierDetailsDTO> getAllSuppliers() {
        List<SupplierDetailsDTO> supDetailsDTO = new LinkedList<>();
        List<SupplierDetails> supplierDetails = supplierSystem.getAllSuppliers();

        for (SupplierDetails supd: supplierDetails) {
            supDetailsDTO.add(supplierDetailsToDTO(supd));
        }

        return supDetailsDTO;
    }

    @Override
    public boolean addContactInfo(int supplierId, String contactPersonName, String phoneNumber, String email) {
        return supplierSystem.addContactInfo(supplierId, contactPersonName, phoneNumber, email);
    }

    @Override
    public boolean removeContactPerson(int supplierId, String email) {
        return this.supplierSystem.RemoveContactFromSupplier(supplierId,email);
    }

    @Override
    public List<Integer> addContractToSupplier(int supplierId, String contractInfo, List<Days> days, List<SupplierProductDTO> products) {
        List<AddProduct> addProducts = new LinkedList<>();

        for (SupplierProductDTO addProduct : products) {
            addProducts.add(AddPITOToAddProduct(addProduct));
        }

        return supplierSystem.addContractToSupplier(supplierId, contractInfo, days, addProducts);
    }

    @Override
    public boolean addProductToContract(int supplierId, SupplierProductDTO product) {
        return supplierSystem.addProductToContract(supplierId, AddPITOToAddProduct(product));
    }

    @Override
    public List<ProductDiscountsDTO> getAmountDiscountReport(int supplierId){
        List<ProductDiscounts> productDiscounts = supplierSystem.getAmountDiscountReport(supplierId);

        if(productDiscounts == null){
            return null;
        }

        List<ProductDiscountsDTO> productDiscountsDTOS = new LinkedList<>();
        for(ProductDiscounts productDiscount : productDiscounts){
            productDiscountsDTOS.add(ProductDiscountToDTO(productDiscount));
        }

        return productDiscountsDTOS;
    }

    @Override
    public List<SupplierProductDTO> getAllSupplierProducts(int supplierId) {
        return this.supplierSystem.getAllSupplierProducts(supplierId);
    }

    @Override
    public SupplierProductDTO getAllInformationAboutSuppliersProduct(int supplierId, int barcode) {
        AddProduct supplierProductInfo= this.supplierSystem.getAllInformationAboutSuppliersProduct(supplierId, barcode);
        return AddPITOToAddProductReverse(supplierProductInfo);
    }

    @Override
    public List<String> getPurchaseHistory(int supplierId) {
        return supplierSystem.getPurchaseHistory(supplierId);
    }

    public SupplierDetailsDTO getSupplierInfo(int supID)
    {
        return this.supplierSystem.getSupplierInformation(supID);
    }

    public List<ContactInfoDTO> getSupplierContacts(int supID)
    {
        return this.supplierSystem.getSupplierContacts(supID);
    }

    public ContractWithSupplierDTO getSupplierContractInfo(int supID)
    {
        return this.supplierSystem.getSupplierContractInfo(supID);
    }

    public List<String> getSupplyingDaysNamesBySupID(int supID) {
        return this.supplierSystem.getSupplyingDaysNamesBySupID(supID);
    }

    public List<Integer> getSupplyingDaysNumbersBySupID(int supID) {
        return this.supplierSystem.getSupplyingDaysNumbersBySupID(supID);
    }




    private static SupplierDetailsDTO supplierDetailsToDTO(SupplierDetails supplierDetails){

        if(supplierDetails == null){
            return null;
        }

        return new SupplierDetailsDTO(
                supplierDetails.supplierId,
                supplierDetails.supplierName
        );
    }

    private static AddProduct AddPITOToAddProduct(SupplierProductDTO supplierProductDTO){

        if(supplierProductDTO == null){
            return null;
        }
        SystemProduct systemProduct = supplierProductDTO.systemProduct;

        ProductDiscounts productDiscount = new ProductDiscounts(supplierProductDTO.barcode, supplierProductDTO.discounts.discountPerAmount, supplierProductDTO.originalPrice);
        Product product = null;
        if(systemProduct != null) {
            product = new Product(systemProduct.barcode, systemProduct.manufacture, systemProduct.name, systemProduct.category,
                    systemProduct.subCategory, systemProduct.size, systemProduct.freqSupply, systemProduct.minPrice);
        }
        return new AddProduct(
                supplierProductDTO.barcode,
                supplierProductDTO.productCatalogNumber,
                supplierProductDTO.originalPrice,
                productDiscount,
                product);
    }

    private static SupplierProductDTO AddPITOToAddProductReverse(AddProduct addProduct){

        if(addProduct == null){
            return null;
        }

        Product product = addProduct.product;
        SystemProduct systemProduct = new SystemProduct(product.getBarCode(), product.getManufacture(), product.getName(), product.getCategory(), product.getSubCategory(),
                product.getSize(), product.getFreqSupply(), product.getMinPrice());

        ProductDiscountsDTO productDiscounts = new ProductDiscountsDTO(addProduct.barCode,addProduct.productDiscounts.discountPerAmount,addProduct.originalPrice);

        return new SupplierProductDTO(
                addProduct.barCode,
                addProduct.productCatalogNumber,
                addProduct.originalPrice,
                productDiscounts,
                systemProduct);
    }

    private static ProductDiscountsDTO ProductDiscountToDTO(ProductDiscounts productDiscounts){
        return new ProductDiscountsDTO(
                productDiscounts.barCode,
                productDiscounts.discountPerAmount,
                productDiscounts.originalPrice);
    }

    public static SimpleSupplierProductDTO ContractProductToSupplerProductDTO(SupplierProductInfo product){
        return new SimpleSupplierProductDTO(
                product.barCode,
                product.productCatalogNumber);
    }


}
