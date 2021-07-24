package Sup_Inv.Inventory.View;
import Presentation.CurrStore;
import Presentation.Inventory2SuppliersCtrl;
import Sup_Inv.Inventory.Interfaces.Observer;
import Sup_Inv.Inventory.Interfaces.myObservable;
import Sup_Inv.Inventory.Logic.Inventory;
//imporinv.t DummyItem;
import Sup_Inv.Inventory.Logic.OrderItem;
import Sup_Inv.Inventory.Logic.ShortageOrder;
import Sup_Inv.Inventory.Persistence.DTO.InventoryDTO;
import Sup_Inv.Inventory.Persistence.Mappers.InventoriesMapper;
import Sup_Inv.Result.Result;
import Sup_Inv.Suppliers.Service.OrderDTO;
import Sup_Inv.Suppliers.Service.ProductInOrderDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


public class InvService implements myObservable {
    //private MainInterface myController;
    private View view;
    private HashMap<String, Inventory> superLeeInvs; //inventory id, inventory
    private Inventory currInv;
    private boolean terminateInv = false;
    private boolean terminateSys = false;
    private boolean terminate = false;
    private Scanner myScanner;
    private final List<Observer> observers;
    private Inventory2SuppliersCtrl myInv2Sup;
    private InventoriesMapper inventoriesMapper;
    private String ansStr;

    //region singelton Constructor
    private static InvService instance = null;
    private InvService(){
        this.view = new View();
        this.superLeeInvs = new HashMap<>();
        observers = new ArrayList<>();
        this.myScanner = new Scanner(System.in);
        this.register(view);
        inventoriesMapper = InventoriesMapper.getInstance();
    }

    public static InvService getInstance(){
        if(instance == null)
            instance = new InvService();
        return instance;
    }
    //endregion

    public Boolean mainLoop() {
        this.myInv2Sup = Inventory2SuppliersCtrl.getInstance();
        enterStoreInv();
        //notifyObserver(String.format("Welcome to shop # %s! : %s", ansStr, currInv.getShopName()));

        while(!terminateSys) {
            terminateInv = false;
            //region new_register loop
//            notifyObserver("--------------\nWelcome to your Super-Lee inventory!\n--------------\n" +
//                    "Please choose one of the following options:\n" +
//                    "\t[n] New shop \n \t[r] Register your shop \n\t[b] back to main menu");
//            ansStr = myScanner.nextLine();
//            if (ansStr.equals("n") || ansStr.equals("N")) {
//                this.currInv = newShop();
//                inventoriesMapper.insert(new InventoryDTO(currInv.getShopNum(), currInv.getShopName()));
//            }
//            else if (ansStr.equals("r") || ansStr.equals("R")) {
//                String shops = "Super lee shops:\n";
//                for (String shop : superLeeInvs.keySet())
//                     shops += String.format("\t%s. %s\t", shop, superLeeInvs.get(shop).getShopName());
//                shops += "\n";
//                notifyObserver(shops + "Type your shop number:");
//                ansStr = myScanner.nextLine();
//                if (!superLeeInvs.containsKey(ansStr)) {
//                    notifyObserver("I know that you can't wait to be part of Super-Lee, but please remember your shop id...");
//                    terminateInv = true;
//                }
//                else {
//                    this.currInv = superLeeInvs.get(ansStr);
//                    notifyObserver(String.format("Welcome to shop # %s! : %s", ansStr, currInv.getShopName()));
//                }
//            }
//            else if (ansStr.equals("b") || ansStr.equals("B")){
//                terminateInv = true;
//                terminateSys = true;
//            }
//            else {
//                notifyObserver("wrong typing!");
//                terminateInv = true;
//            }
            //endregion
            while (!terminateInv) {
                notifyObserver("\n__'" + currInv.getShopName() + "' inventory__\n" +
                        "Please choose one of the following options:\n-------\n" +
                        "\t[i] Items: update and reports quantities\n" +
                        "\t[r] Records: update and reports\n" +
                        "\t[d] Defectives and Expired Items: update and reports\n" +
                        "\t[b] Back to suppliers-inventory menu\n" +
                        "\t[c] Close\n");
                ansStr = myScanner.nextLine();
                if(ansStr.equals("i") || ansStr.equals("I")) {
                    terminate = itemsFunctions();
                    if(terminate) return terminate;
                }
                else if(ansStr.equals("r") || ansStr.equals("R")){
                    terminate = recordsFunctions();
                    if(terminate) return terminate;
                }
                else if(ansStr.equals("d") || ansStr.equals("D")) {
                    terminate = defectivesFunctions();
                    if(terminate) return terminate;
                }
                else if(ansStr.equals("b") || ansStr.equals("B")) {
                    terminateInv = true;
                    terminateSys = true;
                }
                else if(ansStr.equals("c") || ansStr.equals("C")) {
                    terminateInv = true;
                    terminateSys = true;
                    terminate = true;
                }
                else
                    notifyObserver("-- wrong order --");
            }
        }
        terminateSys = false;
        terminateInv = false;
        return terminate;
    }

