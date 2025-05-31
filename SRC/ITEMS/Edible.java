package SRC.ITEMS;

import SRC.ENTITY.Player;

public interface Edible {

    void consume(Player player);

    int getEnergyValue();
}
