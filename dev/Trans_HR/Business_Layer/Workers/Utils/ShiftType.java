package Trans_HR.Business_Layer.Workers.Utils;

public class ShiftType {
    private int shiftTypeSN;
    private String shiftType;

    public ShiftType(int shiftTypeSN, String shiftType) {
        this.shiftTypeSN = shiftTypeSN;
        this.shiftType = shiftType;
    }

    public String getShiftType() {
        return shiftType;
    }

    public void setShiftType(String shiftType) {
        this.shiftType = shiftType;
    }

    public int getShiftTypeSN() {
        return shiftTypeSN;
    }

    public void setShiftTypeSN(int shiftTypeSN) {
        this.shiftTypeSN = shiftTypeSN;
    }
}
