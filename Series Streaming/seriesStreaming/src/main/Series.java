package main;
import java.util.*;


public class Series {
    
    public static ArrayList<SeriesModel> seriesID = new ArrayList<>();

    public static boolean ageRestriction(int age) {
        return age >= 2 && age <= 18;
    }

    public static boolean captureSeries(int id, String name, int age, int noe) {
        for (SeriesModel model : seriesID) {
            if (model.getID() == id) {
                throw new IllegalArgumentException("Series with ID " + id + " already exists.");
            }
        }
        if (!ageRestriction(age)) {
            return false; //invalid age
        }

        seriesID = Save_Load.load();

        SeriesModel model = new SeriesModel();
        model.setID(id);
        model.setName(name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase());
        model.setAge(age);
        model.setNumberOfEpisodes(noe);

        seriesID.add(model);
        Save_Load.save(seriesID);
        return true;

    }
    
    public static SeriesModel searchSeries(int id) {
        seriesID = Save_Load.load();
        for (SeriesModel model : seriesID) {
            if (model.getID() == id) {
                return model;
            }
        }
        return null;
    }
    
    public static boolean updateSeries(int id, String field, String newValue) {
        seriesID = Save_Load.load();
        for (SeriesModel model : seriesID) {
            if (model.getID() == id) {
                switch (field.toLowerCase()) {
                    case "id":
                        model.setID(Integer.parseInt(newValue));
                        break;
                    case "name":
                        model.setName(newValue);
                        break;
                    case "age":
                        try {
                            int newAge = Integer.parseInt(newValue);
                            model.setAge(newAge);
                        } catch (NumberFormatException e) { return false; }
                        break;
                    case "episodes":
                        model.setNumberOfEpisodes(Integer.parseInt(newValue));
                        break;
                    default: return false;
                }
                Save_Load.save(seriesID);
                return true;
            }
        }
        return false;
    }

    
    public static boolean deleteSeries(int id) {
        Series.seriesID = Save_Load.load();

        boolean removed = seriesID.removeIf(model -> model.getID() == id);
        if (!removed) {
            throw new IllegalArgumentException("Series with ID " + id + " not found.");
        }
        Save_Load.save(seriesID);
        return removed;
    }

    public static List<SeriesModel> seriesReport() {
        return Save_Load.load();
    }
}
