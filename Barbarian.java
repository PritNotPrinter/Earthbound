import java.util.ArrayList;

public class Barbarian extends Player {
    
    // Special state for combo mechanic - tracks if last move was Rush. if it was, punch is stronger
    private boolean lastActionWasRush = false;
    
    /**
     * Constructor - creates a new Barbarian character.
     * @param characterName - the name chosen by the player
     */
    public Barbarian(String characterName) {
        // Call parent constructor with class name "Barbarian"
        super(characterName, "Barbarian");
        // Load sprite animations
        initializeAnimations();
    }
    
    /**
     * Set up all the sprite animations for the Barbarian.
     * 
     * Loads from the barbarian asset folder with different action types.
     * The "run meat" folder has the rushing animation (because the 'barbarian' is running while holding meat)
     */
    private void initializeAnimations() {
        // Create a new animation manager for Barbarian sprites
        animationManager = new AnimationManager("Barbarian");
        
        // Get the base path to Barbarian assets
        String basePath = AssetManager.getPlayerAssetPath("Barbarian");
        
        // Load each animation from its folder
        animationManager.loadAnimation("idle", basePath + "/idle");     
        animationManager.loadAnimation("attack", basePath + "/attack"); 
        animationManager.loadAnimation("rush", basePath + "/run meat"); 
        
        // Set frame delay - 100ms = 10 FPS animation speed
        animationManager.setFrameDelay(100);
    }
    
    @Override
    protected void initializeStats() {
        this.maxHP = 80;         
        this.currentHP = 80;     
        this.maxMana = 30;      
        this.currentMana = 30;  
        this.attack = 12;       
        this.defense = 10;      
    }
    
    /**
     * Get the list of attack moves.
     * 
     * @return ArrayList containing "Rush" and "Punch"
     */
    @Override
    public ArrayList<String> getAttackMoves() {
        ArrayList<String> moves = new ArrayList<>();
        moves.add("Rush");   // charging attack, enables combo
        moves.add("Punch");  // this is the attack where the 'barbarian' punches with meat, just because c:
        return moves;
    }
    
    /**
     * Get the list of defense moves.
     * 
     * @return ArrayList containing "Heal"
     */
    @Override
    public ArrayList<String> getDefenseMoves() {
        ArrayList<String> moves = new ArrayList<>();
        moves.add("Heal");   // restore HP
        return moves;
    }
    

    @Override
    public int executeAttack(String moveName) {
        if (moveName.equals("Rush")) {
   
            lastActionWasRush = true;  
            if (!useMana(10)) return 0; 
            return attack + 5;  
            
        } else if (moveName.equals("Punch")) {
            int damage = attack + 3;  

            if (lastActionWasRush) {
                damage += 8;  
            }
            

            lastActionWasRush = false;
            
            if (!useMana(5)) return 0; 
            return damage;
        }
        

        lastActionWasRush = false;
        return 0;
    }
    

    @Override
    public int executeDefense(String moveName) {
         if (moveName.equals("Heal")) {
            if (!useMana(15)) return 0;  
            heal(30);  
            return 0;  
        }
        return 0;
    }


    @Override
    public String getActionLabel(String moveName, boolean isAttack) {
        if (moveName.equals("Rush")) {
            return "Rush (ATK+5, -10 MP)";
        } else if (moveName.equals("Punch")) {
            return "Punch (ATK+3, +8 combo, -5 MP)";  // shows combo bonus!
        } else if (moveName.equals("Heal")) {
            return "Heal (+30 HP, -15 MP)";
        }
        return moveName;
    }
    
    /**
     * Setter for combo state (used by save/load system).
     * 
     * @param value - whether Rush was just used
     */
    public void setLastActionWasRush(boolean value) {
        this.lastActionWasRush = value;
    }
    
    /**
     * Get class-specific data for saving.
     * 
     * Save the combo state so it persists between game sessions.
     * Uses "1" for true and "0" for false (simple string format).
     * 
     * @return "1" if Rush was used last, "0" otherwise
     */
    @Override
    public String getClassData() {
        return lastActionWasRush ? "1" : "0";
    }
    
    /**
     * Load class-specific data from save file.
     * 
     * @param data - "1" or "0" for combo state
     */
    @Override
    public void loadClassData(String data) {
        this.lastActionWasRush = "1".equals(data);
    }
    
    /**
     * Get the animation manager, initializing if needed.
     * 
     * This override ensures animations work even after loading a save file.
     * If animationManager is null (wasn't saved), we recreate it.
     * 
     * @return the AnimationManager for this Barbarian
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
     * Get the current attack (punch) animation frame.
     * @return BufferedImage of current attack frame
     */
    public java.awt.image.BufferedImage getAttackAnimation() {
        return getAnimationManager().getFrame("attack");
    }
    
    /**
     * Get the current block animation frame.
     * @return BufferedImage of current block frame
     */
    public java.awt.image.BufferedImage getBlockAnimation() {
        return getAnimationManager().getFrame("block");
    }
    
    /**
     * Get the current rush animation frame.
     * @return BufferedImage of current rush frame
     */
    public java.awt.image.BufferedImage getRushAnimation() {
        return getAnimationManager().getFrame("rush");
    }
}
