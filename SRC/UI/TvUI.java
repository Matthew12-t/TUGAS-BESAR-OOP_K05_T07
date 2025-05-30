package SRC.UI;

import SRC.MAIN.GamePanel;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TvUI {
    private GamePanel gamePanel;
    private boolean isWatching = false;
    private BufferedImage currentTvImage;
    private long watchStartTime;
    private static final int WATCH_DURATION_MS = 5000; 
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(255, 215, 0); 
    private static final Font MESSAGE_FONT = new Font("Arial", Font.PLAIN, 18);
    
    public TvUI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void startWatchingTV() {
        Season currentSeason = gamePanel.getSeason();
        Weather currentWeather = gamePanel.getWeather();
        
        loadTvImage(currentSeason, currentWeather);        
        isWatching = true;
        watchStartTime = System.currentTimeMillis();
        
        // Set game state to TV watching state to show TV overlay over the map
        gamePanel.setGameState(GamePanel.TV_STATE);
        
        System.out.println("DEBUG: Started watching TV - Season: " + currentSeason + ", Weather: " + currentWeather);
    }
      /**
     * Load TV image based on season and weather
     */
    private void loadTvImage(Season currentSeason, Weather currentWeather) {
        String seasonName = getSeasonFileName(currentSeason);
        String weatherName = getWeatherFileName(currentWeather);
        String imagePath = "RES/OBJECT/TV/tv_" + seasonName + weatherName + ".png";
        
        try {
            // Try loading from file system first (this works best in this project)
            currentTvImage = ImageIO.read(new File(imagePath));
            System.out.println("DEBUG: Loaded TV image: " + imagePath);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("TV image not found: " + imagePath + " - " + e.getMessage());
            // Load default TV image if specific one not found
            loadDefaultTvImage();
        }
    }
      /**
     * Load default TV image as fallback
     */
    private void loadDefaultTvImage() {
        try {
            // Try to load a default fallback image (using tv_polos as default)
            currentTvImage = ImageIO.read(new File("RES/OBJECT/TV/tv_polos.png"));
            if (currentTvImage == null) {
                // Try using one of the season images as fallback
                currentTvImage = ImageIO.read(new File("RES/OBJECT/TV/tv_springsun.png"));
            }
            System.out.println("DEBUG: Loaded fallback TV image");
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Could not load any TV images: " + e.getMessage());
            currentTvImage = null;
        }
    }
    
    /**
     * Convert Season enum to filename string
     */
    private String getSeasonFileName(Season season) {
        switch (season) {
            case SPRING:
                return "spring";
            case SUMMER:
                return "summer";
            case FALL:
                return "fall";
            case WINTER:
                return "winter";
            default:
                return "spring";
        }
    }
    
    /**
     * Convert Weather enum to filename string
     */
    private String getWeatherFileName(Weather weather) {
        switch (weather) {
            case SUNNY:
                return "sun";
            case RAINY:
                return "rain";
            default:
                return "sun";
        }
    }
    
    /**
     * Update TV UI - handle auto-close after duration
     */
    public void update() {
        if (isWatching) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - watchStartTime >= WATCH_DURATION_MS) {
                stopWatchingTV();
            }
        }
    }    /**
     * Draw TV watching screen
     */
    public void draw(Graphics2D g2) {
        if (!isWatching) {
            return;
        }
        
        int screenWidth = gamePanel.getScreenWidth();
        int screenHeight = gamePanel.getScreenHeight();
        
        // Calculate TV window size (smaller than full screen)
        int tvWindowWidth = Math.min(400, screenWidth * 2 / 3);
        int tvWindowHeight = Math.min(300, screenHeight * 2 / 3);
        int tvWindowX = (screenWidth - tvWindowWidth) / 2;
        int tvWindowY = (screenHeight - tvWindowHeight) / 2;
        
        // Draw TV window background with border
        g2.setColor(new Color(0, 0, 0, 200)); // Darker background for TV window
        g2.fillRoundRect(tvWindowX - 20, tvWindowY - 40, tvWindowWidth + 40, tvWindowHeight + 80, 15, 15);
        
        // Draw TV window border
        g2.setColor(new Color(100, 100, 100));
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(tvWindowX - 20, tvWindowY - 40, tvWindowWidth + 40, tvWindowHeight + 80, 15, 15);          // Draw TV content image if available (centered and scaled to fit TV window)
        if (currentTvImage != null) {
            // Calculate image scaling to fit within TV window
            int maxImageWidth = tvWindowWidth - 20;
            int maxImageHeight = tvWindowHeight - 60; // Leave space for info below
            
            // Calculate scale to fit image within TV window
            double scaleX = (double) maxImageWidth / currentTvImage.getWidth();
            double scaleY = (double) maxImageHeight / currentTvImage.getHeight();
            double scale = Math.min(scaleX, scaleY);
            
            int scaledWidth = (int) (currentTvImage.getWidth() * scale);
            int scaledHeight = (int) (currentTvImage.getHeight() * scale);
            int imageX = tvWindowX + (tvWindowWidth - scaledWidth) / 2;
            int imageY = tvWindowY + 10;
            
            // Draw the TV content image scaled and centered within TV window
            g2.drawImage(currentTvImage, imageX, imageY, scaledWidth, scaledHeight, null);
            
            // Draw TV info below the image within TV window
            drawTvInfo(g2, imageX, imageY + scaledHeight + 10, scaledWidth);
        } else {
            // Draw placeholder content within TV window
            int placeholderWidth = tvWindowWidth - 20;
            int placeholderHeight = tvWindowHeight - 60;
            int placeholderX = tvWindowX + 10;
            int placeholderY = tvWindowY + 10;
            
            // Draw placeholder background
            g2.setColor(new Color(100, 100, 200));
            g2.fillRect(placeholderX, placeholderY, placeholderWidth, placeholderHeight);
            
            // Draw placeholder text
            g2.setColor(TEXT_COLOR);
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            String placeholderText = "TV Program";
            FontMetrics metrics = g2.getFontMetrics();
            int textX = placeholderX + (placeholderWidth - metrics.stringWidth(placeholderText)) / 2;
            int textY = placeholderY + placeholderHeight / 2;
            g2.drawString(placeholderText, textX, textY);
            
            String noImageText = "Image Not Found";
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            metrics = g2.getFontMetrics();
            textX = placeholderX + (placeholderWidth - metrics.stringWidth(noImageText)) / 2;
            textY += 20;
            g2.drawString(noImageText, textX, textY);
            
            // Draw TV info below placeholder within TV window
            drawTvInfo(g2, placeholderX, placeholderY + placeholderHeight + 10, placeholderWidth);
        }
    }   
    private void drawTvInfo(Graphics2D g2, int x, int y, int width) {
        Season currentSeason = gamePanel.getSeason();
        Weather currentWeather = gamePanel.getWeather();
        
        g2.setFont(MESSAGE_FONT);
        g2.setColor(TEXT_COLOR);
        
        String programInfo = "current weather: " + getSeasonFileName(currentSeason) + 
                           " " + getWeatherFileName(currentWeather) +"ny.";
        FontMetrics metrics = g2.getFontMetrics();
        
        // Center the text within the given width
        int textWidth = metrics.stringWidth(programInfo);
        int centerX = x + width / 2;
        int textX = centerX - textWidth / 2;
        
        g2.drawString(programInfo, textX, y);        
        // Draw progress bar within TV window
        long currentTime = System.currentTimeMillis();
        float progress = Math.min(1.0f, (float)(currentTime - watchStartTime) / WATCH_DURATION_MS);
        
        int barWidth = Math.min(150, width - 20);
        int barHeight = 8;
        int barX = centerX - barWidth / 2;
        int barY = y + 20;
        
        // Progress bar background
        g2.setColor(new Color(100, 100, 100));
        g2.fillRect(barX, barY, barWidth, barHeight);
        
        // Progress bar fill
        g2.setColor(ACCENT_COLOR);
        g2.fillRect(barX, barY, (int)(barWidth * progress), barHeight);
    }
    
      /**
     * Stop watching TV
     */
    public void stopWatchingTV() {
        if (isWatching) {
            isWatching = false;
            currentTvImage = null;              
            gamePanel.addMinutes(15); 
            
            // Return to play state
            gamePanel.setGameState(GamePanel.PLAY_STATE);
            
            System.out.println("DEBUG: Stopped watching TV - 30 minutes passed");
        }
    }
    
    /**
     * Handle escape key press to stop watching
     */
    public void handleEscapePress() {
        if (isWatching) {
            stopWatchingTV();
        }
    }
    
    /**
     * Check if currently watching TV
     */
    public boolean isWatching() {
        return isWatching;
    }
    
    /**
     * Get TV program description based on season and weather
     */
    public String getTvProgramDescription(Season season, Weather weather) {
        String seasonName = getSeasonFileName(season);
        String weatherName = getWeatherFileName(weather);
        
        return "A " + seasonName + " " + weatherName + " themed program is currently playing. " +
               "Perfect for this " + seasonName + " " + weatherName + " day!";
    }
}
