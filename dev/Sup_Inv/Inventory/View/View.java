package Sup_Inv.Inventory.View;

import Sup_Inv.Inventory.Interfaces.Observer;

public class View implements Observer
{
    public View(){}

    @Override
    public void onEvent(String msg) {
        System.out.println(msg);
    }
}
