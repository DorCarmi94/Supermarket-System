package Trans_HR.Data_Layer.Dummy_objects;

public class dummy_License {
    private int SN;
    private String LicenseType;


    public dummy_License(int SN,String LicenseType) {
        this.SN=SN;
        this.LicenseType = LicenseType;
    }

    public int getSN(){ return this.SN;}
    public String getLicenseType(){ return this.LicenseType;}

}
