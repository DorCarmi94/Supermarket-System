package Sup_Inv.Suppliers.Presentation;

import Sup_Inv.Suppliers.Presentation.Order.OrderMenu;
import Sup_Inv.Suppliers.Service.OrderAndProductCtrl;
import Sup_Inv.Suppliers.Service.OrderAndProductManagement;
import Sup_Inv.Suppliers.Service.SupplierCtrl;
import Sup_Inv.Suppliers.Service.SupplierManagment;

public class MainMenu extends MenuOfMenus {

    private MainMenu(SupplierManagment supplierManagment, OrderAndProductManagement orderAndProductManagement) {
        super(supplierManagment, orderAndProductManagement);
    }

    public MainMenu() {
        super(new SupplierCtrl(), new OrderAndProductCtrl());
    }

    protected void createMenuMap() {
        addMenuOption("Supplier menu", "s", new SupplierMenu(supplierManagment, orderAndProductManagement));
        addMenuOption("Order menu", "o",new OrderMenu(supplierManagment, orderAndProductManagement));
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
