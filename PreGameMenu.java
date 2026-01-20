import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * PreGameMenu.java
 * - "PREPARE FOR BATTLE" title
 * - Difficulty and stage info
 * - Player character info with sprite (left side)
 * - "VS" in the middle
 * - Enemy info with sprite (right side)
 * - "START BATTLE" button
 */
public class PreGameMenu extends JPanel {
    
    // ==================== INSTANCE VARIABLES ====================
    private BufferedImage backgroundFrame;    // background image
    private EarthboundFrame parentFrame;      // main window reference
    private GameSession session;              // game state data
    private BufferedImage playerSprite;       // player's sprite for preview
    private BufferedImage enemySprite;        // enemy's sprite for preview
    
    /**
     * Constructor - sets up the pre-battle screen.
     * 
     * @param session - the current game session with player/enemy data
     */
    public PreGameMenu(GameSession session) {
        this.session = session;
        setLayout(null);
        setOpaque(false);
        
        loadBackground();
        loadSprites();
        
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
            backgroundFrame = file.exists() ? ImageIO.read(file) : AssetManager.createPlaceholder(1000, 750);
        } catch (Exception e) {
            backgroundFrame = AssetManager.createPlaceholder(1000, 750);
        }
    }
    
    /**
     * Load sprites for player and enemy preview.
     * 
     * Uses AssetManager to get the first idle frame for each.
     */
    private void loadSprites() {
        // Load player sprite based on their class
        Player player = session.getPlayer();
        playerSprite = AssetManager.loadPlayerSprite(player.getClassName(), "idle");
        
        // Load enemy sprite based on their type
        Enemy enemy = session.getCurrentEnemy();
        enemySprite = AssetManager.loadEnemySprite(enemy.getEnemyType());
    }
    
    /**
     * Set up all UI components.
     * 
     * Creates the versus screen layout with player on left,
     * enemy on right, and battle button at bottom.
     */
    private void initializeUI() {
        Player player = session.getPlayer();
        Enemy enemy = session.getCurrentEnemy();
        
        // ========== TITLE ==========
        JLabel titleLabel = new JLabel("PREPARE FOR BATTLE");
        titleLabel.setFont(new Font("CASTELLAR", Font.BOLD, 32));
        titleLabel.setForeground(new Color(218, 165, 32));  // gold
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBounds(200, 100, 600, 40);
        add(titleLabel);
        
        // ========== DIFFICULTY AND STAGE INFO ==========
        JLabel diffLabel = new JLabel(
            "Difficulty: " + (session.getDifficulty() == 1 ? "NORMAL" : "HARD") + " | Stage: " + session.getStage()
        );
        diffLabel.setFont(new Font("CASTELLAR", Font.BOLD, 18));
        diffLabel.setForeground(Color.WHITE);
        diffLabel.setHorizontalAlignment(JLabel.CENTER);
        diffLabel.setBounds(200, 145, 600, 25);
        add(diffLabel);
        
        // ========== PLAYER INFO (LEFT SIDE) ==========
        JLabel playerTitle = new JLabel("YOUR CHARACTER");
        playerTitle.setFont(new Font("CASTELLAR", Font.BOLD, 16));
        playerTitle.setForeground(new Color(100, 200, 100));  // green for player
        playerTitle.setBounds(150, 200, 200, 25);
        add(playerTitle);
        
        // Player name
        JLabel playerName = new JLabel(player.getCharacterName());
        playerName.setFont(new Font("Arial", Font.BOLD, 14));
        playerName.setForeground(Color.WHITE);
        playerName.setBounds(150, 370, 200, 20);
        add(playerName);
        
        // Player class and level
        JLabel playerClass = new JLabel("Class: " + player.getClassName() + " | Level: " + player.getLevel());
        playerClass.setFont(new Font("Arial", Font.PLAIN, 12));
        playerClass.setForeground(Color.LIGHT_GRAY);
        playerClass.setBounds(150, 390, 200, 20);
        add(playerClass);
        
        // Player stats
        JLabel playerStats = new JLabel("HP: " + player.getMaxHP() + " | Mana: " + player.getMaxMana() + " | ATK: " + player.getAttack());
        playerStats.setFont(new Font("Arial", Font.PLAIN, 12));
        playerStats.setForeground(Color.LIGHT_GRAY);
        playerStats.setBounds(150, 410, 250, 20);
        add(playerStats);
        
        // ========== VS LABEL (CENTER) ==========
        JLabel vsLabel = new JLabel("VS");
        vsLabel.setFont(new Font("CASTELLAR", Font.BOLD, 36));
        vsLabel.setForeground(new Color(255, 200, 50));  // gold/yellow
        vsLabel.setHorizontalAlignment(JLabel.CENTER);
        vsLabel.setBounds(450, 300, 100, 40);
        add(vsLabel);
        
        // ========== ENEMY INFO (RIGHT SIDE) ==========
        JLabel enemyTitle = new JLabel("ENEMY");
        enemyTitle.setFont(new Font("CASTELLAR", Font.BOLD, 16));
        enemyTitle.setForeground(new Color(255, 100, 100));  // red for enemy
        enemyTitle.setBounds(650, 200, 200, 25);
        add(enemyTitle);
        
        // Enemy name
        JLabel enemyName = new JLabel(enemy.getEnemyName());
        enemyName.setFont(new Font("Arial", Font.BOLD, 14));
        enemyName.setForeground(new Color(255, 100, 100));
        enemyName.setBounds(650, 370, 200, 20);
        add(enemyName);
        
        // Enemy type and stage
        JLabel enemyType = new JLabel("Type: " + enemy.getEnemyType() + " | Stage: " + enemy.getStage());
        enemyType.setFont(new Font("Arial", Font.PLAIN, 12));
        enemyType.setForeground(Color.LIGHT_GRAY);
        enemyType.setBounds(650, 390, 200, 20);
        add(enemyType);
        
        // Enemy stats 
        JLabel enemyStats = new JLabel("HP: " + enemy.getMaxHP() + " | ATK: " + enemy.getAttack() + " | DEF: " + enemy.getDefense());
        enemyStats.setFont(new Font("Arial", Font.PLAIN, 12));
        enemyStats.setForeground(Color.LIGHT_GRAY);
        enemyStats.setBounds(650, 410, 250, 20);
        add(enemyStats);
        
        // ========== START BATTLE BUTTON ==========
        JButton startBtn = new JButton("START BATTLE");
        startBtn.setFont(new Font("CASTELLAR", Font.BOLD, 20));
        startBtn.setForeground(Color.BLACK);
        startBtn.setBackground(new Color(100, 200, 100));  // green = go!
        startBtn.setBounds(350, 550, 300, 60);
        startBtn.setFocusPainted(false);
        startBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Switch to GameManager when clicked
        startBtn.addActionListener(e -> parentFrame.switchPanel(new GameManager(session)));
        add(startBtn);
    }
    
    /**
     * Custom painting for battle preview screen.
     * 
     * Draws:
     * - Dark background
     * - Background image
     * - Semi-transparent overlay
     * - Player sprite (left)
     * - Enemy sprite (right)
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Dark gray background
        g.setColor(new Color(70, 70, 70));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Background image
        if (backgroundFrame != null) {
            g.drawImage(backgroundFrame, 0, 0, getWidth(), getHeight(), null);
        }
        
        // Dark semi-transparent overlay for atmosphere
        g.setColor(new Color(0, 0, 0, 100));  // 100 alpha = translucent
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Player sprite on left side
        if (playerSprite != null) {
            g.drawImage(playerSprite, 150, 230, 130, 130, this);
        } else {
            // Blue placeholder if sprite not loaded
            g.setColor(new Color(100, 100, 200));
            g.fillRect(150, 230, 130, 130);
        }
        
        // Enemy sprite on right side
        if (enemySprite != null) {
            g.drawImage(enemySprite, 650, 230, 130, 130, this);
        } else {
            // Red placeholder if sprite not loaded
            g.setColor(new Color(200, 100, 100));
            g.fillRect(650, 230, 130, 130);
        }
        
        super.paintComponent(g);
    }
}
