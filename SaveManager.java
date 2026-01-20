import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * SaveManager.java
 * - View  existing save slots
 * - Load a saved game
 * - Create a new save in an empty slot
 * - Delete (reset) a save slot
 * 
 * Each slot shows:
 * - Character name, class, level, HP, Mana if save exists
 * - "CREATE SAVE" button if slot is empty
 * - "LOAD SAVE" and "RESET" buttons if slot has data
 * 
 */
public class SaveManager extends JPanel {
    
    // ==================== INSTANCE VARIABLES ====================
    private BufferedImage backgroundFrame;    // background image
    private EarthboundFrame parentFrame;      // main window reference
    private String username;                  // who is logged in
    
    /**
     * Constructor - creates the save manager screen.
     * 
     * @param username - the logged-in user's username
     */
    public SaveManager(String username) {
        this.username = username;
        setLayout(null);  // absolute positioning
        setBackground(new Color(70, 70, 70));
        setOpaque(false);  // allows custom painting
        
        loadBackground();
        
        // Wait for panel to be added to frame before getting parent reference
        SwingUtilities.invokeLater(() -> {
            parentFrame = (EarthboundFrame) SwingUtilities.getWindowAncestor(this);
            initializeUI();
        });
    }
    
    /**
     * Load the background image.
     * Falls back to placeholder if loading fails.
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
     * 
     * Creates the title and two save slot panels.
     */
    private void initializeUI() {
        // ========== TITLE ==========
        JLabel titleLabel = new JLabel("SELECT SAVE");
        titleLabel.setFont(new Font("CASTELLAR", Font.BOLD, 42));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBounds(200, 100, 600, 50);
        add(titleLabel);
        
        // ========== SAVE SLOTS ==========
        // Create two save slot panels at different Y positions
        createSaveSlotPanel(1, 250);  // Slot 1 at y=250
        createSaveSlotPanel(2, 450);  // Slot 2 at y=450
    }
    
    /**
     * Create a save slot panel with appropriate buttons.
     * 
     * Checks if the slot has data and shows different content:
     * - If save exists: shows player info + Load/Reset buttons
     * - If empty: shows Create Save button
     * 
     * @param slotNumber - which slot (1 or 2)
     * @param yPos - vertical position for the panel
     */
    private void createSaveSlotPanel(int slotNumber, int yPos) {
        // Create the container panel for this slot
        JPanel slotPanel = new JPanel();
        slotPanel.setLayout(null);
        slotPanel.setBounds(200, yPos, 600, 120);
        slotPanel.setBackground(new Color(80, 80, 80));
        // Gold border to make it look nice
        slotPanel.setBorder(BorderFactory.createLineBorder(new Color(218, 165, 32), 2));
        
        // ========== SLOT TITLE ==========
        JLabel slotTitle = new JLabel("SAVE SLOT #" + slotNumber);
        slotTitle.setFont(new Font("CASTELLAR", Font.BOLD, 20));
        slotTitle.setForeground(new Color(218, 165, 32));  // gold
        slotTitle.setBounds(10, 5, 200, 30);
        slotPanel.add(slotTitle);
        
        // Check if this slot has save data
        boolean slotExists = SaveFileManager.saveSlotExists(username, slotNumber);
        
        if (slotExists) {
            // ========== SLOT HAS DATA - Show player info ==========
            Player player = SaveFileManager.loadPlayer(username, slotNumber);
            
            if (player != null) {
                // Format player info string
                String playerInfo = String.format(
                    "%s | Class: %s | Level: %d | HP: %d | Mana: %d",
                    player.getCharacterName(),
                    player.getClassName(),
                    player.getLevel(),
                    player.getMaxHP(),
                    player.getMaxMana()
                );
                
                JLabel playerLabel = new JLabel(playerInfo);
                playerLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                playerLabel.setForeground(Color.WHITE);
                playerLabel.setBounds(10, 35, 580, 30);
                slotPanel.add(playerLabel);
            }
            
            // ========== LOAD SAVE BUTTON ==========
            JButton loadBtn = createSlotButton("LOAD SAVE");
            loadBtn.setBounds(300, 70, 130, 40);
            loadBtn.addActionListener(e -> {
                // Load the player and create a new game session
                Player player2 = SaveFileManager.loadPlayer(username, slotNumber);
                if (player2 != null) {
                    // difficulty 1 = normal mode for loaded saves
                    GameSession session = new GameSession(username, slotNumber, player2, 1);
                    parentFrame.switchPanel(new PreGameMenu(session));
                }
            });
            slotPanel.add(loadBtn);
            
            // ========== RESET (DELETE) BUTTON ==========
            JButton resetBtn = createSlotButton("RESET");
            resetBtn.setBounds(440, 70, 130, 40);
            resetBtn.setBackground(new Color(200, 50, 50));  // red = danger!
            resetBtn.addActionListener(e -> {
                // Confirm before deleting!
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this save?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
                    
                if (confirm == JOptionPane.YES_OPTION) {
                    // Delete the save and refresh the screen
                    SaveFileManager.deleteSave(username, slotNumber);
                    parentFrame.switchPanel(new SaveManager(username));
                }
            });
            slotPanel.add(resetBtn);
            
        } else {
            // ========== EMPTY SLOT - Show create button ==========
            JButton createBtn = createSlotButton("CREATE SAVE");
            createBtn.setBounds(450, 70, 140, 40);
            createBtn.addActionListener(e -> {
                // Go to class selection to create new character
                parentFrame.switchPanel(new SelectClass(username, slotNumber, null));
            });
            slotPanel.add(createBtn);
        }
        
        add(slotPanel);
    }
    
    /**
     * Helper method to create styled buttons for save slots.
     * 
     * All buttons have the same gold style with hover effects.
     * 
     * @param text - button label
     * @return styled JButton
     */
    private JButton createSlotButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("CASTELLAR", Font.BOLD, 12));
        btn.setForeground(Color.BLACK);
        btn.setBackground(new Color(218, 165, 32));  // gold
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createRaisedBevelBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect - brighter gold on mouse enter
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(255, 215, 0));  // bright gold
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
     * @param g - Graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        if (backgroundFrame != null) {
            g.drawImage(backgroundFrame, 0, 0, getWidth(), getHeight(), null);
        }
        super.paintComponent(g);
    }
}
