import javax.swing.*;

/**
 * EarthboundFrame.java - The main game window
 * 
 * This class creates the actual window you see when playing the game.
 */
public class EarthboundFrame extends JFrame {
    
    
    public EarthboundFrame() {
        // title
        setTitle("Earthbound");
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Window size in pixels (width x height) - picked 1000x750 because that's what my backgrounds were in
        setSize(1000, 750);
        
        // centers the window on screen 
        setLocationRelativeTo(null);
        
        // Prevents resizing so the backgrounds work
        setResizable(false);
        
        // mainmenu is the first thing players should see
        setContentPane(new MainMenu());
        setVisible(true);
    }
    
    /**
     * Switch to a different panel (screen) in the game.
     * login, menu, combat, etc.
     * 
     * @param newPanel - the JPanel to switch to (MainMenu, GameManager, etc.)
     */
    public void switchPanel(JPanel newPanel) {
        // Replace the current content with the new panel
        setContentPane(newPanel);
        
        // revalidate() tells Swing to recalculate the layout
        revalidate();
        
        // repaint() forces a visual refresh 
        repaint();
    }
}
