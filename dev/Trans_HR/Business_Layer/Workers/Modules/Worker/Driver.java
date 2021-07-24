package Trans_HR.Business_Layer.Workers.Modules.Worker;


import Trans_HR.Business_Layer.Modules.License;
import Trans_HR.Business_Layer.Transportations.Modules.Transportation;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

    public class Driver extends Worker{

    private List<String> licenses;
    private List<Transportation> transportations;

    public Driver(int id, String name, String phoneNumber, int bankAccount, int salary, Date date, String jobTitle, int Sn, int storeSN,String licenses) {
        super(id, name, phoneNumber, bankAccount, salary, date, jobTitle, Sn, storeSN);
        licenses = licenses.replaceAll(" ", "");
        this.licenses = new LinkedList<>();
        this.licenses.addAll(Arrays.asList(licenses.split(",")));
        this.licenses.removeIf(license -> !( license.equals("C") || license.equals("C1") ));
        transportations = new LinkedList<>();
    }


        @Override
        public String toString() {
            SimpleDateFormat daty = new SimpleDateFormat("dd/MM/yyyy");
            String dat = daty.format(super.getWorkerStartingDate());
            return "sn. " + getWorkerSn() + "\n" +
                    "id: " + getWorkerId() + "\n" +
                    "name: '" + getWorkerName() + '\'' + "\n" +
                    "phoneNumber: " + getWorkerPhone() + "\n" +
                    "bankAccount: " + getWorkerBankAccount() + "\n" +
                    "salary: " + getWorkerSalary() + "\n" +
                    "date: " + dat + "\n" +
                    "jobTitle: '" + getWorkerJobTitle() + '\'' + "\n" +
                    "license: " + this.licenses.toString() + "\n" +
                    "constrains: " + printConstrains()  + "\n" ;
        }

        public List<String> getLicenses()
    {
        return this.licenses;
    }

    public void addDate(Transportation transportation)
    {
        boolean isfound=false;
        for(Transportation t: this.transportations){
            if(transportation.getId()==t.getId())
                isfound=true;
        }
        if(!isfound)
            this.transportations.add(transportation);
    }

    public void Remove_date(Integer transportationID){
        transportations.removeIf(transportation1 -> transportationID == transportation1.getId());
    }

    public boolean addLicense(String license){
        if(this.getLicenses().contains(license)){
            return false;
        }
        else{
            this.getLicenses().add(license);
            return true;
        }
    }

    public List<Transportation> getTransportations(){
        return transportations;
    }
        public boolean checkLicense(List<License> license_list) {
            boolean output = false;
            for (String license : this.licenses) {
                for(License license1:license_list){
                    if(license.equals(license1.getLicenseType())){
                        output = true;
                        break;
                    }
                }
            }
            return output;
        }

    public boolean checkIfFree(Date date, String shiftType)
    {
        int departureTime;
        if(shiftType.equals("MORNING"))
            departureTime=1;
        else departureTime=2;

        for(Transportation transportation: this.transportations){
            if (date.equals(transportation.getDate())& departureTime == transportation.getDepartureTime())
                return false;
        }
        return true;
    }
}
