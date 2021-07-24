package Trans_HR.Data_Layer.Dummy_objects;

public class dummy_Area {
    private int SN;
    private String AreaName;


    public dummy_Area(int sn,String areaName) {
        this.SN=sn;
        this.AreaName = areaName;
    }

    public String getAreaName() {
        return AreaName;
    }

    public void setAreaName(String areaName) {
        this.AreaName = areaName;
    }

    public int getSN(){ return this.SN;}

}
