package SRC.UI;

import SRC.ENTITY.NPCEntity;
import SRC.ITEMS.Item;
import SRC.MAIN.GamePanel;
import java.awt.*;

public class NPCUi {
    private final GamePanel gamePanel;
    private NPCEntity giftingTargetNPC = null;
    private String messagePanelText = null;
    private long messagePanelTimestamp = 0;
    private static final int MESSAGE_PANE_DURATION_MS = 3500;

    public NPCUi(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void showMessagePanel(String text) {
        messagePanelText = text;
        messagePanelTimestamp = System.currentTimeMillis();
        gamePanel.repaint();
    }

    public void drawMessagePanel(Graphics2D g2) {
        if (messagePanelText != null) {
            long now = System.currentTimeMillis();
            if (now - messagePanelTimestamp > MESSAGE_PANE_DURATION_MS) {
                messagePanelText = null;
                return;
            }
            int panelWidth = 625;
            int panelHeight = 60;
            int x = (gamePanel.getScreenWidth() - panelWidth) / 2;
            int y = gamePanel.getScreenHeight() - panelHeight - 125;
            g2.setColor(new Color(0,0,0,200));
            g2.fillRoundRect(x, y, panelWidth, panelHeight, 18, 18);
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12));
            int textX = x + 15;
            int textY = y + 35;
            g2.drawString(messagePanelText, textX, textY);
        }
    }

    public void tryGiftToNearbyNPC() {
        NPCEntity nearbyNPC = getNearbyNPC(1); // 1 tile away only
        if (nearbyNPC != null) {
            gamePanel.setGameState(GamePanel.INVENTORY_STATE);
            giftingTargetNPC = nearbyNPC;
        }
    }

    public void tryTalkToNearbyNPC() {
        NPCEntity nearbyNPC = getNearbyNPC(1);
        if (nearbyNPC != null) {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream oldOut = System.out;
            try {
                System.setOut(new java.io.PrintStream(baos));
                nearbyNPC.interact(gamePanel.getPlayer());
            } finally {
                System.setOut(oldOut);
            }
            String npcReply = baos.toString().trim();
            if (npcReply.isEmpty()) npcReply = nearbyNPC.getNPCName() + ": ...";
            showMessagePanel(npcReply);
            gamePanel.addMinutes(10); // Tambah waktu 10 menit saat talking
        }
    }

    public void confirmGiftFromInventory() {
        if (giftingTargetNPC != null) {
            Item[] items = gamePanel.getPlayer().getInventoryItems();
            int selectedSlot = gamePanel.getMouseHandler().getSelectedSlotIndex();
            if (selectedSlot >= 0 && items[selectedSlot] != null) {
                String reactionMsg = giftingTargetNPC.receiveGift(items[selectedSlot]);
                showMessagePanel(reactionMsg);
                gamePanel.getPlayer().removeOneItemFromInventory(selectedSlot);
                gamePanel.getPlayer().setEnergy(gamePanel.getPlayer().getEnergy() - 5);
                gamePanel.addMinutes(10);
                giftingTargetNPC = null;
                gamePanel.setGameState(GamePanel.PLAY_STATE);
            }
        }
    }

    public NPCEntity getGiftingTargetNPC() {
        return giftingTargetNPC;
    }

    /**
     * Returns the first NPC exactly 'distance' tiles away from the player, or null if none.
     */
    public NPCEntity getNearbyNPC(int distance) {
        SRC.MAP.Map currentMap = gamePanel.getCurrentMap();
        if (currentMap instanceof SRC.MAP.NPC_HOUSE.NPCHouseMap) {
            java.util.ArrayList<NPCEntity> npcs = null;
            if (currentMap instanceof SRC.MAP.NPC_HOUSE.AbigailHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.AbigailHouseMap) currentMap).getNPCs();
            } else if (currentMap instanceof SRC.MAP.NPC_HOUSE.DascoHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.DascoHouseMap) currentMap).getNPCs();
            } else if (currentMap instanceof SRC.MAP.NPC_HOUSE.EmilyHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.EmilyHouseMap) currentMap).getNPCs();
            } else if (currentMap instanceof SRC.MAP.NPC_HOUSE.CarolineHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.CarolineHouseMap) currentMap).getNPCs();
            } else if (currentMap instanceof SRC.MAP.NPC_HOUSE.PerryHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.PerryHouseMap) currentMap).getNPCs();
            } else if (currentMap instanceof SRC.MAP.NPC_HOUSE.MayorTadiHouseMap) {
                npcs = ((SRC.MAP.NPC_HOUSE.MayorTadiHouseMap) currentMap).getNPCs();
            }
            if (npcs != null) {
                int playerTileX = gamePanel.getPlayer().getWorldX() / gamePanel.getTileSize();
                int playerTileY = gamePanel.getPlayer().getWorldY() / gamePanel.getTileSize();
                for (NPCEntity npc : npcs) {
                    if ((playerTileX == 4 && playerTileY == 3)) {
                        return npc;
                    }
                }
            }
        }
        return null;
    }
}
