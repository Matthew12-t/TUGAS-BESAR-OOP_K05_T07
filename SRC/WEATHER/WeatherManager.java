package SRC.WEATHER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * WeatherManager handles advanced weather generation logic
 * Ensures minimum rainy days per season cycle (10 days)
 */
public class WeatherManager {
    
    // Weather pattern settings
    private static final int SEASON_CYCLE_DAYS = 10;
    private static final int MIN_RAINY_DAYS_PER_CYCLE = 2;
    private static final int MAX_RAINY_DAYS_PER_CYCLE = 4;
    
    // Current season cycle tracking
    private List<Weather> currentSeasonCycle;
    private int currentDayInCycle;
    
    /**
     * Constructor - generates weather pattern for first season cycle
     */
    public WeatherManager() {
        generateNewSeasonCycle();
        currentDayInCycle = 0;
    }
    
    /**
     * Get weather for current day and advance to next day
     * @return Weather for current day
     */
    public Weather getNextDayWeather() {
        Weather todayWeather = currentSeasonCycle.get(currentDayInCycle);
        
        // Advance to next day
        currentDayInCycle++;
        
        // If we've completed the season cycle, generate new one
        if (currentDayInCycle >= SEASON_CYCLE_DAYS) {
            generateNewSeasonCycle();
            currentDayInCycle = 0;
        }
        
        return todayWeather;
    }
    
    /**
     * Generate weather pattern for a complete season cycle (10 days)
     * Ensures minimum rainy days while maintaining variety
     */
    private void generateNewSeasonCycle() {
        currentSeasonCycle = new ArrayList<>();
        
        // Calculate rainy days for this cycle (between min and max)
        int rainyDaysCount = MIN_RAINY_DAYS_PER_CYCLE + 
                            (int)(Math.random() * (MAX_RAINY_DAYS_PER_CYCLE - MIN_RAINY_DAYS_PER_CYCLE + 1));
        
        // Create list with required rainy days
        for (int i = 0; i < rainyDaysCount; i++) {
            currentSeasonCycle.add(Weather.RAINY);
        }
        
        // Fill remaining days with sunny weather
        for (int i = rainyDaysCount; i < SEASON_CYCLE_DAYS; i++) {
            currentSeasonCycle.add(Weather.SUNNY);
        }
        
        // Shuffle the list to randomize weather pattern
        Collections.shuffle(currentSeasonCycle);
        
        // Apply weather pattern rules for better gameplay experience
        applyWeatherPatternRules();
        
        System.out.println("DEBUG: Generated new season weather cycle with " + rainyDaysCount + " rainy days");
        printWeatherCycle();
    }
    
    /**
     * Apply rules to make weather patterns more realistic and gameplay-friendly
     */
    private void applyWeatherPatternRules() {
        // Rule 1: Try to avoid more than 3 consecutive rainy days
        preventLongRainyStreaks();
        
        // Rule 2: Ensure at least one sunny day between rainy streaks when possible
        insertSunnyBreaks();
    }
    
    /**
     * Prevent more than 3 consecutive rainy days
     */
    private void preventLongRainyStreaks() {
        for (int i = 0; i <= currentSeasonCycle.size() - 4; i++) {
            // Check for 4 consecutive rainy days
            boolean fourConsecutiveRainy = true;
            for (int j = i; j < i + 4; j++) {
                if (currentSeasonCycle.get(j) != Weather.RAINY) {
                    fourConsecutiveRainy = false;
                    break;
                }
            }
            
            if (fourConsecutiveRainy) {
                // Find a sunny day to swap with the 4th rainy day
                for (int k = 0; k < currentSeasonCycle.size(); k++) {
                    if (k < i || k >= i + 4) { // Outside the streak
                        if (currentSeasonCycle.get(k) == Weather.SUNNY) {
                            // Swap the 4th rainy day with this sunny day
                            Collections.swap(currentSeasonCycle, i + 3, k);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Try to insert sunny breaks between rainy days when possible
     */
    private void insertSunnyBreaks() {
        // Look for patterns where we can improve weather distribution
        // This is a simplified version - could be made more sophisticated
        for (int i = 0; i < currentSeasonCycle.size() - 2; i++) {
            if (currentSeasonCycle.get(i) == Weather.RAINY && 
                currentSeasonCycle.get(i + 1) == Weather.RAINY && 
                currentSeasonCycle.get(i + 2) == Weather.RAINY) {
                
                // Try to find a sunny day to swap with middle rainy day
                for (int j = 0; j < currentSeasonCycle.size(); j++) {
                    if (j != i && j != i + 1 && j != i + 2 && 
                        currentSeasonCycle.get(j) == Weather.SUNNY) {
                        Collections.swap(currentSeasonCycle, i + 1, j);
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Get weather for a specific day in current cycle (0-indexed)
     * @param dayInCycle Day in current season cycle (0-9)
     * @return Weather for that day
     */
    public Weather getWeatherForDay(int dayInCycle) {
        if (dayInCycle >= 0 && dayInCycle < currentSeasonCycle.size()) {
            return currentSeasonCycle.get(dayInCycle);
        }
        return Weather.SUNNY; // Default fallback
    }
    
    /**
     * Get current day in season cycle
     * @return Current day in cycle (0-9)
     */
    public int getCurrentDayInCycle() {
        return currentDayInCycle;
    }
    
    /**
     * Get complete weather cycle for current season
     * @return List of weather for all 10 days
     */
    public List<Weather> getCurrentWeatherCycle() {
        return new ArrayList<>(currentSeasonCycle);
    }
    
    /**
     * Reset to start of current cycle (useful for testing or season changes)
     */
    public void resetCycle() {
        currentDayInCycle = 0;
    }
    
    /**
     * Force generation of new season cycle (useful for season changes)
     */
    public void forceNewCycle() {
        generateNewSeasonCycle();
        currentDayInCycle = 0;
    }
    
    /**
     * Debug method to print current weather cycle
     */
    private void printWeatherCycle() {
        System.out.print("DEBUG: Weather cycle: ");
        for (int i = 0; i < currentSeasonCycle.size(); i++) {
            String weatherSymbol = (currentSeasonCycle.get(i) == Weather.RAINY) ? "R" : "S";
            System.out.print(weatherSymbol);
            if (i < currentSeasonCycle.size() - 1) System.out.print("-");
        }
        System.out.println();
    }
    
    /**
     * Get statistics for current weather cycle
     * @return String with cycle statistics
     */
    public String getCycleStatistics() {
        int rainyCount = 0;
        int sunnyCount = 0;
        
        for (Weather weather : currentSeasonCycle) {
            if (weather == Weather.RAINY) {
                rainyCount++;
            } else {
                sunnyCount++;
            }
        }
        
        return String.format("Cycle Stats - Rainy: %d, Sunny: %d, Current Day: %d/10", 
                           rainyCount, sunnyCount, currentDayInCycle + 1);
    }
}
