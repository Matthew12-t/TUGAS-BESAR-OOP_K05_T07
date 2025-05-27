package SRC.MAP;

import SRC.MAIN.GamePanel;

/**
 * Emily's House Map - A 12x12 NPC house
 */
public class EmilyHouseMap extends NPCHouseMap {
    
    /**
     * Constructor for EmilyHouseMap
     * @param gp GamePanel reference
     */
    public EmilyHouseMap(GamePanel gp) {
        super(gp, "Emily's House");
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
}
