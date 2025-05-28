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
    }    
    /**
     * Calculate sell price based on fish properties using the pricing formula
     */
    private static int calculateSellPrice(String type, Season season, Weather weather, String location) {
        int basePrice;
        
        // Base price by type
        switch (type) {
            case "Common":
                basePrice = 25;
                break;
            case "Regular":
                basePrice = 75;
                break;
            case "Legendary":
                basePrice = 1000;
                break;
            default:
                basePrice = 50;
        }
        
        // Season multiplier (1-4 based on number of seasons)
        double seasonMultiplier = season == Season.ANY ? 4.0 : 1.0;
        
        // Weather multiplier (1-2 based on weather variations)
        double weatherMultiplier = weather == Weather.ANY ? 2.0 : 1.5;
        
        // Location multiplier (1-4 based on location count)
        double locationMultiplier;
        switch (location) {
            case "Forest River":
                locationMultiplier = 1.2;
                break;
            case "Mountain Lake":
                locationMultiplier = 1.5;
                break;
            case "Pond":
                locationMultiplier = 1.0;
                break;
            case "Ocean":
                locationMultiplier = 1.8;
                break;
            default:
                locationMultiplier = 1.0;
        }
        
        return (int) (basePrice * seasonMultiplier * weatherMultiplier * locationMultiplier);
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
    