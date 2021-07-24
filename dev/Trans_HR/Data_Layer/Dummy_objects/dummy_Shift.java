package Trans_HR.Data_Layer.Dummy_objects;
import javafx.util.Pair;

import java.util.Date;
import java.util.List;

public class dummy_Shift {

    private Date date;
    private int shift_type;
    private int manager;
    private List<Integer> shift_workers;
    private int Sn;
    private int Branch;

    public dummy_Shift(Date date, int shift_type, int manager, int Sn, int Branch){
        this.date=date;
        this.shift_type=shift_type;
        this.manager=manager;
        this.Sn=Sn;
        this.Branch=Branch;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getShift_type() {
        return shift_type;
    }

    public void setShift_type(int shift_type) {
        this.shift_type = shift_type;
    }

    public int getManager() {
        return manager;
    }

    public void setManager(int manager) {
        this.manager = manager;
    }

    public List<Integer> getShift_workers() {
        return shift_workers;
    }

    public void setShift_workers(List<Integer> shift_workers) {
        this.shift_workers = shift_workers;
    }

    public int getSn() {
        return Sn;
    }

    public void setSn(int sn) {
        Sn = sn;
    }

    public int getBranch() {
        return Branch;
    }

    public void setBranch(int branch) {
        Branch = branch;
    }
}
