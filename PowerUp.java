import java.awt.*;
import java.util.Random;

public class PowerUp {
    public int x, y;
    public int type;
    public int width = 20, height = 20;

    // 0: Extend Paddle (Green)
    // 1: Extra Ball (Cyan)
    // 2: Slow Down (Magenta)

    public PowerUp(int x, int y){
        this.x = x;
        this.y = y;
        this.type = new Random().nextInt(3);
    }

    public void draw(Graphics2D g) {
        if(type == 0) g.setColor(Color.GREEN);
        else if(type == 1) g.setColor(Color.CYAN);
        else g.setColor(Color.MAGENTA);
        
        g.fillOval(x, y, width, height);
        g.setColor(Color.WHITE);
        g.drawOval(x, y, width, height);
    }
}