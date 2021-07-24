package Sup_Inv.Suppliers.Presentation.Order;

import Sup_Inv.Result.Result;
import Sup_Inv.Suppliers.Presentation.Menu_Option;
import Sup_Inv.Suppliers.Service.OrderAndProductManagement;
import Sup_Inv.Suppliers.Service.ProductInOrderDTO;
import Sup_Inv.Suppliers.Structs.Days;
import Sup_Inv.Suppliers.Structs.StructUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class CreatePeriodicalOrder extends Menu_Option {


    private OrderAndProductManagement orderAndProductManagement;

    public CreatePeriodicalOrder(OrderAndProductManagement orderAndProductManagement) {
        this.orderAndProductManagement = orderAndProductManagement;
    }

    @Override
    public void apply() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int shopNumber;

        try {
            List<ProductInOrderDTO> products = new LinkedList<>();

            shopNumber = readInt("Enter the shop number", reader);
            if(shopNumber < 0){
                return;
            }

            String daysStr = readString("Enter supply days : {days of the week (Sunday...) suppurated with coma (,) }\n",reader);
            String[] dayArr = daysStr.toUpperCase().split(",");
            List<Days> days = StructUtils.getDaysList(dayArr);

            int weekP = readIntPos("Enter the week period","Week period is positive number", reader);
            if(weekP < 0){
                return;
            }

            int numberOfProducts = readInt("Enter the numbers of product you want the order", reader);

            System.out.println("Enter barcode and amount\nformat:<Barcode> <amount>");
            for(int i=0; i < numberOfProducts; i=i+1){
                String[] productAndAmount = reader.readLine().split(" ");

                if(productAndAmount.length != 2){
                    System.out.println("Invalid product amount format");
                    continue;
                }

                products.add(new ProductInOrderDTO(
                        Integer.parseInt(productAndAmount[0]),
                        Integer.parseInt(productAndAmount[1])));
            }

            Result<List<Integer>> res = orderAndProductManagement.createPeriodicalOrder(products, days, weekP, shopNumber);
            if(res.isOk()){
                System.out.println("Orders id : "+ res.getValue());
            } else {
                System.out.println(res.getMessage());
            }

        } catch (Exception e){
            System.out.println("Error reading input");
        }
    }
}
