package SRC.MAP;

import SRC.MAIN.GamePanel;
import SRC.OBJECT.OBJ_House;
import SRC.OBJECT.ObjectDeployer;
import SRC.TILES.Tile;

/**
 * WorldMap class represents the main world map in the game
 */
public class WorldMap extends Map {
    
    // Object deployer for placing objects on the map
    private ObjectDeployer objDeployer;
    
    /**
     * Constructor for the WorldMap
     * @param gp GamePanel reference
     */
    public WorldMap(GamePanel gp) {
        // Use the game panel's world dimensions, allow up to 20 objects on world map
        super(gp, "World Map", gp.getMaxWorldCol(), gp.getMaxWorldRow(), 20);
        
        // Initialize object deployer
        objDeployer = new ObjectDeployer(gp);
    }
    
    /**
     * Initialize the world map with specific terrain features
     */
    @Override
    protected void initializeMap() {
        // Start with default initialization (all grass)
        super.initializeMap();
        boolean loadSuccess = loadMapFromFile("RES/map.txt");
        System.out.println("Attempting to load map file from: RES/map.txt");
        // Jika gagal membaca file, buat map secara manual (backup)
        if (!loadSuccess) {
            System.out.println("Falling back to default map generation");
            int centerX = maxCol / 2;
            int centerY = maxRow / 2;
            int lakeSize = 5;
            for (int col = centerX - lakeSize; col <= centerX + lakeSize; col++) {
                for (int row = centerY - lakeSize; row <= centerY + lakeSize; row++) {
                    if (Math.sqrt(Math.pow(col - centerX, 2) + Math.pow(row - centerY, 2)) <= lakeSize) {
                        setTile(col, row, Tile.TILE_WATER); // Water tile
                    }
                }
            }
        }
        // Tambahkan tile teleport di ujung kiri untuk ke FarmMap
        for (int row = 0; row < maxRow; row++) {
            setTile(0, row, Tile.TILE_TELEPORT);
        }
    }
    
    /**
     * Setup initial objects in the world map
     */
    @Override
    public void setupInitialObjects() {
        // Tempatkan objek berdasarkan data di map.txt
        // Menyusun map objek berdasarkan kode tile
        for (int col = 0; col < maxCol; col++) {
            for (int row = 0; row < maxRow; row++) {
                int tileType = getTile(col, row);
                
                // Jika tile bertipe 2, letakkan rumah di sana
                if (tileType == 2) {
                    // Periksa apakah ini adalah pojok kiri atas dari kelompok tile bertipe 2
                    // untuk memastikan rumah hanya diletakkan satu kali per area
                    boolean isTopLeft = true;
                    
                    // Periksa tetangga di kiri dan atas (jika ada)
                    if (col > 0 && getTile(col-1, row) == 2) isTopLeft = false;
                    if (row > 0 && getTile(col, row-1) == 2) isTopLeft = false;
                    
                    // Jika ini adalah pojok kiri atas, letakkan rumah
                    if (isTopLeft) {
                        objDeployer.deployHouse(col, row);
                    }
                }
            }
        }
        
        // Jika tidak ada rumah yang berhasil diletakkan, gunakan peletakan default
        boolean houseExists = false;
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null && objects[i] instanceof OBJ_House) {
                houseExists = true;
                break;
            }
        }
        
        if (!houseExists) {
            System.out.println("No houses were placed based on map data, using default placements");
            objDeployer.deployHouse(5, 5);
            objDeployer.deployHouse(15, 8);
            objDeployer.deployHouse(25, 20);
            objDeployer.deployHouse(10, 25);
        }
        
        // Deploy beberapa kolam secara acak
        int numPonds = 3 + (int)(Math.random() * 3); // 3-5 kolam
        int attempts = 0;
        int pondsDeployed = 0;
        
        while (pondsDeployed < numPonds && attempts < 100) {
            int pondCol = 5 + (int)(Math.random() * (maxCol - 10));
            int pondRow = 5 + (int)(Math.random() * (maxRow - 10));
            
            // Pastikan tidak menempatkan kolam di air
            if (getTile(pondCol, pondRow) != Tile.TILE_WATER && isValidPlacement(pondCol, pondRow)) {
                objDeployer.deployPond(pondCol, pondRow);
                pondsDeployed++;
            }
            
            attempts++;
        }
        
        // Deploy shipping bin di dekat rumah
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null && objects[i] instanceof OBJ_House) {
                int houseCol = objects[i].getPosition().getCol();
                int houseRow = objects[i].getPosition().getRow();
                
                // Coba tempatkan shipping bin di sebelah kanan rumah dengan jarak 2 tiles
                if (isValidPlacement(houseCol + 8, houseRow)) {
                    objDeployer.deployShippingBin(houseCol + 8, houseRow);
                }
            }
        }
    }
    
    /**
     * Custom method for WorldMap: get distance to nearest house
     * @param col Column position to check from
     * @param row Row position to check from
     * @return Distance to nearest house, or -1 if no houses exist
     */
    public int getDistanceToNearestHouse(int col, int row) {
        int nearestDistance = Integer.MAX_VALUE;
        boolean foundHouse = false;
        
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null && objects[i] instanceof OBJ_House) {
                int objCol = objects[i].getPosition().getCol();
                int objRow = objects[i].getPosition().getRow();
                
                int distance = Math.abs(col - objCol) + Math.abs(row - objRow);
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    foundHouse = true;
                }
            }
        }
        
        return foundHouse ? nearestDistance : -1;
    }
}