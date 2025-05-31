import SRC.ITEMS.Fish;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;
import SRC.TIME.GameTime;

public class TestFishEnergy {
    public static void main(String[] args) {
        System.out.println("Testing Fish Energy System...");
        
        Fish commonFish = new Fish("Test Common Fish", "Common", "Ocean", Season.ANY, Weather.ANY, 
                                   new GameTime(6, 0), new GameTime(18, 0));
        
        Fish regularFish = new Fish("Test Regular Fish", "Regular", "Ocean", Season.ANY, Weather.ANY, 
                                    new GameTime(6, 0), new GameTime(18, 0));
        
        Fish legendaryFish = new Fish("Test Legendary Fish", "Legendary", "Ocean", Season.ANY, Weather.ANY, 
                                      new GameTime(6, 0), new GameTime(18, 0));
        
        System.out.println("Common Fish energy value: " + commonFish.getEnergyValue());
        System.out.println("Regular Fish energy value: " + regularFish.getEnergyValue());
        System.out.println("Legendary Fish energy value: " + legendaryFish.getEnergyValue());
        
        System.out.println("\nAll fish should provide 1 energy point as per requirements.");
        
        boolean allCorrect = (commonFish.getEnergyValue() == 1) && 
                           (regularFish.getEnergyValue() == 1) && 
                           (legendaryFish.getEnergyValue() == 1);
        
        System.out.println("Test " + (allCorrect ? "PASSED" : "FAILED"));
    }
}
