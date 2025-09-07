package main;

import java.util.*;

public class SeriesUI {
    static Scanner input = new Scanner(System.in);
    public static void menu() {
        
        System.out.println("\n****************************\n" + "\nPlease select one of the following items:\n" + "\n****************************\n");
        System.out.println("(1) Capture New Series\n"
                        + "(2) Search for a series\n"
                        + "(3) Update series age restriction\n"
                        + "(4) Delete a series\n"
                        + "(5) Print series report\n"
                        + "(6) Exit application"); //Printing the Menu.
        System.out.println("\n****************************\n");   
        menuLoop(); //Calling loop to look through menu items.
    }

    public static void menuLoop() {
        boolean status = true;
        String in = input.nextLine();

        while (status == true) {
            try {
                int choice = Integer.parseInt(in);
                switch (choice) {
                    case 1:
                        captureSeriesUI();
                        status = false;
                        break;
                    case 2:
                        searchSeriesUI();
                        status = false;
                        break;
                    case 3:
                        updateSeriesUI();
                        status = false;
                        break;
                    case 4:
                        deleteSeriesUI();
                        status = false;
                        break;
                    case 5:
                        seriesReportUI();
                        status = false;
                        break;
                    case 6:
                        exitApplication();
                        status = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.");
                        status = false; //To stop the while loop and avoid infinite prints
                        menuLoop(); //Restarting the loop
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Character entered is not a number!");
                status = false;
                menuLoop();
            }
        }
    }

    private static void captureSeriesUI() {
        System.out.println("Enter series ID: ");
        int id = Integer.parseInt(input.nextLine()); //Getting the id

        System.out.println("Enter series Name: ");
        String name = input.nextLine(); //Getting the series name

        System.out.println("Enter age restriction: ");
        int age = Integer.parseInt(input.nextLine()); //Getting the series age

        System.out.println("Enter number of episodes: ");
        int noe = Integer.parseInt(input.nextLine()); //Getting the series Number of Episodes

        System.out.println("You entered:");
        System.out.println("ID: " + id + "\n"
                        + "Name: " + name + "\n"
                        + "Age restriction: " + age + "\n"
                        + "No. of episodes: " + noe);
        System.out.println("Is this correct? (Enter yes or no)"); //Confrming details with user
        String proceed = input.nextLine();

        //Confirmation logic

        if (proceed.toLowerCase().equalsIgnoreCase("yes")) {
            try{
            Series.captureSeries(id, name, age, noe); //Attempting to capture details
            System.out.println("Series successfully captured");
            whileTrueContinue(() -> captureSeriesUI());
            }catch (IllegalArgumentException e){
                System.out.println("Error! Invalid series data at: " + e); //Presenting error if data is invalid
                retryLoop(() -> captureSeriesUI()); //Offering user the opportunity to retry 
            }
        } else if (proceed.toLowerCase().equalsIgnoreCase("no")) {
            retryLoop(() -> captureSeriesUI()); //Retry opportunity
        }
    }
    
    private static void searchSeriesUI() {

        //Search logic

        System.out.println("Enter series ID:");
        int id = Integer.parseInt(input.nextLine()); //Getting the search ID
        SeriesModel model = Series.searchSeries(id); // Call searchSeries with the ID and store the matching SeriesModel (or null if not found)

        if (model != null) {
            System.out.println("You searched for: " + model.getName() + "\n" + "Is this correct? (Enter yes or no)");
            String proceed = input.nextLine();
            if (proceed.toLowerCase().equalsIgnoreCase("yes")) {
                singleReport(id); //Printing the details for the given ID
            } else if (proceed.toLowerCase().equalsIgnoreCase("no")) {
                retryLoop(() -> searchSeriesUI()); //Retry opportunity
            }
        }else {
            System.out.println("Series with ID: " + id + " not found."); //Not found error
        }
    }

    private static void updateSeriesUI() {
        System.out.println("Enter ID of series you would like to update:");
        int id = Integer.parseInt(input.nextLine());
        SeriesModel model = Series.searchSeries(id);
        if (model != null) {
            System.out.println("You searched for: " + model.getName() + "\n" + "Is this correct?(Enter yes or no)");
            String proceed = input.nextLine();
            if (proceed.toLowerCase().equalsIgnoreCase("yes")) {
                System.out.println("What would you like to change? (Enter ID, Name, Age or Number of Episodes)");
                String procession = input.nextLine();
                switch (procession.toLowerCase()) {
                    case "id":
                        System.out.println("Enter the new ID:");
                        String newID = input.nextLine();
                        Series.updateSeries(id, procession, newID);
                        break;
                    case "name":
                        System.out.println("Enter the new Name:");
                        String newName = input.nextLine();
                        Series.updateSeries(id, procession, newName);
                        break;
                    case "age":
                        System.out.println("Enter the new Age:");
                        String newAge = input.nextLine();
                        Series.updateSeries(id, procession, newAge);
                        break;
                    case "number of episodes", "no. of episodes":
                        System.out.println("Enter the new Number of Episodes: ");
                        String newNOE = input.nextLine();
                        Series.updateSeries(id, procession, newNOE);
                        break;
                    default:
                        System.out.println("Error! " + procession + " is not a valid input.");
                        break;
                }
            } else if (proceed.toLowerCase().equalsIgnoreCase("no")) {
                retryLoop(() -> updateSeriesUI());
            }
        }else {
            System.out.println("Series with ID: " + id + " not found.");
        }
    }
    
    private static void deleteSeriesUI() {
        boolean status = true;
        try {
            System.out.println("Enter the ID of the series you would like to delete:");
            int id = Integer.parseInt(input.nextLine());
            for (SeriesModel model : Series.seriesID) {
                if (model.getID() == id) {
                    while (status == true) {
                        System.out.println("Are you sure you want to delete: " + model.getName());
                        String proceed = input.nextLine();
                        if (proceed.toLowerCase().equalsIgnoreCase("yes")) {
                            Series.deleteSeries(id);
                            System.out.println("Series: " + model.getName() + " deleted successfully!");
                            status = false;
                        } else if (proceed.toLowerCase().equalsIgnoreCase("no")) {
                            retryLoop(() -> deleteSeriesUI());
                            status = false;
                        } else {
                            System.out.println("Error! " + proceed + " is not a valid input.");
                            continue;
                        }
                    }
                }
            }
            whileTrueContinue(() -> deleteSeriesUI()); //Offering chance to delete more than one series
        } catch (NumberFormatException e) {
            System.out.println("Error! Please enter a numeric ID.");
            retryLoop(() -> deleteSeriesUI());
        } catch (IllegalArgumentException e) {
            System.out.println("Error! " + e.getMessage());
            retryLoop(() -> deleteSeriesUI());
        }
    }
    
    private static void seriesReportUI() {
        List<SeriesModel> report = Series.seriesReport();

        if (report.isEmpty()) {
            System.out.println("No series data found.");
        } else {
            System.out.println("\n---- Series Report ----\n");
            System.out.println("****************************\n");
            for (SeriesModel model : report) {
                System.out.println(
                    "ID: " + model.getID() + "\n" +
                    "Name: " + model.getName() + "\n" +
                    "Age restriction: " + model.getAge() + "\n" +
                    "Number of Episodes: " + model.getNumberOfEpisodes() + "\n" +
                    "\n****************************\n"
                );
            }
        }
        closeLoop(() -> seriesReportUI());
    }

    private static void singleReport(int id) {
        for (SeriesModel model : Series.seriesID) {
            System.out.println("\n****************************\n");
            if (model.getID() == id) {
                System.out.println(
                    "ID: " + model.getID() + "\n" +
                    "Name: " + model.getName() + "\n" +
                    "Age restriction: " + model.getAge() + "\n" +
                    "Number of Episodes: " + model.getNumberOfEpisodes() + "\n" +"\n****************************\n"
                );
            } else {
                System.out.println("Error! Series with ID: " + id + " was not found.");
            }
        }
    }

    public static void exitApplication() {
        System.out.println("Goodbye");
        System.exit(0);
    }

    private static void whileTrueContinue(Runnable action) {
        boolean status = true;
        while (status == true) {
            System.out.println(
                    "Would you like to return to the main menu or enter another (Enter (1) for main menu and (2) to enter another)");
            int confirm = Integer.parseInt(input.nextLine());
            if (confirm == 1) {
                menu();
                status = false;
            } else if (confirm == 2) {
                action.run();
                status = false;
            } else {
                System.out.println("Error! " + confirm + " is not a valid input.");
                continue;
            }
        }
    }

    private static void retryLoop(Runnable action) {
        boolean status = true;
        while (status == true) {
            System.out.println("Would you like to retry or cancel? (Enter Retry or Cancel)");
            String procession = input.nextLine();
            if (procession.toLowerCase().equalsIgnoreCase("retry")) {
                action.run();
                status = false;
            } else if (procession.toLowerCase().equalsIgnoreCase("cancel")) {
                closeLoop(action);;
                status = false;
            } else {
                System.out.println("Error! " + procession + "is not a valid input.");
                continue;
            }
        }
    }

    private static void closeLoop(Runnable action) {
        boolean status = true;
        while(status == true){
            System.out.println(
                    "Would you like to return to the main menu or close the application (Enter (1) fro main menu or (2) to close)");
            int proceed = Integer.parseInt(input.nextLine());
            if (proceed == 1) {
                menu();
                status = false;
            } else if (proceed == 2) {
                exitApplication();
                status = false;
            } else {
                System.out.println("Error! " + proceed + " is not a valid input.");
            }
        }
    }
}
