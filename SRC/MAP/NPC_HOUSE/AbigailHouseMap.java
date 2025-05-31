package SRC.MAP.NPC_HOUSE;

import java.awt.Graphics2D;
import java.util.ArrayList;
import SRC.MAIN.GamePanel;
import SRC.ENTITY.AbigailEntity;
import SRC.ENTITY.NPCEntity;


public class AbigailHouseMap extends NPCHouseMap {
    

    private ArrayList<NPCEntity> npcs;
    

    public AbigailHouseMap(GamePanel gp) {
        super(gp, "Abigail's House");
        

        npcs = new ArrayList<>();
    }
    

    @Override
    protected void initializeMap() {
        initializeFromFile("abigailhousemap.txt");
    }

    @Override
    public void setupInitialObjects() {
        setupObjectsFromFile("abigailhousemap.txt");
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
                        System.out.println("Moved Abigail NPC from edge to center position");
                    }
                    
                    AbigailEntity abigail = new AbigailEntity(gp, worldX, worldY);
                    npcs.add(abigail);
                    
                    System.out.println("Placed Abigail NPC at position (" + col + "," + row + ") - worldX: " + worldX + ", worldY: " + worldY);
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
