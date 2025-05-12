package SRC.TILE;

import java.awt.Image;

public class Tile {
    
    public Image image;
    public boolean collision = false;
    
    public Tile() {
        this.image = null;
        this.collision = false;
    }
    
    public Tile(Image image, boolean collision) {
        this.image = image;
        this.collision = collision;
    }
}
