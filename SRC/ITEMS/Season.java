package SRC.ITEMS;

public class Season {
    private final String[] seasonNames = {"Spring", "Summer", "Fall", "Winter"};
    
    private int currentSeasonIndex = 0;
    
    public Season() {
        this.currentSeasonIndex = 0;
    }
    
    public Season(int seasonIndex) {
        if (seasonIndex >= 0 && seasonIndex < seasonNames.length) {
            this.currentSeasonIndex = seasonIndex;
        } else {
            this.currentSeasonIndex = 0; // Default to Spring if invalid
        }
    }
    
    public String getCurrentSeasonName() {
        return seasonNames[currentSeasonIndex];
    }
    
    public int getCurrentSeasonIndex() {
        return currentSeasonIndex;
    }
    

    public void setCurrentSeasonIndex(int index) {
        if (index >= 0 && index < seasonNames.length) {
            this.currentSeasonIndex = index;
        }
    }
   
    public String[] getSeasonNames() {
        return seasonNames;
    }
    
    public void nextSeason() {
        currentSeasonIndex = (currentSeasonIndex + 1) % seasonNames.length;
    }
   
    public void resetToSpring() {
        currentSeasonIndex = 0;
    }
    
    public int getSeasonCount() {
        return seasonNames.length;
    }
}
