package Trans_HR.Business_Layer.Transportations.Controllers;


import Trans_HR.Business_Layer.Modules.License;
import Trans_HR.Business_Layer.Service;
import Trans_HR.Business_Layer.Workers.Modules.Worker.Driver;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Drivers_Controller {

    private static class Singelton_Driver {
        private static Drivers_Controller instance = new Drivers_Controller();
    }

    private Drivers_Controller() {
        // initialization code..
    }

    public static Drivers_Controller getInstance() {
        return Singelton_Driver.instance;
    }

    private Service service = Service.getInstance();

    public List<String> getDriverToTrucks(int truckId, Date date)
    {
      //  service.uploadDrivers(); //TODO implement this
        List<License> license_list = Service.getInstance().getHashTrucks().get(truckId).getLicenses();
        List<String> output = new LinkedList<>();
        for (Driver driver : service.getDrivers().values())
        {
            if(driver.checkLicense(license_list))
            {
                String driverToString = driver.getWorkerSn() + ". " + driver.getWorkerName();
                output.add(driverToString);
            }
        }
        return output;
    }
}





