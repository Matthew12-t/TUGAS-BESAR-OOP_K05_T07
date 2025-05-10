package SRC.ITEMS;

import SRC.ENTITY.Player;

public class Crop extends Item implements Edible {
    private int energy;
    private int cropPerHarvest;
    public Crop(String name, int sellPrice, int buyPrice, int cropPerHarvest) {
        super(name, "Crop", sellPrice, buyPrice);
        this.energy = 3;
        this.cropPerHarvest = cropPerHarvest;
    }
    
    public int getCropPerHarvest() {
        return cropPerHarvest;
    }
    // Setters
    public void setEnergyValue(int energy) {
        this.energy = energy;
    }
    public void setCropPerHarvest(int cropPerHarvest) {
        this.cropPerHarvest = cropPerHarvest;
    }
    
    public int getEnergyValue() {
        return this.energy;
    }
    public void consume(Player player) {
        player.setEnergy(player.getEnergy() + getEnergyValue());
        System.out.println("You consumed " + this.getName() + " and gained " + getEnergyValue() + " energy!");
    }
}