    private boolean enterStoreInv() {
        int currStore = CurrStore.getInstance().getStore_id();
        if(CurrStore.getInstance().getStore_id() < 0){
            terminateSys = true;
            terminateInv = true;
            System.out.println("no shops, think about open one....");
            return false;
        }
        else if(superLeeInvs.containsKey(String.valueOf(currStore))) //register
            this.currInv = superLeeInvs.get(String.valueOf(currStore));
        else { //new
            this.currInv = newShop(currStore);
            inventoriesMapper.insert(new InventoryDTO(currInv.getShopNum(), currInv.getShopName()));
        }
        return true;
    }


    //region items
    private boolean itemsFunctions() {
        while(!terminateSys) {
            notifyObserver(
                    "\n__Items__\nPlease choose one of the following options:\n" +
                            "\t[p] Print all open orders for your shop\n" +
                            "\t[r] Receive arrived order to inventory \n" +
                            "\t[u] Update quantities in your inventory after stocktaking \n" +
                            "\t[gr] Get All Items Report \n" +
                            "\t[gi] Get Item Report by id \n" +
                            "\t[gc] Get Item Report By Category \n" +
                            "\t[gs] Get Shortage Item Report  \n" +
                            "\t[b] Back to inventory menu \n" +
                            "\t[c] Close \n");

            ansStr = myScanner.nextLine();
            if (ansStr.equals("p") || ansStr.equals("P")) {
                int shop = Integer.parseInt(currInv.getShopNum());
                String orders = "";
                notifyObserver("Open orders shop # " + shop + ":");
                List<Integer> openOrders = myInv2Sup.receiveAllOpenOrders(shop);
                for (Integer o : openOrders)
                    orders += (o + ", ");
                if(openOrders.size() > 0) {
                    orders = orders.substring(0, orders.length() - 2);
                    notifyObserver(orders);
                }
                else
                    notifyObserver("no open order for your shop...");


            }
            else if (ansStr.equals("r") || ansStr.equals("R")) {
                notifyObserver("Type order id:");
                ansStr = myScanner.nextLine();
                Result<OrderDTO> currOrder = myInv2Sup.receiveSupplierOrder(Integer.parseInt(ansStr));
                if(currOrder != null)
                {
                    if(currOrder.isOk()) {
                        OrderDTO order = currOrder.getValue();
                        notifyObserver("Check order validation:\n [v] valid order, order to Sup_Inv.Inventory.\n [n] not valid order, need to update\n");
                        ansStr = myScanner.nextLine();
                        if (ansStr.equals("v") || ansStr.equals("V")) {}
                        else if (ansStr.equals("n") || ansStr.equals("N")) {
                            notifyObserver("Order details:\n");
                            for (ProductInOrderDTO pdto : order.productInOrderDTOList) {
                                notifyObserver("#" + pdto.barcode + "; amount: " + pdto.amount + ", price: " + pdto.price);
                            }
                            notifyObserver("\nYou may have found defective or missing products on order.\n" +
                                    "Enter in loop the product barcode and correct amount of items you have arranged in the inventory.\n" +
                                    "enter in this format: \n\t<barcode> <amount>\n\t<barcode> <amount>\n\t\t....'0' when finish");

                            int id, correctAmount;
                            String currItem = myScanner.nextLine();
                            String[] splited;
                            while(!currItem.equals("0"))
                            {
                                splited = currItem.split(" ");
                                if(splited.length == 2) {
                                    if(checkValidInputNumber(splited[1]) && checkValidInputNumber(splited[0])) {
                                        id = Integer.parseInt(splited[0]);
                                        correctAmount = Integer.parseInt(splited[1]);
                                        order.updateAmountInOrderByID(id, correctAmount);
                                    }
                                    else
                                        notifyObserver("Wrong type, amount its means number");
                                }
                                else
                                    notifyObserver("Wrong type, check your format typing");
                                currItem = myScanner.nextLine();
                            }
                            notifyObserver("finish updating inventory, sending shortage order to suppliers if needed.");
                        }
                        else {
                            notifyObserver("Wrong input!\n");
                        }
                        myInv2Sup.getOrderFromSuppliers(order);
                    }
                    else{
                        notifyObserver(currOrder.getMessage());
                    }

                }
                // if(myInv2Sup.receiveSupplierOrder(Integer.parseInt(ansStr)))
                //{
                //    notifyObserver("order received successfully and arranged in inventory!");
                //}

            }
            else if (ansStr.equals("u") || ansStr.equals("U")) {
                updInvWorker();
            }
            else if (ansStr.equals("gr") || ansStr.equals("GR")) {
                notifyObserver("--- All items report ---");
                currInv.getItemReport();
            }
            else if (ansStr.equals("gi") || ansStr.equals("GI")) {
                notifyObserver("enter id:");
                ansStr = myScanner.nextLine();
                notifyObserver(String.format("-- Item Report By Id : #%s--", ansStr));
                currInv.getItemReportById(ansStr);
            }
            else if (ansStr.equals("gc") || ansStr.equals("GC")) {
                notifyObserver("enter category:");
                ansStr = myScanner.nextLine();
                notifyObserver(String.format("--- Items Report By Category %s ---", ansStr));
                currInv.getItemReportByCategory(ansStr);
            }
            else if (ansStr.equals("gs") || ansStr.equals("GS")) {
                notifyObserver("--- Shortage Item Report ---");
                currInv.getItemMissing();
            }
            else if (ansStr.equals("b") || ansStr.equals("B")) {terminateSys=true;}
            else if (ansStr.equals("c") || ansStr.equals("C")) {terminateSys=true; terminate = true;}
            else {notifyObserver("wrong order");}
        }
        if(!terminate)
            terminateSys = false;
        return terminate;
    }

