import java.io.File;

/**
 * AssetManager.java
 * - Finding the correct file paths for assets
 * - Loading sprite images from folders
 * - Creating placeholder images when assets are missing
 * - easier for one file to deal with all of the assets vs repeating it for each individual class
 */
public class AssetManager {
    

    public static String getAssetPath(String assetName) {
        
        return "C:\\Users\\sawks\\Home\\School\\Grade 12\\Compsci\\Culminating\\src\\assets\\" + assetName;
    }
    

    public static String getMainMenuFrame() {
        return getAssetPath("MainMenuFrame.png");
    }
    

    public static String getGameMenuFrame() {
        return getAssetPath("GameMenuFrame.png");
    }
    

    public static String getBackButton() {
        return getAssetPath("back.png");
    }
    

    public static String getPlayerAssetPath(String className) {
        // toLowerCase() ensures "Barbarian" and "barbarian" both work
        return getAssetPath("player_assets/" + className.toLowerCase());
    }
    

    public static String getEnemyAssetPath(String enemyType) {
        return getAssetPath("enemy_assets/" + enemyType.toLowerCase());
    }
    
 
    public static String getPlayerAnimationPath(String className, String animationType) {
        return getPlayerAssetPath(className) + "/" + animationType;
    }
    
    public static String getEnemyAnimationPath(String enemyType, String animationType) {
        return getEnemyAssetPath(enemyType) + "/" + animationType;
    }
    
    /**
     * Load a single sprite image from an animation folder
     * used for specific static images (like in the character selection screen or pregame menu)
     */
    public static java.awt.image.BufferedImage loadPlayerSprite(String className, String animationType) {
        String folderPath = getPlayerAnimationPath(className, animationType);
        File folder = new File(folderPath);
        
        // Check if folder exists
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Sprite folder not found: " + folderPath);
            return createPlaceholder(120, 120);  // return gray placeholder instead of null
        }

        // Find all image files in the folder
        File[] files = folder.listFiles((dir, name) -> 
            name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg")
        );
        
        // If we found at least one file, load and return the first one
        if (files != null && files.length > 0) {
            try {
                System.out.println("Loading sprite: " + files[0].getAbsolutePath());
                return javax.imageio.ImageIO.read(files[0]);
            } catch (Exception e) {
                System.err.println("Failed to load sprite: " + e.getMessage());
                e.printStackTrace();
                return createPlaceholder(120, 120);
            }
        }
        
        System.err.println("No image files found in: " + folderPath);
        return createPlaceholder(120, 120);
    }

    /**
     * Load a sprite for an enemy (from their idle animation folder).
     * 
     * Similar to loadPlayerSprite but specifically for enemies.
     * Always loads from "idle" folder since that's the default pose.
     * 
     * @param enemyType - type of enemy
     * @return the first idle frame, or placeholder if not found
     */
    public static java.awt.image.BufferedImage loadEnemySprite(String enemyType) {
        String folderPath = getEnemyAnimationPath(enemyType, "idle");
        File folder = new File(folderPath);
        
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Enemy sprite folder not found: " + folderPath);
            return createPlaceholder(120, 120);
        }

        File[] files = folder.listFiles((dir, name) -> 
            name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg")
        );
        
        if (files != null && files.length > 0) {
            try {
                System.out.println("Loading enemy sprite: " + files[0].getAbsolutePath());
                return javax.imageio.ImageIO.read(files[0]);
            } catch (Exception e) {
                System.err.println("Failed to load enemy sprite: " + e.getMessage());
                e.printStackTrace();
                return createPlaceholder(120, 120);
            }
        }
        
        System.err.println("No image files found in: " + folderPath);
        return createPlaceholder(120, 120);
    }
    
    /**
     * Create a placeholder image when actual assets can't be found.
     * 
     * Instead of crashing when an image is missing, create a gray box
     * with "[Placeholder]" text.
     * 
     * @param width - width of placeholder in pixels
     * @param height - height of placeholder in pixels
     * @return a BufferedImage with gray background and placeholder text
     */
    public static java.awt.image.BufferedImage createPlaceholder(int width, int height) {
        // Create a new blank image with RGB color mode
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(
            width, height, java.awt.image.BufferedImage.TYPE_INT_RGB
        );
        
        // Get a Graphics2D object 
        java.awt.Graphics2D g = img.createGraphics();
        
        // Fill with gray background
        g.setColor(new java.awt.Color(100, 100, 100));
        g.fillRect(0, 0, width, height);
        
        // Draw white text saying "[Placeholder]"
        g.setColor(java.awt.Color.WHITE);
        g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));
        g.drawString("[Placeholder]", width / 2 - 50, height / 2);  
        
        g.dispose();
        
        return img;
    }
}
