package Trans_HR.Business_Layer.Workers.Controllers;

import Trans_HR.Business_Layer.Service;
import Trans_HR.Interface_Layer.Workers.SystemInterfaceWorkers;
import junit.framework.TestCase;

import java.util.Date;

public class ShiftControllerTest extends TestCase {

    public void testCreateShift() {
        int storeSN = Service.getInstance().getShiftController().getCurrentStoreSN();
        if (storeSN == -1) {
            storeSN = 3;
        }

        int sizeBefore = Service.getInstance().getShiftHistory(storeSN).size();
        SystemInterfaceWorkers.getInstance().createShift("morning",10000,"1,2","20-09-2020");
        assertEquals(sizeBefore,Service.getInstance().getShiftHistory(storeSN).size());
    }
}