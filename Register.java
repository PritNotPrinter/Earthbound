import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Register.java
 *
 * - Username text field
 * - Password field (hidden characters)
 * - Create Account button
 * - Error/success message display
 * - Back button to return to main menu
 * 
 * - Username must be at least 3 characters
 * - Password must be at least 4 characters
 * - Username can't already exist
 * On successful registration, user is redirected to Login after 2 seconds.
 * This class extends JPanel and uses custom paintComponent for background.
 */
public class Register extends JPanel {
    
    // ==================== INSTANCE VARIABLES ====================
    private BufferedImage backgroundFrame;    // background image
    private EarthboundFrame parentFrame;      // main window reference
    private JTextField usernameField;         // input for username
    private JPasswordField passwordField;     // input for password (hidden)
    private JLabel errorLabel;                // displays error/success messages
    
    /**
     * Constructor - sets up the register panel.
     */
    public Register() {
        setLayout(null);  // absolute positioning
        setBackground(new Color(40, 40, 40));
        
        loadBackground();
        
        // Get parent frame reference after panel is added
        SwingUtilities.invokeLater(() -> {
            parentFrame = (EarthboundFrame) SwingUtilities.getWindowAncestor(this);
            initializeUI();
        });
    }
    
    /**
     * Load background image from assets.
     */
    private void loadBackground() {
        try {
            File file = new File(AssetManager.getMainMenuFrame());
            if (file.exists()) {
                backgroundFrame = ImageIO.read(file);
            } else {
                backgroundFrame = AssetManager.createPlaceholder(1000, 750);
            }
        } catch (Exception e) {
            backgroundFrame = AssetManager.createPlaceholder(1000, 750);
        }
    }
    
    /**
     * Set up all UI components.
     */
    private void initializeUI() {
        // ========== BACK BUTTON ==========
        // Returns user to main menu
        JButton backBtn = new JButton();
        try {
            // Try to load back arrow image
            File file = new File(AssetManager.getBackButton());
            if (file.exists()) {
                BufferedImage backImg = ImageIO.read(file);
                // Scale image to fit button (smaller than Login's)
                backBtn.setIcon(new ImageIcon(backImg.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
            } else {
                backBtn.setText("BACK");
            }
        } catch (Exception e) {
            backBtn.setText("BACK");
        }
        backBtn.setBounds(200, 120, 60, 60);
        backBtn.setBackground(new Color(218, 165, 32));
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> parentFrame.switchPanel(new MainMenu()));
        add(backBtn);
        
        // ========== TITLE ==========
        JLabel titleLabel = new JLabel("REGISTER");
        titleLabel.setFont(new Font("CASTELLAR", Font.BOLD, 48));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBounds(200, 150, 600, 60);
        add(titleLabel);
        
        // ========== USERNAME LABEL ==========
        JLabel usernameLabel = new JLabel("USERNAME");
        usernameLabel.setFont(new Font("CASTELLAR", Font.BOLD, 18));
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setBounds(250, 250, 150, 30);
        add(usernameLabel);
        
        // ========== USERNAME INPUT ==========
        usernameField = new JTextField();
        usernameField.setBounds(430, 250, 250, 35);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        add(usernameField);
        
        // ========== PASSWORD LABEL ==========
        JLabel passwordLabel = new JLabel("PASSWORD");
        passwordLabel.setFont(new Font("CASTELLAR", Font.BOLD, 18));
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setBounds(250, 310, 150, 30);
        add(passwordLabel);
        
        // ========== PASSWORD INPUT ==========
        passwordField = new JPasswordField();
        passwordField.setBounds(430, 310, 250, 35);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        add(passwordField);
        
        // ========== ERROR/SUCCESS MESSAGE LABEL ==========
        // Shows validation errors or success message
        errorLabel = new JLabel();
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        errorLabel.setForeground(new Color(255, 100, 100));  // starts as red
        errorLabel.setBounds(250, 360, 450, 30);
        add(errorLabel);
        
        // ========== CREATE ACCOUNT BUTTON ==========
        JButton createBtn = new JButton("CREATE ACCOUNT");
        createBtn.setFont(new Font("CASTELLAR", Font.BOLD, 18));
        createBtn.setForeground(Color.BLACK);
        createBtn.setBackground(new Color(218, 165, 32));
        createBtn.setBounds(325, 420, 350, 60);
        createBtn.setFocusPainted(false);
        createBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createBtn.addActionListener(e -> handleRegister());
        add(createBtn);
    }
    
    /**
     * Handle the registration attempt when button is clicked.
     * 
     * Validates input:
     * - Neither field can be empty
     * - Username must be 3+ characters
     * - Password must be 4+ characters
     * - Username must not already exist
     * 
     * On success, shows green message and redirects to Login after 2 seconds.
     */
    private void handleRegister() {
        // Get the entered values
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        // Validate: no empty fields
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields");
            return;
        }
        
        // Validate: username length
        if (username.length() < 3) {
            errorLabel.setText("Username must be at least 3 characters");
            return;
        }
        
        // Validate: password length
        if (password.length() < 4) {
            errorLabel.setText("Password must be at least 4 characters");
            return;
        }
        
        // Validate: username uniqueness
        if (UserManager.userExists(username)) {
            errorLabel.setText("Username already exists");
            return;
        }
        
        // Try to create the account
        if (UserManager.createUser(username, password)) {
            // Show green message
            errorLabel.setForeground(new Color(100, 255, 100));  // change to green
            errorLabel.setText("Account created! Redirecting to login...");
            
            // Wait 2 seconds then go to login screen
            Timer timer = new Timer(2000, e -> parentFrame.switchPanel(new Login()));
            timer.setRepeats(false);  // only fire once
            timer.start();
        } else {
            // Something went wrong with file creation
            errorLabel.setText("Error creating account. Try again.");
        }
    }
    
    /**
     * Custom painting for background.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundFrame != null) {
            g.drawImage(backgroundFrame, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
