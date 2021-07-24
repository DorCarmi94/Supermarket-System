package Sup_Inv.Suppliers.Presentation;

import Sup_Inv.Suppliers.Presentation.Order.*;
import Sup_Inv.Suppliers.Service.OrderAndProductCtrl;
import Sup_Inv.Suppliers.Service.OrderAndProductManagement;
import Sup_Inv.Suppliers.Service.SupplierCtrl;
import Sup_Inv.Suppliers.Service.SupplierManagment;

public class AllSupplierOptionsMenu extends MenuOfMenus {

    private AllSupplierOptionsMenu(SupplierManagment supplierManagment, OrderAndProductManagement orderAndProductManagement) {
        super(supplierManagment, orderAndProductManagement);
    }

    public AllSupplierOptionsMenu() {
        super(new SupplierCtrl(), new OrderAndProductCtrl());
    }

    protected void createMenuMap() {

        //supplier
        addMenuOption("Create supplier card", "c", new HandleSupplierCard(supplierManagment));
        addMenuOption("Get all suppliers", "gas",new GetAllSuppliers(supplierManagment));
        addMenuOption("Get all supplier's contacts", "gasc",new GetAllSupplierContacts(supplierManagment));
        addMenuOption("Get payment options", "gpo",new PaymentOptions(supplierManagment));
        addMenuOption("Update payment options", "upo",new updatePaymentOptions(supplierManagment));

        addMenuOption("Add contact info to supplier", "acis",new AddContactInfoToSupplier(supplierManagment));
        addMenuOption("Add contract to supplier", "acs",new AddContractToSupplier(supplierManagment, orderAndProductManagement));
        addMenuOption("Add product to supplier", "aps",new AddProductToSupplier(supplierManagment, orderAndProductManagement));
        addMenuOption("Discount report", "dr",new GetAmountDiscountReport(supplierManagment));

        addMenuOption("Get all supplier barcode", "gasb" ,new GetAllSuppliersProducts(supplierManagment));
        addMenuOption("Get all supplier products detalis", "gaspd" ,new GetAllSuppliersProductsDetalis(supplierManagment,orderAndProductManagement));

        addMenuOption("Get purchase history from supplier", "gph",new PurchaseHistoryFromSupplier(supplierManagment));

        //order
        addMenuOption("Create new order", "cno",new CreateNewOrder(orderAndProductManagement));
        addMenuOption("Create periodical order", "cpo",new CreatePeriodicalOrder(orderAndProductManagement));

        addMenuOption("Add products to periodical order", "aptpo",new AddProductsToPeriodicalOrder(orderAndProductManagement));
        addMenuOption("Remove products from periodical order", "rpfo", new RemoveProductsPeriodicalOrder(orderAndProductManagement));

        addMenuOption("Update order arrival day", "uoad",new UpdateOrderArrivalDay(orderAndProductManagement));
        addMenuOption("Update order status", "uos",new UpdateOrderStatus(orderAndProductManagement));

        addMenuOption("Get all open order ids", "gaodi",new GetAllOpenOrdersIds(orderAndProductManagement));
        addMenuOption("Get all periodical open order ids", "gapoo",new GetAllOpenPeriodicalOrders(orderAndProductManagement));
        addMenuOption("Get order details", "gord",new OrderDetails(orderAndProductManagement));
    }

    public void apply(String optionInput){
        Menu_Option option = getMenuWithIndex(optionInput);
        if (option == null) {
            System.out.println("Invalid function");
            return;
        }
        option.apply();
    }
}
