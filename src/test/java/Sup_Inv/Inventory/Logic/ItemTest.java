package Sup_Inv.Inventory.Logic;

import Sup_Inv.Inventory.Persistence.DTO.ItemDTO;
import Sup_Inv.Inventory.View.View;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ItemTest {

    public Item item;
    public View view = new View();

    @Before
    public void setUp() throws Exception {
        item = new Item(view, new ItemDTO("3", "11", "0", "0", "milk", "Tnuva", "Diary",
                "Drinks", "M", 3, 20));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void checkMinimumQuant() {
        assertTrue(item.checkMinimumQuant());
    }

    @Test
    public void updateMyQuantities() {
        item.updateMyQuantities(200, 200, '+');
        assertFalse(item.checkMinimumQuant());
    }

    @Test
    public void updateMyQuantities1() {
        item.updateMyQuantities(200, 200, '-');
        assertTrue(item.checkMinimumQuant());
    }


}