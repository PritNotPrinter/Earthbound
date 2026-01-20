import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * SelectClass.java
 * Displays three class options with:
 * - Sprite preview
 * - Class name
 * - Stats and description
 * 
 * Available classes:
 * - Mage
 * - Barbarian
 * - Archer
 */
public class SelectClass extends JPanel {
    
    // ==================== INSTANCE VARIABLES ====================
    private BufferedImage backgroundFrame;    // background image
    private EarthboundFrame parentFrame;      // main window reference
    private String username;                  // logged-in user
    private int saveSlot;                     // which save slot to use
    private Player existingPlayer;            // null for new game, set for class change
    private String selectedClass = null;      // tracks which class is selected
    
    /**
     * Constructor - sets up the class selection screen.
     * 
     * @param username 
     * @param saveSlot 
     * @param existingPlayer
     */
    public SelectClass(String username, int saveSlot, Player existingPlayer) {
        this.username = username;
        this.saveSlot = saveSlot;
        this.existingPlayer = existingPlayer;
        
        setLayout(null);
        setBackground(new Color(70, 70, 70));
        setOpaque(false);
        
        loadBackground();
        
        SwingUtilities.invokeLater(() -> {
            parentFrame = (EarthboundFrame) SwingUtilities.getWindowAncestor(this);
            initializeUI();
        });
    }
    
