/**
 * Goblin.java - Stage 2 enemy (medium difficulty)
 */
public class Goblin extends Enemy {
    

    public Goblin() {
        // Call parent constructor: name, type, stage 2
        super("Goblin", "Goblin", 2);
    }
    
    /**
     * Initialize Goblin-specific stats.
     */
    @Override
    protected void initializeStats() {
        this.maxHP = 50;      
        this.currentHP = 50;  
        this.attack = 11;     
        this.defense = 5;     
        
        this.abilities.add("Stab");        
        this.abilities.add("Poison Dart"); 
        this.abilities.add("Quick Strike"); 
    }
    
    /**
     * Set up all the sprite animations for the Goblin.
     * 
     * Loads from the goblin asset folder.
     */
    @Override
    protected void initializeAnimations() {
        // Create animation manager for Goblin
        animationManager = new AnimationManager("Goblin");
        
        // Get base path to goblin assets
        String basePath = AssetManager.getEnemyAssetPath("Goblin");
        
        // Load each animation
        animationManager.loadAnimation("idle", basePath + "/idle");       
        animationManager.loadAnimation("attack1", basePath + "/attack1"); 
        animationManager.loadAnimation("attack2", basePath + "/attack2"); 
        
        // Set frame delay for animation speed
        animationManager.setFrameDelay(100);
    }
    
    /**
     * Decide what action to take this turn.
     */
    @Override
    public String decideAction(Player opponent) {
        // Get a random number from 0-99
        int random = (int)(Math.random() * 100);
        
        // Use weighted probability to pick an attack
        if (random < 40) {
            // 40% chance
            return "Quick Strike";
            
        } else if (random < 70) {
            // 30% chance
            return "Stab";
        }
        
        // Remaining 30% 
        return "Poison Dart";
    }
    
    /**
     * Execute an attack and return the damage.
     */
    @Override
    public int executeAttack(String moveName) {
        if (moveName.equals("Stab")) {
            // Medium attack: base attack + 4 = 15 damage
            return attack + 4;
            
        } else if (moveName.equals("Poison Dart")) {
            // Strong attack: base attack + 6 = 17 damage
            return attack + 6;
            
        } else if (moveName.equals("Quick Strike")) {
            // Quick attack: base attack + 3 = 14 damage
            return attack + 3;
        }
        
        // Fallback - just use base attack
        return attack;
    }
}
