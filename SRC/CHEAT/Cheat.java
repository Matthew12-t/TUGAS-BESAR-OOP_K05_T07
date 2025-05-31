package SRC.CHEAT;

import SRC.ENTITY.Player;
import SRC.INVENTORY.Inventory;
import SRC.DATA.GameData;
import SRC.UI.ClockUI;
import SRC.MAIN.GamePanel;
import SRC.ITEMS.Item;
import SRC.SEASON.Season;
import SRC.WEATHER.Weather;

public class Cheat {
    private Player player;
    private Inventory inventory;
    private GameData gameData;
    private ClockUI clockUI;
    private GamePanel gp;    
    
    public Cheat(Player player, Inventory inventory, ClockUI clockUI, GamePanel gp) {
        this.player = player;
        this.inventory = inventory;
        this.clockUI = clockUI;
        this.gp = gp;
    }
    
    public String executeCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            return "Empty command";
        }
        
        String[] parts = command.trim().toLowerCase().split("\\s+");
        
        try {
            if (parts[0].equals("add")) {
                if (parts.length < 3) {
                    return "Invalid add command format. Use:\n" +
                           "add gold {amount}\n" +
                           "add time {minutes}\n" +
                           "add item {name} {quantity}";
                }
                  switch (parts[1]) {                    case "gold":
                        if (parts.length != 3) return "Usage: add gold {amount}";
                        return addGold(parts);
                    case "time":
                        if (parts.length != 3) return "Usage: add time {minutes}";
                        return addTime(parts);
                    case "item":
                        if (parts.length < 4) return "Usage: add item {name} {quantity}";
                        return addItem(parts);
                    default:
                        return "Invalid add command. Valid options: gold, time, item";
                }            
            } 
            
            else if (parts[0].equals("goto")) {
                if (parts.length == 2 && parts[1].equals("endgame")) {
                    return gotoEndgame();
                } else if (parts.length != 3) {
                    return "Invalid goto command format. Use:\n" +
                           "goto endgame\n" +
                           "goto season {winter/spring/fall/summer}\n" +
                           "goto weather {rainy/sunny}";
                }
                
                switch (parts[1]) {
                    case "season":
                        return changeSeason(parts);
                    case "weather":
                        return changeWeather(parts);
                    default:
                        return "Invalid goto command. Valid options: endgame, season, weather";
                }
            } else {
                return "Invalid command. Valid commands start with: add, goto";
            }
        } catch (Exception e) {
            return "Error executing command: " + e.getMessage();
        }
    }    private String addItem(String[] parts) {
        try {
            int quantity = Integer.parseInt(parts[parts.length - 1]);
            
            StringBuilder itemName = new StringBuilder();
            for (int i = 2; i < parts.length - 1; i++) {
                if (i > 2) itemName.append(" ");
                String word = parts[i];
                if (word.length() > 0) {
                    word = word.substring(0, 1).toUpperCase() + word.substring(1);
                }
                itemName.append(word);
            }
            
            String itemNameStr = itemName.toString().trim();
            if (itemNameStr.isEmpty()) {
                return "Item name cannot be empty";
            }
            
            Item item = GameData.getItem(itemNameStr, quantity);
            if (item != null) {
                inventory.addItem(item, quantity);
                return "Added " + quantity + " " + itemNameStr + " to inventory";
            } else {
                return "Item '" + itemNameStr + "' not found";
            }
        } catch (NumberFormatException e) {
            return "Invalid quantity. Please enter a valid number.";
        }
    }
      private String addTime(String[] parts) {
        try {
            int minutes = Integer.parseInt(parts[2]);
            
            gp.addMinutes(minutes);
            
            SRC.TIME.Time currentTime = gp.getCurrentTime();
            int hour = currentTime.getHour();
            if (hour >= 2 && hour <= 5) {
                player.getPlayerAction().checkAutomaticSleep();
            }
            
            return "Added " + minutes + " minutes to game time";
        } catch (NumberFormatException e) {
            return "Invalid time. Please enter a valid number of minutes.";
        }
    }
    
    private String addGold(String[] parts) {
        try {
            int goldAmount = Integer.parseInt(parts[2]);
            player.setGold(player.getGold() + goldAmount); 
            return "Added " + goldAmount + " gold. Current gold: " + player.getGold();
        } catch (NumberFormatException e) {
            return "Invalid gold amount. Please enter a valid number.";
        }
    }
    
    private String changeSeason(String[] parts) {
        String season = parts[2].toLowerCase();
        Season newSeason;
        
        switch (season) {
            case "winter":
                newSeason = Season.WINTER;
                break;
            case "spring":
                newSeason = Season.SPRING;
                break;
            case "summer":
                newSeason = Season.SUMMER;
                break;
            case "fall":
            case "autumn":
                newSeason = Season.FALL;
                break;
            default:
                return "Invalid season. Use: winter, spring, summer, fall";
        }
        
        gp.setSeason(newSeason);
        return "Changed season to " + newSeason.getDisplayName();
    }
    
    private String changeWeather(String[] parts) {
        String weather = parts[2].toLowerCase();
        Weather newWeather;
        
        switch (weather) {
            case "sunny":
                newWeather = Weather.SUNNY;
                break;
            case "rainy":
                newWeather = Weather.RAINY;
                break;
            default:
                return "Invalid weather. Use: sunny, rainy";
        }
          gp.setWeather(newWeather);
        return "Changed weather to " + newWeather.getDisplayName();
    }
    
    private String gotoEndgame() {
        try {
            player.setGold(17209);
            
            player.setMarried(true);
            
            return "EndGame milestone achieved!\n" +
                   "Player gold set to: 17,209\n" +
                   "Player married to Emily: Yes\n" +
                   "EndGame UI should now trigger!";
        } catch (Exception e) {
            return "Error setting endgame conditions: " + e.getMessage();
        }
    }
}
