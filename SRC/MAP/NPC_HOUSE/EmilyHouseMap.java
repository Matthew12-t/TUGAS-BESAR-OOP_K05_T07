package SRC.MAP.NPC_HOUSE;

import java.awt.Graphics2D;
import java.util.ArrayList;
import SRC.MAIN.GamePanel;
import SRC.ENTITY.EmilyEntity;
import SRC.ENTITY.NPCEntity;

/**
 * Emily's House Map - A 12x12 NPC house
 */
public class EmilyHouseMap extends NPCHouseMap {
    
    // List to store NPCs in this house
    private ArrayList<NPCEntity> npcs;
    
    /**
     * Constructor for EmilyHouseMap
     * @param gp GamePanel reference
     */
    public EmilyHouseMap(GamePanel gp) {
        super(gp, "Emily's House");
        
        // Initialize NPC list
        npcs = new ArrayList<>();
    }
    
    /**
     * Initialize the map with tiles from emilyhousemap.txt
     */
    @Override
    protected void initializeMap() {
        initializeFromFile("emilyhousemap.txt");
    }
      /**
     * Set up initial objects in Emily's house
     */
    @Override
    public void setupInitialObjects() {
        setupObjectsFromFile("emilyhousemap.txt");
    }
    
    /**
     * Override setupObjectsFromFile to handle NPC placement (character 'n')
     * @param mapFileName The txt file to read objects from
     */
    @Override
    protected void setupObjectsFromFile(String mapFileName) {
        super.setupObjectsFromFile(mapFileName);
        
        npcs.clear();
        
        // Now handle NPC placement
        for (int row = 0; row < NPC_HOUSE_ROWS; row++) {
            for (int col = 0; col < NPC_HOUSE_COLS; col++) {
                char mapChar = super.getMapFileChar(col, row, "RES/MAP_TXT/" + mapFileName);
                  
                if (mapChar == 'n') {
                    int worldX = col * gp.getTileSize();
                    int worldY = row * gp.getTileSize();
                    if (col < 2 || col > NPC_HOUSE_COLS - 3 || row < 2 || row > NPC_HOUSE_ROWS - 3) {
                        worldX = (NPC_HOUSE_COLS / 2) * gp.getTileSize();
                        worldY = (NPC_HOUSE_ROWS / 2) * gp.getTileSize();
                        System.out.println("Moved Emily NPC from edge to center position");
                    }
                    EmilyEntity emily = new EmilyEntity(gp, worldX, worldY);
                    npcs.add(emily);
                    
                    System.out.println("Placed Emily NPC at position (" + col + "," + row + ") - worldX: " + worldX + ", worldY: " + worldY);
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
        int cameraX = gp.getCameraX();
        int cameraY = gp.getCameraY();
        int screenWidth = gp.getScreenWidth();
        int screenHeight = gp.getScreenHeight();
        
        for (NPCEntity npc : npcs) {
            if (npc.getWorldX() < cameraX || 
                npc.getWorldX() > cameraX + screenWidth ||
                npc.getWorldY() < cameraY || 
                npc.getWorldY() > cameraY + screenHeight) {
                
                System.out.println("Repositioning NPC " + npc.getNPCName() + " to be visible in camera view");
                
                int centerWorldX = cameraX + (screenWidth / 2) - (gp.getTileSize() / 2);
                int centerWorldY = cameraY + (screenHeight / 2) - (gp.getTileSize() / 2);
                
                npc.setWorldX(centerWorldX);
                npc.setWorldY(centerWorldY);
                
                System.out.println("Repositioned NPC to: worldX=" + centerWorldX + ", worldY=" + centerWorldY);
            }
        }
    }
}

