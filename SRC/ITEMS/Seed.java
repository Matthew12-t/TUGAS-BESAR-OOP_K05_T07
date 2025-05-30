package SRC.ITEMS;

public class Seed extends Item {
    private String season;
    private int daysToHarvest;    
    public Seed(String name, int sellPrice, int buyPrice, String season, int daysToHarvest) {
        super(name, "Seed", sellPrice, buyPrice);
        this.season = season;
        this.daysToHarvest = daysToHarvest;        // Load image untuk seed ini
        // Extract crop name from seed name (remove "Seed" suffix and trim)
        String cropName = name.replace(" Seed", "").trim();
        String folderName = cropName.toUpperCase().replace(" ", ""); // Remove spaces for folder
        String fileName = cropName.toLowerCase().replace(" ", "_"); // Use underscores for file
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