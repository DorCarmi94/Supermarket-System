package Trans_HR.Data_Layer.Dummy_objects;

import java.util.LinkedList;
import java.util.List;

public class dummy_Truck {

    private int SN;
    private int license_number;
    private String model;
    private double weight;
    private double max_weight;
    private List<Integer> license_type;

    public dummy_Truck(int SN, int license_number, String model, double weight, double max_weight, List<Integer> license_type){
        this.SN = SN;
        this.license_number=license_number;
        this.model=model;
        this.weight=weight;
        this.max_weight=max_weight;
        this.license_type=license_type;
    }

    public dummy_Truck(int SN, int license_number, String model, double weight, double max_weight){
        this.SN = SN;
        this.license_number=license_number;
        this.model=model;
        this.weight=weight;
        this.max_weight=max_weight;
        this.license_type = new LinkedList<>();
    }

    public int getLicense_number() {
        return license_number;
    }

    public void setLicense_number(int license_number) {
        this.license_number = license_number;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getMax_weight() {
        return max_weight;
    }

    public void setMax_weight(double max_weight) {
        this.max_weight = max_weight;
    }

    public int getSN() {
        return SN;
    }

    public void setSN(int SN) {
        this.SN = SN;
    }

    public List<Integer> getLicense_type() {
        return license_type;
    }

    public void setLicense_type(List<Integer> license_type) {
        this.license_type = license_type;
    }
}
