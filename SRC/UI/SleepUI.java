package SRC.UI;

import SRC.MAIN.GamePanel;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class SleepUI {
    
    
    public enum SleepTrigger {
        MANUAL,     
        LOW_ENERGY, 
        LATE_TIME   
    }
    
    public static class SleepResult {
        private String message;
        private String backgroundImage;
        private int income;
        private int day;
        private Season season;
        private Weather weather;
        private SleepTrigger trigger;
        
        public SleepResult(String message, String backgroundImage, int income, 
                          int day, Season season, Weather weather, SleepTrigger trigger) {
            this.message = message;
            this.backgroundImage = backgroundImage;
            this.income = income;
            this.day = day;
            this.season = season;
            this.weather = weather;
            this.trigger = trigger;
        }
        
        
        public String getMessage() { return message; }
        public String getBackgroundImage() { return backgroundImage; }
        public int getIncome() { return income; }
        public int getDay() { return day; }
        public Season getSeason() { return season; }
        public Weather getWeather() { return weather; }
        public SleepTrigger getTrigger() { return trigger; }
        
        
        public void setMessage(String message) { this.message = message; }
        public void setBackgroundImage(String backgroundImage) { this.backgroundImage = backgroundImage; }
        public void setIncome(int income) { this.income = income; }
        public void setDay(int day) { this.day = day; }
        public void setSeason(Season season) { this.season = season; }
        public void setWeather(Weather weather) { this.weather = weather; }
        public void setTrigger(SleepTrigger trigger) { this.trigger = trigger; }
    }
    
    
    private static final Map<SleepTrigger, String[]> SLEEP_MESSAGES = new HashMap<>();
    
    
    private static final Map<SleepTrigger, String> SLEEP_BACKGROUNDS = new HashMap<>();
    
    static {
        
        SLEEP_MESSAGES.put(SleepTrigger.MANUAL, new String[] {
            "You had a good night's rest!",
            "You slept peacefully through the night.",
            "A restful sleep has restored your energy!",
            "Sweet dreams filled your night.",
            "You wake up feeling refreshed and ready for a new day!"
        });
        
        SLEEP_MESSAGES.put(SleepTrigger.LOW_ENERGY, new String[] {
            "You collapsed from exhaustion...",
            "Your body couldn't take it anymore and you fell asleep.",
            "Fatigue overwhelmed you and you passed out.",
            "You were too tired to continue and fell into a deep sleep.",
            "Exhaustion took over and you slept wherever you were."
        });
        
        SLEEP_MESSAGES.put(SleepTrigger.LATE_TIME, new String[] {
            "The late hour caught up with you...",
            "You couldn't fight the sleepiness any longer.",
            "The night was too deep and sleep took you.",
            "Your eyelids grew heavy and you drifted off to sleep.",
            "The late night finally claimed you and you fell asleep."
        });
        
        
        SLEEP_BACKGROUNDS.put(SleepTrigger.MANUAL, "RES/SLEEP/sleeping_Background.png");
        SLEEP_BACKGROUNDS.put(SleepTrigger.LOW_ENERGY, "RES/SLEEP/sleeping_Background.png");
        SLEEP_BACKGROUNDS.put(SleepTrigger.LATE_TIME, "RES/SLEEP/sleeping_Background.png");
    }
    
    private GamePanel gamePanel;
    private SleepResult currentSleepResult;
    private boolean isDisplaying;
    private BufferedImage backgroundImage;
    private long displayStartTime;
    
    
    private static final Color OVERLAY_COLOR = new Color(0, 0, 0, 150);
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color ACCENT_COLOR = new Color(255, 215, 0); 
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font MESSAGE_FONT = new Font("Arial", Font.PLAIN, 18);
    private static final Font INFO_FONT = new Font("Arial", Font.PLAIN, 16);
    
    public SleepUI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.isDisplaying = false;
    }
    
    /**
     * Show sleep result screen
     */
    public void showSleepResult(SleepResult sleepResult) {
        this.currentSleepResult = sleepResult;
        this.isDisplaying = true;
        this.displayStartTime = System.currentTimeMillis();
        
        
        loadBackgroundImage(sleepResult.getBackgroundImage());
        
        System.out.println("Displaying sleep result: " + sleepResult.getTrigger());
    }
      /**
     * Load background image for sleep screen
     */
    private void loadBackgroundImage(String imagePath) {
        try {
            
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                backgroundImage = ImageIO.read(imageFile);
                System.out.println("DEBUG: Sleep background image loaded from file: " + imagePath);
            } else {
                
                backgroundImage = ImageIO.read(getClass().getResourceAsStream(imagePath));
                if (backgroundImage == null) {
                    
                    backgroundImage = ImageIO.read(new File("RES/SLEEP/sleeping_Background.png"));
                    System.out.println("DEBUG: Using fallback sleep background: RES/SLEEP/sleeping_Background.png");
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Could not load sleep background image: " + imagePath + " - Error: " + e.getMessage());
            
            backgroundImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = backgroundImage.createGraphics();
            g.setColor(new Color(25, 25, 50)); 
            g.fillRect(0, 0, 800, 600);
            g.dispose();
        }
    }
      
    public void update() {
    }
    
    public void handleEnterPress() {
        if (isDisplaying) {
            isDisplaying = false;
            currentSleepResult = null;
            
            gamePanel.setGameState(GamePanel.PLAY_STATE);
            System.out.println("Sleep screen closed by Enter key");
        }
    }
    
    /**
     * Draw sleep result screen
     */
    public void draw(Graphics2D g2) {
        if (!isDisplaying || currentSleepResult == null) {
            return;
        }
        
        int screenWidth = gamePanel.getScreenWidth();
        int screenHeight = gamePanel.getScreenHeight();
        
        
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
        }
        
        
        g2.setColor(OVERLAY_COLOR);
        g2.fillRect(0, 0, screenWidth, screenHeight);
        
        
        int centerX = screenWidth / 2;
        int startY = screenHeight / 4;
        int lineHeight = 30;
        
        
        g2.setFont(TITLE_FONT);
        g2.setColor(ACCENT_COLOR);
        FontMetrics titleMetrics = g2.getFontMetrics();
        
        String title = getTitleForTrigger(currentSleepResult.getTrigger());
        int titleX = centerX - titleMetrics.stringWidth(title) / 2;
        g2.drawString(title, titleX, startY);
        
        
        g2.setFont(MESSAGE_FONT);
        g2.setColor(TEXT_COLOR);
        FontMetrics messageMetrics = g2.getFontMetrics();
        
        String message = currentSleepResult.getMessage();
        String[] messageLines = wrapText(message, messageMetrics, screenWidth - 100);
        
        int messageY = startY + 60;
        for (String line : messageLines) {
            int lineX = centerX - messageMetrics.stringWidth(line) / 2;
            g2.drawString(line, lineX, messageY);
            messageY += lineHeight;
        }
          
        drawInfoBox(g2, centerX, messageY + 40);
        
        
        drawEnterHint(g2, screenWidth, screenHeight);
    }
      /**
     * Get title text based on sleep trigger
     */
    private String getTitleForTrigger(SleepTrigger trigger) {
        switch (trigger) {
            case MANUAL:
                return "Good Night's Sleep";
            case LOW_ENERGY:
                return "Exhaustion Sleep";
            case LATE_TIME:
                return "Late Night Sleep";
            default:
                return "Sleep";
        }
    }
    
    /**
     * Draw information box with game stats
     */
    private void drawInfoBox(Graphics2D g2, int centerX, int startY) {
        int boxWidth = 400;
        int boxHeight = 120;
        int boxX = centerX - boxWidth / 2;
        
        
        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(boxX, startY, boxWidth, boxHeight, 10, 10);
        
        
        g2.setColor(ACCENT_COLOR);
        g2.drawRoundRect(boxX, startY, boxWidth, boxHeight, 10, 10);
        
        
        g2.setFont(INFO_FONT);
        g2.setColor(TEXT_COLOR);
        
        int textX = boxX + 20;
        int textY = startY + 25;
        int lineSpacing = 20;
        
        g2.drawString("Day: " + currentSleepResult.getDay(), textX, textY);
        g2.drawString("Season: " + currentSleepResult.getSeason().toString(), textX, textY + lineSpacing);
        g2.drawString("Weather: " + currentSleepResult.getWeather().toString(), textX, textY + lineSpacing * 2);
          
        g2.setColor(ACCENT_COLOR);
        g2.drawString("Shipping Income: " + currentSleepResult.getIncome() + " coins", textX, textY + lineSpacing * 3);
    }
      /**
     * Draw Enter key hint
     */
    private void drawEnterHint(Graphics2D g2, int screenWidth, int screenHeight) {
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(ACCENT_COLOR);
        String hint = "Press Enter to continue...";
        FontMetrics hintMetrics = g2.getFontMetrics();
        int hintX = (screenWidth - hintMetrics.stringWidth(hint)) / 2;
        int hintY = screenHeight - 30;
        g2.drawString(hint, hintX, hintY);
    }
    
    /**
     * Wrap text to fit within specified width
     */
    private String[] wrapText(String text, FontMetrics metrics, int maxWidth) {
        String[] words = text.split(" ");
        java.util.List<String> lines = new java.util.ArrayList<>();
        String currentLine = "";
        
        for (String word : words) {
            String testLine = currentLine.isEmpty() ? word : currentLine + " " + word;
            if (metrics.stringWidth(testLine) <= maxWidth) {
                currentLine = testLine;
            } else {
                if (!currentLine.isEmpty()) {
                    lines.add(currentLine);
                }
                currentLine = word;
            }
        }
        
        if (!currentLine.isEmpty()) {
            lines.add(currentLine);
        }
        
        return lines.toArray(new String[0]);
    }
      /**
     * Check if sleep UI is currently displaying
     */
    public boolean isDisplaying() {
        return isDisplaying;
    }
    
    /**
     * Force close sleep UI
     */
    public void close() {
        isDisplaying = false;
        currentSleepResult = null;
    }
    
    
    
    /**
     * Get random sleep message based on trigger type
     */
    public static String getSleepMessage(SleepTrigger trigger) {
        String[] messages = SLEEP_MESSAGES.get(trigger);
        if (messages == null || messages.length == 0) {
            return "You slept through the night.";
        }
        return messages[(int)(Math.random() * messages.length)];
    }
    
    /**
     * Get background image for sleep trigger
     */
    public static String getSleepBackground(SleepTrigger trigger) {
        return SLEEP_BACKGROUNDS.getOrDefault(trigger, "/RES/UI/sleep_default.png");
    }
    
    /**
     * Calculate daily income based on game progress
     */
    public static int calculateDailyIncome(int day, Season season) {
        
        int baseIncome = 100;
        int dayMultiplier = day / 7; 
        int seasonBonus = 0;
        
        
        switch (season) {
            case SPRING:
                seasonBonus = 50; 
                break;
            case SUMMER:
                seasonBonus = 75; 
                break;
            case FALL:
                seasonBonus = 100; 
                break;
            case WINTER:
                seasonBonus = 25; 
                break;
            case ANY:
                seasonBonus = 60; 
                break;
        }
        
        return baseIncome + (dayMultiplier * 25) + seasonBonus + (int)(Math.random() * 50);
    }
      /**
     * Calculate total income including shipping bin earnings
     */
    public static int calculateTotalIncome(int day, Season season, int shippingBinValue) {
        
        return shippingBinValue;
    }
      /**
     * Create a complete sleep result
     */
    public static SleepResult createSleepResult(SleepTrigger trigger, int day, 
                                              Season season, Weather weather) {
        String message = getSleepMessage(trigger);
        String background = getSleepBackground(trigger);
        int income = 0; 
        
        return new SleepResult(message, background, income, day, season, weather, trigger);
    }
    
    /**
     * Create a complete sleep result with shipping bin income
     */
    public static SleepResult createSleepResultWithShipping(SleepTrigger trigger, int day, 
                                                          Season season, Weather weather, 
                                                          int shippingBinValue) {
        String message = getSleepMessage(trigger);
        String background = getSleepBackground(trigger);
        int totalIncome = calculateTotalIncome(day, season, shippingBinValue);
        
        return new SleepResult(message, background, totalIncome, day, season, weather, trigger);
    }
    
    /**
     * Format complete sleep information for display
     */
    public static String formatSleepInfo(SleepResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append(result.getMessage()).append("\n\n");
        sb.append("Day: ").append(result.getDay()).append("\n");
        sb.append("Season: ").append(result.getSeason().toString()).append("\n");
        sb.append("Weather: ").append(result.getWeather().toString()).append("\n");
        sb.append("Daily Income: ").append(result.getIncome()).append(" coins\n");
        
        return sb.toString();
    }
}
