import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements KeyListener, ActionListener, MouseMotionListener {
    Timer timer;
    int delay = 8;
    int paddleX = 350;
    int paddleWidth = 100;
    ArrayList<Ball> balls;
    Brick bricks;
    LevelGenerator levelGen;
    JFrame parent;
    boolean endless;
    int level;
    int score = 0;
    ArrayList<PowerUp> powerUps = new ArrayList<>();
    
    // Game States
    boolean isPlaying = true;
    boolean isGameOver = false;
    boolean isLevelComplete = false;
    boolean isPaused = false;

    // Endless Mode Timer
    int endlessFrameCount = 0;
    int totalBricksBroken = 0;

    public GamePanel(JFrame parent, int level, boolean endless) {
        this.parent = parent;
        this.level = level;
        this.endless = endless;
        
        // Increase difficulty steadily with level
        if (!endless) {
            int initialDelay = 10;
            delay = Math.max(3, initialDelay - (level / 4));
        }

        addKeyListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        balls = new ArrayList<>();
        balls.add(new CircleBall(400, 400));
        
        if (endless) {
            levelGen = new LevelGenerator(new Random().nextInt(10) + 1, true);
        } else {
            levelGen = new LevelGenerator(level, false);
        }
        bricks = new Brick(levelGen.map);
        
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Background
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // Draw Bricks
        bricks.draw(g2d);

        // Draw PowerUps
        for (PowerUp pu : powerUps) {
            pu.draw(g2d);
        }

        // Draw Paddle
        g2d.setColor(new Color(50, 200, 255));
        g2d.fillRect(paddleX, 550, paddleWidth, 15);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(paddleX, 550, paddleWidth, 15);

        // Draw Balls
        for (Ball b : balls) {
            b.draw(g2d);
        }

        // UI Information
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Score: " + score, 20, 30);
        if (endless) g2d.drawString("Mode: Endless", 640, 30);
        else g2d.drawString("Level: " + level, 680, 30);
        
        // Add controls info
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString("M/ESC = Menu   |   P = Pause", 300, 30);

        // Overlay states
        if (isGameOver) {
            drawCenteredString(g2d, "GAME OVER", 48, new Color(255, 50, 50), getHeight() / 2 - 40);
            drawCenteredString(g2d, "Score: " + score, 24, Color.WHITE, getHeight() / 2 + 10);
            drawCenteredString(g2d, "Press [M] to Return to Menu", 20, Color.LIGHT_GRAY, getHeight() / 2 + 50);
        } else if (isLevelComplete) {
            drawCenteredString(g2d, "LEVEL " + level + " COMPLETE!", 48, new Color(50, 255, 50), getHeight() / 2 - 40);
            drawCenteredString(g2d, "Score: " + score, 24, Color.WHITE, getHeight() / 2 + 10);
            drawCenteredString(g2d, "Press [SPACE] for Next Level | [M] for Menu", 20, Color.LIGHT_GRAY, getHeight() / 2 + 50);
        } else if (isPaused) {
            drawCenteredString(g2d, "PAUSED", 48, Color.YELLOW, getHeight() / 2 - 20);
            drawCenteredString(g2d, "Press [SPACE] to Resume | [M] for Menu", 20, Color.LIGHT_GRAY, getHeight() / 2 + 30);
        }
        
        g2d.dispose();
    }
    
    private void drawCenteredString(Graphics2D g, String text, int size, Color c, int y) {
        g.setFont(new Font("Arial", Font.BOLD, size));
        g.setColor(c);
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        g.drawString(text, x, y);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isPlaying || isPaused) return;

        timer.start();
        
        for (int i = 0; i < balls.size(); i++) {
            Ball b = balls.get(i);
            b.move();
            checkCollision(b);
        }

        updatePowerUps();
        checkGameState();

        // Endless mode logic
        if (endless) {
            endlessFrameCount++;
            // Shift down approx every 8 seconds (if delay is ~8ms, 1000 frames is ~8s)
            if (endlessFrameCount > 1000) {
                levelGen.shiftDown();
                bricks.map = levelGen.map;
                endlessFrameCount = 0;
            }
        }

        repaint();
    }

    public void checkCollision(Ball b) {
        if (b.x < 0 || b.x > 780 - b.size) {
            b.dx *= -1;
        }
        if (b.y < 0) {
            b.dy *= -1;
        }
        // Paddle collision
        if (b.y + b.size > 550 && b.y < 565 && b.x + b.size > paddleX && b.x < paddleX + paddleWidth) {
            b.dy *= -1;
            // Add some English (spin) based on where it hit the paddle
            int hitPoint = b.x - paddleX;
            b.dx = (hitPoint - (paddleWidth / 2)) / 10;
            if(b.dx == 0) b.dx = 1;
        }

        // Brick collision0
        boolean hit = false;
        for (int i = 0; i < bricks.map.length; i++) {
            for (int j = 0; j < bricks.map[0].length; j++) {
                if (bricks.map[i][j] > 0) {
                    int brickX = j * bricks.brickWidth + 80;
                    int brickY = i * bricks.brickHeight + 50;
                    int bw = bricks.brickWidth;
                    int bh = bricks.brickHeight;

                    // AABB collision
                    if (b.x + b.size >= brickX && b.x <= brickX + bw && b.y + b.size >= brickY && b.y <= brickY + bh) {
                        bricks.map[i][j] = 0;
                        totalBricksBroken++;
                        score += 10;
                        hit = true;
                        
                        // Decide bounce direction based on overlap
                        int overlapLeft = (b.x + b.size) - brickX;
                        int overlapRight = (brickX + bw) - b.x;
                        int overlapTop = (b.y + b.size) - brickY;
                        int overlapBottom = (brickY + bh) - b.y;
                        
                        int minOverlapX = Math.min(overlapLeft, overlapRight);
                        int minOverlapY = Math.min(overlapTop, overlapBottom);
                        
                        if (minOverlapX < minOverlapY) {
                            b.dx *= -1;
                        } else {
                            b.dy *= -1;
                        }

                        // Spawn powerup (10% chance)
                        if (new Random().nextInt(10) == 0) {
                            powerUps.add(new PowerUp(brickX + bw / 2, brickY + bh / 2));
                        }
                        return; // Break out after hitting one brick to prevent double-hits
                    }
                }
            }
        }
    }

    public void updatePowerUps() {
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp pu = powerUps.get(i);
            pu.y += 3;
            
            // Catch power up
            if (pu.y + pu.height >= 550 && pu.y <= 565 && pu.x + pu.width >= paddleX && pu.x <= paddleX + paddleWidth) {
                if (pu.type == 0) {
                    paddleWidth = Math.min(250, paddleWidth + 30);
                } else if (pu.type == 1) {
                    balls.add(new SquareBall(paddleX + paddleWidth / 2, 530));
                } else if (pu.type == 2) {
                    delay += 2; // slow down
                    timer.setDelay(delay);
                }
                powerUps.remove(i);
                i--;
                score += 5;
            } else if (pu.y > 600) {
                powerUps.remove(i);
                i--;
            }
        }
    }

    public void checkGameState() {
        // Remove dead balls
        for (int i = 0; i < balls.size(); i++) {
            if (balls.get(i).y > 600) {
                balls.remove(i);
                i--;
            }
        }
        
        if (balls.isEmpty()) {
            isPlaying = false;
            isGameOver = true;
            timer.stop();
        }

        // Check if bricks reached bottom in endless mode
        if (endless) {
            boolean hitBottom = false;
            for (int i = 0; i < bricks.map.length; i++) {
                for (int j = 0; j < bricks.map[0].length; j++) {
                    if (bricks.map[i][j] > 0) {
                        int brickY = i * bricks.brickHeight + 50;
                        if (brickY + bricks.brickHeight >= 550) {
                            hitBottom = true;
                            break;
                        }
                    }
                }
                if (hitBottom) break;
            }
            if (hitBottom) {
                isPlaying = false;
                isGameOver = true;
                timer.stop();
            }
        }

        // Check level complete
        if (!endless) {
            boolean levelCleared = true;
            for (int i = 0; i < bricks.map.length; i++) {
                for (int j = 0; j < bricks.map[0].length; j++) {
                    if (bricks.map[i][j] > 0) {
                        levelCleared = false;
                        break;
                    }
                }
            }
            if (levelCleared) {
                isPlaying = false;
                isLevelComplete = true;
                timer.stop();
                if (level < 3) {
                    MenuPanel.levelUnlocked[level] = true; // unlock next level
                    SaveManager.saveProgress(); // Ensure progress is saved
                } else if (level == 3) {
                    MenuPanel.levelUnlocked[3] = true; // mark as fully completed
                    SaveManager.saveProgress(); // save the completion state
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(parent, "Congratulations! You have completed all the levels!");
                    });
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Always allow M or ESC to return to menu
        if (e.getKeyCode() == KeyEvent.VK_M || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            timer.stop();
            parent.getContentPane().removeAll();
            MenuPanel menu = new MenuPanel(parent);
            parent.getContentPane().add(menu);
            parent.revalidate();
            parent.repaint();
            menu.requestFocusInWindow();
            return;
        }

        if (!isPlaying) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                if (isLevelComplete && level < 3) {
                    // Next Level
                    parent.getContentPane().removeAll();
                    GamePanel nextLevel = new GamePanel(parent, level + 1, false);
                    parent.getContentPane().add(nextLevel);
                    parent.revalidate();
                    parent.repaint();
                    nextLevel.requestFocusInWindow();
                }
            }
            return;
        }

        // Pause game
        if (e.getKeyCode() == KeyEvent.VK_P) {
            isPaused = true;
            repaint();
            return;
        }

        if (isPaused) {
            // Unpause game
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                isPaused = false;
                repaint();
            }
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            paddleX = Math.max(0, paddleX - 30);
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            paddleX = Math.min(800 - paddleWidth, paddleX + 30);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!isPlaying) return;
        paddleX = e.getX() - paddleWidth / 2;
        if (paddleX < 0) paddleX = 0;
        if (paddleX > 800 - paddleWidth) paddleX = 800 - paddleWidth;
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
}