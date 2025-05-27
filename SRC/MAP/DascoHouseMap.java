package SRC.MAP;

import SRC.MAIN.GamePanel;

/**
 * Dasco's House Map - A 12x12 NPC house
 */
public class DascoHouseMap extends NPCHouseMap {
    
    /**
     * Constructor for DascoHouseMap
     * @param gp GamePanel reference
     */
    public DascoHouseMap(GamePanel gp) {
        super(gp, "Dasco's House");
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
}
