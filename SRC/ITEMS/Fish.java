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
    private int energy;    public Fish(String name, String type, String location, Season season, Weather weather, GameTime startTime, GameTime endTime) {
        super(name, "Fish", calculateSellPrice(type, season, weather, location, null, startTime, endTime), calculateBuyPrice(type));
        this.type = type;
        this.location = location;
        this.season = season;
        this.weather = weather;
        this.startTime = startTime;
        this.endTime = endTime;
        this.energy = calculateEnergyValue(type);
        
        String imagePath = "RES/FISH/" + name.toLowerCase().replace(" ", "_") + ".png";
        loadImage(imagePath);
    }

    public Fish(String name, String type, String location, Season season, Weather weather, String timeRanges) {
        super(name, "Fish", calculateSellPrice(type, season, weather, location, timeRanges, null, null), calculateBuyPrice(type));
        this.type = type;
        this.location = location;
        this.season = season;
        this.weather = weather;
        this.startTime = null;
        this.endTime = null;
        this.energy = calculateEnergyValue(type);
        
        String imagePath = "RES/FISH/" + name.toLowerCase().replace(" ", "_") + ".png";
        loadImage(imagePath);
    }

    public Fish(String name, int sellPrice, int buyPrice, String type, String location, String season, String weather, Time time) {
        super(name, "Fish", sellPrice, buyPrice);
        this.type = type;
        this.location = location;
        this.season = Season.parseFromString(season);
        this.weather = Weather.parseFromString(weather);
        this.startTime = new GameTime(time.getHour(), time.getMinute());
        this.endTime = new GameTime(23, 59); 
        this.energy = 1;
        
        String imagePath = "RES/FISH/" + name.toLowerCase().replace(" ", "_") + ".png";
        loadImage(imagePath);
    }    
      private static int calculateSellPrice(String type, Season season, Weather weather, String location, String timeRanges, GameTime startTime, GameTime endTime) {
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
        
        int seasonCount = (season == Season.ANY) ? 4 : 1;
        double seasonMultiplier = 4.0 / seasonCount;
        
        int weatherCount = (weather == Weather.ANY) ? 2 : 1;
        double weatherMultiplier = 2.0 / weatherCount;
        
        int hoursCount = 24;
        
        if (timeRanges != null && !timeRanges.isEmpty()) {
            String[] ranges = timeRanges.split(",");
            int totalHours = 0;
            for (String range : ranges) {
                range = range.trim();
                if (range.contains("-")) {
                    String[] hours = range.split("-");
                    int rangeStart = Integer.parseInt(hours[0]);
                    int rangeEnd = Integer.parseInt(hours[1]);
                    if (rangeEnd >= rangeStart) {
                        totalHours += rangeEnd - rangeStart;
                    } else {
                        totalHours += (24 - rangeStart) + rangeEnd;
                    }
                }
            }
            if (totalHours > 0) {
                hoursCount = totalHours;
            }
        } else if (startTime != null && endTime != null) {
            int startHour = startTime.getHour();
            int endHour = endTime.getHour();
            if (endHour >= startHour) {
                hoursCount = endHour - startHour;
            } else {
                hoursCount = (24 - startHour) + endHour;
            }
        }
        
        double timeMultiplier = 24.0 / hoursCount;
        
        int locationCount = 1;
        if (location.contains(",")) {
            locationCount = location.split(",").length;
        }
        double locationMultiplier = 4.0 / locationCount;
        
        double price = seasonMultiplier * timeMultiplier * weatherMultiplier * locationMultiplier * baseValue;
        
        return (int)Math.round(price);
    }
    
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
      private static int calculateEnergyValue(String type) {
        return 1;
    }


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
    
    public String getSeasonString() {
        return season.toString();
    }
    
    public String getWeatherString() {
        return weather.toString();
    }
    
    public boolean canBeCaughtAt(GameTime currentTime, Season currentSeason, Weather currentWeather) {
        if (season != Season.ANY && season != currentSeason) {
            return false;
        }
        
        if (weather != Weather.ANY && weather != currentWeather) {
            return false;
        }
        
        return currentTime.isInTimeRange(startTime, endTime);
    }

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
        player.setEnergy(player.getEnergy() + getEnergyValue());
        System.out.println("You consumed " + this.getName() + " and gained " + getEnergyValue() + " energy!");
    }
}
    