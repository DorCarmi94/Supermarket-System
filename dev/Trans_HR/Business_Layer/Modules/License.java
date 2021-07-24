package Trans_HR.Business_Layer.Modules;

public class License {


    private int licenseSN;
    private String licenseType;



    public License(int licenseSN, String licenseType){
        this.licenseSN = licenseSN;
        this.licenseType = licenseType;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public int getLicenseSN() {
        return licenseSN;
    }

    public void setLicenseSN(int licenseSN) {
        this.licenseSN = licenseSN;
    }
}



