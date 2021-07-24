package Sup_Inv.Suppliers.Presentation;

import Sup_Inv.Suppliers.Service.OrderAndProductManagement;

import Sup_Inv.Suppliers.Service.SupplierManagment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public abstract class MenuOfMenus extends Menu_Option {
    protected SupplierManagment supplierManagment;
    protected OrderAndProductManagement orderAndProductManagement;

    protected Map<String, Menu_Option> optionMap;
    protected List<Pair<String, String>> optionToIndex;

    public MenuOfMenus(SupplierManagment supplierManagment, OrderAndProductManagement orderAndProductManagement){
        this.supplierManagment = supplierManagment;
        this.orderAndProductManagement = orderAndProductManagement;

        optionMap = new HashMap<>();
        optionToIndex = new  LinkedList<>();

        createMenuMap();
        addMenuOption("Return to menu", "r", null);
    }

    protected void addMenuOption(String menuName, String shortName, Menu_Option menu){
        optionMap.put(menuName, menu);
        optionToIndex.add(new Pair<>(shortName, menuName));
    }

    abstract protected void createMenuMap();

    public void print_menu(){
        System.out.println("\n+++++++++++++++++++++++++++++++++++++\n");
        System.out.println("Choose from the options");

        for(Pair<String, String> menuOp : optionToIndex){
            System.out.println(String.format("(%s)\t%s", menuOp.fst, menuOp.snd));

        }
    }

    protected Menu_Option getMenuWithIndex(String option){
        String menuName = null;

        for(Pair<String, String> menuOp : optionToIndex){
            if(menuOp.fst.equals(option)) {
                menuName = menuOp.snd;
                break;
            }
        }

        return optionMap.getOrDefault(menuName, null);
    }

    public void apply() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while(true){
            String input;

            print_menu();
            System.out.print("Option: ");
            try {
                input = reader.readLine().toLowerCase();
            } catch (IOException e){
                System.out.println("Error reading input");
                continue;
            }
            System.out.println();

            if(input.equals("r")){
                return;
            }

            Menu_Option option = getMenuWithIndex(input);

            if (option == null) {
                System.out.println("Invalid function");
                continue;
            }

            option.apply();
        }

    }
}
