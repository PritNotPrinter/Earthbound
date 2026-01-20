import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * - Drawing the battle scene with animated sprites
 * - Displaying HP/Mana bars for player and enemy
 * - Combat action buttons (attacks, defenses)
 * - Battle log showing what's happening
 * - Turn-based combat flow
 * extends JPanel and uses paintComponent for custom rendering.
 * The animation is driven by a Timer that fires every 100ms.
 */
public class GameManager extends JPanel {
    
    // ==================== VISUALS ====================
    private BufferedImage backgroundFrame;    // background image 
    private EarthboundFrame parentFrame;     
    
    // ==================== GAME STATE ====================
    private GameSession session;              // contains player, enemy, and all game data
    private JPanel actionPanel;               // panel containing combat action buttons
    private boolean combatActive = true;      // flag to prevent button spam during animations
    
    // ==================== UI COMPONENTS ====================
    // Progress bars for visual HP/Mana display
    private JProgressBar playerHPBar, playerManaBar, enemyHPBar;
    // Labels showing exact HP/Mana values
    private JLabel playerHPLabel, playerManaLabel, enemyHPLabel;
    
    // ==================== ANIMATION ====================
    private Timer animationTimer;                           // fires every 100ms to update animations
    private AnimationManager playerAnimManager;             // handles player sprite animation
    private AnimationManager enemyAnimManager;              // handles enemy sprite animation
    private String currentPlayerAnimation = "idle";         // which animation the player is showing
    private String currentEnemyAnimation = "idle";          // which animation the enemy is showing
    
    /**
     * sets up the combat screen.
     */
    public GameManager(GameSession session) {
        this.session = session;
        setLayout(null);      
        setOpaque(false);     
        
        loadBackground();            // load the background image
        initializeAnimationManagers();  // set up sprite animations
        
        // invokeLater ensures this runs after everything else
        SwingUtilities.invokeLater(() -> {
            parentFrame = (EarthboundFrame) SwingUtilities.getWindowAncestor(this);
            loadUI();  // build all the UI components
        });
    }
    
    /**
     * Load the background image for the battle scene.
     * 
     * If loading fails, creates a placeholder so the game still works.
     */
    private void loadBackground() {
        try {
            File file = new File(AssetManager.getGameMenuFrame());
            // If file exists, load it; otherwise use placeholder
            backgroundFrame = file.exists() ? ImageIO.read(file) : AssetManager.createPlaceholder(1000, 750);
        } catch (Exception e) {
            // Any error = use placeholder
            backgroundFrame = AssetManager.createPlaceholder(1000, 750);
        }
    }
    
    /**
     * Initialize animation managers for player and enemy. loads the appropriate sprite sheets based on player class and enemy type.
     */
    private void initializeAnimationManagers() {
        Player player = session.getPlayer();
        Enemy enemy = session.getCurrentEnemy();
        
        //  PLAYER ANIMATIONS 
        String playerClass = player.getClassName();
        playerAnimManager = new AnimationManager(playerClass);
        String playerPath = AssetManager.getPlayerAssetPath(playerClass);
        
        // Load different animations based on which class the player chose
        if (playerClass.equals("Barbarian")) {
            playerAnimManager.loadAnimation("idle", playerPath + "/idle");
            playerAnimManager.loadAnimation("attack", playerPath + "/attack");
            playerAnimManager.loadAnimation("rush", playerPath + "/run meat");
            
        } else if (playerClass.equals("Archer")) {
            playerAnimManager.loadAnimation("idle", playerPath + "/idle");
            playerAnimManager.loadAnimation("shoot", playerPath + "/shoot");
            playerAnimManager.loadAnimation("dodge", playerPath + "/dodge");
            
        } else if (playerClass.equals("Mage")) {
            playerAnimManager.loadAnimation("idle", playerPath + "/idle");
            playerAnimManager.loadAnimation("fireball", playerPath + "/attack1");
            playerAnimManager.loadAnimation("lightning", playerPath + "/attack2");
        }
        playerAnimManager.setFrameDelay(100);  // 10 FPS
        
        //  ENEMY ANIMATIONS 
        String enemyType = enemy.getEnemyType();
        enemyAnimManager = new AnimationManager(enemyType);
        String enemyPath = AssetManager.getEnemyAssetPath(enemyType);
        
        // All enemies have idle, attack1, and attack2
        enemyAnimManager.loadAnimation("idle", enemyPath + "/idle");
        enemyAnimManager.loadAnimation("attack1", enemyPath + "/attack1");
        enemyAnimManager.loadAnimation("attack2", enemyPath + "/attack2");
        enemyAnimManager.setFrameDelay(100);  // 10 FPS
    }
    
