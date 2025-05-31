package SRC.MAP.NPC_HOUSE;

import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;
import SRC.MAIN.GamePanel;
import SRC.MAP.Map;
import SRC.OBJECT.ObjectDeployer;
import SRC.TILES.Tile;


public abstract class NPCHouseMap extends Map {
    public static final int NPC_HOUSE_COLS = 13;
    public static final int NPC_HOUSE_ROWS = 9;    
    protected Set<String> drawnLargeTiles = new HashSet<>();
    protected Set<String> deployedLargeObjects = new HashSet<>();
    protected ObjectDeployer objDeployer;

    public NPCHouseMap(GamePanel gp, String houseName) {
        
        super(gp, houseName, NPC_HOUSE_COLS, NPC_HOUSE_ROWS, 50);

        
        objDeployer = new ObjectDeployer(gp);
    }


    protected void initializeFromFile(String mapFileName) {
        
        super.initializeMap();
        
        
        boolean loadSuccess = loadMapFromFile("RES/MAP_TXT/" + mapFileName);
        
        if (loadSuccess) {
            System.out.println("Setting up tiles from " + mapFileName + "...");
            
            
            for (int row = 0; row < NPC_HOUSE_ROWS; row++) {
                for (int col = 0; col < NPC_HOUSE_COLS; col++) {
                    
                    char mapChar = super.getMapFileChar(col, row, "RES/MAP_TXT/" + mapFileName);
                    
                    
                    switch (mapChar) {
                        case '2':
                            setTileInMap(col, row, Tile.TILE_WALL1);
                            break;                        
                        default:
                            setTileInMap(col, row, Tile.TILE_FLOOR);
                            break;
                    }
                }
            }
        } else {
            System.out.println("Failed to load " + mapFileName + ", using default floor tiles");
            
            for (int col = 0; col < NPC_HOUSE_COLS; col++) {
                for (int row = 0; row < NPC_HOUSE_ROWS; row++) {
                    setTileInMap(col, row, Tile.TILE_FLOOR);
                }
            }
        }
        
        
        int mid1 = (NPC_HOUSE_COLS / 2) - 1;
        int mid2 = (NPC_HOUSE_COLS / 2);
        setTileInMap(mid1, NPC_HOUSE_ROWS - 1, Tile.TILE_TELEPORT);
        setTileInMap(mid2, NPC_HOUSE_ROWS - 1, Tile.TILE_TELEPORT);
    }    

    protected void setupObjectsFromFile(String mapFileName) {
        
        deployedLargeObjects.clear();
        
        for (int row = 0; row < NPC_HOUSE_ROWS; row++) {
            for (int col = 0; col < NPC_HOUSE_COLS; col++) {
                
                String mapValue = super.getMapFileValue(col, row, "RES/MAP_TXT/" + mapFileName);
                
                if (mapValue.length() == 0) {
                    continue;
                }
                
                
                if (mapValue.length() > 1) {
                    if (mapValue.charAt(0) == 'c' && mapValue.length() == 2 && Character.isDigit(mapValue.charAt(1))) {
                        
                        int mode = Character.getNumericValue(mapValue.charAt(1));
                        System.out.println("Deploying chair mode " + mode + " at " + col + "," + row);
                        objDeployer.deployChair(col, row, mode);
                        continue;
                    } else if (mapValue.charAt(0) == 't' && mapValue.length() == 2 && Character.isDigit(mapValue.charAt(1))) {
                        
                        int mode = Character.getNumericValue(mapValue.charAt(1));
                        System.out.println("Deploying table mode " + mode + " at " + col + "," + row);
                        objDeployer.deployTable(col, row, mode);
                        continue;
                    }
                }
                  
                char mapChar = mapValue.charAt(0);
                final int finalCol = col;
                final int finalRow = row;
                
                switch (mapChar) {
                    case 'b':
                        
                        deployLargeObject("bed", finalCol, finalRow, 2, 4, () -> objDeployer.deployBed(finalCol, finalRow));
                        break;
                    case 's':
                        objDeployer.deployStove(finalCol, finalRow);
                        break;
                    case 'r':
                        objDeployer.deployRak(finalCol, finalRow);
                        break;
                    case 't':
                        
                        objDeployer.deployTv(finalCol, finalRow);
                        break;
                    case 'C':
                        
                        deployLargeObject("chimney", finalCol, finalRow, 2, 3, () -> objDeployer.deployChimney(finalCol, finalRow));
                        break;
                }
            }
        }
    }


    @Override
    protected void drawTile(Graphics2D g2, int col, int row, int tileType) {
        
        int worldX = col * gp.getTileSize();
        int worldY = row * gp.getTileSize();
        
        
        int screenX = worldX - gp.getCameraX();
        int screenY = worldY - gp.getCameraY();
        
        
        if (worldX + gp.getTileSize() > gp.getCameraX() &&
            worldX - gp.getTileSize() < gp.getCameraX() + gp.getScreenWidth() &&
            worldY + gp.getTileSize() > gp.getCameraY() &&
            worldY - gp.getTileSize() < gp.getCameraY() + gp.getScreenHeight()) {
              
            if (tileType == Tile.TILE_TELEPORT) {
                Tile.makeTeleportTile(g2, screenX, screenY, gp.getTileSize(), "house"); 
            } else {
                
                Tile.drawTileByType(g2, screenX, screenY, gp.getTileSize(), tileType);
            }
        }
    }

    private void deployLargeObject(String objectType, int col, int row, int width, int height, Runnable deployAction) {
        
        for (int r = row; r < row + height; r++) {
            for (int c = col; c < col + width; c++) {
                String key = objectType + "_" + c + "_" + r;
                if (deployedLargeObjects.contains(key)) {
                    
                    return;
                }
            }
        }
        
        
        for (int r = row; r < row + height; r++) {
            for (int c = col; c < col + width; c++) {
                String key = objectType + "_" + c + "_" + r;
                deployedLargeObjects.add(key);
            }
        }
        
        
        deployAction.run();
    }


    @Override
    public void draw(Graphics2D g2) {
        
        drawnLargeTiles.clear();
        
        
        drawTiles(g2);
        
        
        drawObjects(g2);
    }
}
