
public class Plent extends Enemy {
    
    /**
     * Constructor - creates a new Plent boss.
     * 
     * Passes "Plent" as name/type and stage 3 to parent.
     */
    public Plent() {
        // Call parent constructor: name, type, stage 3 
        super("Plent", "Plent", 3);
    }
    
    /**
     * Initialize Plent-specific stats.
     * 
     */
    @Override
    protected void initializeStats() {
        this.maxHP = 120;     
        this.currentHP = 120;  
        this.attack = 18;      
        this.defense = 12;  
        
       
        this.abilities.add("Tail");    
        this.abilities.add("Poison"); 
    }
    
    /**
     * Set up all the sprite animations for the Plent.
     * Loads from the plent asset folder.
     */
    @Override
    protected void initializeAnimations() {
        // Create animation manager for Plent
        animationManager = new AnimationManager("Plent");
        
        // Get base path to plent assets
        String basePath = AssetManager.getEnemyAssetPath("Plent");
        
        // Load each animation
        animationManager.loadAnimation("idle", basePath + "/idle");        // standing still
        animationManager.loadAnimation("attack1", basePath + "/attack1");  // tail animation
        animationManager.loadAnimation("attack2", basePath + "/attack2");  // poison animation
        
        // Set frame delay for animation speed
        animationManager.setFrameDelay(100);
    }
    
    /**
     * Decide what action to take this turn.
     * - 35% chance: Tail
     * - 65% chance: Poison
     */
    @Override
    public String decideAction(Player opponent) {
        // Get a random number from 0-99
        int random = (int)(Math.random() * 100);
        
        if (random < 35) {

            return "Tail";
        } else {

            return "Poison";
        }
    }
    
    /**
     * Execute an attack and return the damage.
     */
    @Override
    public int executeAttack(String moveName) {
        if (moveName.equals("Tail")) {
            // Tail attack: base attack + 8 = 26 damage
            return attack + 8;
            
        } else if (moveName.equals("Poison")) {
            // POISON: base attack + 15 = 33 damage
            return attack + 15;
        } 
        
        // Fallback - just use base attack
        return attack;
    }
}