    private boolean checkValidInputNumber(String num_str) {
        for(int i=0; i<num_str.length(); i++)
            if(num_str.charAt(i) < '0' || num_str.charAt(i) > '9')
                return false;
        return true;
    }

    private void updInvWorker() {
        String id;
        int quanMissStock;
        int quanMissShop;
        OrderItem currOrderItem;
        ShortageOrder shortageOrder = new ShortageOrder(Integer.parseInt(currInv.getShopNum()));
        notifyObserver("Type the updated quantities for each item you want, in the following format:\n " +
                "<'id' 'Amount of missing quantity in stock' 'Amount of missing quantity in shop'>\n " +
                "<0> when you finish");
        String currItem = myScanner.nextLine();
        String[] splited;

        while(!currItem.equals("0"))
        {
            splited = currItem.split(" ");
            if(splited.length == 3) {
                id = splited[0];
                if(!checkValidInputNumber(splited[1]) || !checkValidInputNumber(splited[2])){
                    notifyObserver("Wrong type, amount its means number");
                }
                else {
                    quanMissStock = Integer.parseInt(splited[1]);
                    quanMissShop = Integer.parseInt(splited[2]);
                    currOrderItem = currInv.updateInventoryWorkers(id, quanMissStock, quanMissShop);
                    if(currOrderItem != null && currOrderItem.getId() == -1)
                        notifyObserver("wrong id - item isn't exist, type again");
                    else if (currOrderItem != null)
                        shortageOrder.addItemToOrder(currOrderItem);
                }
            }
            else
                notifyObserver("You probably type the wrong format or id that isn't exist, type again in the format:\n" +
                        "<'id' 'Amount of missing quantity in stock' 'Amount of missing quantity in shop'>");
            currItem = myScanner.nextLine();
        }
        notifyObserver("finish updating inventory, sending shortage order to suppliers if needed.");
        if(shortageOrder.getLength() > 0) {
            Result<Integer> res = myInv2Sup.placeNewShortageOrder(shortageOrder);
            if(res.isFailure())
                notifyObserver(res.getMessage());
        }
    }
    public void getOrderFromSuppliers(OrderDTO order){
        currInv.updateInventorySuppliers(order, this);
        //notifyObserver("-- Update Sup_Inv.Inventory Sup_Inv.Suppliers --");
        //HashMap<ItemDTO, Integer> supply = new HashMap<>();
    }
    //endregion
    //region records
    private boolean recordsFunctions() {
        while(!terminateSys) {
            notifyObserver(
                    "__Records__\nPlease choose one of the following options:\n" +
                            "\t[s] Set New Price For Item \n" +
                            "\t[gr] Get Cost & Price All Items Report \n" +
                            "\t[gi] Get Cost & Price Item Report By Id \n" +
                            "\t[b] Back to inventory menu \n" +
                            "\t[c] Close \n");
            ansStr = myScanner.nextLine();
                if (ansStr.equals("s") || ansStr.equals("S")) {
                setNewPrice();
            }
                else if (ansStr.equals("gr") || ansStr.equals("GR")) {
                notifyObserver("--- Cost & Price Item Report ---");
                currInv.getGeneralRecordsReport();
            }
                else if (ansStr.equals("gi") || ansStr.equals("GI")) {
                notifyObserver("enter id:");
                ansStr = myScanner.nextLine();
                notifyObserver(String.format("--- Cost & Price Item Report By Id : %s --", ansStr));
                currInv.getRecordsReportById(ansStr);
            }
                else if (ansStr.equals("b") || ansStr.equals("B")) {terminateSys=true;}
                else if (ansStr.equals("c") || ansStr.equals("C")) {terminateSys=true; terminate = true;}
                else {notifyObserver("wrong order");}
        }
        if(!terminate)
            terminateSys = false;
        return terminate;
    }
    private void setNewPrice() {
        notifyObserver("enter id: ");
        String id = myScanner.nextLine();
        String[] lastRecInfo = currInv.getLastRec(id);
        String nameLast = lastRecInfo[0];
        String priceLast = lastRecInfo[1];
        notifyObserver("item #" + id + " -> current price: " + priceLast + "\n" +
                "type new price: ");
        String newPrice = myScanner.nextLine();
        currInv.setNewPrice(id, newPrice, nameLast, priceLast);
    }
    //endregion
    //region defectives
    private boolean defectivesFunctions() {
        while(!terminateSys) {
            notifyObserver(
                    "__Defectives-Expired__\nPlease choose one of the following options:\n" +
                            "\t[u] Update defective/expired Items in your inventory \n" +
                            "\t[gr] Get All Defective and Expired Report\n" +
                            "\t[gi] Get Defective and Expired Report By Id\n" +
                            "\t[b] Back to inventory menu \n" +
                            "\t[c] Close \n");
            ansStr = myScanner.nextLine();
            if (ansStr.equals("u") || ansStr.equals("U")) {
                updDef();
            }
            else if (ansStr.equals("gr") || ansStr.equals("GR")) {
                notifyObserver("--- General Defective Report ---");
                currInv.getDefectivesReport();
            }
            else if (ansStr.equals("gi") || ansStr.equals("GI")) {
                notifyObserver("enter id:");
                String id = myScanner.nextLine();
                notifyObserver("-- Defective/Expired Report By Id : "+ id + "--");
                currInv.getDefectivesReportById(id);
            }
            else if (ansStr.equals("b") || ansStr.equals("B")) {terminateSys=true;}
            else if (ansStr.equals("c") || ansStr.equals("C")) {terminateSys=true; terminate = true;}
            else {notifyObserver("wrong order");}
        }
        if(!terminate)
            terminateSys = false;
        return terminate;
    }
    private void updDef() {
        notifyObserver("Enter the defect or expired items quantities for each item you want, in the following format:\n " +
                "<'id' 'quantity' 'expired?(y/n)' 'defective?'(y/n)'> //(y/n) = type just y or n\n " +
                "<0> when you finish");
        String currItem = myScanner.nextLine();
        String[] splited;

        while(!currItem.equals("0"))
        {
            splited = currItem.split(" ");
            currInv.updateDefectives(splited);
            currItem = myScanner.nextLine();
        }
        notifyObserver("--- finish updating defectives and expired ---");
    }
    //endregion

