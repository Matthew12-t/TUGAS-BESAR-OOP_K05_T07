package SRC.DATA;

import SRC.ITEMS.Fish;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;
import SRC.TIME.GameTime;
import java.util.HashMap;
import java.util.Map;

public class FishData {
    private static Map<String, Fish> fish = new HashMap<>();
    
    static {
        initializeFish();
    } 
 
        private static void initializeFish() {
        // Common Fish (3 types) with multiple locations
        fish.put("Bullhead", new Fish("Bullhead", "Common", "Mountain Lake", Season.ANY, Weather.ANY, 
            new GameTime(0, 0), new GameTime(23, 59)));
        
        // Carp - Mountain Lake and Pond
        fish.put("Carp", new Fish("Carp", "Common", "Mountain Lake", Season.ANY, Weather.ANY, 
            new GameTime(0, 0), new GameTime(23, 59)));
        fish.put("Carp", new Fish("Carp", "Common", "Pond", Season.ANY, Weather.ANY, 
            new GameTime(0, 0), new GameTime(23, 59)));
        
        // Chub - Forest River and Mountain Lake
        fish.put("Chub_ForestRiver", new Fish("Chub", "Common", "Forest River", Season.ANY, Weather.ANY, 
            new GameTime(0, 0), new GameTime(23, 59)));
        fish.put("Chub_MountainLake", new Fish("Chub", "Common", "Mountain Lake", Season.ANY, Weather.ANY, 
            new GameTime(0, 0), new GameTime(23, 59)));
        
        // Regular Fish (12 types) with multiple locations
        fish.put("Largemouth Bass", new Fish("Largemouth Bass", "Regular", "Mountain Lake", Season.ANY, Weather.ANY, 
            new GameTime(6, 0), new GameTime(18, 0)));
        
        // Rainbow Trout - Forest River and Mountain Lake
        fish.put("Rainbow Trout_ForestRiver", new Fish("Rainbow Trout", "Regular", "Forest River", Season.SUMMER, Weather.SUNNY, 
            new GameTime(6, 0), new GameTime(18, 0)));
        fish.put("Rainbow Trout_MountainLake", new Fish("Rainbow Trout", "Regular", "Mountain Lake", Season.SUMMER, Weather.SUNNY, 
            new GameTime(6, 0), new GameTime(18, 0)));
        
        // Sturgeon - Summer and Winter seasons
        fish.put("Sturgeon_Summer", new Fish("Sturgeon", "Regular", "Mountain Lake", Season.SUMMER, Weather.ANY, 
            new GameTime(6, 0), new GameTime(18, 0)));
        fish.put("Sturgeon_Winter", new Fish("Sturgeon", "Regular", "Mountain Lake", Season.WINTER, Weather.ANY, 
            new GameTime(6, 0), new GameTime(18, 0)));
        
        // Midnight Carp - Mountain Lake and Pond, Winter and Fall
        fish.put("Midnight Carp_MountainLake_Winter", new Fish("Midnight Carp", "Regular", "Mountain Lake", Season.WINTER, Weather.ANY, 
            new GameTime(20, 0), new GameTime(2, 0)));
        fish.put("Midnight Carp_MountainLake_Fall", new Fish("Midnight Carp", "Regular", "Mountain Lake", Season.FALL, Weather.ANY, 
            new GameTime(20, 0), new GameTime(2, 0)));
        fish.put("Midnight Carp_Pond_Winter", new Fish("Midnight Carp", "Regular", "Pond", Season.WINTER, Weather.ANY, 
            new GameTime(20, 0), new GameTime(2, 0)));
        fish.put("Midnight Carp_Pond_Fall", new Fish("Midnight Carp", "Regular", "Pond", Season.FALL, Weather.ANY, 
            new GameTime(20, 0), new GameTime(2, 0)));
        
        // Ocean Fish - Spring and Summer for Flounder
        fish.put("Flounder_Spring", new Fish("Flounder", "Regular", "Ocean", Season.SPRING, Weather.ANY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        fish.put("Flounder_Summer", new Fish("Flounder", "Regular", "Ocean", Season.SUMMER, Weather.ANY, 
            new GameTime(6, 0), new GameTime(22, 0)));
          // Halibut - combined time periods (6-11 and 19-2)
        fish.put("Halibut", new Fish("Halibut", "Regular", "Ocean", Season.ANY, Weather.ANY, "6-11,19-2"));
        
        fish.put("Octopus", new Fish("Octopus", "Regular", "Ocean", Season.SUMMER, Weather.ANY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        fish.put("Pufferfish", new Fish("Pufferfish", "Regular", "Ocean", Season.SUMMER, Weather.SUNNY, 
            new GameTime(0, 0), new GameTime(16, 0)));
        fish.put("Sardine", new Fish("Sardine", "Regular", "Ocean", Season.ANY, Weather.ANY, 
            new GameTime(6, 0), new GameTime(18, 0)));
        
        // Super Cucumber - Summer, Fall, Winter
        fish.put("Super Cucumber_Summer", new Fish("Super Cucumber", "Regular", "Ocean", Season.SUMMER, Weather.ANY, 
            new GameTime(18, 0), new GameTime(2, 0)));
        fish.put("Super Cucumber_Fall", new Fish("Super Cucumber", "Regular", "Ocean", Season.FALL, Weather.ANY, 
            new GameTime(18, 0), new GameTime(2, 0)));
        fish.put("Super Cucumber_Winter", new Fish("Super Cucumber", "Regular", "Ocean", Season.WINTER, Weather.ANY, 
            new GameTime(18, 0), new GameTime(2, 0)));
        
        // Catfish - Forest River and Pond, Spring, Summer, Fall
        fish.put("Catfish_ForestRiver_Spring", new Fish("Catfish", "Regular", "Forest River", Season.SPRING, Weather.RAINY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        fish.put("Catfish_ForestRiver_Summer", new Fish("Catfish", "Regular", "Forest River", Season.SUMMER, Weather.RAINY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        fish.put("Catfish_ForestRiver_Fall", new Fish("Catfish", "Regular", "Forest River", Season.FALL, Weather.RAINY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        fish.put("Catfish_Pond_Spring", new Fish("Catfish", "Regular", "Pond", Season.SPRING, Weather.RAINY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        fish.put("Catfish_Pond_Summer", new Fish("Catfish", "Regular", "Pond", Season.SUMMER, Weather.RAINY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        fish.put("Catfish_Pond_Fall", new Fish("Catfish", "Regular", "Pond", Season.FALL, Weather.RAINY, 
            new GameTime(6, 0), new GameTime(22, 0)));
        
        fish.put("Salmon", new Fish("Salmon", "Regular", "Forest River", Season.FALL, Weather.ANY, 
            new GameTime(6, 0), new GameTime(18, 0)));
        
        // Legendary Fish (4 types)
        fish.put("Angler", new Fish("Angler", "Legendary", "Pond", Season.FALL, Weather.ANY, 
            new GameTime(8, 0), new GameTime(20, 0)));
        fish.put("Crimsonfish", new Fish("Crimsonfish", "Legendary", "Ocean", Season.SUMMER, Weather.ANY, 
            new GameTime(8, 0), new GameTime(20, 0)));
        fish.put("Glacierfish", new Fish("Glacierfish", "Legendary", "Forest River", Season.WINTER, Weather.ANY, 
            new GameTime(8, 0), new GameTime(20, 0)));
        fish.put("Legend", new Fish("Legend", "Legendary", "Mountain Lake", Season.SPRING, Weather.RAINY, 
            new GameTime(8, 0), new GameTime(20, 0)));
        
    }
    

    public static Fish getFish(String fishName) {
        Fish exactMatch = fish.get(fishName);
        if (exactMatch != null) {
            return exactMatch;
        }
        
        for (Map.Entry<String, Fish> entry : fish.entrySet()) {
            if (entry.getValue().getName().equals(fishName)) {
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    public static Fish getFishByKey(String fishKey) {
        return fish.get(fishKey);
    }


    public static Fish addFish(String fishName, int quantity) {
        Fish fishItem = getFish(fishName);
        if (fishItem != null) {
            return fishItem;
        } else {
            return null;
        }
    }

    public static boolean hasFish(String fishName) {
        if (fish.containsKey(fishName)) {
            return true;
        }
        
        for (Fish fishItem : fish.values()) {
            if (fishItem.getName().equals(fishName)) {
                return true;
            }
        }
        
        return false;
    }
    public static Map<String, Fish> getFishByLocation(String location) {
        Map<String, Fish> locationFish = new HashMap<>();
        for (Map.Entry<String, Fish> entry : fish.entrySet()) {
            if (entry.getValue().getLocation().equalsIgnoreCase(location)) {
                locationFish.put(entry.getKey(), entry.getValue());
            }
        }
        return locationFish;
    }
    
    public static Map<String, Fish> getFishByType(String type) {
        Map<String, Fish> typeFish = new HashMap<>();
        for (Map.Entry<String, Fish> entry : fish.entrySet()) {
            if (entry.getValue().getType().equalsIgnoreCase(type)) {
                typeFish.put(entry.getKey(), entry.getValue());
            }
        }
        return typeFish;
    }
    

    public static Map<String, Fish> getCatchableFish(String location, GameTime currentTime, Season currentSeason, Weather currentWeather) {
        Map<String, Fish> catchableFish = new HashMap<>();
        for (Map.Entry<String, Fish> entry : fish.entrySet()) {
            Fish fishItem = entry.getValue();
            if (fishItem.getLocation().equalsIgnoreCase(location) && 
                fishItem.canBeCaughtAt(currentTime, currentSeason, currentWeather)) {
                catchableFish.put(entry.getKey(), fishItem);
            }
        }
        return catchableFish;
    }
    
    public static Map<String, Fish> getAllFish() {
        return new HashMap<>(fish);
    }
}
