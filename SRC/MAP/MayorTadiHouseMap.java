package SRC.MAP;

import SRC.MAIN.GamePanel;

/**
 * Mayor Tadi's House Map - A 12x12 NPC house
 */
public class MayorTadiHouseMap extends NPCHouseMap {
    
    /**
     * Constructor for MayorTadiHouseMap
     * @param gp GamePanel reference
     */
    public MayorTadiHouseMap(GamePanel gp) {
        super(gp, "Mayor Tadi's House");
    }
    
    /**
     * Initialize the map with tiles from mayortadihousemap.txt
     */
    @Override
    protected void initializeMap() {
        initializeFromFile("mayortadihousemap.txt");
    }
    
    /**
     * Set up initial objects in Mayor Tadi's house
     */
    @Override
    public void setupInitialObjects() {
        setupObjectsFromFile("mayortadihousemap.txt");
    }
}
