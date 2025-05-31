package SRC.ITEMS;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;

public abstract class Item {
    private String name;
    private String category;
    private int sellPrice;
    private int buyPrice;
    private BufferedImage image;

    // Constructor
    public Item(String name, String category, int sellPrice, int buyPrice) {
        this.name = name;
        this.category = category;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.image = null;
    }
    

    protected void loadImage(String imagePath) {
        try {
            this.image = ImageIO.read(new File(imagePath));
        } catch (Exception e) {
            System.err.println("Error loading image for " + name + ": " + e.getMessage());
        }
    }
    // Getters
    public String getName() {
        return name;
    }
    public String getCategory() {
        return category;
    }
    public int getSellPrice() {
        return sellPrice;
    }
    public int getBuyPrice() {
        return buyPrice;
    }
    // Setters
    public void setName(String name) {
        this.name = name;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }
    public void setBuyPrice(int buyPrice) {
        this.buyPrice = buyPrice;
    }    
    
    // Image getter
    public BufferedImage getImage() {
        return image;
    }
}
