package Presentation;

import Sup_Inv.DataAccess.SupInvDBConn;
import Sup_Inv.Inventory.View.InvService;
import Sup_Inv.Suppliers.Presentation.AllSupplierOptionsMenu;
import Trans_HR.Business_Layer.Transportations.Utils.Buisness_Exception;
import Trans_HR.Data_Layer.Mapper;
import Trans_HR.Interface_Layer.Workers.SystemInterfaceWorkers;
import Trans_HR.Presentation_Layer.Transportations.TransportationMenu;
import Trans_HR.Presentation_Layer.Workers.HR;
import com.sun.javafx.binding.StringFormatter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class WelcomeMenu {

    private static AllSupplierOptionsMenu supplierMenu;
    private static InvService inventoryMenu;
    private static Boolean terminate;
    private static Scanner sc = new Scanner(System.in);

    public WelcomeMenu() {
        supplierMenu = new AllSupplierOptionsMenu();
        inventoryMenu = InvService.getInstance();
    }


    public void newStart() throws Buisness_Exception {
            chooseStore_start(sc);
            //SupInvDBConn.closeConn();
    }

    public static void chooseStore_start(Scanner sc) throws Buisness_Exception {
        try {
            Mapper.getInstance().init();
        } catch (Exception e) {

        }
        System.out.println("--------------\nWelcome to SUPER LEE !");
        printStoreId();
        System.out.println("Stores:");
        System.out.println("1. Choose your store");
        System.out.println("2. Add new store");
        System.out.println("\t-------");
        System.out.println("3. Choose Job Title");
        System.out.println("0. Exit");
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input, please try again");
            sc.next();
        }
        SystemInterfaceWorkers.getInstance().getStores();
        int userChoose = sc.nextInt();
        if (userChoose == 1) {
            if (!(SystemInterfaceWorkers.getInstance().printAllStores())) {
                // Empty
                chooseStore_start(sc);
            } else {
                // Store.count > 0
                while (!sc.hasNextInt()) {
                    System.out.println("Invalid input, please try again");
                    sc.next();
                }
                userChoose = sc.nextInt();
                CurrStore.getInstance().setStore_id(userChoose);
                if (!(SystemInterfaceWorkers.getInstance().setCurrentStore(userChoose))) {
                    chooseStore_start(sc);
                } else {
                    chooseJob(sc);
                }
            }
        } else if (userChoose == 2) {

            Scanner scan = new Scanner(System.in);
            System.out.println("Please choose the name of the store"); //chose area
            String name = scan.nextLine();
            System.out.println("Please choose city of the store");
            String city = scan.nextLine();
            System.out.println("Please choose street of the store");
            String street = scan.nextLine();
            System.out.println("Please choose street's number of the store");
            String number = scan.nextLine();
            System.out.println("Please enter name of contact for the store");
            String name_of_contact = scan.nextLine();
            System.out.println("Please enter contact's person phone");
            String phone = scan.nextLine();
            System.out.println("Please choose store's area (A/B/C/D)");
            String area = scan.nextLine();
            boolean result = false;
            result = SystemInterfaceWorkers.getInstance().addNewStore(name, city, street, number, name_of_contact, phone, area);
            if (result) {
                System.out.println("The store was added successfully\n");
                chooseStore_start(sc);
            } else {
                System.out.println("Input error\n");
                chooseStore_start(sc);
            }
        }
        else if (userChoose == 3) {
            //transportNoNeedShopNum();
            chooseJob(sc);
        }
        else if (userChoose == 0) {
            return;
        } else {
            System.out.println("Invalid input, please try again");
            chooseStore_start(sc);
        }
    }

    private static void printStoreId() {
        if(CurrStore.getInstance().getStore_id() != -1) {
            int store_id = CurrStore.getInstance().getStore_id();
            System.out.println("----------\nCurrent Store: " + store_id + "\n----------");
        }
        else
            System.out.println("----------\nCurrent Store: no registered shop\n----------");
    }

    public static void chooseJob(Scanner sc) throws Buisness_Exception {

        while (true) {
            printStoreId();
            System.out.println("Please choose your job title:");
            System.out.println("[h] HR\n[s] Storekeeper\n[m] Manager\n[l] Logistic mg\n[b] back");
            System.out.print("Option: ");
            while (!sc.hasNext()) {
                System.out.println("Invalid input, please try again");
                sc.next();
            }
            String ansStr = sc.next().toUpperCase();
            if(ansStr.equals("H")) {
                if(CurrStore.getInstance().getStore_id() == -1) {
                    System.out.println("You need a registered shop to use HR manager actions.");
                    chooseJob(sc);
                }
                HR.workingLoop(sc);
            }
            else if(ansStr.equals("S")){
                start_invSup();
            }
            else if(ansStr.equals("M")) {
                if(CurrStore.getInstance().getStore_id() == -1) {
                    System.out.println("You need a registered shop to use manager actions.");
                    chooseJob(sc);
                }
                managerMenu();
            }
            else if(ansStr.equals("L")) {
                TransportationMenu.Menu();
            }
            else if(ansStr.equals("B")) {
                chooseStore_start(sc);
            }
            else {
                System.out.println("Invalid input, please try again");
            }
        }
    }

    private static void transportNoNeedShopNum() throws Buisness_Exception {
        while (true) {
            System.out.println("--------------\nYou didn't choose store number, if its mistake, press back\n" +
                    "Please choose your job title:");
            System.out.println("[l] Logistic mg\n[b] back");
            System.out.print("Option: ");
            while (!sc.hasNext()) {
                System.out.println("Invalid input, please try again");
                sc.next();
            }
            String ansStr = sc.next();
            if(ansStr.equals("l") || ansStr.equals("L")) {
                TransportationMenu.Menu();
            }
            else if(ansStr.equals("b") || ansStr.equals("B")) {
                chooseStore_start(sc);
            }
            else {
                System.out.println("Invalid input, please try again");
            }
        }
    }

    public static void start_invSup() throws Buisness_Exception {
        terminate = false;
        String option = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        inventoryMenu.loadDB();

        while (!terminate) {
            System.out.println("Please choose one of the following options:");
            System.out.println("[s] Supplier menu\n[i] Inventory menu\n[c] Close");
            System.out.print("Option: ");

            try {
                option = reader.readLine();
            } catch (IOException e) {
                System.out.println("Invalid option");
            } catch (Exception e) {
                System.out.println("Something went wrong");
            }

            if (option.equals("s") || option.equals("S")) {
                supplierMenu.apply();
            } else if (option.equals("i") || option.equals("I")) {
                terminate = inventoryMenu.mainLoop();
            } else if (option.equals("c") || option.equals("C")) {
                break;
            } else {
                System.out.println("Invalid option");
            }
        }
    }

    private static void managerMenu() throws Buisness_Exception {
        while (true) {
            System.out.println("--------------MANAGER MENU-----------\n");
            System.out.println("\t[s] Shift report\n" +
                            "\t[w] Workers report\n---\n" +
                            "\t[gr] Get All Items Report \n" +
                            "\t[gi] Get Item Report by id \n" +
                            "\t[gc] Get Item Report By Category \n" +
                            "\t[gs] Get Shortage Item Report\n---\n" +
                            "\t[ss] Show transportation\n" +
                            "\t[st] Show trucks\n---\n" +
                            "\t[gas] Get all suppliers\n" +
                            "\t[gasc] Get all supplier's contacts\n" +
                            "\t[gpo] Get payment options \n" +
                            "\t[dr]	Discount report\n" +
                            "\t[gasb] Get all supplier barcode\n" +
                            "\t[gaspd] Get all supplier products detalis\n" +
                            "\t[gph] Get purchase history from supplier\n" +
                            "\t[gaodi] Get all open order ids\n" +
                            "\t[gapoo] Get all periodical open order ids\n" +
                            "\t[gord] Get order details\n" +
                            "\t[b] back\n");
            System.out.print("Option: ");
            while (!sc.hasNext()) {
                System.out.println("Invalid input, please try again");
                sc.next();
            }
            String ansStr = sc.next().toUpperCase();
            if(ansStr.toUpperCase().equals("S") || ansStr.toUpperCase().equals("W")) {

                if(ansStr.toUpperCase().equals("S")){
                    SystemInterfaceWorkers.getInstance().printAllShiftFromThisShop();
                } else {
                    SystemInterfaceWorkers.getInstance().printAllWorkersFromThisShop();
                }

            }
            else if(ansStr.equals("GR") || ansStr.equals("GI") || ansStr.equals("GC") || ansStr.equals("GS") ||
                    ansStr.equals("RGR") || ansStr.equals("RGI") || ansStr.equals("DGR") || ansStr.equals("DGI")) {
                inventoryMenu.invMngMenu(ansStr);
            }
            else if(ansStr.toUpperCase().equals("SS") || ansStr.toLowerCase().equals("st")) {
                if(ansStr.toLowerCase().equals("ss")){
                    TransportationMenu.Show_transports();
                } else {
                    TransportationMenu.Show_trucks();
                }
            }
            else if(ansStr.equals("GAS") || ansStr.equals("GASC") ||ansStr.equals("GPO") ||ansStr.equals("DR") ||
                    ansStr.equals("GASB") ||ansStr.equals("GASPD") ||ansStr.equals("GPH") ||ansStr.equals("GAODI") ||
                    ansStr.equals("GAPOO") ||ansStr.equals("GORD")) {
                supplierMenu.apply(ansStr.toLowerCase());
            }
            else if(ansStr.equals("b") || ansStr.equals("B")) {
                chooseJob(sc);
            }
            else {
                System.out.println("Invalid input, please try again");
            }
        }
    }


}