package SRC.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import SRC.ENTITY.Player;
import SRC.TIME.Time;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;


public class ClockUI {
    
    private Time time = new Time(6, 0); 
    private int day = 1;
    private int month = 1;
    private String[] dayNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
    private int dayOfWeek = 0;
    private long lastTimeUpdate = System.currentTimeMillis();    private String[] seasons = {"Spring", "Summer", "Fall", "Winter"};
    private int currentSeasonIndex = 0;
    private BufferedImage clockImage; 
    
    
    private BufferedImage weatherImage;
    private BufferedImage seasonImage;    
    private Weather currentWeather = Weather.SUNNY;
    private Season currentSeason = Season.SPRING;
    
    
    private SRC.WEATHER.WeatherManager weatherManager;
      
    private boolean isTimePaused = false;
    
    
    private SRC.MAIN.GamePanel gamePanel;
    
    
    private int screenWidth;
    private int screenHeight;    

    public ClockUI(int screenWidth, int screenHeight, SRC.MAIN.GamePanel gamePanel) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.gamePanel = gamePanel;
          
        this.weatherManager = new SRC.WEATHER.WeatherManager();
        
        loadClockImage();
        loadWeatherImage();
        loadSeasonImage();
    }

    private void loadClockImage() {
        try {
            String imagePath = getClockImagePath();
            clockImage = ImageIO.read(new File(imagePath));
            System.out.println("Loaded clock image: " + imagePath);
        } catch (Exception e) {
            System.err.println("Error loading clock image: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    private String getClockImagePath() {
        int hour = time.getHour();
        
        if (hour == 3 || hour == 15) { 
            return "RES/CLOCK/clock_.3pm.png";
        } else if (hour == 7) { 
            return "RES/CLOCK/clock_.7am.png";
        } else if (hour == 18) { 
            return "RES/CLOCK/clock_6pm.png";
        } else {
            
            return "RES/CLOCK/clock_6pm.png";
        }
    }
    

    private void loadWeatherImage() {
        try {
            String imagePath;
            if (currentWeather == Weather.RAINY) {
                imagePath = "RES/CLOCK/rainy.png";
            } else {
                imagePath = "RES/CLOCK/sunny.png";
            }
            weatherImage = ImageIO.read(new File(imagePath));
        } catch (Exception e) {
            System.err.println("Error loading weather image: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    private void loadSeasonImage() {
        try {
            String imagePath;
            switch (currentSeason) {
                case SPRING:
                    imagePath = "RES/CLOCK/spring.png";
                    break;
                case SUMMER:
                    imagePath = "RES/CLOCK/summer.png";
                    break;
                case FALL:
                    imagePath = "RES/CLOCK/fall.png";
                    break;
                case WINTER:
                    imagePath = "RES/CLOCK/winter.png";
                    break;
                default:
                    imagePath = "RES/CLOCK/spring.png";
                    break;
            }
            seasonImage = ImageIO.read(new File(imagePath));
        } catch (Exception e) {
            System.err.println("Error loading season image: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    public void advanceGameTime() {
        
        if (isTimePaused) {
            return;
        }
        
        
        int minute = time.getMinute() + 5;
        int hour = time.getHour();
        if (minute >= 60) {
            minute -= 60;
            hour++;
        }        if (hour >= 24) {
            hour = 0;
            minute = 0;
            day++;
            dayOfWeek = (dayOfWeek + 1) % 7;
            
            
            if (gamePanel != null && gamePanel.getTileManager() != null) {
                int removedPlants = gamePanel.getTileManager().removeMismatchedSeasonPlants();
                if (removedPlants > 0) {
                    System.out.println("Natural time progression: Removed " + removedPlants + " plants due to season change.");
                }
            }
            
            
            Weather oldWeather = currentWeather;
            currentWeather = weatherManager.getNextDayWeather();
            
            
            if (oldWeather != currentWeather) {
                loadWeatherImage();
            }
            
            
            System.out.println("DEBUG: " + weatherManager.getCycleStatistics());
              if ((day - 1) % 10 == 0 && day != 1) {
                Season oldSeason = currentSeason;
                currentSeasonIndex = (currentSeasonIndex + 1) % seasons.length;
                
                Season[] seasonValues = Season.values();
                if (currentSeasonIndex < seasonValues.length) {
                    currentSeason = seasonValues[currentSeasonIndex];
                } else {
                    currentSeasonIndex = 0;
                    currentSeason = Season.SPRING;
                }
                
                
                if (oldSeason != currentSeason) {
                    loadSeasonImage();
                }
                
                
                weatherManager.forceNewCycle();
                System.out.println("DEBUG: New season " + currentSeason.getDisplayName() + " started - generated new weather cycle");
            }
            
            if (day > 30) {
                day = 1;
                month++;
                if (month > 12) {
                    month = 1;
                    
                }
                currentSeason = Season.SPRING;
                currentSeasonIndex = 0;
                currentWeather = Weather.SUNNY;
                
                
                loadWeatherImage();
                loadSeasonImage();
            }
        }
        
        
        int oldHour = time.getHour();
        time.setHour(hour);
        time.setMinute(minute);
        
        
        if (oldHour != hour && (hour == 3 || hour == 7 || hour == 15 || hour == 18)) {
            loadClockImage();
        }
    }
    
    /**
     * Check if it's time to update the clock (called every frame)
     * @return true if time was updated, false otherwise
     */
    public boolean updateIfNeeded() {
        
        if (System.currentTimeMillis() - lastTimeUpdate >= 1000) {
            advanceGameTime();
            lastTimeUpdate = System.currentTimeMillis();
            return true;
        }
        return false;
    }
    
    /**
     * Draw the time information on screen
     * @param g2 Graphics2D object to draw with
     * @param player Player object to get gold information
     */
    public void drawTimeInfo(Graphics2D g2, Player player) {
        String hari = dayNames[dayOfWeek];
        String tanggal = String.format("%02d", day);
        String musim = currentSeason.getDisplayName();
        int hour24 = time.getHour();
        int hour12 = hour24 % 12;
        if (hour12 == 0) hour12 = 12;
        String ampm = (hour24 < 12 || hour24 == 24) ? "AM" : "PM";
        String jam = String.format("%02d:%02d %s", hour12, time.getMinute(), ampm);
        String cuaca = currentWeather.getDisplayName();
        String goldText = String.valueOf(player.getGold());        
        if (clockImage != null) {
            int clockWidth = 128;
            int clockX = screenWidth - clockWidth - 10;
            int clockY = 10;
            g2.drawImage(clockImage, clockX, clockY, 128, 128, null); 
            
            
            if (weatherImage != null) {
                int weatherSize = 20; 
                int weatherX = clockX + 50; 
                int weatherY = clockY + 30; 
                g2.drawImage(weatherImage, weatherX, weatherY, weatherSize, weatherSize, null);
            }
            
            
            if (seasonImage != null) {
                int seasonSize = 20; 
                int seasonX = clockX + 95; 
                int seasonY = clockY + 30; 
                g2.drawImage(seasonImage, seasonX, seasonY, seasonSize, seasonSize, null);
            }
            
            
            int textX = clockX + (clockWidth / 2) - 15;
            int textY = clockY + (128 / 2) + 5; 
            g2.setColor(Color.BLACK);
            Font originalFont = g2.getFont();
            Font boldFont = originalFont.deriveFont(Font.BOLD, originalFont.getSize() + 2);
            g2.setFont(boldFont);
            g2.drawString(hari + ", " + tanggal, textX, textY - 43);
            g2.drawString(jam, textX, textY);
            
            
            g2.setColor(new Color(206, 82, 82)); 
            g2.setFont(originalFont.deriveFont(Font.BOLD, 14f)); 
            FontMetrics goldMetrics = g2.getFontMetrics();
            int goldTextX = clockX + clockWidth - goldMetrics.stringWidth(goldText) - 13; 
            int goldTextY = clockY + 103; 
            g2.drawString(goldText, goldTextX, goldTextY);
            
            g2.setFont(originalFont);
        } else {
            
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRoundRect(screenWidth - 120, 10, 110, 130, 15, 15); 
            g2.setColor(Color.BLACK);
            g2.drawString(hari + ", " + tanggal, screenWidth - 110, 30);
            g2.drawString(jam, screenWidth - 110, 55);
            g2.drawString(musim, screenWidth - 110, 80); 
            g2.drawString(cuaca, screenWidth - 110, 105); 
            g2.drawString(goldText, screenWidth - 110, 130); 
        }
    }
    
    /**
     * Pause the game time during fishing
     */
    public void pauseTime() {
        isTimePaused = true;
    }
    
    /**
     * Resume the game time after fishing
     */
    public void resumeTime() {
        isTimePaused = false;
    }
    
    /**
     * Add specific minutes to the game time
     * @param minutes Minutes to add
     */
    public void addGameTime(int minutes) {
        int currentMinute = time.getMinute() + minutes;
        int currentHour = time.getHour();
        
        if (currentMinute >= 60) {
            currentHour += currentMinute / 60;
            currentMinute = currentMinute % 60;
        }
        
        if (currentHour >= 24) {
            currentHour = currentHour % 24;
            
        }
        
        time.setHour(currentHour);
        time.setMinute(currentMinute);
    }    /**
     * Advance to next day (doesn't change time)
     */
    public void advanceToNextDay() {
        
        day++;
        dayOfWeek = (dayOfWeek + 1) % 7;
        
        
        Weather oldWeather = currentWeather;
        currentWeather = weatherManager.getNextDayWeather();
        
        
        if (oldWeather != currentWeather) {
            loadWeatherImage();
        }
        
        
        System.out.println("DEBUG: " + weatherManager.getCycleStatistics());
        
        
        if ((day - 1) % 10 == 0 && day != 1) {
            Season oldSeason = currentSeason;
            currentSeasonIndex = (currentSeasonIndex + 1) % seasons.length;
            
            Season[] seasonValues = Season.values();
            if (currentSeasonIndex < seasonValues.length) {
                currentSeason = seasonValues[currentSeasonIndex];
            } else {
                currentSeasonIndex = 0;
                currentSeason = Season.SPRING;
            }
            
            
            if (oldSeason != currentSeason) {
                loadSeasonImage();
            }
            
            
            weatherManager.forceNewCycle();
            System.out.println("DEBUG: New season " + currentSeason.getDisplayName() + " started - generated new weather cycle");
        }
        
        
        if (day > 30) {
            day = 1;
            month++;
            if (month > 12) {
                month = 1;
            }
            currentSeason = Season.SPRING;
            currentSeasonIndex = 0;
            currentWeather = Weather.SUNNY;
            
            
            loadWeatherImage();
            loadSeasonImage();
        }
        
        System.out.println("Advanced to next day: Day " + day);
    }
    
    
    /**
     * Get current time object
     * @return Current Time object
     */
    public Time getCurrentTime() {
        return time;
    }
    
    /**
     * Get current day
     * @return Current day
     */
    public int getCurrentDay() {
        return day;
    }
    
    /**
     * Get current season
     * @return Current Season
     */
    public Season getCurrentSeason() {
        return currentSeason;
    }
    
    /**
     * Get current weather
     * @return Current Weather
     */
    public Weather getCurrentWeather() {
        return currentWeather;
    }
    
    /**
     * Get current season name
     * @return Current season display name
     */
    public String getCurrentSeasonName() {
        return currentSeason.getDisplayName();
    }
    
    /**
     * Get current weather
     * @return Current weather
     */
    public Weather getWeather() {
        return currentWeather;
    }
    
    /**
     * Check if it's currently raining
     * @return true if rainy, false otherwise
     */
    public boolean isRainy() {
        return currentWeather == Weather.RAINY;
    }
    
    /**
     * Check if it's currently sunny
     * @return true if sunny, false otherwise
     */
    public boolean isSunny() {
        return currentWeather == Weather.SUNNY;
    }
    
    /**
     * Get weather as string
     * @return Weather display name
     */
    public String getWeatherString() {
        return currentWeather.getDisplayName();
    }
    
    /**
     * Check if time is currently paused
     * @return true if paused, false otherwise
     */
    public boolean isTimePaused() {
        return isTimePaused;
    }
    
    /**
     * Get current month
     * @return Current month
     */
    public int getCurrentMonth() {
        return month;
    }
      /**
     * Get current day of week
     * @return Day of week index (0-6)
     */
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    
    /**
     * Get day name for current day of week
     * @return Day name (Mon, Tue, etc.)
     */
    public String getCurrentDayName() {
        return dayNames[dayOfWeek];
    }
      /**
     * Force reload all images (clock, weather, season)
     * Useful when manually changing time/weather/season
     */
    public void reloadAllImages() {
        loadClockImage();
        loadWeatherImage();
        loadSeasonImage();
    }
    
    /**
     * Get weather manager instance
     * @return WeatherManager instance
     */
    public SRC.WEATHER.WeatherManager getWeatherManager() {
        return weatherManager;
    }
    
    /**
     * Set weather manually (for cheat system)
     * This bypasses the WeatherManager and sets weather directly
     * @param weather Weather to set
     */
    public void setWeather(Weather weather) {
        Weather oldWeather = currentWeather;
        currentWeather = weather;
        
        
        if (oldWeather != currentWeather) {
            loadWeatherImage();
        }
    }
    
    /**
     * Set season manually (for cheat system)
     * @param season Season to set
     */
    public void setSeason(Season season) {
        Season oldSeason = currentSeason;
        currentSeason = season;
        
        
        Season[] seasonValues = Season.values();
        for (int i = 0; i < seasonValues.length; i++) {
            if (seasonValues[i] == season) {
                currentSeasonIndex = i;
                break;
            }
        }
        
        
        if (oldSeason != currentSeason) {
            loadSeasonImage();
        }
        
        
        weatherManager.forceNewCycle();
        System.out.println("DEBUG: Season manually changed to " + season.getDisplayName() + " - generated new weather cycle");
    }
}
