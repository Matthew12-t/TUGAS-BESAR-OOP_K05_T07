package SRC.ENDGAME;

import SRC.ENTITY.Player;
import SRC.ENTITY.NPCEntity;
import SRC.ITEMS.Fish;
import SRC.MAIN.GamePanel;
import SRC.MAP.Map;
import SRC.MAP.NPC_HOUSE.AbigailHouseMap;
import SRC.MAP.NPC_HOUSE.CarolineHouseMap;
import SRC.MAP.NPC_HOUSE.DascoHouseMap;
import SRC.MAP.NPC_HOUSE.EmilyHouseMap;
import SRC.MAP.NPC_HOUSE.MayorTadiHouseMap;
import SRC.MAP.NPC_HOUSE.PerryHouseMap;
import SRC.MAP.StoreMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Class for handling end game statistics and milestone validation
 * Spakbor Hills has infinite gameplay but shows statistics when milestones are reached
 */
public class EndGame {
    private Player player;
    private GamePanel gamePanel;
    private static final int GOLD_MILESTONE = 17209; // 17,209g milestone
    
    // Calculated statistics
    private int totalIncome;
    private int totalExpenditure;
    private int totalDaysPlayed;
    private int cropsHarvested;
    private int fishCaught;
    private int commonFishCaught;
    private int regularFishCaught;
    private int legendaryFishCaught;
    
    // Per season averages
    private double avgSeasonIncome;
    private double avgSeasonExpenditure;
    
    // NPC relationship statistics
    private List<NPCRelationshipData> npcRelationships;
      public EndGame(Player player, GamePanel gamePanel) {
        this.player = player;
        this.gamePanel = gamePanel;
        calculateStatistics();
        collectNPCData();
    }
    
    /**
     * Inner class to store NPC relationship data
     */
    public static class NPCRelationshipData {
        private String npcName;
        private int heartPoints;
        private String relationshipStatus;
        private String location;
        
        public NPCRelationshipData(String npcName, int heartPoints, String relationshipStatus, String location) {
            this.npcName = npcName;
            this.heartPoints = heartPoints;
            this.relationshipStatus = relationshipStatus;
            this.location = location;
        }
        
        public String getNpcName() { return npcName; }
        public int getHeartPoints() { return heartPoints; }
        public String getRelationshipStatus() { return relationshipStatus; }
        public String getLocation() { return location; }
    }
      /**
     * Check if end game milestone has been reached
     * @return true if BOTH gold milestone (17,209g) AND marriage milestone are reached
     */
    public boolean isMilestoneReached() {
        return isGoldMilestoneReached() && isMarriageMilestoneReached();
    }
    
    /**
     * Check if gold milestone (17,209g) has been reached
     * @return true if player has >= 17,209 gold
     */
    private boolean isGoldMilestoneReached() {
        return player.getGold() >= GOLD_MILESTONE;
    }
    
    /**
     * Check if marriage milestone has been reached
     * @return true if player is married
     */
    private boolean isMarriageMilestoneReached() {
        return player.isMarried();
    }
    
    /**
     * Calculate all statistics with validation
     */
    private void calculateStatistics() {
        // Validate player data before calculating
        if (player == null) {
            throw new IllegalStateException("Player cannot be null for EndGame statistics");
        }
        
        // Calculate financial statistics
        totalIncome = player.getTotalIncome();
        totalExpenditure = player.getTotalExpenditure();
        
        // Validate financial data
        if (totalIncome < 0 || totalExpenditure < 0) {
            System.err.println("Warning: Invalid financial data detected");
            totalIncome = Math.max(0, totalIncome);
            totalExpenditure = Math.max(0, totalExpenditure);
        }
        
        // Calculate time-based statistics
        totalDaysPlayed = player.getDaysPlayed();
        if (totalDaysPlayed <= 0) {
            totalDaysPlayed = 1; // Minimum 1 day to avoid division by zero
        }
        
        // Calculate season averages (28 days per season)
        int totalSeasons = Math.max(1, (totalDaysPlayed / 28) + 1);
        avgSeasonIncome = (double) totalIncome / totalSeasons;
        avgSeasonExpenditure = (double) totalExpenditure / totalSeasons;
        
        // Calculate farming statistics
        cropsHarvested = player.getTotalCropsHarvested();
        if (cropsHarvested < 0) {
            cropsHarvested = 0;
        }
        
        // Calculate fishing statistics
        calculateFishStatistics();
    }
    
    /**
     * Calculate fishing statistics with validation
     */
    private void calculateFishStatistics() {
        fishCaught = player.getTotalFishCaught();
        commonFishCaught = 0;
        regularFishCaught = 0;
        legendaryFishCaught = 0;
        
        // Validate fish count
        if (fishCaught < 0) {
            fishCaught = 0;
        }
        
        // Count fish by rarity
        List<Fish> caughtFish = player.getCaughtFish();
        if (caughtFish != null) {
            for (Fish fish : caughtFish) {
                if (fish != null) {
                    String fishType = fish.getType().toLowerCase();
                    switch (fishType) {
                        case "common":
                            commonFishCaught++;
                            break;
                        case "regular":
                            regularFishCaught++;
                            break;
                        case "legendary":
                            legendaryFishCaught++;
                            break;
                        default:
                            // Unknown type counts as common
                            commonFishCaught++;
                            break;
                    }
                }
            }
        }
        
        // Validate fish counts add up correctly
        int calculatedTotal = commonFishCaught + regularFishCaught + legendaryFishCaught;
        if (calculatedTotal != fishCaught && caughtFish != null) {
            System.err.println("Warning: Fish count mismatch. Using calculated values.");
            fishCaught = calculatedTotal;        }
    }
    
