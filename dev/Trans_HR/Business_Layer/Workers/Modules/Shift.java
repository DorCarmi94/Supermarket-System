package Trans_HR.Business_Layer.Workers.Modules;


import Trans_HR.Business_Layer.Workers.Modules.Worker.Worker;
import Trans_HR.Business_Layer.Workers.Utils.enums;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Shift implements Comparable<Shift>{
    private enums shiftType;
    private Worker manager;
    private List<Worker> shiftWorkers;
    private Date date;
    private int sn;
    private int storeSN;

    public Shift(Date date, enums shiftType, Worker manager, List<Worker> shiftWorkers, int sn,int storeSN) {
        this.date = date;
        this.shiftType = shiftType;
        this.manager = manager;
        this.shiftWorkers = shiftWorkers;
        this.sn = sn;
        this.storeSN = storeSN;
    }

    public void printShift() {
        SimpleDateFormat daty = new SimpleDateFormat("dd-MM-yyyy");
        String dat = daty.format(date);
        System.out.println("Shift SN. " + this.sn);
        System.out.println("Manager SN: " + this.manager.getWorkerSn() + " Manager Name: " + this.manager.getWorkerName());
        System.out.println("Date: " + dat);
        System.out.println("Type: " + this.shiftType);
    }

    public int getShiftSn() { return this.sn; }

    public Date getDate() {
        return date;
    }

    public enums getShiftType() {
        return shiftType;
    }

    public List<Worker> getShiftWorkers() {
        return shiftWorkers;
    }

    public Worker getManager() {
        return manager;
    }

    @Override
    public int compareTo(Shift shiftToCompare) {
        if(shiftToCompare.equals(this)){
            return 0;
        }
        return this.getDate().compareTo(shiftToCompare.date);
    }

    public int getStoreSN() {
        return storeSN;
    }
}
