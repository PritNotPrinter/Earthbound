import java.util.ArrayList;

/**
 * Enemy.java - Abstract base class for all enemies
 * 
 * This works just like Player.java but for enemies
 * - HP, attack, defense stats
 * - Animation management
 * - Damage/healing methods
 * - Abstract methods that each enemy type must implement
 * enemies share common code (like takeDamage, heal) so abstract classes make sense
 */
public abstract class Enemy {
    
    // ==================== IDENTITY ====================
    protected String enemyName;   // display name (e.g., "Skeleton")
    protected String enemyType;   // type identifier for asset loading
    
    // ==================== PROGRESSION ====================
    protected int level;          // enemy level (matches stage number)
    protected int stage;          // which stage this enemy appears on (1, 2, or 3)
    
    // ==================== COMBAT STATS ====================
    protected int currentHP;      // how much HP the enemy has right now
    protected int maxHP;          // maximum HP the enemy can have
    protected int attack;         // base attack power for damage calculations
    protected int defense;       
    
    // ==================== ABILITIES ====================
    protected ArrayList<String> abilities;  // list of moves this enemy can use
    
    // ==================== ANIMATION ====================
    protected AnimationManager animationManager;  // handles sprite animation
    
    /**
     * @param enemyName - display name for the enemy
     * @param enemyType - type string for asset loading
     * @param stage - which stage (1-3) this enemy is for
     */
    public Enemy(String enemyName, String enemyType, int stage) {
        this.enemyName = enemyName;
        this.enemyType = enemyType;
        this.stage = stage;
        this.level = stage;  // level matches stage
        this.abilities = new ArrayList<>();  // initialize empty ability list
        initializeStats();       // set up HP, attack, defense
        initializeAnimations();  // load sprite animations
    }
    
    /**
     * Enemy-specific stats.
     * 
     * Each subclass sets their own HP, attack, defense, and abilities
     */
    protected abstract void initializeStats();
    
    /**
     * Initialize enemy animations.
     * Each subclass loads their own sprite sheets from assets folder.
     */
    protected abstract void initializeAnimations();
    
    /**
     * enemy action logic
     * @param opponent - the player, so enemy can make decisions based on player state
     * @return the name of the move the enemy will use
     */
    public abstract String decideAction(Player opponent);
    
    /**
     * attack and return damage dealt
     * 
     * @param moveName - which attack move to execute
     * @return the amount of damage to deal to the player
     */
    public abstract int executeAttack(String moveName);
    
    /**
     * Take damage from a player attack
     * 
     * Uses Math.max to ensure HP never goes below 0.
     * 
     * @param damage - amount of damage to take
     */
    public void takeDamage(int damage) {
        this.currentHP = Math.max(0, this.currentHP - damage);
    }
    
    /**
     * Restore HP
     * 
     * Uses Math.min to ensure HP never exceeds maxHP.
     * 
     * @param amount - amount of HP to restore
     */
    public void heal(int amount) {
        // Clamp HP to maximum (can't overheal!)
        this.currentHP = Math.min(this.maxHP, this.currentHP + amount);
    }
    
    //  GETTERS 
    
    public String getEnemyName() { return enemyName; }
    public String getEnemyType() { return enemyType; }
    
    public int getLevel() { return level; }
    public int getStage() { return stage; }
    
    public int getCurrentHP() { return currentHP; }
    public int getMaxHP() { return maxHP; }
    
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    
    public ArrayList<String> getAbilities() { return abilities; }
    

    public boolean isAlive() { return currentHP > 0; }
    
    /**
     * Get the animation manager, initializing if needed.
     * 
     * @return the AnimationManager for this enemy
     */
    public AnimationManager getAnimationManager() {
        if (animationManager == null) {
            initializeAnimations();
        }
        return animationManager;
    }
}
