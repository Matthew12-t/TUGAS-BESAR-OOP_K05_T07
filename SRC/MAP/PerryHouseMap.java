package SRC.MAP;

import SRC.MAIN.GamePanel;

/**
 * Perry's House Map - A 12x12 NPC house
 */
public class PerryHouseMap extends NPCHouseMap {
    
    /**
     * Constructor for PerryHouseMap
     * @param gp GamePanel reference
     */
    public PerryHouseMap(GamePanel gp) {
        super(gp, "Perry's House");
    }
    
    /**
     * Initialize the map with tiles from perryhousemap.txt
     */
    @Override
    protected void initializeMap() {
        initializeFromFile("perryhousemap.txt");
    }
    
    /**
     * Set up initial objects in Perry's house
     */
    @Override
    public void setupInitialObjects() {
        setupObjectsFromFile("perryhousemap.txt");
    }
}
