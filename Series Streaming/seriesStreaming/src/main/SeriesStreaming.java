package main;
import java.util.*;

public class SeriesStreaming {

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        String in;
        int cont;

        Series.seriesID = Save_Load.load(); //Loading the json at the start of the app
        
        System.out.println("\nLatest Series - 2025");
        System.out.println("\n*****************************\n");
        System.out.println("Enter (1) to continue to the Menu and any other key then ENTER to exit.");
        in = input.nextLine(); //Taking input as a String to begin to allow the user to enter ANY key and have it be valid.

        try{
            cont = Integer.parseInt(in); //Parsing the input to an integer to check if menu is opening or if app is closing.
            if (cont == 1) {
                SeriesUI.menu(); //Opening menu if integer is 1.
            } else {
                SeriesUI.exitApplication(); //Closing app if integer is not 1.
            }
        }catch (NumberFormatException e){ //Handling app closure if input is not an integer.
            input.close();
            SeriesUI.exitApplication();
        }
    }
}