    /**
     * Collect NPC relationship data from all maps
     */
    private void collectNPCData() {
        npcRelationships = new ArrayList<>();
        
        // Collect NPCs from all house maps
        collectNPCsFromMap((AbigailHouseMap) gamePanel.getAbigailHouseMap(), "Abigail's House");
        collectNPCsFromMap((CarolineHouseMap) gamePanel.getCarolineHouseMap(), "Caroline's House");
        collectNPCsFromMap((DascoHouseMap) gamePanel.getDascoHouseMap(), "Dasco's House");
        collectNPCsFromMap((EmilyHouseMap) gamePanel.getEmilyHouseMap(), "Emily's House");
        collectNPCsFromMap((MayorTadiHouseMap) gamePanel.getMayorTadiHouseMap(), "Mayor Tadi's House");
        collectNPCsFromMap((PerryHouseMap) gamePanel.getPerryHouseMap(), "Perry's House");
        
        // Collect NPCs from store map
        collectNPCsFromStoreMap((StoreMap) gamePanel.getStoreMap());
    }
    
    /**
     * Collect NPCs from a specific house map
     */
    private void collectNPCsFromMap(Object houseMap, String location) {
        try {
            // Use reflection to get NPCs list since all house maps have getNPCs() method
            java.lang.reflect.Method getNPCsMethod = houseMap.getClass().getMethod("getNPCs");
            @SuppressWarnings("unchecked")
            List<NPCEntity> npcs = (List<NPCEntity>) getNPCsMethod.invoke(houseMap);
            
            if (npcs != null) {
                for (NPCEntity npc : npcs) {
                    if (npc != null) {
                        String relationshipStatus = getRelationshipStatus(npc.getHeartPoints());
                        npcRelationships.add(new NPCRelationshipData(
                            npc.getNPCName(),
                            npc.getHeartPoints(),
                            relationshipStatus,
                            location
                        ));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error collecting NPCs from " + location + ": " + e.getMessage());
        }
    }
    
    /**
     * Collect NPCs from store map
     */
    private void collectNPCsFromStoreMap(StoreMap storeMap) {
        try {
            List<NPCEntity> npcs = storeMap.getNPCs();
            if (npcs != null) {
                for (NPCEntity npc : npcs) {
                    if (npc != null) {
                        String relationshipStatus = getRelationshipStatus(npc.getHeartPoints());
                        npcRelationships.add(new NPCRelationshipData(
                            npc.getNPCName(),
                            npc.getHeartPoints(),
                            relationshipStatus,
                            "Store"
                        ));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error collecting NPCs from Store: " + e.getMessage());
        }
    }
    
    /**
     * Convert heart points to relationship status
     */
    private String getRelationshipStatus(int heartPoints) {
        if (heartPoints >= 150) {
            return "Spouse";
        } else if (heartPoints >= 120) {
            return "Fiance";
        } else if (heartPoints >= 100) {
            return "Close Friend";
        } else if (heartPoints >= 60) {
            return "Good Friend";
        } else if (heartPoints >= 30) {
            return "Friend";
        } else if (heartPoints >= 10) {
            return "Acquaintance";
        } else {
            return "Stranger";
        }
    }
    
    // Getters for statistics (all validated)
    public int getTotalIncome() { 
        return totalIncome; 
    }
    
    public int getTotalExpenditure() { 
        return totalExpenditure; 
    }
    
    public double getAvgSeasonIncome() { 
        return Math.round(avgSeasonIncome * 100.0) / 100.0; // Round to 2 decimal places
    }
    
    public double getAvgSeasonExpenditure() { 
        return Math.round(avgSeasonExpenditure * 100.0) / 100.0; // Round to 2 decimal places
    }
    
    public int getTotalDaysPlayed() { 
        return totalDaysPlayed; 
    }
    
    public int getCropsHarvested() { 
        return cropsHarvested; 
    }
    
    public int getFishCaught() { 
        return fishCaught; 
    }
    
    public int getCommonFishCaught() { 
        return commonFishCaught; 
    }
    
    public int getRegularFishCaught() { 
        return regularFishCaught; 
    }
    
    public int getLegendaryFishCaught() { 
        return legendaryFishCaught; 
    }
    
    public List<NPCRelationshipData> getNpcRelationships() {
        return npcRelationships;
    }
    
    public Player getPlayer() { 
        return player;
    }
      /**
     * Get which milestone was reached (for UI display)
     * @return description of the milestone reached
     */
    public String getMilestoneReached() {
        if (isMarriageMilestoneReached() && isGoldMilestoneReached()) {
            return "Both Marriage and Gold Milestones Achieved!";
        } else {
            return "Milestone Requirements Not Met";
        }
    }
}