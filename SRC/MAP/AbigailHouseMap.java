package SRC.MAP;

import SRC.MAIN.GamePanel;

/**
 * Abigail's House Map - A 12x12 NPC house
 */
public class AbigailHouseMap extends NPCHouseMap {
    
    /**
     * Constructor for AbigailHouseMap
     * @param gp GamePanel reference
     */
    public AbigailHouseMap(GamePanel gp) {
        super(gp, "Abigail's House");
    }
    
    /**
     * Initialize the map with tiles from abigailhousemap.txt
     */
    @Override
    protected void initializeMap() {
        initializeFromFile("abigailhousemap.txt");
    }
    
    /**
     * Set up initial objects in Abigail's house
     */
    @Override
    public void setupInitialObjects() {
        setupObjectsFromFile("abigailhousemap.txt");
    }
}
