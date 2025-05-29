package SRC.DATA;

import SRC.ITEMS.Tool;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;

/**
 * Data class for managing all tool items with static initialization
 */
public class ToolData {
    private static Map<String, Tool> tools = new HashMap<>();
    
    // Static initializer to load all tools
    static {
        initializeTools();
    }
    
    /**
     * Initialize all tool items
     */
    private static void initializeTools() {
        // Farming Tools
        tools.put("Hoe", new Tool("Hoe", 100, 50, "Used for tilling soil"));
        tools.put("Watering Can", new Tool("Watering Can", 150, 2000, "Used for watering crops"));
        tools.put("Scythe", new Tool("Scythe", 250, 100, "Used for harvesting crops"));
        
        // Mining Tools
        tools.put("Pickaxe", new Tool("Pickaxe", 500, 2000, "Used for mining rocks"));
        tools.put("Copper Pickaxe", new Tool("Copper Pickaxe", 1000, 5000, "Upgraded pickaxe"));
        tools.put("Iron Pickaxe", new Tool("Iron Pickaxe", 2500, 25000, "Advanced pickaxe"));
        
        // Fishing Tools
        tools.put("Fishing Rod", new Tool("Fishing Rod", 300, 500, "Used for catching fish"));
        tools.put("Fiberglass Rod", new Tool("Fiberglass Rod", 800, 1800, "Better fishing rod"));
        
        tools.put("Proposal Ring", new Tool("Proposal Ring", 3000, 250, "Used for Propoing NPC"));
        
        System.out.println("ToolData: Initialized " + tools.size() + " tool types");
    }
    
    /**
     * Get a tool by name
     * @param toolName Name of the tool
     * @return Tool object or null if not found
     */
    public static Tool getTool(String toolName) {
        return tools.get(toolName);
    }
    
    /**
     * Add tool to inventory with specified quantity (returns the tool item)
     * @param toolName Name of the tool to add
     * @param quantity Quantity to add (for compatibility, not used in return)
     * @return Tool item ready to be added to inventory
     */
    public static Tool addTool(String toolName, int quantity) {
        Tool tool = getTool(toolName);
        if (tool != null) {
            return tool;
        } else {
            System.err.println("Tool not found: " + toolName);
            return null;
        }
    }
    
    /**
     * Check if tool exists
     * @param toolName Name of the tool
     * @return true if tool exists
     */
    public static boolean hasTool(String toolName) {
        return tools.containsKey(toolName);
    }
    
    /**
     * Get tools by category
     * @param category Category to filter by
     * @return Map of tools for the specified category
     */
    public static Map<String, Tool> getToolsByCategory(String category) {
        Map<String, Tool> categoryTools = new HashMap<>();
        for (Map.Entry<String, Tool> entry : tools.entrySet()) {
            if (entry.getValue().getDescription().toLowerCase().contains(category.toLowerCase())) {
                categoryTools.put(entry.getKey(), entry.getValue());
            }
        }
        return categoryTools;
    }
    
    /**
     * Get all tools
     * @return Map of all tools
     */
    public static Map<String, Tool> getAllTools() {
        return new HashMap<>(tools);
    }
}
