package SRC.ITEMS;

import SRC.ENTITY.Player;

public interface Edible {
    // Method to consume the item
    void consume(Player player);
    // Method to get the energy value of the item
    int getEnergyValue();
}
