package SRC.ITEMS;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Weather {
    public static final int SUNNY = 0;
    public static final int RAINY = 1;
    private int currentWeather;
    private Random random;
    
    private List<Integer> weatherForecast;
    private static final int DAYS_IN_SEGMENT = 10;
    private static final int MIN_RAINY_DAYS = 2;
    
    public Weather() {
        random = new Random();
        currentWeather = SUNNY; // Default to sunny
        generateWeatherForecast();
    }
    
    public void generateWeatherForecast() {
        weatherForecast = new ArrayList<>();
        for (int i = 0; i < DAYS_IN_SEGMENT; i++) {
            weatherForecast.add(SUNNY);
        }
        int rainyDaysAdded = 0;
        while (rainyDaysAdded < MIN_RAINY_DAYS) {
            int randomDay = random.nextInt(DAYS_IN_SEGMENT);
            if (weatherForecast.get(randomDay) != RAINY) {
                weatherForecast.set(randomDay, RAINY);
                rainyDaysAdded++;
            }
        }
        
        int additionalRainyDays = random.nextInt(2); 
        
        for (int i = 0; i < additionalRainyDays; i++) {
            int randomDay = random.nextInt(DAYS_IN_SEGMENT);
            if (weatherForecast.get(randomDay) != RAINY) {
                weatherForecast.set(randomDay, RAINY);
            }
        }
        if (!weatherForecast.isEmpty()) {
            currentWeather = weatherForecast.get(0);
        }
    }
    public int getWeatherForDay(int dayInSegment) {
        int dayIndex = (dayInSegment - 1) % DAYS_IN_SEGMENT;
        
        if (dayIndex < 0 || dayIndex >= weatherForecast.size()) {
            return SUNNY; 
        }
        
        return weatherForecast.get(dayIndex);
    }
  
    public void setCurrentWeather(int day) {
        currentWeather = getWeatherForDay(day);
    }
 
    public int getCurrentWeather() {
        return currentWeather;
    }

    public boolean isRainy() {
        return currentWeather == RAINY;
    }

    public boolean isSunny() {
        return currentWeather == SUNNY;
    }
    

    public String getWeatherString() {
        return currentWeather == RAINY ? "Rainy" : "Sunny";
    }
    
    public void newSegment() {
        generateWeatherForecast();
    }
    
    public void newSeason() {
        generateWeatherForecast();
    }
}
