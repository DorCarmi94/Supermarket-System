package Sup_Inv.Suppliers.Service;

import java.util.List;

public class OrderDTO {

    public List<ProductInOrderDTO> productInOrderDTOList;
    public int shopID;

    public OrderDTO(int shopID, List<ProductInOrderDTO> productInOrderDTOS){
        this.shopID = shopID;
        this.productInOrderDTOList = productInOrderDTOS;
    }

    public void updateAmountInOrderByID(int barcode, int amount){
        for (ProductInOrderDTO pd : productInOrderDTOList) {
            if(pd.barcode == barcode)
                pd.setAmount(amount);
        }
    }



}
