package Sup_Inv.Suppliers.Service;

import java.util.List;

public class ContractWithSupplierDTO {

    public List<String> dailyInfo;
    public String contractDetails;

    public ContractWithSupplierDTO(List<String> dailyInfo,String contractDetails)
    {
        this.contractDetails=contractDetails;
        this.dailyInfo=dailyInfo;
    }
}
