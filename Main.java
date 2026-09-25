import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {
    public static void main(String[] args) {
        String username = JOptionPane.showInputDialog(null, "Enter your Username:", "Login", JOptionPane.QUESTION_MESSAGE);
        if (username == null || username.trim().isEmpty()) {
            username = "Guest";
        }
        
        boolean userExists = SaveManager.isExistingUser(username);
        // Load the saved progress for this user
        SaveManager.loadProgress(username);

        JFrame obj = new JFrame();
        obj.setTitle("Brick Breaker PRO - Player: " + SaveManager.currentUser);
        // Use a fixed size instead of resizable to prevent layout bugs
        obj.setBounds(100, 100, 800, 600);
        obj.setResizable(false);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        if (userExists && MenuPanel.levelUnlocked[3]) {
            JOptionPane.showMessageDialog(obj, "Congratulations! You have completed all the levels!");
        }
        
        MenuPanel menu = new MenuPanel(obj);
        obj.add(menu);
        obj.setVisible(true);
        obj.getContentPane().getComponent(0).requestFocusInWindow();
    }
}