package SRC.MAP.NPC_HOUSE;

import java.awt.Graphics2D;
import java.util.ArrayList;
import SRC.MAIN.GamePanel;
import SRC.ENTITY.PerryEntity;
import SRC.ENTITY.NPCEntity;


public class PerryHouseMap extends NPCHouseMap {
    
    private ArrayList<NPCEntity> npcs;

    public PerryHouseMap(GamePanel gp) {
        super(gp, "Perry's House");
        
        
        npcs = new ArrayList<>();
    }
    

    @Override
    protected void initializeMap() {
        initializeFromFile("perryhousemap.txt");
    }
    
 
    @Override
    public void setupInitialObjects() {
        setupObjectsFromFile("perryhousemap.txt");
    }
    

    @Override
    protected void setupObjectsFromFile(String mapFileName) {
        
        super.setupObjectsFromFile(mapFileName);
        
        
        npcs.clear();
        
        
        for (int row = 0; row < NPC_HOUSE_ROWS; row++) {
            for (int col = 0; col < NPC_HOUSE_COLS; col++) {
                
                char mapChar = super.getMapFileChar(col, row, "RES/MAP_TXT/" + mapFileName);
                  
                
                if (mapChar == 'n') {
                    
                    int worldX = col * gp.getTileSize();
                    int worldY = row * gp.getTileSize();
                    
                    
                    if (col < 2 || col > NPC_HOUSE_COLS - 3 || row < 2 || row > NPC_HOUSE_ROWS - 3) {
                        
                        worldX = (NPC_HOUSE_COLS / 2) * gp.getTileSize();
                        worldY = (NPC_HOUSE_ROWS / 2) * gp.getTileSize();
                        System.out.println("Moved Perry NPC from edge to center position");
                    }
                    
                    
                    PerryEntity perry = new PerryEntity(gp, worldX, worldY);
                    npcs.add(perry);
                    
                    System.out.println("Placed Perry NPC at position (" + col + "," + row + ") - worldX: " + worldX + ", worldY: " + worldY);
                }
            }
        }
    }
    

    @Override
    public void draw(Graphics2D g2) {
        
        super.draw(g2);
        
        
        for (NPCEntity npc : npcs) {
            npc.draw(g2);
        }
    }
    

    public void updateNPCs() {
        for (NPCEntity npc : npcs) {
            npc.update();
        }
    }
    

    public ArrayList<NPCEntity> getNPCs() {
        return npcs;
    }
    

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
