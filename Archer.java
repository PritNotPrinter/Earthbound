import java.util.ArrayList;


public class Archer extends Player {
    
    private int dodgeCounter = 0;
    

    public Archer(String characterName) {
        // Call parent constructor with class name "Archer"
        super(characterName, "Archer");
        // Load  sprite animations
        initializeAnimations();
    }
    
    /**
     * Set up all the sprite animations for the Archer.
     * Loads from the archer asset folder
     */
    private void initializeAnimations() {
        // Create a new animation manager for Archer sprites
        animationManager = new AnimationManager("Archer");
        
        // Get the base path to Archer assets
        String basePath = AssetManager.getPlayerAssetPath("Archer");
        
        // Load each animation from its folder
        animationManager.loadAnimation("idle", basePath + "/idle");    // standing still
        animationManager.loadAnimation("shoot", basePath + "/shoot");  // bow attack
        animationManager.loadAnimation("dodge", basePath + "/dodge");  // evasion move
        
        // frame delay of 100ms = 10 FPS 
        animationManager.setFrameDelay(100);
    }
    
    /**
     * Initialize Archer-specific starting stats.
     */
    @Override
    protected void initializeStats() {
        this.maxHP = 60;         
        this.maxMana = 50;       
        this.currentMana = 50;  
        this.attack = 13;        
        this.defense = 7;        
    }
    
    /**
     * Get the list of attack moves.
     */
    @Override
    public ArrayList<String> getAttackMoves() {
        ArrayList<String> moves = new ArrayList<>();
        moves.add("Bow");   // ranged arrow shot
        return moves;
    }
    
    /**
     * Get the list of defense moves.
     */
    @Override
    public ArrayList<String> getDefenseMoves() {
        ArrayList<String> moves = new ArrayList<>();
        moves.add("Dodge");  // evasion + block
        moves.add("Heal");   // restore HP
        return moves;
    }
    
    /**
     * Execute an attack move.
     * Bow is the only attack
     * @param moveName - which attack to use
     * @return damage dealt, or 0 if not enough mana
     */
    @Override
    public int executeAttack(String moveName) {
        if (moveName.equals("Bow")) {
            if (!useMana(12)) return 0;  // costs 12 mana
            return attack + 7;  // base attack + 7 = 20 damage at level 1!
        }
        return 0;
    }
    
    /**
     * Execute a defense move.
     * Dodge
     * Heal restores HP
     * @param moveName - which defense to use
     * @return block value (10 for Dodge, 0 for Heal)
     */
    @Override
    public int executeDefense(String moveName) {
        if (moveName.equals("Dodge")) {
            if (!useMana(10)) return 0;  
            dodgeCounter++;  
            return 10; 
            
        } else if (moveName.equals("Heal")) {
            if (!useMana(15)) return 0;  // costs 15 mana
            heal(20);  // restore 20 HP
            return 0;  
        }
        return 0;
    }

    /**
     * Get formatted labels for action buttons.
     * 
     * @param moveName - name of the move
     * @param isAttack - whether it's an attack or defense
     * @return formatted string like "Bow (ATK+7, -12 MP)"
     */
    @Override
    public String getActionLabel(String moveName, boolean isAttack) {
        if (moveName.equals("Bow")) {
            return "Bow (ATK+7, -12 MP)";
        } else if (moveName.equals("Dodge")) {
            return "Dodge (+10 block, -10 MP)";
        } else if (moveName.equals("Heal")) {
            return "Heal (+20 HP, -15 MP)";
        }
        return moveName;
    }
    

    public int getDodgeCounter() {
        return dodgeCounter;
    }
    

    public void resetDodgeCounter() {
        this.dodgeCounter = 0;
    }
    
    /**
     * Get class-specific data for saving.
     * @return the dodge counter as a string
     */
    @Override
    public String getClassData() {
        return String.valueOf(dodgeCounter);
    }
    
    /**
     * Load class-specific data from save file.
     * 
     * @param data - the dodge counter as a string
     */
    @Override
    public void loadClassData(String data) {
        try {
            // Try to parse the integer
            this.dodgeCounter = Integer.parseInt(data);
        } catch (NumberFormatException e) {
            // If parsing fails, just reset to 0
            this.dodgeCounter = 0;
        }
    }
    
    /**
     * Get the animation manager, initializing if needed.
     * 
     * @return the AnimationManager for this Archer
     */
    @Override
    public AnimationManager getAnimationManager() {
        // Lazy initialization - create if doesn't exist
        if (animationManager == null) {
            initializeAnimations();
        }
        return animationManager;
    }
    
    /**
     * Get the current idle animation frame.
     * @return BufferedImage of current idle frame
     */
    public java.awt.image.BufferedImage getIdleAnimation() {
        return getAnimationManager().getFrame("idle");
    }
    
    /**
     * Get the current shoot (bow attack) animation frame.
     * @return BufferedImage of current shoot frame
     */
    public java.awt.image.BufferedImage getShootAnimation() {
        return getAnimationManager().getFrame("shoot");
    }
    
    /**
     * Get the current dodge animation frame.
     * @return BufferedImage of current dodge frame
     */
    public java.awt.image.BufferedImage getDodgeAnimation() {
        return getAnimationManager().getFrame("dodge");
    }
}
