import java.util.Random;

public class LevelGenerator {
    public int map[][];
    private final int cols = 8;
    
    public LevelGenerator(int level, boolean isEndless){
        // Level limits rows up to 10 max
        int rows = Math.min(3 + level, 10);
        map = new int[rows][cols];
        
        Random r;
        if (isEndless) {
            r = new Random(); // Truly random every time
        } else {
            r = new Random(level * 1337L); // Predictable structure for each level
        }
        generate(r);
    }

    public void generate(Random r){
        int type = r.nextInt(3); // 3 different patterns

        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                if(type == 0) {
                    map[i][j] = (j % 2 == 0) ? 1 : 0; // Checkered
                } else if(type == 1) {
                    map[i][j] = 1; // Full row
                } else {
                    map[i][j] = r.nextInt(2); // Random
                }
            }
        }
        // Ensure at least some bricks exist
        boolean hasBricks = false;
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                if(map[i][j] > 0) hasBricks = true;
            }
        }
        if(!hasBricks) map[0][0] = 1;
    }

    public void shiftDown(){
        // Create a new map with one extra row
        int newRows = map.length + 1;
        int[][] newMap = new int[newRows][cols];

        // Shift all existing rows down by 1
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < cols; j++){
                newMap[i + 1][j] = map[i][j];
            }
        }
        
        // Generate new top row
        Random r = new Random();
        for(int j = 0; j < cols; j++){
            newMap[0][j] = r.nextInt(2);
        }
        
        map = newMap;
    }
}