package SRC.MAP;
import java.util.ArrayList;
import SRC.ENTITY.NPCEntity;
import SRC.MAIN.GamePanel;
import SRC.OBJECT.ObjectDeployer;
import SRC.TILES.Tile;

public class StoreMap extends Map {    // Size of the store map
    public static final int STORE_COLS = 12;
    public static final int STORE_ROWS = 9;

    // Object deployer for placing objects on the map
    private ObjectDeployer objDeployer;

    // List to store NPCs in the store
    private ArrayList<NPCEntity> npcs;

    /**
     * Constructor for the StoreMap
     * @param gp GamePanel reference
     */
    public StoreMap(GamePanel gp) {
        // Store map allows up to 100 objects
        super(gp, "Store Map", STORE_COLS, STORE_ROWS, 100);

        // Initialize object deployer
        objDeployer = new ObjectDeployer(gp);
        // InitiaIze NPC list
        npcs = new ArrayList<>();
    }

    /**
     * Get the list of NPCs in the store
     * @return ArrayList of NPCEntity
     */
    public ArrayList<NPCEntity> getNPCs() {
        return npcs;
    }

    /**
     * Set the list of NPCs in the store
     * @param npcs ArrayList of NPCEntity
     */
    public void setNPCs(ArrayList<NPCEntity> npcs) {
        this.npcs = npcs;
    }

    /**
     * Add a single NPC to the store
     */
    public void addNPC(NPCEntity npc) {
        this.npcs.add(npc);
    }

    /**
     * Initialize the store map with various types of tiles from store.txt
     */
    @Override
    protected void initializeMap() {
        // Don't call super.initializeMap() to avoid grass default
        
        // Initialize map tile data array
        // This is normally done in the parent constructor, but we need to ensure it's clean
        
        // Load map from file
        boolean loadSuccess = loadMapFromFile("RES/MAP_TXT/store.txt");
        
        if (loadSuccess) {
            System.out.println("Setting up tiles from store.txt...");
            
            // Process tiles based on the file content
            for (int row = 0; row < STORE_ROWS; row++) {
                for (int col = 0; col < STORE_COLS; col++) {
                    // Get character from the original file
                    char mapChar = super.getMapFileChar(col, row, "RES/MAP_TXT/store.txt");                    
                    
                    // Debug print
                    System.out.println("Position (" + col + "," + row + "): char = '" + mapChar + "'");
                    
                    // Process based on character
                    switch (mapChar) {
                        case 'w':
                            setTileInMap(col, row, Tile.TILE_WALL1);
                            System.out.println("Set WALL at (" + col + "," + row + ")");
                            break;
                        case '0':
                            setTileInMap(col, row, Tile.TILE_FLOOR);
                            System.out.println("Set FLOOR at (" + col + "," + row + ")");
                            break;
                        case 'c':
                        case 'd':
                        case 's':
                        case 'S':
                            // For object positions, also use floor tile as base
                            setTileInMap(col, row, Tile.TILE_FLOOR);
                            System.out.println("Set FLOOR (object position) at (" + col + "," + row + ") for char '" + mapChar + "'");
                            break;
                        default:
                            setTileInMap(col, row, Tile.TILE_FLOOR);
                            System.out.println("Set FLOOR (default) at (" + col + "," + row + ") for char '" + mapChar + "'");
                            break;
                    }
                }
            }
        } else {
            System.out.println("Failed to load store.txt, using all floor tiles");
            // Fallback: all floor tiles
            for (int col = 0; col < STORE_COLS; col++) {
                for (int row = 0; row < STORE_ROWS; row++) {
                    setTileInMap(col, row, Tile.TILE_FLOOR);
                }
            }
        }
        
        // Add teleport tile in the middle of the last row
        int mid1 = (STORE_COLS / 2) - 1;
        int mid2 = (STORE_COLS / 2);
        setTileInMap(mid1, STORE_ROWS - 1, Tile.TILE_TELEPORT);
        setTileInMap(mid2, STORE_ROWS - 1, Tile.TILE_TELEPORT);
    }/**
     * Set up initial objects in the store map based on predefined positions
     */
    @Override
    public void setupInitialObjects() {
        setupObjectsFromFile("store.txt");
    }

    /**
     * Deploy objects and NPCs in the store based on store.txt
     */
    protected void setupObjectsFromFile(String mapFileName) {
        // Deploy store objects as before
        objDeployer.deployStoreDecoration4(5, 1);
        objDeployer.deployStoreDecoration3(3, 2);
        objDeployer.deployStoreDecoration2(1, 3);
        objDeployer.deployStoreDecoration1(7, 2);
        objDeployer.deployStoreDecoration1(7, 5);

        // Deploy NPCs at 'n' positions
        npcs.clear();
        for (int row = 0; row < STORE_ROWS; row++) {
            for (int col = 0; col < STORE_COLS; col++) {
                char mapChar = super.getMapFileChar(col, row, "RES/MAP_TXT/" + mapFileName);
                if (mapChar == 'n') {
                    int worldX = col * gp.getTileSize();
                    int worldY = row * gp.getTileSize();
                    // For now, always deploy Emily at 'n' in store
                    SRC.ENTITY.EmilyEntity emily = new SRC.ENTITY.EmilyEntity(gp, worldX, worldY);
                    npcs.add(emily);
                    System.out.println("Placed Emily NPC at position (" + col + "," + row + ") - worldX: " + worldX + ", worldY: " + worldY);
                    ensureNPCsVisible(); // Ensure NPC is visible in camera view
                }
            }
        }
    }

    /**
     * Draw the store map and its NPCs
     */
    @Override
    public void draw(java.awt.Graphics2D g2) {
        super.draw(g2); // Draw tiles and objects
        for (NPCEntity npc : npcs) {
            npc.draw(g2);
        }
    }

    /**
     * Update all NPCs in the store
     */
    public void updateNPCs() {
        for (NPCEntity npc : npcs) {
            npc.update();
        }
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
                // Place NPC in the center of visible area
                System.out.println("Repositioning NPC " + npc.getNPCName() + " to be visible in camera view");
                int centerWorldX = cameraX + (screenWidth / 2) - (gp.getTileSize() / 2);
                int centerWorldY = cameraY + (screenHeight / 2) - (gp.getTileSize() / 2);
                npc.setWorldX(centerWorldX - (gp.getTileSize()*2) + 25);
                npc.setWorldY(centerWorldY);
            
        }
    }
}
