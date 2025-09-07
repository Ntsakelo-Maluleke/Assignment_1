package main;
public class SeriesModel {
    public int seriesID;
    public String seriesName;
    public int seriesAgeRestriction;
    public int seriesNumberOfEpisodes;

    public void setID(int ID) {
        seriesID = ID;
    }

    public void setName(String name) {
        seriesName = name;
    }

    public void setAge(int age) {
        seriesAgeRestriction = age;
    }

    public void setNumberOfEpisodes(int no_Episodes) {
        seriesNumberOfEpisodes = no_Episodes;
    }

    public int getID() {
        return seriesID;
    }
    public String getName() {
        return seriesName;
    }
    public int getAge() {
        return seriesAgeRestriction;
    }
    public int getNumberOfEpisodes() {
        return seriesNumberOfEpisodes;
    }
}
