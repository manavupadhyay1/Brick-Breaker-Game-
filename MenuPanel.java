import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MenuPanel extends JPanel implements ActionListener {

    JButton levelBtn[], endlessBtn, quitBtn, changeUserBtn;
    JFrame parent;
    List<String> leaderboard;

    public static boolean levelUnlocked[] = new boolean[4];

    static {
        levelUnlocked[0] = true;
    }

    public MenuPanel(JFrame parent) {
        this.parent = parent;
        this.leaderboard = SaveManager.getLeaderboard();
        setLayout(null);


        // Modern Title
        JLabel title = new JLabel("BRICK BREAKER PRO", SwingConstants.CENTER); // changed title positioning to center
        title.setFont(new Font("Monospaced", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        title.setBounds(100, 70, 600, 60);
        add(title);

        changeUserBtn = createStyledButton("Change User");
        changeUserBtn.setBounds(630, 20, 140, 30);
        changeUserBtn.setFont(new Font("Arial", Font.BOLD, 12));
        changeUserBtn.addActionListener(this);
        add(changeUserBtn);

        levelBtn = new JButton[3];

        // 3 buttons for 3 levels
        for(int i = 0; i < 3; i++){
            levelBtn[i] = createStyledButton("Level " + (i+1));
            // Spread across width
            levelBtn[i].setBounds(180 + i * 160, 200, 120, 50);
            levelBtn[i].addActionListener(this);

            if(!levelUnlocked[i]){
                levelBtn[i].setEnabled(false);
                levelBtn[i].setBackground(Color.DARK_GRAY);
            }
            add(levelBtn[i]);
        }

        endlessBtn = createStyledButton("Endless Mode");
        endlessBtn.setBounds(300, 450, 200, 40);
        endlessBtn.addActionListener(this);
        add(endlessBtn);

        quitBtn = createStyledButton("Quit");
        quitBtn.setBounds(300, 505, 200, 40);
        quitBtn.addActionListener(this);
        add(quitBtn);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(50, 150, 250));
        btn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if(btn.isEnabled()) btn.setBackground(new Color(80, 180, 255));
            }
            public void mouseExited(MouseEvent evt) {
                if(btn.isEnabled()) btn.setBackground(new Color(50, 150, 250));
            }
        });
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Simple plain black background
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw Leaderboard
        g2d.setColor(new Color(30, 30, 30));
        g2d.fillRoundRect(550, 420, 200, 130, 15, 15);
        g2d.setColor(new Color(50, 150, 250));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(550, 420, 200, 130, 15, 15);
        
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        g2d.setColor(Color.YELLOW);
        g2d.drawString("TOP PLAYERS", 600, 440);

        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(Color.LIGHT_GRAY);
        if (leaderboard != null && !leaderboard.isEmpty()) {
            int y = 465;
            for (String entry : leaderboard) {
                g2d.drawString(entry, 565, y);
                y += 20;
            }
        } else {
            g2d.drawString("No players yet.", 600, 480);
        }
    }

    public void actionPerformed(ActionEvent e) {
        for(int i = 0; i < 3; i++){
            if(e.getSource() == levelBtn[i]){
                parent.getContentPane().removeAll();
                GamePanel gp = new GamePanel(parent, i + 1, false);
                parent.getContentPane().add(gp);
                parent.revalidate();
                parent.repaint();
                gp.requestFocusInWindow();
            }
        }

        if(e.getSource() == endlessBtn){
            parent.getContentPane().removeAll();
            GamePanel gp = new GamePanel(parent, 1, true);
            parent.getContentPane().add(gp);
            parent.revalidate();
            parent.repaint();
            gp.requestFocusInWindow();
        }

        if(e.getSource() == quitBtn){
            System.exit(0);
        }

        if(e.getSource() == changeUserBtn){
            String newUsername = JOptionPane.showInputDialog(parent, "Enter your Username:", "Change User", JOptionPane.QUESTION_MESSAGE);
            if (newUsername != null && !newUsername.trim().isEmpty()) {
                boolean userExists = SaveManager.isExistingUser(newUsername);
                SaveManager.loadProgress(newUsername);
                
                parent.setTitle("Brick Breaker PRO - Player: " + SaveManager.currentUser);
                
                if (userExists && MenuPanel.levelUnlocked[3]) {
                    JOptionPane.showMessageDialog(parent, "Congratulations! You have completed all the levels!");
                }
                
                // Refresh the menu panel to show unlocked levels for the new user
                parent.getContentPane().removeAll();
                MenuPanel menu = new MenuPanel(parent);
                parent.getContentPane().add(menu);
                parent.revalidate();
                parent.repaint();
                menu.requestFocusInWindow();
            }
        }


    }
}