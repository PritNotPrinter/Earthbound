/**
 * - Who's playing (username, save slot)
 * -  character (Player object)
 * - Current enemy 
 * - Game settings (difficulty, stage)
 * - Combat stats (damage dealt/taken, XP gained)
 * 
 */
public class GameSession {
    
    //  USER INFO 
    private String username;      // who's logged in
    private int saveSlot;         // which save file (0, 1, or 2)
    
    //  GAME OBJECTS 
    private Player player;        // the character
    private Enemy currentEnemy;   // enemy fighting
    
    //  SETTINGS 
    private int difficulty;       // 1 = Normal, 2 = Hard (enemies get +30% stats)
    private int stage;          
    
    //  ROUND STATISTICS 

    private int roundsDamageDealt;       // total damage player dealt this fight
    private int roundsDamageTaken;       // total damage player took this fight
    private long roundsExperienceGained; // XP earned this fight
    
    /**
     * Constructor - creates a new game session.
     * 
     * @param username - the logged-in player's username
     * @param saveSlot - which save file slot (0, 1, 2)
     * @param player - the Player character object
     * @param difficulty - 1 for Normal, 2 for Hard
     */
    public GameSession(String username, int saveSlot, Player player, int difficulty) {
        this.username = username;
        this.saveSlot = saveSlot;
        this.player = player;
        this.difficulty = difficulty;
        this.stage = 1;  // always start at stage 1
        
        // Initialize round stats to 0
        this.roundsDamageDealt = 0;
        this.roundsDamageTaken = 0;
        this.roundsExperienceGained = 0;
        
        // Create the first enemy!
        generateEnemy();
    }
    
    /**
     * Generate the enemy for the current stage.
     * 
     * Stage 1 → Goblin (medium enemy, comes first)
     * Stage 2 → Skeleton (actually the weakest, but comes second)
     * Stage 3 → Plent (supposed to be the boss)
     */
    private void generateEnemy() {
        // Use switch to create the right enemy based on stage
        switch (stage) {
            case 1:
                currentEnemy = new Goblin();   // first fight
                break;
            case 2:
                currentEnemy = new Skeleton(); // second fight  
                break;
            case 3:
                currentEnemy = new Plent();    // third fight
                break;
            default:
                // Fallback in case something goes wrong
                currentEnemy = new Goblin();
        }
        
        // HARD MODE: Scale enemy stats by 30%
        if (difficulty == 2) {
            currentEnemy.maxHP = (int) (currentEnemy.maxHP * 1.3);      // 30% more HP
            currentEnemy.attack = (int) (currentEnemy.attack * 1.3);    // 30% more attack
            currentEnemy.defense = (int) (currentEnemy.defense * 1.3);  // 30% more defense
        }
    }
    
    /**
     * Progress to the next stage after defeating an enemy.
     */
    public void nextStage() {
        if (stage < 4) {
            stage++;  // go to next stage
            
            // Reset round stats for the new fight
            roundsDamageDealt = 0;
            roundsDamageTaken = 0;
            roundsExperienceGained = 0;
            
            // Create the new enemy for this stage
            generateEnemy();
        }
        // If stage >= 4, game is complete
    }
    
    //  GETTERS AND SETTERS 
    
    public String getUsername() { return username; }
    public int getSaveSlot() { return saveSlot; }
    
    public Player getPlayer() { return player; }
    
    public Enemy getCurrentEnemy() { return currentEnemy; }
    
    public int getDifficulty() { return difficulty; }
    public int getStage() { return stage; }
    
    // Round damage dealt tracking
    public int getRoundsDamageDealt() { return roundsDamageDealt; }
    public void addRoundsDamageDealt(int damage) { 
        this.roundsDamageDealt += damage; 
    }
    
    // Round damage taken tracking
    public int getRoundsDamageTaken() { return roundsDamageTaken; }
    public void addRoundsDamageTaken(int damage) { 
        this.roundsDamageTaken += damage; 
    }
    
    // Round XP tracking
    public long getRoundsExperienceGained() { return roundsExperienceGained; }
    public void addRoundsExperienceGained(long exp) { 
        this.roundsExperienceGained += exp; 
    }
}
