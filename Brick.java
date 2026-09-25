import java.awt.*;

public class Brick {

    public int map[][];
    public int brickWidth;
    public int brickHeight;

    public Brick(int map[][]){
        this.map = map;
        this.brickWidth = 70;
        this.brickHeight = 30;
    }

    public void draw(Graphics2D g){
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                if(map[i][j] > 0){
                    // Multi-colored bricks based on row for a gradient effect
                    int red = Math.min(255, 100 + i * 30);
                    int green = Math.min(255, 50 + j * 20);
                    int blue = 200 - i * 15;
                    Color brickColor = new Color(red, green, blue);
                    
                    g.setColor(brickColor);
                    // Simple rectangle for simpler UI
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                    
                    // Simple border
                    g.setColor(Color.BLACK);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }
}