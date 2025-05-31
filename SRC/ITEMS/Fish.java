package SRC.ITEMS;

import SRC.ENTITY.Player;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;
import SRC.TIME.GameTime;
import SRC.TIME.Time;

public class Fish extends Item implements Edible {
    private String type;
    private String location;
    private Season season;
    private Weather weather;
    private GameTime startTime;
    private GameTime endTime;
    private int energy;

    // New constructor for enhanced fishing system
    public Fish(String name, String type, String location, Season season, Weather weather, GameTime startTime, GameTime endTime) {
        super(name, "Fish", calculateSellPrice(type, season, weather, location), calculateBuyPrice(type));
        this.type = type;
        this.location = location;
        this.season = season;
        this.weather = weather;
        this.startTime = startTime;
        this.endTime = endTime;
        this.energy = calculateEnergyValue(type);
        
        // Load image untuk fish ini
        String imagePath = "RES/FISH/" + name.toLowerCase().replace(" ", "_") + ".png";
        loadImage(imagePath);
    }

    // Legacy constructor for backward compatibility
    public Fish(String name, int sellPrice, int buyPrice, String type, String location, String season, String weather, Time time) {
        super(name, "Fish", sellPrice, buyPrice);
        this.type = type;
        this.location = location;
        this.season = Season.parseFromString(season);
        this.weather = Weather.parseFromString(weather);
        this.startTime = new GameTime(time.getHour(), time.getMinute());
        this.endTime = new GameTime(23, 59); // Default end time
        this.energy = 1;
        
        // Load image untuk fish ini
        String imagePath = "RES/FISH/" + name.toLowerCase().replace(" ", "_") + ".png";
        loadImage(imagePath);
    }      /**
     * Calculate sell price based on fish properties using the pricing formula:
     * Price = 4^(number of seasons) × 2^(number of hours) × 2^(number of weather variations) × 4^(number of locations) × C
     * Where C = 10 for common fish, C = 5 for regular fish, C = 25 for legendary fish
     * 
     * NOTE: This implementation is simplified as we can't determine all factors at creation time:
     * - Number of seasons: Simplified to 4 (ANY) or 1 (specific season)
     * - Number of hours: Can't be calculated correctly here (handled in FishPriceValidator)
     * - Weather variations: 2 (ANY) or 1 (specific weather)
     * - Number of locations: Simplified to 1 for all fish (actual number handled in FishPriceValidator)
     */
    private static int calculateSellPrice(String type, Season season, Weather weather, String location) {
        // Base value (C) by type
        int baseValue;
        switch (type) {
            case "Common":
                baseValue = 10;
                break;
            case "Regular":
                baseValue = 5;
                break;
            case "Legendary":
                baseValue = 25;
                break;
            default:
                baseValue = 5;
        }
        
        // Calculate number of seasons
        int numSeasons = (season == Season.ANY) ? 4 : 1;
        
        // Calculate number of weather variations
        int numWeather = (weather == Weather.ANY) ? 2 : 1;
        
        // Calculate hours (can't be determined accurately at creation time, use default)
        // For exact calculation, see FishPriceValidator.java
        int startHour = 0;
        int endHour = 0;
        
        if (startHour != endHour) {
            startHour = 12; // Default assumption
        }
        
        // Number of locations (can't be determined accurately at creation time, use default)
        int numLocations = 1;
        
        // Calculate using formula
        double price = Math.pow(4, numSeasons) * 
                      Math.pow(2, numWeather) * 
                      Math.pow(4, numLocations) * 
                      baseValue;
        
        return (int)price;
    }
    
    /**
     * Calculate buy price (typically 40% of sell price)
     */
    private static int calculateBuyPrice(String type) {
        switch (type) {
            case "Common":
                return 10;
            case "Regular":
                return 30;
            case "Legendary":
                return 200;
            default:
                return 20;
        }
    }
    
    /**
     * Calculate energy value based on fish type
     */
    private static int calculateEnergyValue(String type) {
        switch (type) {
            case "Common":
                return 15;
            case "Regular":
                return 25;
            case "Legendary":
                return 50;
            default:
                return 20;
        }
    }

    // Getters
    public String getType() {
        return type;
    }
    
    public String getLocation() {
        return location;
    }
    
    public Season getSeason() {
        return season;
    }
    
    public Weather getWeather() {
        return weather;
    }
    
    public GameTime getStartTime() {
        return startTime;
    }
    
    public GameTime getEndTime() {
        return endTime;
    }
    
    // Legacy getter for backward compatibility
    public String getSeasonString() {
        return season.toString();
    }
    
    public String getWeatherString() {
        return weather.toString();
    }
    
    /**
     * Check if fish can be caught at current time, season, and weather
     */
    public boolean canBeCaughtAt(GameTime currentTime, Season currentSeason, Weather currentWeather) {
        // Check season requirement
        if (season != Season.ANY && season != currentSeason) {
            return false;
        }
        
        // Check weather requirement
        if (weather != Weather.ANY && weather != currentWeather) {
            return false;
        }
        
        // Check time requirement
        return currentTime.isInTimeRange(startTime, endTime);
    }

    // Setters
    public void setType(String type) {
        this.type = type;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public void setSeason(Season season) {
        this.season = season;
    }
    
    public void setWeather(Weather weather) {
        this.weather = weather;
    }
    
    public void setStartTime(GameTime startTime) {
        this.startTime = startTime;
    }
    
    public void setEndTime(GameTime endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public int getEnergyValue() {
        return this.energy;
    }
    
    @Override
    public void consume(Player player) {
        // Logic to consume the fish and increase player's energy
        player.setEnergy(player.getEnergy() + getEnergyValue());
        System.out.println("You consumed " + this.getName() + " and gained " + getEnergyValue() + " energy!");
    }
}
    