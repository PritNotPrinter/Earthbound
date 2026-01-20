import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * SaveFileManager.java - Handles reading/writing game saves to files
 * - Saving player data to text files
 * - Loading player data from text files
 * - Checking if saves exist
 * - Deleting saves
 * 
 * className,characterName,level,currentHP,maxHP,currentMana,maxMana,attack,defense,experience,classData
 */
public class SaveFileManager {
    
    // Path to the saves folder
    private static final String SAVES_PATH = "saves/";
    
    /**
     * Static initializer block - runs when the class is first loaded.
     * 
     * This creates the saves directory if it doesn't exist.
     * Using a static block ensures this happens automatically
     * before any save/load operations are attempted.
     */
    static {
        try {
            // createDirectories creates all folders in the path if needed
            Files.createDirectories(Paths.get(SAVES_PATH));
        } catch (IOException e) {
            // save might fail later but game can run
            e.printStackTrace();
        }
    }
    
    /**
     * Save player data to a text file.
     * @param username - the logged-in player's username
     * @param saveSlot - which slot (1 or 2)
     * @param player - the Player object to save
     * @return true if save succeeded, false if it failed
     */
    public static boolean savePlayer(String username, int saveSlot, Player player) {
        // Build the filename: saves/username_slot1.txt
        String filename = SAVES_PATH + username + "_slot" + saveSlot + ".txt";
        
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename)))) {
            // Get the player's data as a single CSV line and write it
            writer.println(player.toSaveString());
            return true;  // success
            
        } catch (IOException e) {
            // Something went wrong 
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Load player data from a text file.
     * 
     * Reads the save file and recreates the Player object.
     * Uses the Player.fromSaveString()  method.
     * 
     * @param username - the logged-in player's username
     * @param saveSlot - which slot to load
     * @return the loaded Player object, or null if load failed
     */
    public static Player loadPlayer(String username, int saveSlot) {
        // Build the filename to look for
        String filename = SAVES_PATH + username + "_slot" + saveSlot + ".txt";
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Read the first  line
            String line = reader.readLine();
            
            // Make sure we got valid data
            if (line != null && !line.isEmpty()) {
                // Use the factory method to recreate the Player
                return Player.fromSaveString(line);
            }
            return null;  // empty file
            
        } catch (IOException e) {
            // File doesn't exist or couldn't be read
            e.printStackTrace();
            return null;
        }
    }
    

    public static boolean saveSlotExists(String username, int saveSlot) {
        File file = new File(SAVES_PATH + username + "_slot" + saveSlot + ".txt");
        return file.exists();
    }
    
    /**
     * Get all save slot numbers for a user.
     * 
     * @param username - username to check
     * @return List of slot numbers that have saves
     */
    public static List<Integer> getUserSaveSlots(String username) {
        List<Integer> slots = new ArrayList<>();
        
        try {
            // Find all files matching "username_slot*.txt"
            DirectoryStream<Path> stream = Files.newDirectoryStream(
                Paths.get(SAVES_PATH),
                username + "_slot*.txt" 
            );
            
            // Extract slot numbers from filenames
            for (Path path : stream) {
                String filename = path.getFileName().toString();
                // Remove prefix and suffix to get just the number
                String slotStr = filename.replace(username + "_slot", "").replace(".txt", "");
                slots.add(Integer.parseInt(slotStr));
            }
            
            // Sort so slots appear in order
            Collections.sort(slots);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return slots;
    }
    
    /**
     * Delete a save file.
     * @param username - username
     * @param saveSlot - slot to delete
     * @return true if file was deleted, false otherwise
     */
    public static boolean deleteSave(String username, int saveSlot) {
        try {
            // deleteIfExists returns true if file existed and was deleted
            return Files.deleteIfExists(
                Paths.get(SAVES_PATH + username + "_slot" + saveSlot + ".txt")
            );
        } catch (IOException e) {
            return false;
        }
    }
}
