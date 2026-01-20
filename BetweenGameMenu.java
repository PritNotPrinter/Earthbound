import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * BetweenGameMenu.java - Post-battle results and progression screen
 * 
 * - Victory or defeat message
 * - Battle  stats (damage dealt/taken, EXP gained)
 * - Current player HP/Mana remaining
 * - Options for what to do next
 * 
 * Button options:
 * - SAVE & EXIT: Save progress and return to main menu
 * - VIEW STATS: Pop up with detailed player stats
 * - NEXT STAGE: Only appears on victory, advances to next enemy
 * 
 * After completing stage 3 (final boss), shows "All Stages Complete!" instead
 * of a next stage button. 
 * 
 * This class extends JPanel for custom paintComponent rendering.
 */
public class BetweenGameMenu extends JPanel {
    
    // ==================== INSTANCE VARIABLES ====================
    private BufferedImage backgroundFrame;    // background image
    private EarthboundFrame parentFrame;      // main window reference
    private GameSession session;              // game state data
    private BufferedImage playerSprite;       // player sprite for display
    
    /**
     * Constructor - sets up the post-battle screen.
     * 
     * @param session - the current game session with battle results
     */
    public BetweenGameMenu(GameSession session) {
        this.session = session;
        setLayout(null);
        setOpaque(false);
        
        loadBackground();
        loadSprite();
        
        // Wait for component to be added before getting parent
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
     * Load player sprite for display.
     */
    private void loadSprite() {
        Player player = session.getPlayer();
        playerSprite = AssetManager.loadPlayerSprite(player.getClassName(), "idle");
    }
    
    /**
     * Set up all UI components.
     * - Victory/defeat message
     * - Player info
     * - Battle summary panel
     * - Action buttons
     */
    private void initializeUI() {
        Player player = session.getPlayer();
        Enemy enemy = session.getCurrentEnemy();
        
        // ========== VICTORY/DEFEAT HEADER ==========
        // Check if player survived the battle
        boolean victory = player.isAlive();
        
        JLabel resultLabel = new JLabel(victory ? "VICTORY!" : "DEFEAT!");
        resultLabel.setFont(new Font("CASTELLAR", Font.BOLD, 42));
        // Green for victory, red for defeat
        resultLabel.setForeground(victory ? new Color(100, 255, 100) : new Color(255, 100, 100));
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        resultLabel.setBounds(200, 100, 600, 50);
        add(resultLabel);
        
        // ========== STAGE INFO ==========
        JLabel stageLabel = new JLabel(
            "Stage " + session.getStage() + " | " + 
            (session.getDifficulty() == 1 ? "NORMAL" : "HARD") + " Mode"
        );
        stageLabel.setFont(new Font("CASTELLAR", Font.BOLD, 18));
        stageLabel.setForeground(new Color(218, 165, 32));  // gold
        stageLabel.setHorizontalAlignment(JLabel.CENTER);
        stageLabel.setBounds(200, 155, 600, 25);
        add(stageLabel);
        
        // ========== PLAYER INFO (UNDER SPRITE) ==========
        JLabel playerName = new JLabel(player.getCharacterName() + " (" + player.getClassName() + ")");
        playerName.setFont(new Font("Arial", Font.BOLD, 14));
        playerName.setForeground(Color.WHITE);
        playerName.setBounds(150, 350, 200, 20);
        add(playerName);
        
        JLabel playerLevel = new JLabel("Level: " + player.getLevel());
        playerLevel.setFont(new Font("Arial", Font.PLAIN, 12));
        playerLevel.setForeground(Color.LIGHT_GRAY);
        playerLevel.setBounds(150, 370, 200, 20);
        add(playerLevel);
        
        // ========== BATTLE SUMMARY PANEL ==========
        // This panel shows all the stats from the battle
        JPanel summaryPanel = new JPanel(null);
        summaryPanel.setBackground(new Color(60, 60, 60));
        // Gold border with title
        summaryPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(218, 165, 32)),
            "Battle Summary", 0, 0,
            new Font("CASTELLAR", Font.BOLD, 14),
            new Color(218, 165, 32)
        ));
        summaryPanel.setBounds(400, 200, 450, 180);
        
        // Damage dealt (green = good)
        JLabel dmgDealt = new JLabel("Damage Dealt: " + session.getRoundsDamageDealt());
        dmgDealt.setFont(new Font("Arial", Font.PLAIN, 14));
        dmgDealt.setForeground(new Color(100, 255, 100));
        dmgDealt.setBounds(20, 30, 200, 20);
        summaryPanel.add(dmgDealt);
        
        // Damage taken (red = ouch)
        JLabel dmgTaken = new JLabel("Damage Taken: " + session.getRoundsDamageTaken());
        dmgTaken.setFont(new Font("Arial", Font.PLAIN, 14));
        dmgTaken.setForeground(new Color(255, 100, 100));
        dmgTaken.setBounds(20, 55, 200, 20);
        summaryPanel.add(dmgTaken);
        
        // Experience gained (blue = progress)
        JLabel expGained = new JLabel("EXP Gained: " + session.getRoundsExperienceGained());
        expGained.setFont(new Font("Arial", Font.PLAIN, 14));
        expGained.setForeground(new Color(100, 150, 255));
        expGained.setBounds(20, 80, 200, 20);
        summaryPanel.add(expGained);
        
        // HP remaining
        JLabel hpRemain = new JLabel("HP Remaining: " + player.getCurrentHP() + "/" + player.getMaxHP());
        hpRemain.setFont(new Font("Arial", Font.PLAIN, 14));
        hpRemain.setForeground(Color.WHITE);
        hpRemain.setBounds(220, 30, 200, 20);
        summaryPanel.add(hpRemain);
        
        // Mana remaining
        JLabel manaRemain = new JLabel("Mana Remaining: " + player.getCurrentMana() + "/" + player.getMaxMana());
        manaRemain.setFont(new Font("Arial", Font.PLAIN, 14));
        manaRemain.setForeground(Color.WHITE);
        manaRemain.setBounds(220, 55, 200, 20);
        summaryPanel.add(manaRemain);
        
        // Enemy info - shows defeated status if won
        JLabel enemyDefeated = new JLabel("Enemy: " + enemy.getEnemyName() + (victory ? " (Defeated)" : ""));
        enemyDefeated.setFont(new Font("Arial", Font.PLAIN, 14));
        enemyDefeated.setForeground(Color.LIGHT_GRAY);
        enemyDefeated.setBounds(220, 80, 200, 20);
        summaryPanel.add(enemyDefeated);
        
        // Advice text at bottom of summary
        String advice = victory ? "Ready for the next challenge?" : "Better luck next time!";
        JLabel adviceLabel = new JLabel(advice);
        adviceLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        adviceLabel.setForeground(Color.LIGHT_GRAY);
        adviceLabel.setBounds(20, 120, 400, 20);
        summaryPanel.add(adviceLabel);
        
        add(summaryPanel);
        
        // ========== SAVE & EXIT BUTTON ==========
        // Gold button - saves progress and goes to main menu
        JButton saveBtn = new JButton("SAVE & EXIT");
        saveBtn.setFont(new Font("CASTELLAR", Font.BOLD, 14));
        saveBtn.setBackground(new Color(218, 165, 32));
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.setBounds(150, 550, 200, 50);
        saveBtn.addActionListener(e -> {
            // Save player to file before exiting
            SaveFileManager.savePlayer(session.getUsername(), session.getSaveSlot(), player);
            parentFrame.switchPanel(new MainMenu());
        });
        add(saveBtn);
        
        // ========== VIEW STATS BUTTON ==========
        // Blue button - shows detailed player stats in popup
        JButton statsBtn = new JButton("VIEW STATS");
        statsBtn.setFont(new Font("CASTELLAR", Font.BOLD, 14));
        statsBtn.setBackground(new Color(100, 150, 200));
        statsBtn.setFocusPainted(false);
        statsBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        statsBtn.setBounds(400, 550, 200, 50);
        statsBtn.addActionListener(e -> {
            // Show all player stats in a dialog
            JOptionPane.showMessageDialog(this,
                "Name: " + player.getCharacterName() + "\n" +
                "Class: " + player.getClassName() + "\n" +
                "Level: " + player.getLevel() + "\n" +
                "HP: " + player.getCurrentHP() + "/" + player.getMaxHP() + "\n" +
                "Mana: " + player.getCurrentMana() + "/" + player.getMaxMana() + "\n" +
                "Attack: " + player.getAttack() + " | Defense: " + player.getDefense() + "\n" +
                "Total EXP: " + player.getExperience(),
                "Player Stats", JOptionPane.INFORMATION_MESSAGE
            );
        });
        add(statsBtn);
        
        // ========== NEXT STAGE BUTTON  ==========
        // Only show if player won AND there are more stages
        if (victory && session.getStage() < 3) {
            JButton nextBtn = new JButton("NEXT STAGE");
            nextBtn.setFont(new Font("CASTELLAR", Font.BOLD, 14));
            nextBtn.setBackground(new Color(100, 200, 100));  // green = go!
            nextBtn.setFocusPainted(false);
            nextBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            nextBtn.setBounds(650, 550, 200, 50);
            nextBtn.addActionListener(e -> {
                // Save progress, advance stage, go to pre-game
                SaveFileManager.savePlayer(session.getUsername(), session.getSaveSlot(), player);
                session.nextStage();  // creates next enemy
                parentFrame.switchPanel(new PreGameMenu(session));
            });
            add(nextBtn);
        } else if (victory) {
            JLabel winLabel = new JLabel("All Stages Complete!");
            winLabel.setFont(new Font("CASTELLAR", Font.BOLD, 16));
            winLabel.setForeground(new Color(255, 215, 0));  // gold
            winLabel.setBounds(650, 560, 200, 30);
            add(winLabel);
        }
        // Note: If player lost, no next button - can only save & exit
    }
    
    /**
     * Custom painting for battle results screen.
     * 
     * Draws background, overlay, and player sprite.
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
        
        // Dark overlay for readability
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Player sprite (left side, shows who we are)
        if (playerSprite != null) {
            g.drawImage(playerSprite, 150, 200, 140, 140, this);
        } else {
            // Blue placeholder if sprite not loaded
            g.setColor(new Color(100, 100, 200));
            g.fillRect(150, 200, 140, 140);
        }
        
        super.paintComponent(g);
    }
}
