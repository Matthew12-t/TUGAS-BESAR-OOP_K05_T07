package SRC.ITEMS;

public class Seed extends Item {
    private String season;
    private int daysToHarvest;    
    public Seed(String name, int sellPrice, int buyPrice, String season, int daysToHarvest) {
        super(name, "Seed", sellPrice, buyPrice);
        this.season = season;
        this.daysToHarvest = daysToHarvest;        

        String cropName = name.replace(" Seed", "").trim();
        String folderName = cropName.toUpperCase().replace(" ", ""); 
        String fileName = cropName.toLowerCase().replace(" ", "_"); 
        String imagePath = "RES/SEED/" + folderName + "/" + fileName + "_seed.png";
        loadImage(imagePath);
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