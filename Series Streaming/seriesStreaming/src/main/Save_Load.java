package main;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

import com.google.gson.*;

public class Save_Load { //class dedicated to handling all data processes in relation to the json file
    private static String FILE_NAME = "series.json";
    
    public static void save(ArrayList<SeriesModel> seriesID) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try(FileWriter writer = new FileWriter(FILE_NAME)){
            gson.toJson(seriesID, writer);
            System.out.println("Saved to series.json");
            writer.close();
        } catch (Exception e){
            System.out.println("Error whilst saving" + e.getMessage());
        }
    }
    
    public static ArrayList<SeriesModel> load() {
        Gson gson = new Gson();

        try (FileReader reader = new FileReader(FILE_NAME)) {
            SeriesModel[] seriesArray = gson.fromJson(reader, SeriesModel[].class);
            if (seriesArray != null) {
                return new ArrayList<>(Arrays.asList(seriesArray));
            }
        } catch (Exception e) {
            System.out.println("Error whilst loading" + e.getMessage());
        }
        return new ArrayList<>();
    }
}
