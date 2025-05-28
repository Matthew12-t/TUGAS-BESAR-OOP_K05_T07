package SRC.WEATHER;

/**
 * Enum untuk menangani cuaca dalam game
 */
public enum Weather {
    SUNNY("Sunny"),
    RAINY("Rainy"),
    ANY("Any");
    
    private final String displayName;
    
    Weather(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
      /**
     * Parse weather from string
     */
    public static Weather fromString(String weatherStr) {
        for (Weather weather : Weather.values()) {
            if (weather.displayName.equalsIgnoreCase(weatherStr) || weather.name().equalsIgnoreCase(weatherStr)) {
                return weather;
            }
        }
        return ANY;
    }
    
    /**
     * Parse weather from string (alias for backward compatibility)
     */
    public static Weather parseFromString(String weatherStr) {
        return fromString(weatherStr);
    }
}