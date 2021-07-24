package Trans_HR.Business_Layer.Workers.Modules.Worker;

import Trans_HR.Business_Layer.Workers.Utils.enums;
import javafx.util.Pair;

import java.text.SimpleDateFormat;
import java.util.*;

public class Worker {

    private int sn;
    private int id;
    private String name;
    private String phoneNumber;
    private int bankAccount;
    private int salary;
    private Date date;
    private String jobTitle;
    private int storeSN;
    private HashMap<Pair<enums,enums>,Boolean> constrains;

    @Override
    public String toString() {
        SimpleDateFormat daty = new SimpleDateFormat("dd/MM/yyyy");
        String dat = daty.format(date);
        return "sn. " + sn + "\n" +
                "id: " + id + "\n" +
                "name: '" + name + '\'' + "\n" +
                "phoneNumber: " + phoneNumber + "\n" +
                "bankAccount: " + bankAccount + "\n" +
                "salary: " + salary + "\n" +
                "date: " + dat + "\n" +
                "jobTitle: '" + jobTitle + '\'' + "\n" +
                "constrains: " + printConstrains()  + "\n" ;
    }

    public Worker(int id, String name, String phoneNumber, int bankAccount, int salary, Date date, String jobTitle, int Sn,int storeSN) {
        this.sn = Sn;
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.bankAccount = bankAccount;
        this.salary = salary;
        this.date = date;
        this.jobTitle = jobTitle;
        this.constrains = new HashMap<>();
        this.storeSN = storeSN;
    }

    public String printConstrains(){
        String constrains="";
        if(this.constrains.keySet().isEmpty()){
            constrains = "No Constrains";
        } else {
            Iterator<Pair<enums,enums>> it = this.constrains.keySet().iterator();
            Pair<enums,enums> pair = it.next();
            constrains = constrains + pair.getKey() +" "+ pair.getValue();
            while (it.hasNext()) {
                pair = it.next();
                constrains =  pair.getKey() +" "+ pair.getValue() +", "+ constrains ;
            }
        }
        return  constrains;
    }

    public int getWorkerSn() { return this.sn; }

    public int getWorkerId() {
        return id;
    }

    public String getWorkerName() {
        return name;
    }

    public int getWorkerSalary() {
        return this.salary;
    }

    public String getWorkerJobTitle() {
        return jobTitle;
    }

    public HashMap<Pair<enums, enums>,Boolean> getWorkerConstrains() {
        return constrains;
    }


    public void addConstrainsToWorker(enums day, enums shiftType){
        this.constrains.put( new Pair<>(day,shiftType),false);
    }

    public void resetConstrains() {
        this.constrains = new HashMap<>();
    }

    public void setWorkerSalary(int newSalary){
        if(salary >= 0) {
            this.salary = newSalary;
        }
    }

    public void printWorker(){
        System.out.println(this.sn + ". ID: " + this.id + " Name: " + this.name+" Job Title: "+this.jobTitle);
    }

    public boolean available(Date date, enums sType) {
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
        enums selectedDay= enums.valueOf(dayOfWeek.toUpperCase());
        return !this.constrains.containsKey(new Pair<>(selectedDay,sType));
    }

    public Date getWorkerStartingDate() {
        return this.date;
    }

    public int getStoreSN() {
        return storeSN;
    }

    public String getWorkerPhone() {
        return this.phoneNumber;
    }

    public int getWorkerBankAccount() {
        return this.bankAccount;
    }
}
