package Trans_HR.Data_Layer.Dummy_objects;

import javafx.util.Pair;

import java.util.Date;
import java.util.HashMap;

public class dummy_Worker {



    private int SN;
    private int Id;
    private String name;
    private String phone;
    private int BankAccount;
    private int salary;
    private int storeSN;
    private Date start_Date;
    private String Job_title;

    public dummy_Worker(int sn,int Id, String name, String phone, int BankAccount, int salary, int storeSN,Date start_Date, String job_title){
        this.SN=sn;
        this.Id=Id;
        this.name=name;
        this.phone=phone;
        this.BankAccount=BankAccount;
        this.salary=salary;
        this.storeSN=storeSN;
        this.start_Date=start_Date;
        this.Job_title=job_title;
    }


    public int getSN() {
        return SN;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getBankAccount() {
        return BankAccount;
    }

    public void setBankAccount(int bankAccount) {
        BankAccount = bankAccount;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Date getStart_Date() {
        return start_Date;
    }

    public void setStart_Date(Date start_Date) {
        this.start_Date = start_Date;
    }

    public String getJob_title() {
        return Job_title;
    }

    public void setJob_title(String job_title) {
        Job_title = job_title;
    }

    public int getStoreSN() {
        return storeSN;
    }
}


