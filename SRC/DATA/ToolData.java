package SRC.DATA;

import SRC.ITEMS.Tool;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;


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
        tools.put("Hoe", new Tool("Hoe", 0, 0, "Used for tilling soil"));
        tools.put("Watering Can", new Tool("Watering Can", 0, 0, "Used for watering crops"));
        tools.put("Scythe", new Tool("Scythe", 0, 0, "Used for harvesting crops"));
        tools.put("Pickaxe", new Tool("Pickaxe", 0, 0, "Used for mining rocks"));
        tools.put("Fishing Rod", new Tool("Fishing Rod", 0, 0, "Used for catching fish"));
        tools.put("Proposal Ring", new Tool("Proposal Ring",0, 500, "Used for Propoing NPC"));
        
    }
    
    public static Tool getTool(String toolName) {
        return tools.get(toolName);
    }
    
    public static Tool addTool(String toolName, int quantity) {
        Tool tool = getTool(toolName);
        if (tool != null) {
            return tool;
        } else {
            System.err.println("Tool not found: " + toolName);
            return null;
        }
    }
    
    public static boolean hasTool(String toolName) {
        return tools.containsKey(toolName);
    }
    
    public static Map<String, Tool> getToolsByCategory(String category) {
        Map<String, Tool> categoryTools = new HashMap<>();
        for (Map.Entry<String, Tool> entry : tools.entrySet()) {
            if (entry.getValue().getDescription().toLowerCase().contains(category.toLowerCase())) {
                categoryTools.put(entry.getKey(), entry.getValue());
            }
        }
        return categoryTools;
    }
    
    public static Map<String, Tool> getAllTools() {
        return new HashMap<>(tools);
    }
}
