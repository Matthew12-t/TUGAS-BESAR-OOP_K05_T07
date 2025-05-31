package SRC.TILES;

import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import SRC.MAIN.GamePanel;

/**
 * Kelas Tile merepresentasikan satu unit grid dalam dunia game.
 * Digunakan untuk menyimpan informasi tentang posisi tile dan properti lainnya.
 */
public class Tile {
    // Atribut posisi tile dalam koordinat dunia (pixel)
    private int worldX;
    private int worldY;
    
    // Atribut posisi tile dalam grid (kolom, baris)
    private int col;
    private int row;
    
    // Properti tile
    private boolean collision = false;
    private BufferedImage image;
    private int type;
      // Area solid untuk collision detection
    private Rectangle solidArea;// Referensi ke GamePanel untuk akses ukuran tile
    // Konstanta untuk tipe-tipe tile
    public static final int TILE_GRASS = 0;
    public static final int TILE_WATER = 1;
    public static final int TILE_TILLABLE = 2; // Tillable land (.)
    public static final int TILE_TILLED = 3;   // Tilled land (t)
    public static final int TILE_PLANTED = 4;  // Planted land (l)
    public static final int TILE_TELEPORT = 5; // teleport    
    public static final int TILE_FLOOR = 6; // lantai
    public static final int TILE_WALL1 = 7;  // tembok
    public static final int TILE_PATH = 8;
    public static final int TILE_FOREST_GRASS1 = 9;
    public static final int TILE_FOREST_GRASS2 = 10;
    public static final int TILE_EDGE = 11;
    public static final int TILE_PLATFORM = 12; 
    public static final int TILE_EDGE_MAP = 13;
    public static final int TILE_WALL2 = 14; //tembok horizontal
    public static final int TILE_WALL3 = 15; //temmbok vertikal
    //montain
    public final static int TILE_EDGE2 = 16; 
    public final static int TILE_GRASSEDGE = 17;
    public final static int TILE_WATER2 = 18;
    public final static int TILE_PATH2 = 19;
    //ocean
    public final static int TILE_WATER3 = 20;
    public final static int TILE_ISLAND = 21;
    public final static int TILE_BRIDGE = 22;
    
    //land
    public final static int TILE_TILLED_LAND = 24;
    public final static int TILE_PLANTED_LAND = 25;
    public final static int TILE_LAND = 26;

    // Resource untuk tile
    private static Image grassTile;
    private static Image waterTile;
    private static Image pathTile;
    private static Image tilledTile;
    private static Image plantedTile;
    private static Image floorTile;
    private static Image wallTile1;
    private static Image wallTile2;
    private static Image wallTile3;
    private static Image ForestGrassTile1;
    private static Image ForestGrassTile2;
    private static Image EdgeTile;
    private static Image PlatformTile;
    private static Image Edge2;
    private static Image GrassEdge;
    private static Image Water2;
    private static Image pathTile2;
    //ocean tiles
    private static Image Water3;
    private static Image IslandTile;
    private static Image BridgeTile;    //tile
    private static Image tilledLandTile;
    private static Image plantedLandTile;
    private static Image landTile;
    private static Image sandTile;

    
    public Tile(GamePanel gp, int col, int row) {
        this.col = col;
        this.row = row;
        this.worldX = col * gp.getTileSize();
        this.worldY = row * gp.getTileSize();
        this.solidArea = new Rectangle(0, 0, gp.getTileSize(), gp.getTileSize());
    }
    
    
    // Getter dan Setter
    public int getWorldX() {
        return worldX;
    }
    
    public void setWorldX(int worldX) {
        this.worldX = worldX;
        this.col = worldX / solidArea.width;
    }
    
    public int getWorldY() {
        return worldY;
    }
    
    public void setWorldY(int worldY) {
        this.worldY = worldY;
        this.row = worldY / solidArea.height;
    }
    
    public int getCol() {
        return col;
    }
    
    public void setCol(GamePanel gp, int col) {
        this.col = col;
        this.worldX = col * gp.getTileSize();
    }
    
    public int getRow() {
        return row;
    }
    
    public void setRow(GamePanel gp, int row) {
        this.row = row;
        this.worldY = row * gp.getTileSize();
    }
    
    public boolean hasCollision() {
        return collision;
    }
    
    public void setCollision(boolean collision) {
        this.collision = collision;
    }
    
