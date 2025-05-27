package SRC.MAP;

import SRC.MAIN.GamePanel;

/**
 * Caroline's House Map - A 12x12 NPC house
 */
public class CarolineHouseMap extends NPCHouseMap {
    
    /**
     * Constructor for CarolineHouseMap
     * @param gp GamePanel reference
     */
    public CarolineHouseMap(GamePanel gp) {
        super(gp, "Caroline's House");
    }
    
    /**
     * Initialize the map with tiles from carolinehousemap.txt
     */
    @Override
    protected void initializeMap() {
        initializeFromFile("carolinehousemap.txt");
    }
    
    /**
     * Set up initial objects in Caroline's house
     */
    @Override
    public void setupInitialObjects() {
        setupObjectsFromFile("carolinehousemap.txt");
    }
}
