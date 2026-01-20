import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * AnimationManager.java - Handles loading and playing sprite animations
 * 
 * This class is responsible for:
 * 1. Loading PNG image files from folders (each png is a frame)
 * 2. Cycling through frames to create animation effects
 * 
 * Each character (player or enemy) gets their own AnimationManager instance.
 * The manager holds multiple animations (idle, attack, etc.) and can switch between them.
 */
public class AnimationManager {
    
    // HashMap storing all animations - key is animation name (like "idle"), value is list of frames
    private HashMap<String, ArrayList<BufferedImage>> animations;
    
    // Which character class this manager belongs to (for debugging mostly)
    private String characterClass;
    
    // Which frame we're currently showing (0, 1, 2, etc.)
    private int currentFrameIndex;
    
    // Timestamp of last changed frames, controls animationspeed
    private long lastFrameTime;
    
    // milliseconds between frames (animation speed)
    private int frameDelay; 
    
    /**
     * Constructor - creates a new animation manager for a character.
     * @param characterClass - name of the character (Barbarian, Mage, etc.)
     */
    public AnimationManager(String characterClass) {
        this.characterClass = characterClass;                    // store what this manager is for
        this.animations = new HashMap<>();                       // empty map to hold the animation frames
        this.currentFrameIndex = 0;                              // start at first frame
        this.lastFrameTime = System.currentTimeMillis();         // record current time
        this.frameDelay = 100;                                   // default 100ms between frames (10 FPS)
    }
    
    /**
     * Load an animation from a folder of image files.
     * 
     * This reads all PNG/JPG files from a folder, sorts them by number,
     * and stores them as frames for the named animation.
     * 
     * Example: loadAnimation("idle", "assets/player_assets/barbarian/idle")
     * This would load all images from the idle folder and store them under "idle".
     * 
     * @param animationName - what to call this animation (used to retrieve it later)
     * @param folderPath - path to folder containing the image files
     * @return true if loading succeeded, false if it failed
     */
    public boolean loadAnimation(String animationName, String folderPath) {
        // ArrayList to hold all the frames we load
        ArrayList<BufferedImage> frames = new ArrayList<>();
        
        // Create a File object for the folder
        File folder = new File(folderPath);
        
        // Check if the folder actually exists and is a directory
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Animation folder not found: " + folderPath);
            return false;  // if folder doesnt exist
        }
        
        // Get list of image files in the folder
        // The lambda filter only accepts .png and .jpg files, ignores thumbnails and spritesheets
        File[] files = folder.listFiles((dir, name) -> {
            String lowerName = name.toLowerCase();  
            return (lowerName.matches("\\d+\\.png") ||          // matches files like "1.png", "2.png"
                    lowerName.matches("\\d+\\.jpg") ||          // matches files like "1.jpg", "2.jpg"
                    lowerName.contains(".png") ||               // any PNG file
                    lowerName.contains(".jpg")) &&              // any JPG file
                   !lowerName.equals("thumb.db") &&             // ignore Windows thumbnail cache
                   !lowerName.contains("spritesheet");          // ignore spritesheet files (because there's 1 spritesheet file in goblin\idle)
        });
        
        if (files == null || files.length == 0) {
            System.err.println("No animation frames found in: " + folderPath);
            return false;
        }
        
        // Sort files by the number in their filename
        // This ensures frames play in correct order (1.png, 2.png, 3.png, etc.)
        java.util.Arrays.sort(files, (f1, f2) -> {
            // Extract just the digits from filenames
            String name1 = f1.getName().replaceAll("\\D", "");  // remove all non-digits
            String name2 = f2.getName().replaceAll("\\D", "");
            
            // If no numbers found in either file, sort alphabetically instead
            if (name1.isEmpty() && name2.isEmpty()) {
                return f1.getName().compareTo(f2.getName());
            }
            // Files without numbers go to the end
            if (name1.isEmpty()) return 1;
            if (name2.isEmpty()) return -1;
            
            // Compare  numbers
            int num1 = Integer.parseInt(name1);
            int num2 = Integer.parseInt(name2);
            return Integer.compare(num1, num2);
        });
        
        // Load each image file into memory
        for (File file : files) {
            try {
                // ImageIO.read() loads the image file into a BufferedImage
                BufferedImage frame = ImageIO.read(file);
                if (frame != null) {
                    frames.add(frame);  // add to  frames list
                }
            } catch (Exception e) {
                // If one file fails to load, skip it
                System.err.println("Failed to load frame: " + file.getName());
            }
        }
        
        // Ensure at least 1 frame loaded
        if (frames.isEmpty()) {
            System.err.println("No frames successfully loaded from: " + folderPath);
            return false;
        }
        
        // Store animation in hashmap
        animations.put(animationName, frames);
        System.out.println("Loaded animation '" + animationName + "' with " + frames.size() + " frames");
        return true;
    }
    
    /**
     * Get the current frame of an animation.
     * Handles the frame cycling automatically - each time frameDelay passes, moves to the next frame.
     * @param animationName - which animation to get a frame from ("idle", "attack", etc.)
     * @return the current BufferedImage frame, or a placeholder if animation not found
     */
    public BufferedImage getFrame(String animationName) {
        ArrayList<BufferedImage> frames = animations.get(animationName);
        
        // If animation doesn't exist or is empty, return a placeholder image
        if (frames == null || frames.isEmpty()) {
            return AssetManager.createPlaceholder(128, 128);
        }
        
        // Check if enough time has passed to advance to next frame
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastFrameTime >= frameDelay) {
            // Move to next frame, wrapping around to 0 when we reach the end through the modulo operator
            currentFrameIndex = (currentFrameIndex + 1) % frames.size();
            lastFrameTime = currentTime;  // record when we changed frames
        }
        
        // Return the current frame
        return frames.get(currentFrameIndex);
    }
    
    /**
     * Reset animation to the first frame.
     * 
     * Call this when switching animations or resetting them
     */
    public void resetAnimation() {
        currentFrameIndex = 0;                           
        lastFrameTime = System.currentTimeMillis();     
    }
    
    /**
     * Set animationspeed
     */
    public void setFrameDelay(int delayMs) {
        this.frameDelay = delayMs;
    }
    
    /**
     * Check if an animation has been loaded.
     * 
     * @param animationName - name of animation to check
     * @return true if animation exists and has at least one frame
     */
    public boolean hasAnimation(String animationName) {
        return animations.containsKey(animationName) && !animations.get(animationName).isEmpty();
    }

    /**
     * Get how many frames an animation has.
     * 
     * @param animationName - name of animation to check
     * @return number of frames, or 0 if animation doesn't exist
     */
    public int getFrameCount(String animationName) {
        ArrayList<BufferedImage> frames = animations.get(animationName);
        return frames != null ? frames.size() : 0;
    }
}
