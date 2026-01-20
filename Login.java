import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * - Username text field
 * - Password field (hidden characters)
 * - Submit button
 * - Error message display
 * - Back button to return to main menu

 */
public class Login extends JPanel {
    

    private BufferedImage backgroundFrame;    // background image
    private EarthboundFrame parentFrame;     
    private JTextField usernameField;         // input for username
    private JPasswordField passwordField;     // input for password (hidden)
    private JLabel errorLabel;               
    

    public Login() {
        setLayout(null); 
        setBackground(new Color(40, 40, 40));
        
        loadBackground();
        

        SwingUtilities.invokeLater(() -> {
            parentFrame = (EarthboundFrame) SwingUtilities.getWindowAncestor(this);
            initializeUI();
        });
    }
    
    /**
     * Load background image from assets.
     */
    private void loadBackground() {
        try {
            File file = new File(AssetManager.getMainMenuFrame());
            if (file.exists()) {
                backgroundFrame = ImageIO.read(file);
            } else {
                backgroundFrame = AssetManager.createPlaceholder(1000, 750);
            }
        } catch (Exception e) {
            backgroundFrame = AssetManager.createPlaceholder(1000, 750);
        }
    }
    
    /**
     * Set up all UI components.
     */
    private void initializeUI() {
        // ========== BACK BUTTON ==========
        // Returns user to main menu
        JButton backBtn = new JButton();
        try {
            // Try to load back arrow image
            File file = new File(AssetManager.getBackButton());
            if (file.exists()) {
                BufferedImage backImg = ImageIO.read(file);
                // Scale image to fit button
                backBtn.setIcon(new ImageIcon(backImg.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH)));
            } else {
                backBtn.setText("BACK");  // fallback text
            }
        } catch (Exception e) {
            backBtn.setText("BACK");
        }
        backBtn.setBounds(200, 120, 60, 60);
        backBtn.setBackground(new Color(218, 165, 32));
        backBtn.setFocusPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> parentFrame.switchPanel(new MainMenu()));
        add(backBtn);
        
        //  TITLE 
        JLabel titleLabel = new JLabel("LOGIN");
        titleLabel.setFont(new Font("CASTELLAR", Font.BOLD, 48));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setBounds(200, 150, 600, 60);
        add(titleLabel);
        
        //  USERNAME LABEL 
        JLabel usernameLabel = new JLabel("USERNAME");
        usernameLabel.setFont(new Font("CASTELLAR", Font.BOLD, 18));
        usernameLabel.setForeground(Color.BLACK);
        usernameLabel.setBounds(250, 250, 150, 30);
        add(usernameLabel);
        
        //  USERNAME INPUT 
        usernameField = new JTextField();
        usernameField.setBounds(430, 250, 250, 35);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 16));
        add(usernameField);
        
        //  PASSWORD LABEL 
        JLabel passwordLabel = new JLabel("PASSWORD");
        passwordLabel.setFont(new Font("CASTELLAR", Font.BOLD, 18));
        passwordLabel.setForeground(Color.BLACK);
        passwordLabel.setBounds(250, 310, 150, 30);
        add(passwordLabel);
        
        //  PASSWORD INPUT 
       
        passwordField = new JPasswordField();
        passwordField.setBounds(430, 310, 250, 35);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        add(passwordField);
        
        //  ERROR MESSAGE LABEL 
     
        errorLabel = new JLabel();
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        errorLabel.setForeground(new Color(255, 100, 100));  // red for errors
        errorLabel.setBounds(250, 360, 450, 30);
        add(errorLabel);
        
        //  SUBMIT BUTTON 
        JButton submitBtn = new JButton("SUBMIT");
        submitBtn.setFont(new Font("CASTELLAR", Font.BOLD, 18));
        submitBtn.setForeground(Color.BLACK);
        submitBtn.setBackground(new Color(218, 165, 32));
        submitBtn.setBounds(350, 420, 300, 60);
        submitBtn.setFocusPainted(false);
        submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitBtn.addActionListener(e -> handleLogin());
        add(submitBtn);
    }
    

    private void handleLogin() {
        // Get the entered values
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        // Check for empty fields
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields");
            return;
        }
        
        // Validate credentials using UserManager
        if (UserManager.validateUser(username, password)) {
          
            errorLabel.setText("");
            parentFrame.switchPanel(new SaveManager(username));
        } else {
            // Failed, so show error and clear password
            errorLabel.setText("Invalid username or password");
            passwordField.setText("");
        }
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundFrame != null) {
            g.drawImage(backgroundFrame, 0, 0, getWidth(), getHeight(), null);
        }
    }
}
