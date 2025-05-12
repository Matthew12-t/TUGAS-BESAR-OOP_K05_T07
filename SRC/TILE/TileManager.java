package SRC.TILE;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

import SRC.MAIN.GamePanel;

public class TileManager {

    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;
    
    public TileManager(GamePanel gp) {
        
        this.gp = gp;
        tile = new Tile[10]; // Can store 10 different types of tiles
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        
        loadTiles();
        loadMap("/RES/maps/map01.txt");
    }
    
    public void loadTiles() {
        try {
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/RES/tile/grass.png"));
            tile[0].collision = false;
            
            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/RES/tile/water.png"));
            tile[1].collision = true;
            
            // Load more tiles as needed
            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/RES/OBJECT/house.png"));
            tile[2].collision = true;
            
        } catch (IOException e) {
            System.err.println("Error loading tile images: " + e.getMessage());
            e.printStackTrace();
            
            // Alternative file loading method if resource streams fail
            try {
                tile[0] = new Tile();
                tile[0].image = ImageIO.read(new File("RES/tile/grass.png"));
                tile[0].collision = false;
                
                tile[1] = new Tile();
                tile[1].image = ImageIO.read(new File("RES/tile/water.png"));
                tile[1].collision = true;

                tile[2] = new Tile();
                tile[2].image = ImageIO.read(new File("RES/OBJECT/house.png"));
                tile[2].collision = true;
            } catch (IOException e2) {
                System.err.println("Failed to load tiles using alternative method: " + e2.getMessage());
                e2.printStackTrace();
            }
        }
    }
    
    public void loadMap(String filePath) {
        try {
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br;
            
            if (is != null) {
                br = new BufferedReader(new InputStreamReader(is));
            } else {
                // Alternative loading method if resource stream fails
                br = new BufferedReader(new FileReader("." + filePath));
            }
            
            int col = 0;
            int row = 0;
            
            while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
                
                String line = br.readLine();
                
                if (line == null) {
                    break; // End of file
                }
                
                // Split the line by spaces or any whitespace
                String[] numbers = line.split("\\s+");
                
                while (col < gp.maxWorldCol && col < numbers.length) {
                    int num = Integer.parseInt(numbers[col]);
                    
                    mapTileNum[col][row] = num;
                    col++;
                }
                
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            
            br.close();
            
        } catch (Exception e) {
            System.err.println("Error loading map file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g2, GamePanel gp) {
        int worldCol = 0;
        int worldRow = 0;
        
        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
            
            int tileNum = mapTileNum[worldCol][worldRow];
            
            // Calculate the world position of this tile
            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            
            // Calculate the screen position relative to camera
            int screenX = worldX - gp.cameraX;
            int screenY = worldY - gp.cameraY;
            
            // Only draw tiles that are visible on screen (plus one tile margin for smooth scrolling)
            if (worldX + gp.tileSize > gp.cameraX && 
                worldX - gp.tileSize < gp.cameraX + gp.getscreenWidth() && 
                worldY + gp.tileSize > gp.cameraY && 
                worldY - gp.tileSize < gp.cameraY + gp.getscreenHeight()) {
                  if (tileNum >= 0 && tileNum < tile.length && tile[tileNum] != null && tile[tileNum].image != null) {
                    g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                    
                    // For debugging: Mark collision tiles with a subtle outline
                    // if (tile[tileNum].collision) {
                    //     g2.setColor(new Color(255, 0, 0, 60)); // Very light red
                    //     g2.fillRect(screenX, screenY, gp.tileSize, gp.tileSize);
                    //     g2.setColor(new Color(255, 0, 0, 120)); // Slightly darker red for border
                    //     g2.drawRect(screenX, screenY, gp.tileSize, gp.tileSize);
                    // }
                } else {
                    // Fallback for missing or invalid tiles
                    g2.drawImage(tile[0].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
                }
                
                // For debugging: Draw grid lines
                // g2.setColor(new Color(0, 0, 0, 30)); // Very light black
                // g2.drawRect(screenX, screenY, gp.tileSize, gp.tileSize);
            }
            
            worldCol++;
            
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
    
    public boolean isTileCollision(int worldX, int worldY) {
        // Convert world coordinates to tile coordinates
        int tileCol = worldX / gp.tileSize;
        int tileRow = worldY / gp.tileSize;
        
        // Check if out of bounds
        if (tileCol < 0 || tileCol >= gp.maxWorldCol || 
            tileRow < 0 || tileRow >= gp.maxWorldRow) {
            return true; // Consider out-of-bounds as collision
        }
        
        // Get the tile number at these coordinates
        int tileNum = mapTileNum[tileCol][tileRow];
        
        // Check if this tile has collision
        if (tileNum >= 0 && tileNum < tile.length && tile[tileNum] != null) {
            return tile[tileNum].collision;
        }
        
        // Default to no collision if tile is invalid
        return false;
    }
      // Method to check if there's a collision within a rectangular area
    public boolean checkRectangleCollision(int worldX, int worldY, int width, int height) {
        // Get tile positions for the rectangle corners
        int leftCol = worldX / gp.tileSize;
        int rightCol = (worldX + width - 1) / gp.tileSize;
        int topRow = worldY / gp.tileSize;
        int bottomRow = (worldY + height - 1) / gp.tileSize;
        
        // Clamp values to prevent out-of-bounds
        leftCol = Math.max(0, leftCol);
        rightCol = Math.min(gp.maxWorldCol - 1, rightCol);
        topRow = Math.max(0, topRow);
        bottomRow = Math.min(gp.maxWorldRow - 1, bottomRow);
        
        // Check each tile that the rectangle overlaps
        for (int col = leftCol; col <= rightCol; col++) {
            for (int row = topRow; row <= bottomRow; row++) {
                int tileNum = mapTileNum[col][row];
                if (tileNum >= 0 && tileNum < tile.length && tile[tileNum] != null && tile[tileNum].collision) {
                    return true;
                }
            }
        }
        
        // No collision
        return false;
    }
}