    public BufferedImage getImage() {
        return image;
    }
    
    public void setImage(BufferedImage image) {
        this.image = image;
    }
      public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    public Rectangle getSolidArea() {
        return solidArea;
    }
    
  
    public boolean equals(Tile other) {
        return this.col == other.col && this.row == other.row;
    }
    

    public int distanceTo(Tile other) {
        return Math.abs(this.col - other.col) + Math.abs(this.row - other.row);
    }
    

    public int[] getCenter() {
        int centerX = worldX + solidArea.width / 2;
        int centerY = worldY + solidArea.height / 2;
        return new int[]{centerX, centerY};
    }
    
    public boolean contains(int x, int y) {
        return x >= worldX && x < worldX + solidArea.width &&
               y >= worldY && y < worldY + solidArea.height;
    }
    
    // Static initializer untuk memuat resource
    static {
        loadTileImages();
    }
      /**
     * Memuat gambar-gambar tile yang akan digunakan dalam game
     */
    public static void loadTileImages() {
        try {
            // Load Grass Tile
            grassTile = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/grass.png"));
            if (grassTile == null) {
                grassTile = ImageIO.read(new File("RES/TILE/grass.png"));
            }
            
            // Load Water Tile
            waterTile = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/water.png"));
            if (waterTile == null) {
                waterTile = ImageIO.read(new File("RES/TILE/water.png"));
            }
            
            // Load Floor Tile
            floorTile = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/lantai.png"));
            if (floorTile == null) {
                floorTile = ImageIO.read(new File("RES/TILE/lantai.png"));
            }
            // Load Wall Tile1
            wallTile1 = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/tembok1.png"));
            if (wallTile1 == null) {
                wallTile1 = ImageIO.read(new File("RES/TILE/tembok.png"));
            }

            // Load Wall Tile 2
            wallTile2 = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/tembok2.png"));
            if (wallTile2 == null) {
                wallTile2 = ImageIO.read(new File("RES/TILE/tembok2.png"));
            }

            // Load Wall Tile 3
            wallTile3 = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/tembok3.png"));
            if (wallTile3 == null) {
                wallTile3 = ImageIO.read(new File("RES/TILE/tembok3.png"));
            }
            
            // Load Path Tile
            pathTile = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/path.png"));
            if (pathTile == null) {
                pathTile = ImageIO.read(new File("RES/TILE/path.png"));
            }            // Load Forest Grass Tile 1
            try {
                ForestGrassTile1 = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/forestgrass1.png"));
                if (ForestGrassTile1 == null) {
                    ForestGrassTile1 = ImageIO.read(new File("RES/TILE/forestgrass1.png"));
                    System.out.println("Loaded forestgrass1.png from file system");
                }
            } catch (Exception e) {
                System.err.println("Error loading forestgrass1.png: " + e.getMessage());
            }
            
            // Load Forest Grass Tile 2
            try {
                ForestGrassTile2 = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/forestgrass2.png"));
                if (ForestGrassTile2 == null) {
                    ForestGrassTile2 = ImageIO.read(new File("RES/TILE/forestgrass2.png"));
                    System.out.println("Loaded forestgrass2.png from file system");
                }
            } catch (Exception e) {
                System.err.println("Error loading forestgrass2.png: " + e.getMessage());
            }
            // Load Edge Tile
            EdgeTile = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/edge.png"));
            if (EdgeTile == null) {
                EdgeTile = ImageIO.read(new File("RES/TILE/edge.png"));
            }
            // Load Platform Tile
            PlatformTile = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/platform.png"));
            if (PlatformTile == null) {
                PlatformTile = ImageIO.read(new File("RES/TILE/platform.png"));
            }

            // Load Edge2 Tile
            Edge2 = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/edge2.png"));
            if (Edge2 == null) {
                Edge2 = ImageIO.read(new File("RES/TILE/edge2.png"));
            }

            // Load Grassed Edge Tile
            GrassEdge = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/grassedge.png"));
            if (GrassEdge == null) {
                GrassEdge = ImageIO.read(new File("RES/TILE/grassedge.png"));
            }

            // Load Water2 Tile
            Water2 = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/water2.png"));
            if (Water2 == null) {
                Water2 = ImageIO.read(new File("RES/TILE/water2.png"));
            }

            // Load Path Tile 2
            pathTile2 = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/path2.png"));
            if (pathTile2 == null) {
                pathTile2 = ImageIO.read(new File("RES/TILE/path2.png"));
            }

            // Load Ocean Tiles
            // Load Water3 Tile
            Water3 = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/water3.png"));
            if (Water3 == null) {
                Water3 = ImageIO.read(new File("RES/TILE/water3.png"));
            }

            // Load Island Tile
            IslandTile = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/island.png"));
            if (IslandTile == null) {
                IslandTile = ImageIO.read(new File("RES/TILE/island.png"));
            }

            // Load Bridge Tile
            BridgeTile = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/bridge.png"));
            if (BridgeTile == null) {
                BridgeTile = ImageIO.read(new File("RES/TILE/bridge.png"));
            }
            // Load Tilled Land Tile
            tilledTile = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/Tillted.png"));
            if (tilledLandTile == null) {
                tilledLandTile = ImageIO.read(new File("RES/TILE/Tillted.png"));
            }
          
            // Load Planted Land Tile
            plantedTile = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/Land.png"));
            if (plantedTile == null) {
                plantedTile = ImageIO.read(new File("RES/TILE/Land.png"));
            }

            // Load Land Tile
            landTile = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/Land.png"));
            if (landTile == null) {
                landTile = ImageIO.read(new File("RES/TILE/Land.png"));
            }
            sandTile = ImageIO.read(Tile.class.getResourceAsStream("/RES/TILE/sand.png"));
            if (sandTile == null) {
                sandTile = ImageIO.read(new File("RES/TILE/sand.png"));
            }
            System.out.println("Tile resources loaded successfully!");
        } catch (IOException e) {
            System.err.println("Error loading tile images: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Resource not found: /RES/TILE/grass.png. Check the path.");
            e.printStackTrace();
        }
    }
    public static void drawSandTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(sandTile, screenX, screenY, tileSize, tileSize, null);
    }
    public static void drawGrassTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(grassTile, screenX, screenY, tileSize, tileSize, null);
    }

    public static void drawWaterTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(waterTile, screenX, screenY, tileSize, tileSize, null);
    }

    public static void drawFloorTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(floorTile, screenX, screenY, tileSize, tileSize, null);
    }

    public static void drawWallTile1(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(wallTile1, screenX, screenY, tileSize, tileSize, null);
    }
    public static void drawWallTile2(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(wallTile2, screenX, screenY, tileSize, tileSize, null);
    }

    public static void drawWallTile3(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(wallTile3, screenX, screenY, tileSize, tileSize, null);
    }

    public static void drawForestGrassTile1(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(ForestGrassTile1, screenX, screenY, tileSize, tileSize, null);
    }

    public static void drawForestGrassTile2(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(ForestGrassTile2, screenX, screenY, tileSize, tileSize, null);
    }

    public static void drawEdgeTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(EdgeTile, screenX, screenY, tileSize * 2, tileSize, null);
    }    
    
    public static void drawPlatformTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(PlatformTile, screenX, screenY, tileSize * 4, tileSize * 3, null);
    }

    public static void drawEdge2Tile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(Edge2, screenX, screenY, tileSize * 2, tileSize, null);
    }

    public static void drawGrassEdgeTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(GrassEdge, screenX, screenY, tileSize, tileSize, null);
    }

    public static void drawWater2Tile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(Water2, screenX, screenY, tileSize, tileSize, null);
    }

    public static void drawPathTile2(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(pathTile2, screenX, screenY, tileSize, tileSize, null);
    }
    
    public static void drawWater3Tile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(Water3, screenX, screenY, tileSize, tileSize, null);
    }

    public static void drawIslandTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(IslandTile, screenX, screenY, tileSize * 4, tileSize * 4, null);
    }    
    
    public static void drawBridgeTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(BridgeTile, screenX, screenY, tileSize * 5, tileSize * 2, null);
    }

    public static void drawTilledTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(tilledLandTile, screenX, screenY, tileSize, tileSize, null);
    }    
    
    public static void drawPlantedTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(plantedLandTile, screenX, screenY, tileSize, tileSize, null);
    }
    
    /**
     * Draw planted tile dengan dynamic growth - delegate ke TileManager
     * @param g2 Graphics context
     * @param screenX screen X position
     * @param screenY screen Y position
     * @param tileSize tile size
     * @param tileManager TileManager instance untuk handle growth logic
     * @param col tile column
     * @param row tile row
     */
    public static void drawPlantedTileGrowth(Graphics2D g2, int screenX, int screenY, int tileSize, 
                                           Object tileManager, int col, int row) {
        if (tileManager != null && tileManager instanceof SRC.TILES.TileManager) {
            // Delegate ke TileManager untuk dynamic growth rendering
            ((SRC.TILES.TileManager) tileManager).drawPlantedTileGrowth(g2, screenX, screenY, col, row);
        } else {
            // Fallback ke static planted tile jika TileManager tidak tersedia
            drawPlantedTile(g2, screenX, screenY, tileSize);
        }
    }

    public static void drawLandTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        g2.drawImage(landTile, screenX, screenY, tileSize, tileSize, null);
    }   
    
    public static void drawEdgeMapTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        // Draw a light brown tile for map borders with collision
        g2.setColor(new java.awt.Color(210, 180, 140)); // Light brown color
        g2.fillRect(screenX, screenY, tileSize, tileSize);
        
        // Add a subtle border to make it stand out
        g2.setColor(new java.awt.Color(180, 150, 110)); // Slightly darker brown
        g2.drawRect(screenX, screenY, tileSize - 1, tileSize - 1);
    }      /**
     * Menggambar teleport tile pada posisi layar tertentu
     * @param g2 Graphics context untuk menggambar
     * @param screenX Posisi X pada layar
     * @param screenY Posisi Y pada layar
     * @param tileSize Ukuran tile yang akan digambar
     * @param mapType Type of map context: "house" for house maps, "grass" for outdoor maps, "water" for ocean maps, etc.
     */
    public static void makeTeleportTile(Graphics2D g2, int screenX, int screenY, int tileSize, String mapType) {
        // Draw appropriate base layer depending on map context
        switch (mapType.toLowerCase()) {
            case "house":
            case "floor":
                drawFloorTile(g2, screenX, screenY, tileSize);
                break;           
            case "water":
                drawSandTile(g2, screenX, screenY, tileSize);
                break;
            case "mountain":
                drawPathTile2(g2, screenX, screenY, tileSize);
                break;
            case "forest":
                drawForestGrassTile2(g2, screenX, screenY, tileSize);
                break;
            default:
                drawPathTile(g2, screenX, screenY, tileSize);
                break;
        }
        // Draw magenta circle to indicate teleport point
        g2.setColor(java.awt.Color.MAGENTA);
        int circleSize = (int)(tileSize * 0.6);
        int circleX = screenX + (tileSize - circleSize) / 2;
        int circleY = screenY + (tileSize - circleSize) / 2;
        g2.fillOval(circleX, circleY, circleSize, circleSize);
        
        // Draw white arrow indicators
        g2.setColor(java.awt.Color.WHITE);
        int arrowSize = tileSize / 4;
        
        // Draw four arrows pointing inward to indicate teleport
        // Top arrow
        int[] xPointsTop = {screenX + tileSize/2, screenX + tileSize/2 - arrowSize/2, screenX + tileSize/2 + arrowSize/2};
        int[] yPointsTop = {screenY + tileSize/4, screenY + tileSize/4 + arrowSize, screenY + tileSize/4 + arrowSize};
        g2.fillPolygon(xPointsTop, yPointsTop, 3);
        
        // Bottom arrow
        int[] xPointsBottom = {screenX + tileSize/2, screenX + tileSize/2 - arrowSize/2, screenX + tileSize/2 + arrowSize/2};
        int[] yPointsBottom = {screenY + tileSize*3/4, screenY + tileSize*3/4 - arrowSize, screenY + tileSize*3/4 - arrowSize};
        g2.fillPolygon(xPointsBottom, yPointsBottom, 3);
        
        // Left arrow
        int[] xPointsLeft = {screenX + tileSize/4, screenX + tileSize/4 + arrowSize, screenX + tileSize/4 + arrowSize};
        int[] yPointsLeft = {screenY + tileSize/2, screenY + tileSize/2 - arrowSize/2, screenY + tileSize/2 + arrowSize/2};
        g2.fillPolygon(xPointsLeft, yPointsLeft, 3);
        
        // Right arrow
        int[] xPointsRight = {screenX + tileSize*3/4, screenX + tileSize*3/4 - arrowSize, screenX + tileSize*3/4 - arrowSize};
        int[] yPointsRight = {screenY + tileSize/2, screenY + tileSize/2 - arrowSize/2, screenY + tileSize/2 + arrowSize/2};
        g2.fillPolygon(xPointsRight, yPointsRight, 3);
    }    /**
     * Backward-compatible version of makeTeleportTile (uses grass base by default)
     * @param g2 Graphics context untuk menggambar
     * @param screenX Posisi X pada layar
     * @param screenY Posisi Y pada layar
     * @param tileSize Ukuran tile yang akan digambar
     */
    public static void makeTeleportTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        makeTeleportTile(g2, screenX, screenY, tileSize, "grass");
    }
    
    /**
     * Menggambar path tile pada posisi layar tertentu
     * @param g2 Graphics context untuk menggambar
     * @param screenX Posisi X pada layar
     * @param screenY Posisi Y pada layar
     * @param tileSize Ukuran tile yang akan digambar
     */
    public static void drawPathTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        if (pathTile != null) {
            g2.drawImage(pathTile, screenX, screenY, tileSize, tileSize, null);
        } else {
            g2.fillRect(screenX, screenY, tileSize, tileSize);
            
            // Add path texture with lines
            g2.setColor(new java.awt.Color(180, 150, 110)); // Slightly darker tan
            int lineGap = tileSize / 8;
            
            // Horizontal lines
            for (int i = 1; i < 8; i += 2) {
                g2.drawLine(screenX, screenY + i * lineGap, 
                           screenX + tileSize, screenY + i * lineGap);
            }
            
            // Vertical lines
            for (int i = 1; i < 8; i += 2) {
                g2.drawLine(screenX + i * lineGap, screenY, 
                           screenX + i * lineGap, screenY + tileSize);
            }
        }
    }
    
    /**
     * Menggambar tile sesuai dengan tipe yang diberikan
     * @param g2 Graphics context untuk menggambar
     * @param screenX Posisi X pada layar
     * @param screenY Posisi Y pada layar
     * @param tileSize Ukuran tile yang akan digambar
     * @param tileType Tipe tile yang akan digambar
     */    
    public static void drawTileByType(Graphics2D g2, int screenX, int screenY, int tileSize, int tileType) {
        switch (tileType) {
            case TILE_GRASS:
                drawGrassTile(g2, screenX, screenY, tileSize);
                break;
            case TILE_WATER:
                drawWaterTile(g2, screenX, screenY, tileSize);
                break;
            case TILE_TILLED:
                drawTilledTile(g2, screenX, screenY, tileSize);
                break;
            case TILE_PLANTED:
                drawPlantedTile(g2, screenX, screenY, tileSize);
                break;            case TILE_TELEPORT:
                makeTeleportTile(g2, screenX, screenY, tileSize, "grass"); // Default to outdoor (grass) for backward compatibility
                break;
            case TILE_FLOOR:
                drawFloorTile(g2, screenX, screenY, tileSize);
                break;
            case TILE_WALL1:
                drawWallTile1(g2, screenX, screenY, tileSize);
                break;
            case TILE_WALL2:
                drawWallTile2(g2, screenX, screenY, tileSize);
                break;
            case TILE_WALL3:
                drawWallTile3(g2, screenX, screenY, tileSize);
                break;
            case TILE_PATH:
                drawPathTile(g2, screenX, screenY, tileSize);
                break;
            case TILE_FOREST_GRASS1:
                drawForestGrassTile1(g2, screenX, screenY, tileSize);
                break;
            case TILE_FOREST_GRASS2:
                drawForestGrassTile2(g2, screenX, screenY, tileSize);                
                break;
            case TILE_EDGE:
                drawEdgeTile(g2, screenX, screenY, tileSize);
                break;
            case TILE_EDGE2:
                drawEdge2Tile(g2, screenX, screenY, tileSize);
                break;
            case TILE_EDGE_MAP:
                drawEdgeMapTile(g2, screenX, screenY, tileSize);
                break;
            case TILE_PLATFORM:
                drawPlatformTile(g2, screenX, screenY, tileSize);
                break;
            case TILE_GRASSEDGE:
                drawGrassEdgeTile(g2, screenX, screenY, tileSize);
                break;
            case TILE_WATER2:
                drawWater2Tile(g2, screenX, screenY, tileSize);
                break;            
            case TILE_PATH2:
                drawPathTile2(g2, screenX, screenY, tileSize);
                break;
            case TILE_WATER3:
                drawWater3Tile(g2, screenX, screenY, tileSize);
                break;
            case TILE_ISLAND:
                drawIslandTile(g2, screenX, screenY, tileSize);
                break;            
            case TILE_BRIDGE:
                drawBridgeTile(g2, screenX, screenY, tileSize);
                break;
            case TILE_LAND:
                drawLandTile(g2, screenX, screenY, tileSize);
                break;
            default:
                drawGrassTile(g2, screenX, screenY, tileSize);
                break;
        }
    }
    
    /**
     * Menggambar tile besar sesuai dengan tipe yang diberikan
     * @param g2 Graphics context untuk menggambar
     * @param screenX Posisi X pada layar
     * @param screenY Posisi Y pada layar
     * @param tileSize Ukuran dasar satu tile
     * @param tileType Tipe tile yang akan digambar
     * @param width Lebar dalam jumlah tile
     * @param height Tinggi dalam jumlah tile
     */
    public static void drawLargeTileByType(Graphics2D g2, int screenX, int screenY, int tileSize, int tileType, int width, int height) {        // Validasi tipe tile
        switch (tileType) {
            case TILE_EDGE:
                drawEdgeTile(g2, screenX, screenY, tileSize);
                break;
            case TILE_EDGE_MAP:
                drawEdgeMapTile(g2, screenX, screenY, tileSize);
                break;
            case TILE_PLATFORM:
                drawPlatformTile(g2, screenX, screenY, tileSize);
                break;
            case TILE_WALL2:
                g2.drawImage(wallTile2, screenX, screenY, tileSize * width, tileSize * height, null);
                break;
            case TILE_WALL3:
                g2.drawImage(wallTile3, screenX, screenY, tileSize * width, tileSize * height, null);
                break;
            case TILE_EDGE2:
                g2.drawImage(Edge2, screenX, screenY, tileSize * width, tileSize * height, null);
                break;
            case TILE_ISLAND:
                g2.drawImage(IslandTile, screenX, screenY, tileSize * width, tileSize * height, null);
                break;
            case TILE_BRIDGE:
                g2.drawImage(BridgeTile, screenX, screenY, tileSize * width, tileSize * height, null);
                break;
            default:
                // Untuk tile lain yang mungkin perlu ditambahkan di kemudian hari
                drawTileByType(g2, screenX, screenY, tileSize, tileType);
                break;
        }
    }
    
    /**
     * Mendapatkan dimensi tile berdasarkan tipe tile
     * @param tileType Tipe tile
     * @return Array berisi [width, height] dalam jumlah tile
     */      
    public static int[] getTileDimensions(int tileType) {
        switch (tileType) {
            case TILE_EDGE:
                return new int[]{2, 1}; // Lebar 2 tile, tinggi 1 tile
            case TILE_EDGE_MAP:
                return new int[]{1, 1}; // Tile standar 1x1 untuk tepi map
            case TILE_PLATFORM:
                return new int[]{4, 3}; // Lebar 4 tile, tinggi 3 tile
            case TILE_WALL2:
                return new int[]{9, 1}; // Horizontal wall: width 2, height 1
            case TILE_WALL3:
                return new int[]{1, 5}; // Vertical wall: width 1, height 2
            case TILE_EDGE2:
                return new int[]{2, 1}; // Lebar 2 tile, tinggi 1 tile (same as TILE_EDGE)
            case TILE_ISLAND:
                return new int[]{4, 4}; // Lebar 4 tile, tinggi 4 tile
            case TILE_BRIDGE:
                return new int[]{5, 2}; // Lebar 5 tile, tinggi 2 tile
            default:
                return new int[]{1, 1}; // Tile standar 1x1
        }
    }
      /**
     * Mengecek apakah tipe tile adalah tile besar
     * @param tileType Tipe tile yang akan diperiksa
     * @return true jika tipe tile adalah tile besar
     */    
    public static boolean isLargeTile(int tileType) {
        return tileType == TILE_EDGE || tileType == TILE_PLATFORM || 
               tileType == TILE_WALL2 || tileType == TILE_WALL3 || tileType == TILE_EDGE2 ||
               tileType == TILE_ISLAND || tileType == TILE_BRIDGE;
        // Note: TILE_EDGE_MAP is not a large tile, it's a standard 1x1 tile
    }

    @Override
    public String toString() {
        return "Tile[col=" + col + ", row=" + row + ", type=" + type + "]";
    }
}
