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
    private String type = "";
      // Area solid untuk collision detection
    private Rectangle solidArea;
    
    // Konstanta untuk tipe-tipe tile
    public static final int TILE_GRASS = 0;
    public static final int TILE_WATER = 1;
    public static final int TILE_TILLABLE = 2; // Tillable land (.)
    public static final int TILE_TILLED = 3;   // Tilled land (t)
    public static final int TILE_PLANTED = 4;  // Planted land (l)
    
    // Resource untuk tile
    private static Image grassTile;
    private static Image waterTile;
    private static Image tillableTile;
    private static Image tilledTile;
    private static Image plantedTile;
    
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
            
            // Untuk saat ini, kita gunakan grass sebagai fallback untuk tile yang belum punya gambar
            tillableTile = grassTile;
            tilledTile = grassTile;
            plantedTile = grassTile;
            
            System.out.println("Tile resources loaded successfully!");
        } catch (IOException e) {
            System.err.println("Error loading tile images: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Resource not found: /RES/TILE/grass.png. Check the path.");
            e.printStackTrace();
        }
    }
      /**
     * Menggambar grass tile pada posisi layar tertentu
     * @param g2 Graphics context untuk menggambar
     * @param screenX Posisi X pada layar
     * @param screenY Posisi Y pada layar
     * @param tileSize Ukuran tile yang akan digambar
     */
    public static void drawGrassTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        if (grassTile != null) {
            g2.drawImage(grassTile, screenX, screenY, tileSize, tileSize, null);
        } else {
            // Fallback jika gambar tidak terload
            g2.setColor(java.awt.Color.GREEN);
            g2.fillRect(screenX, screenY, tileSize, tileSize);
        }
    }
    
    /**
     * Menggambar water tile pada posisi layar tertentu
     * @param g2 Graphics context untuk menggambar
     * @param screenX Posisi X pada layar
     * @param screenY Posisi Y pada layar
     * @param tileSize Ukuran tile yang akan digambar
     */
    public static void drawWaterTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        if (waterTile != null) {
            g2.drawImage(waterTile, screenX, screenY, tileSize, tileSize, null);
        } else {
            // Fallback jika gambar tidak terload
            g2.setColor(java.awt.Color.BLUE);
            g2.fillRect(screenX, screenY, tileSize, tileSize);
        }
    }
    
    /**
     * Menggambar tillable land tile pada posisi layar tertentu
     * Tillable land (.) - Tile yang dapat disiapkan untuk tanam
     * @param g2 Graphics context untuk menggambar
     * @param screenX Posisi X pada layar
     * @param screenY Posisi Y pada layar
     * @param tileSize Ukuran tile yang akan digambar
     */
    public static void drawTillableTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        if (tillableTile != null) {
            g2.drawImage(tillableTile, screenX, screenY, tileSize, tileSize, null);
        } else {
            // Fallback jika gambar tidak terload
            g2.setColor(new java.awt.Color(153, 102, 51)); // Light brown
            g2.fillRect(screenX, screenY, tileSize, tileSize);
            
            // Tambahkan titik (.) untuk menandakan tillable
            g2.setColor(java.awt.Color.BLACK);
            int dotSize = tileSize / 10;
            g2.fillOval(screenX + (tileSize/2) - (dotSize/2), 
                         screenY + (tileSize/2) - (dotSize/2), 
                         dotSize, dotSize);
        }
    }
    
    /**
     * Menggambar tilled land tile pada posisi layar tertentu
     * Tilled land (t) - Tile yang dapat ditanamkan seed
     * @param g2 Graphics context untuk menggambar
     * @param screenX Posisi X pada layar
     * @param screenY Posisi Y pada layar
     * @param tileSize Ukuran tile yang akan digambar
     */
    public static void drawTilledTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        if (tilledTile != null) {
            g2.drawImage(tilledTile, screenX, screenY, tileSize, tileSize, null);
        } else {
            // Fallback jika gambar tidak terload
            g2.setColor(new java.awt.Color(102, 51, 0)); // Dark brown
            g2.fillRect(screenX, screenY, tileSize, tileSize);
            
            // Tambahkan garis untuk menandakan tanah yang sudah diolah (t)
            g2.setColor(java.awt.Color.BLACK);
            int lineWidth = tileSize / 10;
            for(int i = 1; i < 3; i++) {
                g2.fillRect(screenX, screenY + (i*tileSize/3), tileSize, lineWidth);
            }
        }
    }
    
    /**
     * Menggambar planted land tile pada posisi layar tertentu
     * Planted land (l) - Tile yang sudah ditanamkan seed
     * @param g2 Graphics context untuk menggambar
     * @param screenX Posisi X pada layar
     * @param screenY Posisi Y pada layar
     * @param tileSize Ukuran tile yang akan digambar
     */
    public static void drawPlantedTile(Graphics2D g2, int screenX, int screenY, int tileSize) {
        if (plantedTile != null) {
            g2.drawImage(plantedTile, screenX, screenY, tileSize, tileSize, null);
        } else {
            // Fallback jika gambar tidak terload - tilled land + tanaman hijau
            g2.setColor(new java.awt.Color(102, 51, 0)); // Dark brown
            g2.fillRect(screenX, screenY, tileSize, tileSize);
            
            // Tambahkan garis untuk menandakan tanah yang sudah diolah
            g2.setColor(java.awt.Color.BLACK);
            int lineWidth = tileSize / 10;
            for(int i = 1; i < 3; i++) {
                g2.fillRect(screenX, screenY + (i*tileSize/3), tileSize, lineWidth);
            }
            
            // Tambahkan tanaman (l)
            g2.setColor(new java.awt.Color(0, 153, 0)); // Bright green
            int plantWidth = tileSize / 5;
            int plantHeight = tileSize / 3;            g2.fillRect(screenX + (tileSize/2) - (plantWidth/2), 
                       screenY + (tileSize/3), 
                       plantWidth, plantHeight);
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
            case TILE_TILLABLE:
                drawTillableTile(g2, screenX, screenY, tileSize);
                break;
            case TILE_TILLED:
                drawTilledTile(g2, screenX, screenY, tileSize);
                break;
            case TILE_PLANTED:
                drawPlantedTile(g2, screenX, screenY, tileSize);
                break;
            default:
                // Default ke grass tile
                drawGrassTile(g2, screenX, screenY, tileSize);
                break;
        }
    }
    
    /**
     * Konstruktor untuk membuat Tile baru berdasarkan kolom dan baris
     * @param gp GamePanel untuk mendapatkan ukuran tile
     * @param col Kolom dalam grid
     * @param row Baris dalam grid
     */
    public Tile(GamePanel gp, int col, int row) {
        this.col = col;
        this.row = row;
        this.worldX = col * gp.getTileSize();
        this.worldY = row * gp.getTileSize();
        this.solidArea = new Rectangle(0, 0, gp.getTileSize(), gp.getTileSize());
    }
    
    /**
     * Konstruktor untuk membuat Tile baru berdasarkan koordinat dunia
     * @param gp GamePanel untuk mendapatkan ukuran tile
     * @param worldX Posisi X dalam koordinat dunia (pixel)
     * @param worldY Posisi Y dalam koordinat dunia (pixel)
     */
    public Tile(GamePanel gp, int worldX, int worldY, boolean isWorldPos) {
        this.worldX = worldX;
        this.worldY = worldY;
        this.col = worldX / gp.getTileSize();
        this.row = worldY / gp.getTileSize();
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
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Rectangle getSolidArea() {
        return solidArea;
    }
    
    /**
     * Mengecek apakah tile ini sama dengan tile lain
     * @param other Tile lain yang akan dibandingkan
     * @return true jika posisi grid (col, row) sama
     */
    public boolean equals(Tile other) {
        return this.col == other.col && this.row == other.row;
    }
    
    /**
     * Menghitung jarak Manhattan antara tile ini dengan tile lain
     * @param other Tile lain
     * @return Jarak Manhattan (jumlah langkah horizontal dan vertikal)
     */
    public int distanceTo(Tile other) {
        return Math.abs(this.col - other.col) + Math.abs(this.row - other.row);
    }
    
    /**
     * Mendapatkan posisi tengah tile dalam koordinat dunia
     * @return Array berisi [centerX, centerY]
     */
    public int[] getCenter() {
        int centerX = worldX + solidArea.width / 2;
        int centerY = worldY + solidArea.height / 2;
        return new int[]{centerX, centerY};
    }
    
    /**
     * Mengecek apakah suatu koordinat dunia terletak di dalam tile ini
     * @param x Koordinat X dunia
     * @param y Koordinat Y dunia
     * @return true jika koordinat berada dalam tile
     */
    public boolean contains(int x, int y) {
        return x >= worldX && x < worldX + solidArea.width &&
               y >= worldY && y < worldY + solidArea.height;
    }
    
    /**
     * Mengembalikan representasi string dari tile
     * @return String dengan format "Tile[col=x, row=y, type=z]"
     */
    @Override
    public String toString() {
        return "Tile[col=" + col + ", row=" + row + ", type=" + type + "]";
    }
}
