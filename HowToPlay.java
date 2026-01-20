import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;


public class HowToPlay extends JPanel {
    
    // ==================== INSTANCE VARIABLES ====================
    private BufferedImage backgroundFrame;    // background image
    private EarthboundFrame parentFrame;      // main window reference
    

    public HowToPlay() {
        setLayout(null);
        setBackground(new Color(40, 40, 40));  // dark gray fallback
        
        loadBackground();
        
        // Wait for component to be added before getting parent
        SwingUtilities.invokeLater(() -> {
            parentFrame = (EarthboundFrame) SwingUtilities.getWindowAncestor(this);
            initializeUI();
        });
    }
    

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
    

    private void initializeUI() {
        //  BACK BUTTON 
        JButton backBtn = new JButton();
        try {
            // Try to load back button image
            File file = new File(AssetManager.getBackButton());
            if (file.exists()) {
                BufferedImage backImg = ImageIO.read(file);
                backBtn.setIcon(new ImageIcon(backImg.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH)));
            } else {
                // Fall back to text if image not found
                backBtn.setText("BACK");
            }
        } catch (Exception e) {
            backBtn.setText("BACK");
        }
        backBtn.setBounds(200, 100, 60, 60);
        backBtn.setBackground(new Color(218, 165, 32));  // gold
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> parentFrame.switchPanel(new MainMenu()));
        add(backBtn);
        
        // ========== TITLE ==========
        JLabel titleLabel = new JLabel("HOW TO PLAY");
        titleLabel.setFont(new Font("CASTELLAR", Font.BOLD, 42));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBounds(200, 115, 600, 50);
        add(titleLabel);
        

        JTextArea instructionsArea = new JTextArea();
        instructionsArea.setText(
            "GAME OBJECTIVE:\n" +
            "Defeat all enemies across multiple stages to complete the level.\n\n" +
            "GAMEPLAY:\n" +
            "• Choose your character class: Mage, Barbarian, or Archer\n" +
            "• Each class has unique abilities and stats\n" +
            "• During combat, select Attack or Defense actions\n" +
            "• Manage your Health (HP) and Mana\n" +
            "• Defeat enemies to gain experience points\n\n" +
            "CHARACTER CLASSES:\n" +
            "MAGE: High Attack & Mana, Low HP. Use magic spells\n" +
            "BARBARIAN: High HP, Low Mana. Rush and Punch attacks\n" +
            "ARCHER: Balanced stats. Quick, precise attacks\n\n" +
            "COMBAT:\n" +
            "• Each turn, select an attack or defense action\n" +
            "• Attacks deal damage to the enemy\n" +
            "• Defenses reduce incoming damage or heal\n" +
            "• Defeat the enemy to earn rewards and progress"
        );
        instructionsArea.setFont(new Font("CASTELLAR", Font.BOLD, 16));
        instructionsArea.setForeground(new Color(218, 165, 32));  
        instructionsArea.setBackground(new Color(40, 40, 40));   
        instructionsArea.setLineWrap(true);    
        instructionsArea.setWrapStyleWord(true); 
        instructionsArea.setEditable(false);     
        instructionsArea.setMargin(new Insets(15, 15, 15, 15));  
        instructionsArea.setCaretColor(new Color(218, 165, 32)); 
        instructionsArea.setBounds(150, 170, 700, 450);
        
        instructionsArea.setBorder(BorderFactory.createLineBorder(new Color(218, 165, 32), 3));
        add(instructionsArea);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw background image if loaded
        if (backgroundFrame != null) {
            g.drawImage(backgroundFrame, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
