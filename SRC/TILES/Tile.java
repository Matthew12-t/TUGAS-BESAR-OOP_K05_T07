package SRC.TILES;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
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
