package SRC.ITEMS;

import SRC.ENTITY.Player;

public class Food extends Item implements Edible {
    private int energy;    public Food(String name, int sellPrice, int buyPrice, int energy) {
        super(name, "Food", sellPrice, buyPrice);
        this.energy = energy;
        
        // Load image for this food item
        String imagePath = "RES/FOOD/" + name.toLowerCase().replace(" ", "_") + ".png";
        loadImage(imagePath);
    }
    // Setters
    public void setEnergy(int energy) {
        this.energy = energy;
    }
    @Override
    public int getEnergyValue() {
        return this.energy;
    }
    @Override
    public void consume(Player player) {
        player.setEnergy(player.getEnergy() + getEnergyValue());
        System.out.println("You consumed " + this.getName() + " and gained " + getEnergyValue() + " energy!");
    }
}
