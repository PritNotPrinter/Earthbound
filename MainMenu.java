import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * MainMenu.java - 
 * - A big title "EARTHBOUND"
 * - Login button (for existing users)
 * - Register button (for new users)
 * - How to Play button (instructions)
 * 
 * All buttons use a consistent gold styling to match the game's theme.
 * 
 * This class extends JPanel and uses custom paintComponent for
 * drawing the background image.
 */
public class MainMenu extends JPanel {
    
    // ==================== INSTANCE VARIABLES ====================
    private BufferedImage backgroundFrame;    // background image
    private EarthboundFrame parentFrame;      // reference to main window
    
    /**
     * Constructor - sets up the main menu panel.
     */
    public MainMenu() {
        setLayout(null);  // absolute positioning for precise button placement
        setBackground(new Color(40, 40, 40));  // dark gray fallback
        
        // Load the background image
        loadBackground();
        
        // Wait for panel to be added to frame, then set up UI
        // SwingUtilities.invokeLater ensures the parent frame exists
        SwingUtilities.invokeLater(() -> {
            parentFrame = (EarthboundFrame) SwingUtilities.getWindowAncestor(this);
            initializeUI();
        });
    }
    
    /**
     * Load the background image from assets.
     * Falls back to a placeholder if loading fails.
     */
    private void loadBackground() {
        try {
            File file = new File(AssetManager.getMainMenuFrame());
            if (file.exists()) {
                backgroundFrame = ImageIO.read(file);
            } else {
                // Create gray placeholder if image doesn't exist
                backgroundFrame = AssetManager.createPlaceholder(1000, 750);
            }
        } catch (Exception e) {
            backgroundFrame = AssetManager.createPlaceholder(1000, 750);
        }
    }
    
    /**
     * Set up all UI components (title, buttons).
     * 
     * Uses absolute positioning with setBounds() for precise control.
     */
    private void initializeUI() {
        // ========== TITLE LABEL ==========
        // Big "EARTHBOUND" text at the top
        JLabel titleLabel = new JLabel("EARTHBOUND");
        titleLabel.setFont(new Font("CASTELLAR", Font.BOLD, 56));  // fancy medieval font
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBounds(200, 150, 600, 80);
        add(titleLabel);
        
        // ========== LOGIN BUTTON ==========
        // Takes user to the login screen
        JButton loginBtn = createGoldButton("LOGIN");
        loginBtn.setBounds(350, 280, 300, 60);
        loginBtn.addActionListener(e -> {
            parentFrame.switchPanel(new Login());
        });
        add(loginBtn);
        
        // ========== REGISTER BUTTON ==========
        // Takes user to create a new account
        JButton registerBtn = createGoldButton("REGISTER");
        registerBtn.setBounds(350, 360, 300, 60);
        registerBtn.addActionListener(e -> {
            parentFrame.switchPanel(new Register());
        });
        add(registerBtn);
        
        // ========== HOW TO PLAY BUTTON ==========
        // Shows game instructions
        JButton howToPlayBtn = createGoldButton("HOW TO PLAY");
        howToPlayBtn.setBounds(350, 440, 300, 60);
        howToPlayBtn.addActionListener(e -> {
            parentFrame.switchPanel(new HowToPlay());
        });
        add(howToPlayBtn);
    }
    
    /**
     * Helper method to create gold-styled buttons.
     * 
     * All menu buttons use this for consistent styling.
     * Includes hover effect (brighter on mouse enter).
     * 
     * @param text - the button label
     * @return styled JButton
     */
    private JButton createGoldButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("CASTELLAR", Font.BOLD, 18));
        btn.setForeground(Color.BLACK);
        btn.setBackground(new Color(218, 165, 32));  // gold color
        btn.setFocusPainted(false);                   // no focus border
        btn.setBorder(BorderFactory.createRaisedBevelBorder());  // 3D raised look
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));  // hand cursor on hover
        
        // Add hover effect with MouseListener
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(255, 215, 0));  // brighter gold
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(218, 165, 32));  // back to normal
            }
        });
        
        return btn;
    }
    
    /**
     * Custom painting for background.
     * 
     * Called automatically by Swing when panel needs to be drawn.
     * Draws the background image scaled to fill the panel.
     * 
     * @param g - Graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);  // call parent first
        if (backgroundFrame != null) {
            g.drawImage(backgroundFrame, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
