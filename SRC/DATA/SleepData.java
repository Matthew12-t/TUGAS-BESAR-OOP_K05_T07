package SRC.DATA;

import SRC.SEASON.Season;
import SRC.WEATHER.Weather;
import java.util.HashMap;
import java.util.Map;

/**
 * SleepData class manages sleep-related data and messages
 */
public class SleepData {
    
    // Sleep trigger types
    public enum SleepTrigger {
        MANUAL,     // Player pressed sleep key
        LOW_ENERGY, // Energy <= 20
        LATE_TIME   // Time = 02:00
    }
    
    // Sleep result data structure
    public static class SleepResult {
        private String message;
        private String backgroundImage;
        private int income;
        private int day;
        private Season season;
        private Weather weather;
        private SleepTrigger trigger;
        
        public SleepResult(String message, String backgroundImage, int income, 
                          int day, Season season, Weather weather, SleepTrigger trigger) {
            this.message = message;
            this.backgroundImage = backgroundImage;
            this.income = income;
            this.day = day;
            this.season = season;
            this.weather = weather;
            this.trigger = trigger;
        }
        
        // Getters
        public String getMessage() { return message; }
        public String getBackgroundImage() { return backgroundImage; }
        public int getIncome() { return income; }
        public int getDay() { return day; }
        public Season getSeason() { return season; }
        public Weather getWeather() { return weather; }
        public SleepTrigger getTrigger() { return trigger; }
        
        // Setters
        public void setMessage(String message) { this.message = message; }
        public void setBackgroundImage(String backgroundImage) { this.backgroundImage = backgroundImage; }
        public void setIncome(int income) { this.income = income; }
        public void setDay(int day) { this.day = day; }
        public void setSeason(Season season) { this.season = season; }
        public void setWeather(Weather weather) { this.weather = weather; }
        public void setTrigger(SleepTrigger trigger) { this.trigger = trigger; }
    }
    
    // Sleep messages based on trigger type
    private static final Map<SleepTrigger, String[]> SLEEP_MESSAGES = new HashMap<>();
    
    // Background images for sleep screens
    private static final Map<SleepTrigger, String> SLEEP_BACKGROUNDS = new HashMap<>();
    
    static {
        // Initialize sleep messages
        SLEEP_MESSAGES.put(SleepTrigger.MANUAL, new String[] {
            "You had a good night's rest!",
            "You slept peacefully through the night.",
            "A restful sleep has restored your energy!",
            "Sweet dreams filled your night.",
            "You wake up feeling refreshed and ready for a new day!"
        });
        
        SLEEP_MESSAGES.put(SleepTrigger.LOW_ENERGY, new String[] {
            "You collapsed from exhaustion...",
            "Your body couldn't take it anymore and you fell asleep.",
            "Fatigue overwhelmed you and you passed out.",
            "You were too tired to continue and fell into a deep sleep.",
            "Exhaustion took over and you slept wherever you were."
        });
        
        SLEEP_MESSAGES.put(SleepTrigger.LATE_TIME, new String[] {
            "The late hour caught up with you...",
            "You couldn't fight the sleepiness any longer.",
            "The night was too deep and sleep took you.",
            "Your eyelids grew heavy and you drifted off to sleep.",
            "The late night finally claimed you and you fell asleep."
        });
          // Initialize background images - semua menggunakan background yang sama
        SLEEP_BACKGROUNDS.put(SleepTrigger.MANUAL, "RES/SLEEP/sleeping_Background.png");
        SLEEP_BACKGROUNDS.put(SleepTrigger.LOW_ENERGY, "RES/SLEEP/sleeping_Background.png");
        SLEEP_BACKGROUNDS.put(SleepTrigger.LATE_TIME, "RES/SLEEP/sleeping_Background.png");
    }
    
    /**
     * Get random sleep message based on trigger type
     */
    public static String getSleepMessage(SleepTrigger trigger) {
        String[] messages = SLEEP_MESSAGES.get(trigger);
        if (messages == null || messages.length == 0) {
            return "You slept through the night.";
        }
        return messages[(int)(Math.random() * messages.length)];
    }
    
    /**
     * Get background image for sleep trigger
     */
    public static String getSleepBackground(SleepTrigger trigger) {
        return SLEEP_BACKGROUNDS.getOrDefault(trigger, "/RES/UI/sleep_default.png");
    }    /**
     * Calculate daily income based on game progress
     */
    public static int calculateDailyIncome(int day, Season season) {
        // Base income starts at 100 and increases with day progression
        int baseIncome = 100;
        int dayMultiplier = day / 7; // Every week increases income
        int seasonBonus = 0;
        
        // Season-based income bonus
        switch (season) {
            case SPRING:
                seasonBonus = 50; // Good growing season
                break;
            case SUMMER:
                seasonBonus = 75; // Peak growing season
                break;
            case FALL:
                seasonBonus = 100; // Harvest season
                break;
            case WINTER:
                seasonBonus = 25; // Slow season
                break;
            case ANY:
                seasonBonus = 60; // Average season bonus
                break;
        }
        
        return baseIncome + (dayMultiplier * 25) + seasonBonus + (int)(Math.random() * 50);
    }
    
    /**
     * Calculate total income including shipping bin earnings
     */
    public static int calculateTotalIncome(int day, Season season, int shippingBinValue) {
        int dailyIncome = calculateDailyIncome(day, season);
        return dailyIncome + shippingBinValue;
    }
      /**
     * Create a complete sleep result
     */
    public static SleepResult createSleepResult(SleepTrigger trigger, int day, 
                                              Season season, Weather weather) {
        String message = getSleepMessage(trigger);
        String background = getSleepBackground(trigger);
        int income = calculateDailyIncome(day, season);
        
        return new SleepResult(message, background, income, day, season, weather, trigger);
    }
    
    /**
     * Create a complete sleep result with shipping bin income
     */
    public static SleepResult createSleepResultWithShipping(SleepTrigger trigger, int day, 
                                                          Season season, Weather weather, 
                                                          int shippingBinValue) {
        String message = getSleepMessage(trigger);
        String background = getSleepBackground(trigger);
        int totalIncome = calculateTotalIncome(day, season, shippingBinValue);
        
        return new SleepResult(message, background, totalIncome, day, season, weather, trigger);
    }
    
    /**
     * Format complete sleep information for display
     */
    public static String formatSleepInfo(SleepResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append(result.getMessage()).append("\n\n");
        sb.append("Day: ").append(result.getDay()).append("\n");
        sb.append("Season: ").append(result.getSeason().toString()).append("\n");
        sb.append("Weather: ").append(result.getWeather().toString()).append("\n");
        sb.append("Daily Income: ").append(result.getIncome()).append(" coins\n");
        
        return sb.toString();
    }
}
