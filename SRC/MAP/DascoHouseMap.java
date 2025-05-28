package SRC.MAP;

import java.awt.Graphics2D;
import java.util.ArrayList;
import SRC.MAIN.GamePanel;
import SRC.ENTITY.DascoEntity;
import SRC.ENTITY.NPCEntity;

/**
 * Dasco's House Map - A 12x12 NPC house
 */
public class DascoHouseMap extends NPCHouseMap {
    
    // List to store NPCs in this house
    private ArrayList<NPCEntity> npcs;
    
    /**
     * Constructor for DascoHouseMap
     * @param gp GamePanel reference
     */
    public DascoHouseMap(GamePanel gp) {
        super(gp, "Dasco's House");
        
        // Initialize NPC list
        npcs = new ArrayList<>();
    }
    
    /**
     * Initialize the map with tiles from dascohousemap.txt
     */
    @Override
    protected void initializeMap() {
        initializeFromFile("dascohousemap.txt");
    }
    
    /**
     * Set up initial objects in Dasco's house
     */
    @Override
    public void setupInitialObjects() {
        setupObjectsFromFile("dascohousemap.txt");
    }
    
    /**
     * Override setupObjectsFromFile to handle NPC placement (character 'n')
     * @param mapFileName The txt file to read objects from
     */
    @Override
    protected void setupObjectsFromFile(String mapFileName) {
        // First, call parent method to handle regular objects
        super.setupObjectsFromFile(mapFileName);
        
        // Clear existing NPCs
        npcs.clear();
        
        // Now handle NPC placement
        for (int row = 0; row < NPC_HOUSE_ROWS; row++) {
            for (int col = 0; col < NPC_HOUSE_COLS; col++) {
                // Get character from the original file
                char mapChar = super.getMapFileChar(col, row, "RES/MAP_TXT/" + mapFileName);
                  
                // Check for NPC placement
                if (mapChar == 'n') {
                    // Calculate world coordinates for the NPC
                    int worldX = col * gp.getTileSize();
                    int worldY = row * gp.getTileSize();
                    
                    // Make sure it's in a visible area (middle of the map)
                    if (col < 2 || col > NPC_HOUSE_COLS - 3 || row < 2 || row > NPC_HOUSE_ROWS - 3) {
                        // If NPC is positioned at the edge, move it to a more central position
                        worldX = (NPC_HOUSE_COLS / 2) * gp.getTileSize();
                        worldY = (NPC_HOUSE_ROWS / 2) * gp.getTileSize();
                        System.out.println("Moved Dasco NPC from edge to center position");
                    }
                    
                    // Create and add Dasco NPC
                    DascoEntity dasco = new DascoEntity(gp, worldX, worldY);
                    npcs.add(dasco);
                    
                    System.out.println("Placed Dasco NPC at position (" + col + "," + row + ") - worldX: " + worldX + ", worldY: " + worldY);
                }
            }
        }
    }
    
    /**
     * Override draw method to render NPCs in addition to tiles and objects
     * @param g2 Graphics2D object for drawing
     */
    @Override
    public void draw(Graphics2D g2) {
        // Call parent draw method to render tiles and objects
        super.draw(g2);
        
        // Draw NPCs on top
        for (NPCEntity npc : npcs) {
            npc.draw(g2);
        }
    }
    
    /**
     * Update NPCs behavior
     */
    public void updateNPCs() {
        for (NPCEntity npc : npcs) {
            npc.update();
        }
    }
    
    /**
     * Get the list of NPCs in this house
     * @return ArrayList of NPCs
     */
    public ArrayList<NPCEntity> getNPCs() {
        return npcs;
    }
    
    /**
     * Ensure NPCs are placed in visible camera area
     * Call this after initializing NPCs and centering camera
     */
    public void ensureNPCsVisible() {
        // Get the camera bounds
        int cameraX = gp.getCameraX();
        int cameraY = gp.getCameraY();
        int screenWidth = gp.getScreenWidth();
        int screenHeight = gp.getScreenHeight();
        
        // Check each NPC
        for (NPCEntity npc : npcs) {
            // Check if NPC is outside the camera view
            if (npc.getWorldX() < cameraX || 
                npc.getWorldX() > cameraX + screenWidth ||
                npc.getWorldY() < cameraY || 
                npc.getWorldY() > cameraY + screenHeight) {
                
                System.out.println("Repositioning NPC " + npc.getNPCName() + " to be visible in camera view");
                
                // Place NPC in the center of visible area
                int centerWorldX = cameraX + (screenWidth / 2) - (gp.getTileSize() / 2);
                int centerWorldY = cameraY + (screenHeight / 2) - (gp.getTileSize() / 2);
                
                // Move the NPC to a visible position
                npc.setWorldX(centerWorldX);
                npc.setWorldY(centerWorldY);
                
                System.out.println("Repositioned NPC to: worldX=" + centerWorldX + ", worldY=" + centerWorldY);
            }
        }
    }
}
