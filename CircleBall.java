import java.awt.Color;
import java.awt.Graphics2D;

public class CircleBall extends Ball {
    public CircleBall(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillOval(x, y, size, size);
    }
}
