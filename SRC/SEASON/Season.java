package SRC.SEASON;

/**
 * Enum untuk menangani musim dalam game
 */
public enum Season {
    SPRING("Spring"),
    SUMMER("Summer"),
    FALL("Fall"),
    WINTER("Winter"),
    ANY("Any");
    
    private final String displayName;
    
    Season(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
      /**
     * Parse season from string
     */
    public static Season fromString(String seasonStr) {
        for (Season season : Season.values()) {
            if (season.displayName.equalsIgnoreCase(seasonStr) || season.name().equalsIgnoreCase(seasonStr)) {
                return season;
            }
        }
        return ANY;
    }
    
    /**
     * Parse season from string (alias for backward compatibility)
     */
    public static Season parseFromString(String seasonStr) {
        return fromString(seasonStr);
    }
}