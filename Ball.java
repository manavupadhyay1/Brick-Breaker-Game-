import java.awt.Graphics2D;

public abstract class Ball {
    public int x, y;
    public int dx = 3, dy = -3;
    public int size = 15; // Increased size for visibility

    public Ball(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void move(){
        x += dx;
        y += dy;
    }

    public abstract void draw(Graphics2D g);
}