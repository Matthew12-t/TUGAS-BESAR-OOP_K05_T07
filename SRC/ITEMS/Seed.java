package SRC.ITEMS;

public class Seed extends Item {
    private String season;
    private int daysToHarvest;
    public Seed(String name, int sellPrice, int buyPrice, String season, int daysToHarvest) {
        super(name, "Seed", sellPrice, buyPrice);
        this.season = season;
        this.daysToHarvest = daysToHarvest;
    }
    // Getters
    public String getSeason() {
        return season;
    }
    public int getDaysToHarvest() {
        return daysToHarvest;
    }
    // Setters
    public void setSeason(String season) {
        this.season = season;
    }
    public void setDaysToHarvest(int daysToHarvest) {
        this.daysToHarvest = daysToHarvest;
    }
    
}