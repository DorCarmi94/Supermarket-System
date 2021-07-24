package Trans_HR.Business_Layer.Workers.Controllers;

import Trans_HR.Business_Layer.Service;
import Trans_HR.Business_Layer.Workers.Modules.Worker.Driver;
import Trans_HR.Interface_Layer.Workers.SystemInterfaceWorkers;
import junit.framework.TestCase;

import java.util.Date;

public class WorkerControllerTest extends TestCase {


    public void testAddDriver() {
        int storeSN = Service.getInstance().getWorkerController().getCurrentStoreSN();
        if (storeSN == -1) {
            storeSN = 3;
        }

        int sizeBefore = Service.getInstance().getWorkerList(storeSN).size();
        Date date = WorkerController.parseDate("20-03-2020");
        SystemInterfaceWorkers.getInstance().addDriver(1000,"test","1231",12,-213,"20-03-2020","Driver","none","C1");
        assertEquals(sizeBefore,Service.getInstance().getWorkerList(storeSN).size());
    }

    public void testAddWorker() {
        int storeSN = Service.getInstance().getWorkerController().getCurrentStoreSN();
        if (storeSN == -1) {
            storeSN = 3;
        }

        int sizeBefore = Service.getInstance().getWorkerList(storeSN).size();
        Date date = WorkerController.parseDate("20-03-2020");
        SystemInterfaceWorkers.getInstance().addWorker(1000,"test","1231",-12,213,"20-03-2020","Storekeeper","none");
        assertEquals(sizeBefore,Service.getInstance().getWorkerList(storeSN).size());
    }
}