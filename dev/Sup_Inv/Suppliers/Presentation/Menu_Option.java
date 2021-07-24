package Sup_Inv.Suppliers.Presentation;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class Menu_Option {

    /**
     * Start the sub menu
     */
    public abstract void apply();

    public String readString(String info, BufferedReader bufferedReader) throws IOException {
        System.out.print(info + ": ");
        return bufferedReader.readLine();
    }

    public int readInt(String info, BufferedReader bufferedReader){
        System.out.print(info + ": ");
        try{
            String input = bufferedReader.readLine();
            return Integer.parseInt(input);
        } catch (IOException e) {
            System.out.println("Error at reading");
        } catch (NumberFormatException e){
            System.out.println("Need to be a number");
        }

        return -1;
    }

    public int readIntPos(String info, String posInfo, BufferedReader bufferedReader){
        int number = -1;
        System.out.print(info + ": ");
        try{
            String input = bufferedReader.readLine();
            number = Integer.parseInt(input);
        } catch (IOException e) {
            System.out.println("Error at reading");
        } catch (NumberFormatException e){
            System.out.println("Need to be a number");
        }

        if(number < 0){
            System.out.println(posInfo);
        }

        return number;
    }
}
