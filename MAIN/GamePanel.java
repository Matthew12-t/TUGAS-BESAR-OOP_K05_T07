package MAIN;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints.Key;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;
    
    final int tileSize = originalTileSize * scale; // 48x48 tile
    final int maxScreenCol = 16;
    final int maxScreenRow = 12;
    final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    final int screenHeight = tileSize * maxScreenRow; // 576 pixels
    final int FPS = 60;

    KeyHandler keyHandler = new KeyHandler();
    MouseHandler mouseHandler = new MouseHandler(this);
    Thread gameThread;

    //set player default position
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;
    
    
    public GamePanel() {
        // Set the size of the game panel
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.addMouseListener(mouseHandler);
        this.setFocusable(true); // Make the panel focusable to receive key events
    }
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start(); 
    }

    @Override
    public void run(){
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if(timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }

    }

    public void update() {
        //keyboard movement
        if(keyHandler.upPressed == true) {
            playerY -= playerSpeed;
        }
        if(keyHandler.downPressed == true) {
            playerY += playerSpeed;
        }
        if(keyHandler.leftPressed == true) {
            playerX -= playerSpeed;
        }
        if(keyHandler.rightPressed == true) {
            playerX += playerSpeed;
        }

        //mouse movement
        if(mouseHandler.hasTarget) {
            int dx = mouseHandler.targetX - (playerX + tileSize/2);
            int dy = mouseHandler.targetY - (playerY + tileSize/2);
            
            // Calculate distance to target
            double distance = Math.sqrt(dx*dx + dy*dy);
            
            // If player is close to target, stop moving
            if(distance < playerSpeed) {
                mouseHandler.hasTarget = false;
            } else {
                // Move player towards target
                double moveRatio = playerSpeed / distance;
                playerX += dx * moveRatio;
                playerY += dy * moveRatio;
            }
        } 
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(Color.white);

        g2.fillRect(playerX, playerY, tileSize, tileSize);

        if(mouseHandler.hasTarget) {
            g2.setColor(Color.red);
            g2.drawOval(mouseHandler.targetX - 5, mouseHandler.targetY - 5, 10, 10);
        }

        g2.dispose();
    }
    
}