    /**
     * - Labels for names and stats
     * - HP/Mana progress bars
     * - Battle log text area
     * - Action buttons for combat
     * - Animation timer
     */
    private void loadUI() {
        Player player = session.getPlayer();
        Enemy enemy = session.getCurrentEnemy();
        
        //  HEADER INFO 
        // Shows difficulty and current stage at top of screen
        JLabel diffLabel = new JLabel("Difficulty: " + (session.getDifficulty() == 1 ? "NORMAL" : "HARD") + " | Stage: " + session.getStage());
        diffLabel.setFont(new Font("CASTELLAR", Font.BOLD, 16));
        diffLabel.setForeground(new Color(218, 165, 32));  // gold color
        diffLabel.setBounds(120, 75, 350, 25);
        add(diffLabel);
        
        //  PLAYER NAME 
        JLabel playerName = new JLabel(player.getCharacterName() + " (" + player.getClassName() + ")");
        playerName.setFont(new Font("Arial", Font.BOLD, 14));
        playerName.setForeground(Color.WHITE);
        playerName.setBounds(120, 100, 200, 20);
        add(playerName);
        
        //  ENEMY NAME 
        JLabel enemyName = new JLabel(enemy.getEnemyName());
        enemyName.setFont(new Font("Arial", Font.BOLD, 14));
        enemyName.setForeground(new Color(255, 100, 100));  // red for enemy
        enemyName.setBounds(650, 100, 200, 20);
        add(enemyName);
        
        //  PLAYER HP BAR 
        playerHPLabel = new JLabel("HP: " + player.getCurrentHP() + "/" + player.getMaxHP());
        playerHPLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        playerHPLabel.setForeground(Color.WHITE);
        playerHPLabel.setBounds(120, 280, 150, 15);
        add(playerHPLabel);
        
        playerHPBar = new JProgressBar(0, player.getMaxHP());
        playerHPBar.setValue(player.getCurrentHP());
        playerHPBar.setBounds(120, 295, 150, 12);
        playerHPBar.setForeground(Color.GREEN);  // green for health
        add(playerHPBar);
        
        //  PLAYER MANA BAR 
        playerManaLabel = new JLabel("Mana: " + player.getCurrentMana() + "/" + player.getMaxMana());
        playerManaLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        playerManaLabel.setForeground(Color.WHITE);
        playerManaLabel.setBounds(120, 310, 150, 15);
        add(playerManaLabel);
        
        playerManaBar = new JProgressBar(0, player.getMaxMana());
        playerManaBar.setValue(player.getCurrentMana());
        playerManaBar.setBounds(120, 325, 150, 12);
        playerManaBar.setForeground(new Color(100, 150, 255));  // blue for mana
        add(playerManaBar);
        
        //  PLAYER STATS 
        JLabel pStats = new JLabel("ATK: " + player.getAttack() + " | DEF: " + player.getDefense());
        pStats.setFont(new Font("Arial", Font.PLAIN, 10));
        pStats.setForeground(Color.LIGHT_GRAY);
        pStats.setBounds(120, 340, 150, 15);
        add(pStats);
        
        //  ENEMY HP BAR 
        enemyHPLabel = new JLabel("HP: " + enemy.getCurrentHP() + "/" + enemy.getMaxHP());
        enemyHPLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        enemyHPLabel.setForeground(Color.WHITE);
        enemyHPLabel.setBounds(650, 280, 150, 15);
        add(enemyHPLabel);
        
        enemyHPBar = new JProgressBar(0, enemy.getMaxHP());
        enemyHPBar.setValue(enemy.getCurrentHP());
        enemyHPBar.setBounds(650, 295, 150, 12);
        enemyHPBar.setForeground(new Color(200, 50, 50));  // red for enemy
        add(enemyHPBar);
        
        //  ENEMY STATS 
        JLabel eStats = new JLabel("ATK: " + enemy.getAttack() + " | DEF: " + enemy.getDefense());
        eStats.setFont(new Font("Arial", Font.PLAIN, 10));
        eStats.setForeground(Color.LIGHT_GRAY);
        eStats.setBounds(650, 310, 150, 15);
        add(eStats);
        
        //  BATTLE LOG 
        JTextArea logArea = new JTextArea();
        logArea.setFont(new Font("Arial", Font.PLAIN, 12));
        logArea.setForeground(Color.WHITE);
        logArea.setBackground(new Color(50, 50, 50));  // dark gray background
        logArea.setEditable(false);  // player can't type in it
        logArea.setLineWrap(true);   // wrap long lines
        logArea.setText("Battle started!\n" + enemy.getEnemyName() + " appears!\n");
        
        // Wrap in scroll pane so player can scroll back through log
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBounds(120, 380, 760, 140);
        logScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(logScroll);
        
        //  ACTION PANEL 
        actionPanel = new JPanel(null);  // null layout for absolute positioning
        actionPanel.setBackground(new Color(60, 60, 60));
        actionPanel.setBounds(120, 530, 760, 150);
        actionPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY), "Combat Actions", 0, 0, 
            new Font("Arial", Font.BOLD, 12), Color.WHITE));
        
        // Get the player's available moves
        ArrayList<String> attacks = player.getAttackMoves();
        ArrayList<String> defenses = player.getDefenseMoves();
        
        // Button dimensions and positioning
        int btnW = 170, btnH = 80;  // button size
        int x = 15, y = 30;         // starting position
        int gap = 185;              // space between buttons
        int idx = 0;                // button index for positioning
        
        // Create attack buttons (red-ish color)
        for (String m : attacks) {
            JButton b = newButton(player.getActionLabel(m, true), new Color(200, 100, 100));
            b.setBounds(x + idx * gap, y, btnW, btnH);
            // Lambda captures the move name and passes to handler
            b.addActionListener(e -> handleAction(m, true, logArea));
            actionPanel.add(b);
            idx++;
        }
        
        // Create defense buttons (blue-ish color)
        for (String m : defenses) {
            JButton b = newButton(player.getActionLabel(m, false), new Color(100, 150, 200));
            b.setBounds(x + idx * gap, y, btnW, btnH);
            b.addActionListener(e -> handleAction(m, false, logArea));
            actionPanel.add(b);
            idx++;
        }
        add(actionPanel);
        
        //  ANIMATION TIMER 
        //  every 100ms to update HP bars and repaint sprites
        animationTimer = new Timer(100, e -> { updateBars(); repaint(); });
        animationTimer.start();
    }
    
    /**
     * @param text - the button label text
     * @param bg - background color (red for attack, blue for defense)
     * @return the styled JButton
     */
    private JButton newButton(String text, Color bg) {
        JButton b = new JButton("<html><center>" + text.toUpperCase() + "</center></html>");
        b.setFont(new Font("CASTELLAR", Font.BOLD, 11));
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);                        
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));    
        return b;
    }
    
    /**
     * Sets combatActive = false to prevent spam clicking during animations.
     * Disables all buttons until the turn is complete.
     * 
     * @param action - which move was selected
     * @param isAttack - true for attack moves, false for defense moves
     * @param log - the battle log text area to update
     */
    private void handleAction(String action, boolean isAttack, JTextArea log) {
        if (!combatActive) return;  
        combatActive = false;       
        
        // Disable all buttons during turn execution
        for (Component c : actionPanel.getComponents()) c.setEnabled(false);
        
        // Execute the turn
        executeTurn(action, isAttack, log);
    }
    
    /**
     * Execute a full combat turn (player action, then enemy action).
     *
     * 1. Player uses their action
     * 2. Check if enemy died → victory
     * 3. Wait 1 second for animation
     * 4. Enemy takes their turn
     * 5. Check if player died → defeat
     * 6. Re-enable buttons for next turn
     * 
     * Uses Timer objects for delays so animations can play.
     * 
     * @param action - the move the player chose
     * @param isAttack - true for attack, false for defense
     * @param log - battle log to update
     */
    private void executeTurn(String action, boolean isAttack, JTextArea log) {
        Player player = session.getPlayer();
        Enemy enemy = session.getCurrentEnemy();
        
        // Trigger player's animation based on their action
        triggerPlayerAnimation(action);
        
        // Execute the move and get damage/block value
        int dmg = isAttack ? player.executeAttack(action) : player.executeDefense(action);
        log.append("You used " + action + "!\n");
        
        // If damage was dealt, apply it to enemy
        if (dmg > 0) {
            enemy.takeDamage(dmg);
            log.append(enemy.getEnemyName() + " took " + dmg + " damage!\n");
            session.addRoundsDamageDealt(dmg);  // track for stats
        }
        
        // Check for victory
        if (!enemy.isAlive()) {
            log.append("\nVictory! " + enemy.getEnemyName() + " defeated!\n");
            
            // Calculate XP reward (increases by 50% per stage)
            long exp = (long)(100 * Math.pow(1.5, session.getStage() - 1));
            player.addExperience(exp);
            session.addRoundsExperienceGained(exp);
            
            // Wait 1 second then go to BetweenGameMenu
            Timer t = new Timer(1000, e -> parentFrame.switchPanel(new BetweenGameMenu(session)));
            t.setRepeats(false);
            t.start();
            return;  
        }
        
        //  ENEMY TURN (after 1 second delay) 
        Timer t = new Timer(1000, e -> {
            // Enemy chooses an action
            String eAction = enemy.decideAction(player);
            triggerEnemyAnimation(eAction, enemy);
            
            // Enemy deals damage
            int eDmg = enemy.executeAttack(eAction);
            log.append(enemy.getEnemyName() + " used " + eAction + "!\n");
            log.append("You took " + eDmg + " damage!\n\n");
            player.takeDamage(eDmg);
            session.addRoundsDamageTaken(eDmg);  // track for stats
            
            // Check for defeat!
            if (!player.isAlive()) {
                log.append("\nDefeat! You have been knocked out!\n");
                Timer t2 = new Timer(1000, ev -> parentFrame.switchPanel(new BetweenGameMenu(session)));
                t2.setRepeats(false);
                t2.start();
                return;
            }
            
            // Re-enable combat for next turn
            combatActive = true;
            for (Component c : actionPanel.getComponents()) c.setEnabled(true);
        });
        t.setRepeats(false);
        t.start();
    }
    
    /**
     * Trigger the appropriate player animation based on their action.
     * 
     * Maps action names to animation names for each class.
     * Animation plays for 600ms then reverts to idle.
     * 
     * @param action - the name of the move used
     */
    private void triggerPlayerAnimation(String action) {
        String cls = session.getPlayer().getClassName();
        
        // Map actions to animations based on class
        if (cls.equals("Barbarian")) {
            currentPlayerAnimation = action.equals("Rush") ? "rush" : action.equals("Punch") ? "attack" : "idle";
        } else if (cls.equals("Archer")) {
            currentPlayerAnimation = action.equals("Bow") ? "shoot" : action.equals("Dodge") ? "dodge" : "idle";
        } else if (cls.equals("Mage")) {
            currentPlayerAnimation = action.equals("Fireball") ? "fireball" : action.equals("Lightning") ? "lightning" : "idle";
        }
        
        playerAnimManager.resetAnimation();  // start from frame 0
        
        // After 600ms, go back to idle animation
        Timer t = new Timer(600, e -> currentPlayerAnimation = "idle");
        t.setRepeats(false);
        t.start();
    }
    
    /**
     * Trigger the enemy's attack animation.
     * 
     * Uses attack1 for the first ability, attack2 for others.
     * Animation plays for 600ms then reverts to idle.
     * 
     * @param action - the name of the enemy's move
     * @param enemy - the enemy object
     */
    private void triggerEnemyAnimation(String action, Enemy enemy) {
        // Check which ability slot this action is in
        int idx = enemy.getAbilities().indexOf(action);
        // Use attack1 for first ability (or if not found), attack2 for second+
        currentEnemyAnimation = idx <= 0 ? "attack1" : "attack2";
        
        enemyAnimManager.resetAnimation();  // start from frame 0
        
        // After 600ms, go back to idle
        Timer t = new Timer(600, e -> currentEnemyAnimation = "idle");
        t.setRepeats(false);
        t.start();
    }
    
    /**
     * Update the HP and Mana bar displays.
     * 
     * Called every 100ms by the animation timer.
     * Updates both the progress bar values and the text labels.
     */
    private void updateBars() {
        Player p = session.getPlayer();
        Enemy e = session.getCurrentEnemy();
        
        // Update player bars
        playerHPBar.setValue(p.getCurrentHP());
        playerHPLabel.setText("HP: " + p.getCurrentHP() + "/" + p.getMaxHP());
        playerManaBar.setValue(p.getCurrentMana());
        playerManaLabel.setText("Mana: " + p.getCurrentMana() + "/" + p.getMaxMana());
        
        // Update enemy bar
        enemyHPBar.setValue(e.getCurrentHP());
        enemyHPLabel.setText("HP: " + e.getCurrentHP() + "/" + e.getMaxHP());
    }
    
    /**
     * Custom painting for the battle scene.
     * 
     * This is called by Swing whenever the panel needs to be redrawn.
     * We override it to draw our custom graphics:
     * - Dark gray base color
     * - Background image
     * - Dark overlay for atmosphere
     * - Player and enemy sprites with animation
     * 
     * @param g - the Graphics context to draw on
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Fill with dark gray as base color
        g.setColor(new Color(40, 40, 40));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw background image (scales to panel size)
        if (backgroundFrame != null) {
            g.drawImage(backgroundFrame, 0, 0, getWidth(), getHeight(), null);
        }
        
        // Draw semi-transparent dark overlay for atmosphere
        g.setColor(new Color(0, 0, 0, 120));  // 120 alpha = semi-transparent
        g.fillRect(0, 0, getWidth(), getHeight());
        
        //  DRAW PLAYER SPRITE 
        BufferedImage pSprite = playerAnimManager.getFrame(currentPlayerAnimation);
        if (pSprite != null) {
            // Draw the current animation frame
            g.drawImage(pSprite, 120, 120, 150, 150, this);
        } else {
            // Fallback: draw a blue placeholder rectangle
            g.setColor(new Color(100, 100, 200));
            g.fillRect(120, 120, 150, 150);
            g.setColor(Color.WHITE);
            g.drawString("Player", 170, 200);
        }
        
        //  DRAW ENEMY SPRITE 
        BufferedImage eSprite = enemyAnimManager.getFrame(currentEnemyAnimation);
        if (eSprite != null) {
            // Draw the current animation frame
            g.drawImage(eSprite, 650, 120, 150, 150, this);
        } else {
            // Fallback: draw a red placeholder rectangle
            g.setColor(new Color(200, 100, 100));
            g.fillRect(650, 120, 150, 150);
            g.setColor(Color.WHITE);
            g.drawString("Enemy", 700, 200);
        }
        
        // Let Swing paint child components (buttons, labels, etc.)
        super.paintComponent(g);
    }
}
