package Trans_HR.Business_Layer.Modules;

public class Area {

    private String areaName;
    private int areaSN;

    public Area( int areaSN, String areaName) {
        this.areaName = areaName;
        this.areaSN = areaSN;
    }

    public int getAreaSN() {
        return areaSN;
    }

    public void setAreaSN(int areaSN) {
        this.areaSN = areaSN;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
}

