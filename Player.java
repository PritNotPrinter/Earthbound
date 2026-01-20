import java.util.ArrayList;

/**
 * Player.java - Abstract base class for all playable characters
 */
public abstract class Player {
    
    // ==================== CHARACTER IDENTITY ====================
    protected String characterName;      // the name the player chose (like "prit")
    protected String className;          // which class this is (Barbarian, Mage, Archer)
    
    // ==================== PROGRESSION STATS ====================
    protected int level;                 // current level (starts at 1, increases with exp)
    protected long experience;           // total experience points earned
    
    // ==================== COMBAT STATS ====================
    protected int currentHP;             // how much health the player has right now
    protected int maxHP;                 // maximum health (increases on level up)
    protected int currentMana;           // how much mana the player has right now
    protected int maxMana;               // maximum mana (used for abilities)
    protected int attack;                // base attack power (affects damage dealt)
    protected int defense;               // base defense (doesn't do much rn but could reduce damage)
    
    // ==================== ABILITIES & ANIMATION ====================
    protected ArrayList<String> abilities;         // list of ability names this class has
    protected AnimationManager animationManager;   // handles sprite animations for this character
    
    /**
     * Constructor - creates a new player character.
     * 
     * Sets up basic stats and calls initializeStats() which each subclass
     * overrides to set their specific starting values.
     * 
     * @param characterName - the name chosen by the player
     * @param className - which class this is (set by subclass)
     */
    public Player(String characterName, String className) {
        this.characterName = characterName;
        this.className = className;
        this.level = 1;              // everyone starts at level 1
        this.experience = 0;         // no exp yet
        this.abilities = new ArrayList<>();
        initializeStats();           // let subclass set HP, mana, etc.
    }
    
    // ==================== ABSTRACT METHODS ====================
    // MUST be implemented by subclasses 
    
    /**
     * Initialize class-specific stats (HP, mana, attack, defense).
     * Each class has different starting values.
     */
    protected abstract void initializeStats();
    
    /**
     * Get the list of attack moves this class can use.
     * @return ArrayList of attack move names
     */
    public abstract ArrayList<String> getAttackMoves();
    
    /**
     * Get the list of defense/utility moves this class can use.
     * @return ArrayList of defense move names
     */
    public abstract ArrayList<String> getDefenseMoves();
    
    /**
     * Execute an attack move and return damage dealt.
     * @param moveName - which attack to use
     * @return damage value, or 0 if attack failed (not enough mana, etc.)
     */
    public abstract int executeAttack(String moveName);
    
    /**
     * Execute a defense move (heal, dodge, etc.).
     * @param moveName - which defense to use
     * @return block value or 0
     */
    public abstract int executeDefense(String moveName);
    
    // ==================== SAVE/LOAD SYSTEM ====================
    
    /**
     * Get class-specific data that needs to be saved.
     * 
     * Some classes have unique state (like Barbarian's lastActionWasRush).
     * Subclasses override this to include their special data.
     * 
     * @return string of class-specific data, or empty string if none
     */
    public String getClassData() {
        return "";  // default: no special data
    }
    
    /**
     * Load class-specific data from a save string.
     * Subclasses override this to restore their special state.
     * 
     * @param data - the saved data string
     */
    public void loadClassData(String data) {
        // Default implementation does nothing
    }

    /**
     * Get a formatted label for action buttons.
     * 
     * Subclasses override this to show things like "(ATK+5, -10 MP)"
     * 
     * @param moveName - name of the move
     * @param isAttack - true if it's an attack move
     * @return formatted string for button display
     */
    public String getActionLabel(String moveName, boolean isAttack) {
        return moveName;  // default: just return the move name
    }

    public String toSaveString() {
        return className + "," + 
               characterName + "," + 
               level + "," + 
               currentHP + "," + 
               maxHP + "," + 
               currentMana + "," + 
               maxMana + "," + 
               attack + "," + 
               defense + "," + 
               experience + "," +
               getClassData();
    }
    
