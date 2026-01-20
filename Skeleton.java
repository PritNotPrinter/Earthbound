
public class Skeleton extends Enemy {
    

    public Skeleton() {
        // Call parent constructor: name, type, stage 1
        super("Skeleton", "Skeleton", 1);
    }
    

    @Override
    protected void initializeStats() {
        this.maxHP = 30;       
        this.currentHP = 30;  
        this.attack = 8;      
        this.defense = 3; 
        

        this.abilities.add("Slash");       
        this.abilities.add("Bone Attack");
    }
    
    /**
     * Set up all the sprite animations for the Skeleton.
     * Loads from the skeleton asset folder.
     */
    @Override
    protected void initializeAnimations() {
        // Create animation manager for Skeleton
        animationManager = new AnimationManager("Skeleton");
        
        // Get base path to skeleton assets
        String basePath = AssetManager.getEnemyAssetPath("Skeleton");
        
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
  
        int random = (int)(Math.random() * abilities.size());
        if (currentHP < maxHP * 0.3) { // does nothing for now
            return abilities.get(random);
        }
        
        return abilities.get(random);
    }
    
    /**
     * Execute an attack and return the damage.
     * 
     * @param moveName - which attack to use
     * @return total damage to deal to player
     */
    @Override
    public int executeAttack(String moveName) {
        if (moveName.equals("Slash")) {
            // Basic attack: base attack + 3 = 11 damage
            return attack + 3;
            
        } else if (moveName.equals("Bone Attack")) {
            // Stronger attack: base attack + 5 = 13 damage
            return attack + 5;
        }
        
        // Fallback - just use base attack
        return attack;
    }
}
