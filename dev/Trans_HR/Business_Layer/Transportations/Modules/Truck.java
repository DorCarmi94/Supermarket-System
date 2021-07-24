package Trans_HR.Business_Layer.Transportations.Modules;


import Trans_HR.Business_Layer.Modules.License;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Truck {

    private static int idcounter=0;
    private int id;
    private int license_number;
    private List<License> licenses_types;
    private String model;
    private double weight;
    private double max_weight;
    private List<Transportation> transportations = new LinkedList<>();

    public Truck(int license_number, List<License> licenses_types, String model, double weight, double max_weight) {
        this.id=idcounter++;
        this.license_number = license_number;
        this.licenses_types = licenses_types;
        this.model = model;
        this.weight = weight;
        this.max_weight = max_weight;
    }

    public Truck(int id,int license_number, List<License> licenses_types, String model, double weight, double max_weight) {
        this.id=id;
        this.license_number = license_number;
        this.licenses_types = licenses_types;
        this.model = model;
        this.weight = weight;
        this.max_weight = max_weight;
        if(id>idcounter)
            idcounter= id+1;
    }

    public double getMax_weight()
    {
        return this.weight+this.max_weight;
    }
    public int getId()
    {
        return this.id;
    }

    public List<License> getLicenses()
    {
        return this.licenses_types;
    }

    public Integer getlicense_number() {
        return this.license_number;
    }

    public String getModel() {
        return this.model;
    }

    public static void setIdCounter(int id){
        if(idcounter<id)
            idcounter = id;
    }

    public void addDate(Transportation transportation)
    {
        this.transportations.add(transportation);
    }


    public boolean checkIfFree(Date date, int departureTime)
    {
        for(Transportation transportation:transportations){
            if (date.equals(transportation.getDate())& departureTime == transportation.getDepartureTime())
                return false;
        }
        return true;
    }

    public void Remove_date(Integer transportationID){
        transportations.removeIf(transportation1 -> transportationID == transportation1.getId());

    }
    public static int getIdCounter(){
        return idcounter;
    }

    public List<Transportation> getTransportations(){
        return transportations;
    }

}