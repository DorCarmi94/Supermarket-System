package Trans_HR.Business_Layer.Transportations.Controllers;


import Trans_HR.Business_Layer.Modules.License;
import Trans_HR.Business_Layer.Service;
import Trans_HR.Business_Layer.Transportations.Modules.Truck;
import Trans_HR.Business_Layer.Transportations.Utils.Buisness_Exception;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Trucks_Controller {

    private static class Singelton_Trucks {
        private static Trucks_Controller instance = new Trucks_Controller();
    }
    private Trucks_Controller() {
        // initialization code..
    }
    public static Trucks_Controller getInstance() {
        return Singelton_Trucks.instance;
    }

    public List<String> Show_LicenseList() throws Buisness_Exception {
        Service service = Service.getInstance();
        List<String> output = new LinkedList<>();
        for(License license : service.getLicense_list().values())
        {
            output.add(license.getLicenseType());
        }
        if (output.isEmpty())
            throw new Buisness_Exception("There are no license in the system\n");
        return output;
    }

    public List<String> getTruckLicenseList(int truckID) throws Buisness_Exception {
        Service service = Service.getInstance();
        List<String> output = new LinkedList<>();
        service.upload_Trucks();
        if(!service.getHashTrucks().containsKey(truckID))
            throw new Buisness_Exception("The truck's id does'nt exist "+"\n");

        List<License> licenses_types = service.getHashTrucks().get(truckID).getLicenses();
        for(License license : licenses_types)
        {
            output.add(license.getLicenseType());
        }
        if (output.isEmpty())
            throw new Buisness_Exception("There are no license to the truck\n");
        return output;
    }



    public List<String> showtrucks() throws Buisness_Exception{
        Service service=Service.getInstance();
        service.upload_Trucks();
        if(service.getHashTrucks().size()==0){
            throw new Buisness_Exception("There are no trucks in the system"+ "\n");
        }
        List<String> result = new LinkedList<>();
        for(Truck trucks: service.getHashTrucks().values()){
            String line = trucks.getId()+". License Number: "+
                    trucks.getlicense_number()+", Model: "+trucks.getModel()+ ".\n Licenses: ";
//            line += trucks.getLicenses().toString()+".";
            for(License license :trucks.getLicenses())
            {
                line += license.getLicenseType()+", ";
            }
            line+=".";
            result.add(line);
        }
        return result;
    }


    public boolean addTruck(int license_number, List<String> licenses_types,
                            String model, double weight, double max_weight) throws Buisness_Exception {
        Service service=Service.getInstance();
        service.set_truck_idCouter();
        boolean result=true;
        if(service.getHashTrucks().containsKey(license_number)){
            throw new Buisness_Exception("The truck driving license is already exist"+ "\n");
        }
        if(max_weight < weight){
            throw new Buisness_Exception("-max weight could not be smaller from weight-\n");
        }
        else {
            service.add_truck(license_number,licenses_types,model,weight,max_weight);
        }
        return result;
    }



    public void removeTruck(int id) throws Buisness_Exception{
        Service service=Service.getInstance();
        service.remove_truck(id);
    }

    public List<String> getFreeTrucks(Date date, int departureTime ) throws Buisness_Exception{
        Service service=Service.getInstance();
        service.upload_Trucks();

        List<String> output = new LinkedList<String>();
        for (Truck truck : service.getHashTrucks().values()) {
            if (truck.checkIfFree(date, departureTime)) {
                String line = truck.getId()+". "+"license number: "+truck.getlicense_number()+
                        ", Model: "+truck.getModel()+".";
                output.add(line);
            }

        }
        return output;
    }



}
