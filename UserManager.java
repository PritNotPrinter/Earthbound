import java.io.*;
import java.nio.file.*;

/**
 * UserManager.java - Handles user account creation and login
 * - Creating new user accounts
 * - Validating login credentials
 * - Checking if usernames exist
text file in the users/ folder:
 * - Filename: username.txt
 * - Contents: the password (plain text)
 * 
 */
public class UserManager {
    
    // Path to the users folder 
    private static final String USERS_PATH = "users/";
    

    static {
        try {
            Files.createDirectories(Paths.get(USERS_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Check if a user account exists.
     * @param username - the username to check
     * @return true if user exists, false otherwise
     */
    public static boolean userExists(String username) {
        File userFile = new File(USERS_PATH + username + ".txt");
        return userFile.exists();
    }
    
    /**
     * Validate login credentials.
     * Checks if the password matches what's stored in the file.
     * @param username - the username trying to log in
     * @param password - the password they entered
     * @return true if credentials are valid, false otherwise
     */
    public static boolean validateUser(String username, String password) {
        // First check if user even exists
        if (!userExists(username)) return false;
        
        try {
            // Read the stored password from file
            String storedPassword = new String(Files.readAllBytes(
                Paths.get(USERS_PATH + username + ".txt")
            ));
            
            // Compare with entered password
            return storedPassword.equals(password);
            
        } catch (IOException e) {
            // File read error = login fails
            return false;
        }
    }
    
    /**
     * Create a new user account.
     * 
     * Creates a text file with the username as filename and
     * password as contents.
     * 
     * @param username - the new username
     * @param password - the new password
     * @return true if account created, false if user already exists
     */
    public static boolean createUser(String username, String password) {
        if (userExists(username)) {
            return false;
        }
        
        try {
            Files.write(
                Paths.get(USERS_PATH + username + ".txt"),
                password.getBytes()  
            );
            return true; 
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
