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


public class EndGame {
    private Player player;
    private GamePanel gamePanel;
    private static final int GOLD_MILESTONE = 17209; 
    

    private int totalIncome;
    private int totalExpenditure;
    private int totalDaysPlayed;
    private int cropsHarvested;
    private int fishCaught;
    private int commonFishCaught;
    private int regularFishCaught;
    private int legendaryFishCaught;
    

    private double avgSeasonIncome;
    private double avgSeasonExpenditure;
    

    private List<NPCRelationshipData> npcRelationships;
      public EndGame(Player player, GamePanel gamePanel) {
        this.player = player;
        this.gamePanel = gamePanel;
        calculateStatistics();
        collectNPCData();
    }
    

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
      
    public boolean isMilestoneReached() {
        return isGoldMilestoneReached() && isMarriageMilestoneReached();
    }
    

    private boolean isGoldMilestoneReached() {
        return player.getGold() >= GOLD_MILESTONE;
    }
    

    private boolean isMarriageMilestoneReached() {
        return player.isMarried();
    }
    

    private void calculateStatistics() {

        if (player == null) {
            throw new IllegalStateException("Player cannot be null for EndGame statistics");
        }
        

        totalIncome = player.getTotalIncome();
        totalExpenditure = player.getTotalExpenditure();
        

        if (totalIncome < 0 || totalExpenditure < 0) {
            System.err.println("Warning: Invalid financial data detected");
            totalIncome = Math.max(0, totalIncome);
            totalExpenditure = Math.max(0, totalExpenditure);
        }
        

        totalDaysPlayed = player.getDaysPlayed();
        if (totalDaysPlayed <= 0) {
            totalDaysPlayed = 1; 
        }
        

        int totalSeasons = Math.max(1, (totalDaysPlayed / 28) + 1);
        avgSeasonIncome = (double) totalIncome / totalSeasons;
        avgSeasonExpenditure = (double) totalExpenditure / totalSeasons;
        

        cropsHarvested = player.getTotalCropsHarvested();
        if (cropsHarvested < 0) {
            cropsHarvested = 0;
        }
        

        calculateFishStatistics();
    }
    

    private void calculateFishStatistics() {
        fishCaught = player.getTotalFishCaught();
        commonFishCaught = 0;
        regularFishCaught = 0;
        legendaryFishCaught = 0;
        

        if (fishCaught < 0) {
            fishCaught = 0;
        }
        

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
    

    private void collectNPCsFromMap(Object houseMap, String location) {
        try {

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
    

    public int getTotalIncome() { 
        return totalIncome; 
    }
    
    public int getTotalExpenditure() { 
        return totalExpenditure; 
    }
    
    public double getAvgSeasonIncome() { 
        return Math.round(avgSeasonIncome * 100.0) / 100.0; 
    }
    
    public double getAvgSeasonExpenditure() { 
        return Math.round(avgSeasonExpenditure * 100.0) / 100.0; 
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

    public String getMilestoneReached() {
        if (isMarriageMilestoneReached() && isGoldMilestoneReached()) {
            return "Both Marriage and Gold Milestones Achieved!";
        } else {
            return "Milestone Requirements Not Met";
        }
    }
}