    //region FUNCTIONS
    public Inventory newShop(int shop_id) {
        //notifyObserver("Choose name for your shop:");
        //String name = myScanner.nextLine();

        String name = inventoriesMapper.loadNewStore(shop_id);
        Inventory newInv = new Inventory(view, shop_id, name);
        superLeeInvs.put(newInv.getShopNum(), newInv);
        notifyObserver(String.format("--> inventory # %s : %s \n--------", newInv.getShopNum(), name));
        return newInv;
    }
    public double askUserPrice(double newCost, double oldCost, String[] lastRecordInfo) {
        String id = lastRecordInfo[0]; String name = lastRecordInfo[1];
        String oldPrice = lastRecordInfo[2];
//        LocalDate oldPriceChangeDate = changeToDate(lastRecordInfo[3]);

        notifyObserver("The supplier has changed the cost of item # " + name + "\n" +
                "old cost: " + oldCost + "$ -- new cost: " + newCost + "$ -- old price: " + oldPrice + "$\n" +
                "would you like to change price?\n type: <y 'new_price' | n>");
        String ans = myScanner.nextLine();
        while(!ans.equals("n") && ans.length() < 3){
            notifyObserver("wrong input, type: <y 'new_price' | n>");
            ans = myScanner.nextLine();
        }
        if(!ans.equals("n")) {
            ans = ans.substring(2);//ans is the new price
            double newPrice = Double.parseDouble(ans);
            //newRecord = new Record(observers, id, name, newCost, LocalDate.now(), newPrice, LocalDate.now());
            notifyObserver("price changed to " + newPrice + "$\n");
            return newPrice;
        }
        else {
            //newRecord = new Record(observers, id, name, newCost, LocalDate.now(), Double.parseDouble(oldPrice), oldPriceChangeDate);
            return Double.parseDouble(oldPrice);
        }
    }
    private LocalDate changeToDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
        //convert String to LocalDate
        LocalDate localDate = LocalDate.parse(date, formatter);
        return localDate;
    }
    //endregion

    //region observer
    @Override
    public void register(Observer o) {
        observers.add(o);
    }
    @Override
    public void notifyObserver(String msg) {
        observers.forEach(o -> o.onEvent(msg));
    }

    public void loadDB() {
        HashMap<String, InventoryDTO> invs = inventoriesMapper.load();
        for (String shopNum : invs.keySet()) {
            superLeeInvs.put(shopNum, new Inventory(view, invs.get(shopNum)));
        }
        for (String inv : superLeeInvs.keySet()) {
            superLeeInvs.get(inv).loadInvDB();
        }
    }

    public void invMngMenu(String ansStr) {
        if(!enterStoreInv())
            System.out.println("You need to register to the store first.");
        else
        {
            if (ansStr.equals("GR")) {
                notifyObserver("--- All items report ---");
                currInv.getItemReport();
            }
            else if (ansStr.equals("GI")) {
                notifyObserver("enter id:");
                ansStr = myScanner.nextLine();
                notifyObserver(String.format("-- Item Report By Id : #%s--", ansStr));
                currInv.getItemReportById(ansStr);
            }
            else if (ansStr.equals("GC")) {
                notifyObserver("enter category:");
                ansStr = myScanner.nextLine();
                notifyObserver(String.format("--- Items Report By Category %s ---", ansStr));
                currInv.getItemReportByCategory(ansStr);
            }
            else if (ansStr.equals("GS")) {
                notifyObserver("--- Shortage Item Report ---");
                currInv.getItemMissing();
            }
            else if (ansStr.equals("RGR")) {
                notifyObserver("--- Cost & Price Item Report ---");
                currInv.getGeneralRecordsReport();
            }
            else if (ansStr.equals("RGI")) {
                notifyObserver("enter id:");
                ansStr = myScanner.nextLine();
                notifyObserver(String.format("--- Cost & Price Item Report By Id : %s --", ansStr));
                currInv.getRecordsReportById(ansStr);
            }
            else if (ansStr.equals("DGR")) {
                notifyObserver("--- General Defective Report ---");
                currInv.getDefectivesReport();
            }
            else if (ansStr.equals("DGI")) {
                notifyObserver("enter id:");
                String id = myScanner.nextLine();
                notifyObserver("-- Defective/Expired Report By Id : "+ id + "--");
                currInv.getDefectivesReportById(id);
            }
            else {
                notifyObserver("wrong order");
            }
        }
    }
    //endregion
}