    /**
     * Load background image.
     */
    private void loadBackground() {
        try {
            File file = new File(AssetManager.getGameMenuFrame());
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
     * 
     * Creates title, three class panels, and submit button.
     */
    private void initializeUI() {
        try {
            // ========== TITLE ==========
            JLabel titleLabel = new JLabel("SELECT CLASS");
            titleLabel.setFont(new Font("CASTELLAR", Font.BOLD, 42));
            titleLabel.setForeground(new Color(218, 165, 32));  // gold
            titleLabel.setHorizontalAlignment(JLabel.CENTER);
            titleLabel.setBounds(200, 50, 600, 50);
            add(titleLabel);
            
            // ========== LOAD SPRITES FOR EACH CLASS ==========
            // Get the first idle frame from each class's animations
            BufferedImage mageSprite = AssetManager.loadPlayerSprite("Mage", "idle");
            BufferedImage barbarianSprite = AssetManager.loadPlayerSprite("Barbarian", "idle");
            BufferedImage archerSprite = AssetManager.loadPlayerSprite("Archer", "idle");
            
            // ========== CREATE CLASS PANELS ==========
            createClassPanel("Mage", 200, 100,
                "HP: 40 | Mana: 80 | Attack: 30\nPowerful magic spells\nLow defense", 
                mageSprite);
            createClassPanel("Barbarian", 700, 100,
                "HP: 80 | Mana: 30 | Attack: 12\nRush attacks and blocking\nHigh durability", 
                barbarianSprite);
            createClassPanel("Archer", 450, 100,
                "HP: 60 | Mana: 50 | Attack: 13\nPrecise ranged attacks\nBalanced fighter", 
                archerSprite);
            
            // ========== SUBMIT BUTTON ==========
            JButton submitBtn = new JButton("SUBMIT");
            submitBtn.setFont(new Font("CASTELLAR", Font.BOLD, 18));
            submitBtn.setForeground(Color.BLACK);
            submitBtn.setBackground(new Color(218, 165, 32));
            submitBtn.setBounds(350, 600, 300, 50);
            submitBtn.setFocusPainted(false);
            submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            submitBtn.addActionListener(e -> handleClassSelection());
            add(submitBtn);
            
        } catch (Exception e) {
            System.err.println("Error initializing SelectClass UI: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Create a panel displaying one class option.
     * 
     * @param className - name of the class
     * @param xPos - horizontal position
     * @param yPos - vertical position
     * @param description - stats and description text
     * @param sprite - sprite image to display
     */
    private void createClassPanel(String className, int xPos, int yPos, String description, BufferedImage sprite) {
        try {
            // Create container panel
            JPanel classPanel = new JPanel();
            classPanel.setLayout(null);
            classPanel.setBounds(xPos, yPos, 140, 480);
            classPanel.setBackground(new Color(80, 80, 80));
            classPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
            classPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Make panel clickable for selection
            classPanel.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    // Set this class as selected
                    selectedClass = className;
                    updateClassPanelSelection();
                }
                public void mouseEntered(MouseEvent e) {
                    // Hover effect - lighter background
                    classPanel.setBackground(new Color(100, 100, 100));
                }
                public void mouseExited(MouseEvent e) {
                    // Reset background on mouse leave
                    classPanel.setBackground(new Color(80, 80, 80));
                }
            });
            
            // ========== SPRITE DISPLAY ==========
            JLabel spriteLabel;
            if (sprite != null) {
                // Scale sprite to fit the label (120x120)
                BufferedImage scaledSprite = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = scaledSprite.createGraphics();
                g2d.drawImage(sprite, 0, 0, 120, 120, null);
                g2d.dispose();
                spriteLabel = new JLabel(new ImageIcon(scaledSprite));
            } else {
                // Placeholder text if sprite fails to load
                spriteLabel = new JLabel("[Sprite]");
                spriteLabel.setFont(new Font("Arial", Font.BOLD, 12));
                spriteLabel.setForeground(new Color(150, 150, 150));
                spriteLabel.setHorizontalAlignment(JLabel.CENTER);
            }
            spriteLabel.setBounds(10, 20, 120, 120);
            spriteLabel.setBackground(new Color(50, 50, 50));
            spriteLabel.setOpaque(true);
            spriteLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            classPanel.add(spriteLabel);
            
            // ========== CLASS NAME ==========
            JLabel nameLabel = new JLabel(className.toUpperCase());
            nameLabel.setFont(new Font("CASTELLAR", Font.BOLD, 16));
            nameLabel.setForeground(new Color(218, 165, 32));  // gold
            nameLabel.setHorizontalAlignment(JLabel.CENTER);
            nameLabel.setBounds(10, 150, 120, 30);
            classPanel.add(nameLabel);
            
            // ========== DESCRIPTION TEXT AREA ==========
            // Shows stats and class description
            JTextArea descArea = new JTextArea(description);
            descArea.setFont(new Font("Arial", Font.PLAIN, 11));
            descArea.setForeground(Color.WHITE);
            descArea.setBackground(new Color(80, 80, 80));
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            descArea.setEditable(false);  // read-only
            descArea.setBounds(5, 185, 130, 280);
            classPanel.add(descArea);
            
            add(classPanel);
            
        } catch (Exception e) {
            System.err.println("Error creating class panel for " + className + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    private void updateClassPanelSelection() {
        repaint();
    }
    

    private void handleClassSelection() {
        // Make sure a class was selected
        if (selectedClass == null) {
            JOptionPane.showMessageDialog(this, "Please select a class", "Class Not Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Create character or use existing
        Player player;
        if (existingPlayer != null) {
            // If reselecting class for existing player
            player = existingPlayer;
        } else {
            // Prompt for character name
            String characterName = JOptionPane.showInputDialog(this, "Enter character name:");
            
            // Default name if empty
            if (characterName == null || characterName.isEmpty()) {
                characterName = "ok";  // fallback name 
            }
            
            // Create the appropriate Player subclass
            player = createPlayerByClass(selectedClass, characterName);
        }
        
        // Save the new character to file
        SaveFileManager.savePlayer(username, saveSlot, player);
        
        // Create game session and go to pre-game menu
        GameSession session = new GameSession(username, saveSlot, player, 1);
        parentFrame.switchPanel(new PreGameMenu(session));
    }
    

    private Player createPlayerByClass(String className, String characterName) {
        switch (className.toLowerCase()) {
            case "mage":
                return new Mage(characterName);
            case "barbarian":
                return new Barbarian(characterName);
            case "archer":
                return new Archer(characterName);
            default:
                // Fallback to Mage if something goes wrong, because Mage is easier for testing
                return new Mage(characterName);
        }
    }
    
    /**
     * Custom painting for background.
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Draw dark gray base
        g.setColor(new Color(70, 70, 70));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw background image on top
        if (backgroundFrame != null) {
            g.drawImage(backgroundFrame, 0, 0, getWidth(), getHeight(), null);
        }
        super.paintComponent(g);
    }
}
