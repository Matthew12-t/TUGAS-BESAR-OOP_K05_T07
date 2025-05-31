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
    
    // NPC Interaction Menu
    private boolean npcInteractionMenuOpen = false;
    private NPCEntity npcInteractionTarget = null;
    private int selectedMenuOption = 0; // 0=Talk, 1=Gift, 2=Propose
    private static final String[] MENU_OPTIONS = {"[T] Talking with NPC", "[G] Gift NPC", "[P] Propose/Married NPC"};

    public NPCUi(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void showMessagePanel(String text) {
        messagePanelText = text;
        messagePanelTimestamp = System.currentTimeMillis();
        gamePanel.repaint();
    }    public void drawMessagePanel(Graphics2D g2) {
        // Draw NPC interaction menu if open
        if (npcInteractionMenuOpen && npcInteractionTarget != null) {
            drawNPCInteractionMenu(g2);
            return;
        }
        
        // Draw regular message panel
        if (messagePanelText != null) {
            long now = System.currentTimeMillis();
            if (now - messagePanelTimestamp > MESSAGE_PANE_DURATION_MS) {
                messagePanelText = null;
                return;
            }
            int panelWidth = 625;
            int panelHeight = 100;
            int x = (gamePanel.getScreenWidth() - panelWidth) / 2;
            int y = gamePanel.getScreenHeight() - panelHeight - 125;
            g2.setColor(new Color(0,0,0,200));
            g2.fillRoundRect(x, y, panelWidth, panelHeight, 18, 18);
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12));
            int textX = x + 15;
            int textY = y + 35;
            // Support multi-line text
            String[] lines = messagePanelText.split("\\n");
            for (String line : lines) {
                g2.drawString(line, textX, textY);
                textY += g2.getFontMetrics().getHeight() + 10;
            }
        }
    }
    
    /**
     * Draw the NPC interaction menu with highlighting
     */
    private void drawNPCInteractionMenu(Graphics2D g2) {
        int panelWidth = 650;
        int panelHeight = 150;
        int x = (gamePanel.getScreenWidth() - panelWidth) / 2;
        int y = gamePanel.getScreenHeight() - panelHeight - 50;
        
        // Draw panel background
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRoundRect(x, y, panelWidth, panelHeight, 18, 18);
        
        // Draw title
        g2.setColor(Color.WHITE);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14));
        String title = "Interact with " + npcInteractionTarget.getNPCName();
        int titleX = x + (panelWidth - g2.getFontMetrics().stringWidth(title)) / 2;
        g2.drawString(title, titleX, y + 30);
        
        // Draw menu options
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 12));
        int optionY = y + 60;
        int optionSpacing = 30;
        
        for (int i = 0; i < MENU_OPTIONS.length; i++) {
            if (i == selectedMenuOption) {
                // Highlight selected option in yellow
                g2.setColor(Color.YELLOW);
            } else {
                g2.setColor(Color.WHITE);
            }
            
            int optionX = x + 20;
            g2.drawString(MENU_OPTIONS[i], optionX, optionY);
            optionY += optionSpacing;
        }
        
        // Draw instructions
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 10));
        String instruction = "Press T, G, or P to select option, then ENTER to confirm. Q to cancel.";
        int instrX = x + (panelWidth - g2.getFontMetrics().stringWidth(instruction)) / 2;
        g2.drawString(instruction, instrX, y + panelHeight - 15);
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
    public void showNPCInteractionMenuPanel() {
        // This method now just opens the menu, actual display is handled in drawMessagePanel
        // The old implementation is removed
    }
    
    /**
     * Open the NPC interaction menu
     */
    public void openNPCInteractionMenu(NPCEntity npc) {
        this.npcInteractionMenuOpen = true;
        this.npcInteractionTarget = npc;
        this.selectedMenuOption = 0; // Default to Talk option
        this.messagePanelText = null; // Clear any existing message
        gamePanel.repaint();
    }
    
    /**
     * Close the NPC interaction menu
     */
    public void closeNPCInteractionMenu() {
        this.npcInteractionMenuOpen = false;
        this.npcInteractionTarget = null;
        this.selectedMenuOption = 0;
        gamePanel.repaint();
    }
    
    /**
     * Check if NPC interaction menu is open
     */
    public boolean isNPCInteractionMenuOpen() {
        return npcInteractionMenuOpen;
    }
    
    /**
     * Handle T key press - select Talk option or execute if already selected
     */
    public void handleTalkKey() {
        if (npcInteractionMenuOpen) {
            if (selectedMenuOption == 0) {
                // Execute talk action
                executeTalkAction();
            } else {
                // Highlight talk option
                selectedMenuOption = 0;
                gamePanel.repaint();
            }
        }
    }
    
    /**
     * Handle G key press - select Gift option or execute if already selected  
     */
    public void handleGiftKey() {
        if (npcInteractionMenuOpen) {
            if (selectedMenuOption == 1) {
                // Execute gift action
                executeGiftAction();
            } else {
                // Highlight gift option
                selectedMenuOption = 1;
                gamePanel.repaint();
            }
        }
    }
    
    /**
     * Handle P key press - select Propose option or execute if already selected
     */
    public void handleProposeKey() {
        if (npcInteractionMenuOpen) {
            if (selectedMenuOption == 2) {
                // Execute propose action
                executeProposeAction();
            } else {
                // Highlight propose option
                selectedMenuOption = 2;
                gamePanel.repaint();
            }
        }
    }
    
    /**
     * Handle Enter key press - execute selected action
     */
    public void handleEnterKey() {
        if (npcInteractionMenuOpen) {
            switch (selectedMenuOption) {
                case 0:
                    executeTalkAction();
                    break;
                case 1:
                    executeGiftAction();
                    break;
                case 2:
                    executeProposeAction();
                    break;
            }
        }
    }
    
    /**
     * Execute talk action with the target NPC
     */
    private void executeTalkAction() {
        if (npcInteractionTarget != null) {
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            java.io.PrintStream oldOut = System.out;
            try {
                System.setOut(new java.io.PrintStream(baos));
                npcInteractionTarget.interact(gamePanel.getPlayer());
            } finally {
                System.setOut(oldOut);
            }
            String npcReply = baos.toString().trim();
            if (npcReply.isEmpty()) npcReply = npcInteractionTarget.getNPCName() + ": ...";
            showMessagePanel(npcReply);
            gamePanel.addMinutes(10);
        }
        closeNPCInteractionMenu();
    }
    
    /**
     * Execute gift action with the target NPC
     */
    private void executeGiftAction() {
        closeNPCInteractionMenu();
        tryGiftToNearbyNPC();        
    }
    
    /**
     * Execute propose action with the target NPC
     */
    private void executeProposeAction() {
        if (npcInteractionTarget != null) {
            String status = npcInteractionTarget.getRelationshipStatus();
            if (status.equals("fiance")) {
                npcInteractionTarget.performAction(gamePanel.getPlayer(), "married");
            } else {
                npcInteractionTarget.performAction(gamePanel.getPlayer(), "propose");
            }
            //showMessagePanel(npcInteractionTarget.getNPCName() + ": Action performed.");
        }
        closeNPCInteractionMenu();
    }

    /**
     * Get the current NPC interaction target
     */
    public NPCEntity getNPCInteractionTarget() {
        return npcInteractionTarget;
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
                    if ((playerTileX == 6 && playerTileY == 5)) {
                        return npc;
                    }
                }
            }
        } else if (currentMap instanceof SRC.MAP.StoreMap) {
            java.util.ArrayList<NPCEntity> npcs = ((SRC.MAP.StoreMap) currentMap).getNPCs();
            int playerTileX = gamePanel.getPlayer().getWorldX() / gamePanel.getTileSize();
            int playerTileY = gamePanel.getPlayer().getWorldY() / gamePanel.getTileSize();
            for (NPCEntity npc : npcs) {
                if ((playerTileX == 4 && playerTileY == 5)) {
                        return npc;
                    }
            }
        }
        return null;
    }

    public boolean isMessagePanelActive() {
        if (messagePanelText != null) {
            long now = System.currentTimeMillis();
            return (now - messagePanelTimestamp <= MESSAGE_PANE_DURATION_MS);
        }
        return false;
    }
    
    /**
     * Clear the message panel (dismiss message)
     */
    public void clearMessagePanel() {
        messagePanelText = null;
        gamePanel.repaint();
    }
}