    /**
     * Create a Player object from a save string.
     * @param saveString - the comma-separated save data
     * @return a Player object (Barbarian, Mage, or Archer) or null if invalid
     */
    public static Player fromSaveString(String saveString) {
        // Split the string by commas into an array
        String[] parts = saveString.split(",");
        
        // Need at least 10 parts for the basic data
        if (parts.length < 10) return null;
        
        // Parse each value from the array
        String className = parts[0];
        String characterName = parts[1];
        int level = Integer.parseInt(parts[2]);
        int currentHP = Integer.parseInt(parts[3]);
        int maxHP = Integer.parseInt(parts[4]);
        int currentMana = Integer.parseInt(parts[5]);
        int maxMana = Integer.parseInt(parts[6]);
        int attack = Integer.parseInt(parts[7]);
        int defense = Integer.parseInt(parts[8]);
        long experience = Long.parseLong(parts[9]);
        
        // Class-specific data is optional (index 10+)
        String classSpecificData = parts.length > 10 ? parts[10] : "";
        
        // Create the appropriate player subclass based on className
        Player player;
        switch (className) {
            case "Barbarian":
                player = new Barbarian(characterName);
                break;
            case "Archer":
                player = new Archer(characterName);
                break;
            case "Mage":
                player = new Mage(characterName);
                break;
            default:
                return null;  // unknown class, can't load
        }
        
        // Override the default stats with the saved values
        player.level = level;
        player.currentHP = currentHP;
        player.maxHP = maxHP;
        player.currentMana = currentMana;
        player.maxMana = maxMana;
        player.attack = attack;
        player.defense = defense;
        player.experience = experience;
        
        // Let the subclass restore its special data
        player.loadClassData(classSpecificData);
        
        return player;
    }
    
    // ==================== COMBAT METHODS ====================
    
    /**
     * Take damage from an attack.
     * Uses Math.max to prevent HP from going negative.
     * 
     * @param damage - amount of damage to take
     */
    public void takeDamage(int damage) {
        this.currentHP = Math.max(0, this.currentHP - damage);
    }
    
    /**
     * Restore HP (healing).
     * Uses Math.min to prevent HP from exceeding max.
     * 
     * @param amount - amount of HP to restore
     */
    public void heal(int amount) {
        this.currentHP = Math.min(this.maxHP, this.currentHP + amount);
    }
    
    /**
     * Restore mana.
     * Uses Math.min to prevent mana from exceeding max.
     * 
     * @param amount - amount of mana to restore
     */
    public void restoreMana(int amount) {
        this.currentMana = Math.min(this.maxMana, this.currentMana + amount);
    }
    
    /**
     * Try to use mana for an ability.
     * 
     * @param amount - mana cost of the ability
     * @return true if had enough mana (and it was deducted), false if not enough
     */
    public boolean useMana(int amount) {
        if (this.currentMana >= amount) {
            this.currentMana -= amount;
            return true;   // success!
        }
        return false;      // not enough mana
    }
    
    // ==================== PROGRESSION METHODS ====================
    
    /**
     * Add experience points and check for level up.
     * 
     * @param exp - experience points to add
     */
    public void addExperience(long exp) {
        this.experience += exp;
        checkLevelUp();
    }
    
    /**
     * Check if player has enough EXP to level up.
     * 
     * Uses exponential scaling: each level requires 1.5x more exp than the last.
     * Level 1->2: 100 exp, Level 2->3: 150 exp, Level 3->4: 225 exp, etc.
     * 
     * This formula makes early levels quick and later levels take longer.
     */
    private void checkLevelUp() {
        // Calculate required exp using exponential formula
        long requiredExp = (long) (100 * Math.pow(1.5, level - 1));
        if (this.experience >= requiredExp) {
            levelUp();
        }
    }
    
    /**
     * Level up the player - increase all stats.
     * 
     * Also fully heals HP and mana as a bonus for leveling.
     */
    private void levelUp() {
        this.level++;
        this.maxHP += 10;            // +10 max HP per level
        this.currentHP = this.maxHP; // full heal
        this.maxMana += 5;           // +5 max mana per level
        this.currentMana = this.maxMana;  // full mana restore
        this.attack += 2;            // +2 attack per level
        this.defense += 1;           // +1 defense per level
    }
    
    // ==================== GETTERS AND SETTERS ====================

    
    public String getCharacterName() { return characterName; }
    public void setCharacterName(String name) { this.characterName = name; }
    
    public String getClassName() { return className; }
    
    public int getLevel() { return level; }
    
    public int getCurrentHP() { return currentHP; }
    public int getMaxHP() { return maxHP; }
    
    public int getCurrentMana() { return currentMana; }
    public int getMaxMana() { return maxMana; }
    
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    
    public long getExperience() { return experience; }
    
    /**
     * Check if player is still alive.
     * @return true if HP > 0
     */
    public boolean isAlive() { return currentHP > 0; }
    
    /**
     * Get the animation manager for this player.
     * @return the AnimationManager instance
     */
    public AnimationManager getAnimationManager() {
        return animationManager;
    }
}
