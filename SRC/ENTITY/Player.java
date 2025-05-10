package SRC.ENTITY;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import SRC.MAIN.GamePanel;
import SRC.MAIN.KeyHandler;
import SRC.MAIN.MouseHandler;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;
    MouseHandler mouseHandler;
      private final int TOTAL_FRAMES = 8;
    
    // Arrays untuk menyimpan 8 frame animasi untuk setiap arah
    private BufferedImage[] up;
    private BufferedImage[] down; 
    private BufferedImage[] left;
    private BufferedImage[] right;
    
    // Variabel animasi
    private int spriteCounter = 0;
    private int spriteNum = 0;
    
    // Ukuran player
    private final int playerScale = 4; // Faktor skala untuk player
    private int playerWidth;
    private int playerHeight;    
    
    public Player(GamePanel gp, KeyHandler keyH, MouseHandler mouseHandler) {
        this.gp = gp;
        this.keyH = keyH;
        this.mouseHandler = mouseHandler;
        
        // Inisialisasi array gambar
        up = new BufferedImage[TOTAL_FRAMES];
        down = new BufferedImage[TOTAL_FRAMES];
        left = new BufferedImage[TOTAL_FRAMES];
        right = new BufferedImage[TOTAL_FRAMES];
        
        // Hitung ukuran player berdasarkan skala
        this.playerWidth = gp.tileSize * playerScale;
        this.playerHeight = gp.tileSize * playerScale;
        
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 4;
        direction = "down"; // default direction
    }

    public void getPlayerImage() {
        try{
            // Load gambar untuk arah ke atas (belakang)
            for (int i = 0; i < TOTAL_FRAMES; i++) {
                up[i] = ImageIO.read(new File("RES/cw belakang/barcode" + (i+1) + ".png"));
            }
            
            // Load gambar untuk arah ke bawah (depan)
            for (int i = 0; i < TOTAL_FRAMES; i++) {
                down[i] = ImageIO.read(new File("RES/cw depan/barcode" + (i+1) + ".png"));
            }
            
            // Load gambar untuk arah ke kiri
            for (int i = 0; i < TOTAL_FRAMES; i++) {
                left[i] = ImageIO.read(new File("RES/cw kiri/barcode" + (i+1) + ".png"));
            }
            
            // Load gambar untuk arah ke kanan
            for (int i = 0; i < TOTAL_FRAMES; i++) {
                right[i] = ImageIO.read(new File("RES/cw kanan/barcode" + (i+1) + ".png"));
            }
            
            System.out.println("Semua gambar player berhasil dimuat!");
        }
        catch(IOException e) {
            System.err.println("Error saat memuat gambar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void update() {
        boolean moving = false;
        
        //keyboard movement
        if(keyH.upPressed == true) {
            direction = "up";
            y -= speed;
            moving = true;
        }
        if(keyH.downPressed == true) {
            direction = "down";
            y += speed;
            moving = true;
        }
        if(keyH.leftPressed == true) {
            direction = "left";
            x -= speed;
            moving = true;
        }
        if(keyH.rightPressed == true) {
            direction = "right";
            x += speed;
            moving = true;
        }        //mouse movement
        if(mouseHandler.hasTarget) {
            int dx = mouseHandler.targetX - (x + playerWidth/2);
            int dy = mouseHandler.targetY - (y + playerHeight/2);
            
            // Calculate distance to target
            double distance = Math.sqrt(dx*dx + dy*dy);
            
            // Tentukan arah berdasarkan vector gerakan
            if(Math.abs(dx) > Math.abs(dy)) {
                if(dx > 0) {
                    direction = "right";
                } else {
                    direction = "left";
                }
            } else {
                if(dy > 0) {
                    direction = "down";
                } else {
                    direction = "up";
                }
            }
            
            // If player is close to target, stop moving
            if(distance < speed) {
                mouseHandler.hasTarget = false;
            } else {
                // Move player towards target
                double moveRatio = speed / distance;
                x += dx * moveRatio;
                y += dy * moveRatio;
                moving = true;
            }
        }
        
        // Update animasi hanya jika bergerak
        if(moving) {
            spriteCounter++;
            // Ubah frame setiap 5 update
            if(spriteCounter > 5) {
                spriteNum = (spriteNum + 1) % TOTAL_FRAMES;
                spriteCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        
        // Pilih gambar yang sesuai berdasarkan arah dan frame animasi
        switch(direction) {
            case "up":
                image = up[spriteNum];
                break;
            case "down":
                image = down[spriteNum];
                break;
            case "left":
                image = left[spriteNum];
                break;
            case "right":
                image = right[spriteNum];
                break;
        }
          // Gambar karakter menggunakan skala
        if(image != null) {
            g2.drawImage(image, x, y, playerWidth, playerHeight, null);
        } else {
            // Fallback jika gambar tidak ditemukan
            g2.setColor(Color.white);
            g2.fillRect(x, y, playerWidth, playerHeight);
        }

        // Debug: Indikator target
        if(mouseHandler.hasTarget) {
            g2.setColor(Color.red);
            g2.drawOval(mouseHandler.targetX - 5, mouseHandler.targetY - 5, 10, 10);
        }
    }
}
