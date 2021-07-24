package Sup_Inv.Suppliers.Presentation.Order;

import Sup_Inv.Suppliers.Presentation.MenuOfMenus;
import Sup_Inv.Suppliers.Presentation.RemoveProductsPeriodicalOrder;
import Sup_Inv.Suppliers.Service.OrderAndProductManagement;
import Sup_Inv.Suppliers.Service.SupplierManagment;

public class OrderMenu extends MenuOfMenus {

    public OrderMenu(SupplierManagment supplierManagment, OrderAndProductManagement orderAndProductManagement) {
        super(supplierManagment, orderAndProductManagement);
    }

    protected void createMenuMap() {
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
}
