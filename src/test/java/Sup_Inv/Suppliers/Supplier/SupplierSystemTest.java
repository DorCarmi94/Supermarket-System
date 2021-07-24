package Sup_Inv.Suppliers.Supplier;

import Sup_Inv.Suppliers.Structs.PaymentOptions;
import org.junit.Assert;

import java.util.List;

import static org.junit.Assert.*;

public class SupplierSystemTest {
    SupplierSystem instance;
    @org.junit.Before
    public void setUp() throws Exception {
        instance=SupplierSystem.getInstance();
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void addPaymentOptions() {
        List<SupplierDetails> supplierDetailsList=instance.getAllSuppliers();
        if(supplierDetailsList.size()==0)
        {
            Assert.fail();
        }
        int supID=supplierDetailsList.get(0).supplierId;
        instance.addPaymentOptions(supID,"EURO");
        Assert.assertTrue(instance.getPaymentOptions(supID).contains("EURO"));

    }

    @org.junit.Test
    public void removePaymentOptions() {
        List<SupplierDetails> supplierDetailsList=instance.getAllSuppliers();
        if(supplierDetailsList.size()==0)
        {
            Assert.fail();
        }
        int supID=supplierDetailsList.get(0).supplierId;
        List<String> options=instance.getPaymentOptions(supID);
        if(supplierDetailsList.size()==0)
        {
            Assert.fail();
        }
        String op=options.get(0);
        instance.removePaymentOptions(supID,op);
        Assert.assertFalse(instance.getPaymentOptions(supID).contains(op));
    }
}