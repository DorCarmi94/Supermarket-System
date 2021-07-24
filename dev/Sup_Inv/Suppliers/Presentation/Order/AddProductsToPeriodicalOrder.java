package Sup_Inv.Suppliers.Presentation.Order;

import Sup_Inv.Result.Result;
import Sup_Inv.Suppliers.Presentation.Menu_Option;
import Sup_Inv.Suppliers.Service.OrderAndProductManagement;
import Sup_Inv.Suppliers.Service.ProductInOrderDTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class AddProductsToPeriodicalOrder extends Menu_Option {


    private OrderAndProductManagement orderAndProductManagement;

    public AddProductsToPeriodicalOrder(OrderAndProductManagement orderAndProductManagement) {
        this.orderAndProductManagement = orderAndProductManagement;
    }

    @Override
    public void apply() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int orderId;

        try {
            List<ProductInOrderDTO> products = new LinkedList<>();

            orderId = readInt("Enter the order ID", reader);
            if(orderId < 0){
                return;
            }

            int numberOfProducts = readInt("Enter the numbers of product you want the add", reader);

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

            Result<List<Integer>> res = orderAndProductManagement.addProductsToPeriodicalOrder(orderId,products);
            if(res.isOk()){
                if(res.getValue().size() > 0) {
                    System.out.println("Those product barcodes wasnt added " + res.getValue().toString());
                }
                else
                {
                    System.out.println("All products added successfully");
                }
            } else {
                System.out.println(res.getMessage());
            }

        } catch (Exception e){
            System.out.println("Error reading input");
        }
    }
}
