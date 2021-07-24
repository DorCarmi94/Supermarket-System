//package Sup_Inv.Suppliers.Service;
//
//import Sup_Inv.Suppliers.Structs.Days;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.LinkedList;
//import java.util.List;
//
//class OrderAndProductCtrlTest {
//    SupplierManagment supplierManagment;
//    OrderAndProductManagement orderAndProductManagement;
//    @BeforeEach
//    void setUp() {
//        supplierManagment= new SupplierCtrl();
//        orderAndProductManagement= new OrderAndProductCtrl();
//
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    void addProductsToPeriodicalOrder() {
//    }
//
//    @Test
//    void addProductToSystem() {
//        //int barcode, String manufacture, String name, String category,
//        //                         String subCategory, String size
////        SystemProduct systemProduct= new SystemProduct(555,"osem","bamba","snack","snack","10gr");
////        this.orderAndProductManagement.addProductToSystem(systemProduct);
//        List<Days> days= new LinkedList<>();
//        List<SupplierProductDTO> supplierProductDTOS= new LinkedList<>();
//        days.add(Days.Friday);
//        //int barcode, String productCatalogNumber, double originalPrice, ProductDiscountsDTO discountPerAmount
//        ProductDiscountsDTO productDiscountsDTO= new ProductDiscountsDTO(101010,2.3);
//        productDiscountsDTO.addDiscount(500,0.6);
//        supplierProductDTOS.add(new SupplierProductDTO(101010,"101010x",15,productDiscountsDTO));
//        this.supplierManagment.addContractToSupplier(1,"best",days,supplierProductDTOS);
//    }
//
//    @Test
//    void removeProductsFromPeriodicalOrder() {
//    }
//
//    @Test
//    void orderArrived() {
//    }
//
//    @Test
//    void orderDetails() {
//    }
//
//    @Test
//    void updateOrderArrivalTime() {
//    }
//
//    @Test
//    void updateOrderStatus() {
//    }
//
//    @Test
//    void getAllOpenOrderIdsByShop() {
//    }
//
//    @Test
//    void productInOrderDTOToPIO() {
//    }
//
//    @Test
//    void productInOrderToDTO() {
//    }
//
//    @Test
//    void systemProductToProduct() {
//    }
//}