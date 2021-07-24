package Presentation;
import Sup_Inv.Inventory.Interfaces.Observer;
import Sup_Inv.Inventory.Interfaces.myObservable;
import Sup_Inv.Inventory.Logic.ShortageOrder;
import Sup_Inv.Inventory.Persistence.DTO.ItemDTO;
import Sup_Inv.Inventory.View.InvService;
import Sup_Inv.Inventory.View.View;
import Sup_Inv.Result.Result;
import Sup_Inv.Suppliers.Structs.Days;
import Sup_Inv.Suppliers.Service.*;

import java.util.*;

public class Inventory2SuppliersCtrl implements myObservable {

    private static Inventory2SuppliersCtrl inventory2SuppliersCtrlInstance = null;
    private SupplierManagment mySupplierManagment;
    private OrderAndProductManagement myOrderAndProductManagement;
    private InvService myInvenoryService;
    private Scanner myScanner;
    private View view;
    public final List<Observer> observers;

    private Inventory2SuppliersCtrl()
    {
        mySupplierManagment= new SupplierCtrl();
        myOrderAndProductManagement= new OrderAndProductCtrl();
        myInvenoryService= InvService.getInstance();
        this.myScanner = new Scanner(System.in);
        observers = new ArrayList<>();
        this.view = new View();
        this.register(view);
    }

    public static Inventory2SuppliersCtrl getInstance() {
        if(inventory2SuppliersCtrlInstance == null)
            inventory2SuppliersCtrlInstance = new Inventory2SuppliersCtrl();
        return inventory2SuppliersCtrlInstance;
    }


    public List<ItemDTO> getAllCatalog()
    {
        return null;
    }
    public Result<Integer> placeNewShortageOrder(ShortageOrder shortageOrder)
    {
        notifyObserver("automatic shortage order arrived successfully!");
        notifyObserver(shortageOrder.toString());

        //List<ProductInOrder> productsToOrder;
        //return mySupplierManger.placeShortageOrder(productsToOrder,day);
        List<ProductInOrderDTO> productInOrderDTOS= new LinkedList<>();
        HashMap<Integer,Integer> order=shortageOrder.getOrder();
        for (Integer barcode:
             order.keySet()) {
            ProductInOrderDTO newProduct= new ProductInOrderDTO(barcode,order.get(barcode));
            productInOrderDTOS.add(newProduct);
        }

        return myOrderAndProductManagement.createRegularOrder(productInOrderDTOS,shortageOrder.getShopNum());

    }

    public Result<List<Integer>> placePeriodicalOrder(List<ProductInOrderDTO> shoppingList, List<Days> days, int weekPeriod, int shopNumber)
    {
        return this.myOrderAndProductManagement.createPeriodicalOrder(shoppingList,days,weekPeriod, shopNumber);
    }

    //TODO: understand how to make this work- which order id? of supplier? of inventory?
    public Result<OrderDTO> receiveSupplierOrder(int orderID)
    {
        return this.myOrderAndProductManagement.orderArrived(orderID);
        //Sup_Inv.Result<OrderDTO> theOrder = this.myOrderAndProductManagement.orderArrived(orderID);
        //if(theOrder!=null)
        //{
        //    myInvenoryService.getOrderFromSuppliers(theOrder);
        //    return true;
        //}
        //else
        //{
        //    return false;
        //}
   }

   public void getOrderFromSuppliers(OrderDTO theOrder){
       myInvenoryService.getOrderFromSuppliers(theOrder);
   }

    public List<Integer> receiveAllOpenOrders(int shopNum)
    {
        return myOrderAndProductManagement.getAllOpenOrderIdsByShop(shopNum);
    }

    //region observer
    @Override
    public void register(Observer o) {
        observers.add(o);
    }
    @Override
    public void notifyObserver(String msg) {
        observers.forEach(o -> o.onEvent(msg));
    }
    //endregion
}
