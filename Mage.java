import java.util.ArrayList;

/**
 * Mage.java 
 */
public class Mage extends Player {
    

    public Mage(String characterName) {
        // Call parent constructor with class name "Mage"
        super(characterName, "Mage");
        // Load our sprite animations
        initializeAnimations();
    }

    private void initializeAnimations() {
        // Create a new animation manager for Mage sprites
        animationManager = new AnimationManager("Mage");
        
        // Get the base path to Mage assets
        String basePath = AssetManager.getPlayerAssetPath("Mage");
        
        // Load each animation from its folder
        animationManager.loadAnimation("idle", basePath + "/idle");        
        animationManager.loadAnimation("fireball", basePath + "/attack1");  
        animationManager.loadAnimation("lightning", basePath + "/attack2");  
        
        // Set frame delay - 100ms = 10 FPS animation speed
        animationManager.setFrameDelay(100);
    }
    

    @Override
    protected void initializeStats() {
        this.maxHP = 40;       
        this.currentHP = 40;    
        this.maxMana = 80;      
        this.currentMana = 80;   
        this.attack = 30;  // normally 15, 30 for testing purposes       
        this.defense = 5;        
    }
    
    /**
     * Get the list of attack moves.
     */
    @Override
    public ArrayList<String> getAttackMoves() {
        ArrayList<String> moves = new ArrayList<>();
        moves.add("Fireball");   
        moves.add("Lightning");  
        return moves;
    }
    

    @Override
    public ArrayList<String> getDefenseMoves() {
        ArrayList<String> moves = new ArrayList<>();
        moves.add("Heal");   // restore HP
        return moves;
    }
    

    @Override
    public int executeAttack(String moveName) {
        if (moveName.equals("Fireball")) {

            if (!useMana(20)) return 0;  // costs 20 mana
            return attack + 10;  // base attack + 10 
            
        } else if (moveName.equals("Lightning")) {

            if (!useMana(15)) return 0;  // costs 15 mana
            return attack + 8;   // base attack + 8 
        }
        return 0;
    }
    

    @Override
    public int executeDefense(String moveName) {
        if (moveName.equals("Heal")) {
            if (!useMana(20)) return 0;  
            heal(25);  
            return 0;  
        }
        return 0;
    }

  
    @Override
    public String getActionLabel(String moveName, boolean isAttack) {
        if (moveName.equals("Fireball")) {
            return "Fireball (ATK+10, -20 MP)";
        } else if (moveName.equals("Lightning")) {
            return "Lightning (ATK+8, -15 MP)";
        } else if (moveName.equals("Heal")) {
            return "Heal (+25 HP, -20 MP)";
        }
        return moveName;
    }
    
    /**
     * Get the animation manager, initializing if needed.
     * 
     * @return the AnimationManager for this Mage
     */
    @Override
    public AnimationManager getAnimationManager() {
      
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
     * Get the current fireball animation frame.
     * @return BufferedImage of current fireball frame
     */
    public java.awt.image.BufferedImage getFireballAnimation() {
        return getAnimationManager().getFrame("fireball");
    }
    
    /**
     * Get the current lightning animation frame.
     * @return BufferedImage of current lightning frame
     */
    public java.awt.image.BufferedImage getLightningAnimation() {
        return getAnimationManager().getFrame("lightning");
    }
}
