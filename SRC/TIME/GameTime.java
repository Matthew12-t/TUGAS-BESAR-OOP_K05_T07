package SRC.TIME;

import SRC.SEASON.Season;
import SRC.WEATHER.Weather;

/**
 * Class untuk menangani waktu dalam game
 */
public class GameTime {
    private int hour;
    private int minute;
    private Season currentSeason;
    private Weather currentWeather;
      public GameTime() {
        this.hour = 6; // Start at 6 AM
        this.minute = 0;
        this.currentSeason = Season.SPRING;
        this.currentWeather = Weather.SUNNY;
    }
    
    public GameTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        this.currentSeason = Season.SPRING;
        this.currentWeather = Weather.SUNNY;
    }
    
    public GameTime(int hour, int minute, Season season, Weather weather) {
        this.hour = hour;
        this.minute = minute;
        this.currentSeason = season;
        this.currentWeather = weather;
    }
    
    /**
     * Add minutes to current time
     */
    public void addMinutes(int minutes) {
        this.minute += minutes;
        while (this.minute >= 60) {
            this.minute -= 60;
            this.hour++;
            if (this.hour >= 24) {
                this.hour = 0;
            }
        }
    }
      /**
     * Check if current time is within the specified time range
     */
    public boolean isTimeInRange(String timeRange) {
        if (timeRange.equalsIgnoreCase("Any")) {
            return true;
        }
        
        // Parse time ranges like "06.00-18.00" or "20.00-02.00"
        String[] ranges = timeRange.split(",");
        
        for (String range : ranges) {
            range = range.trim();
            String[] times = range.split("-");
            if (times.length == 2) {
                int startHour = parseTimeToHour(times[0].trim());
                int endHour = parseTimeToHour(times[1].trim());
                
                if (isCurrentTimeInRange(startHour, endHour)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Check if current time is in range between two GameTime objects
     */
    public boolean isInTimeRange(GameTime startTime, GameTime endTime) {
        int currentMinutes = hour * 60 + minute;
        int startMinutes = startTime.hour * 60 + startTime.minute;
        int endMinutes = endTime.hour * 60 + endTime.minute;
        
        if (startMinutes <= endMinutes) {
            // Normal range like 6:00-18:00
            return currentMinutes >= startMinutes && currentMinutes <= endMinutes;
        } else {
            // Overnight range like 20:00-02:00
            return currentMinutes >= startMinutes || currentMinutes <= endMinutes;
        }
    }
    
    private int parseTimeToHour(String timeStr) {
        try {
            // Handle format like "06.00" or "18.00"
            String[] parts = timeStr.split("\\.");
            return Integer.parseInt(parts[0]);
        } catch (Exception e) {
            return 0;
        }
    }
    
    private boolean isCurrentTimeInRange(int startHour, int endHour) {
        if (startHour <= endHour) {
            // Normal range like 6-18
            return hour >= startHour && hour <= endHour;
        } else {
            // Overnight range like 20-02
            return hour >= startHour || hour <= endHour;
        }
    }
    
    // Getters and setters
    public int getHour() { return hour; }
    public int getMinute() { return minute; }
    public Season getCurrentSeason() { return currentSeason; }
    public Weather getCurrentWeather() { return currentWeather; }
    
    public void setHour(int hour) { this.hour = hour; }
    public void setMinute(int minute) { this.minute = minute; }
    public void setCurrentSeason(Season season) { this.currentSeason = season; }
    public void setCurrentWeather(Weather weather) { this.currentWeather = weather; }
    
    public String getFormattedTime() {
        return String.format("%02d:%02d", hour, minute);
    }
}
