package Map;

import java.util.HashMap;
import java.util.Map;

public class Tile {
    private static final Map<TerrainType, Integer> movementPenalty = new HashMap<>();
    private boolean isLighted = false;

    static {
        movementPenalty.put(TerrainType.GRASS, 3);
        movementPenalty.put(TerrainType.WATER, Integer.MAX_VALUE);
        movementPenalty.put(TerrainType.MOUNTAIN, Integer.MAX_VALUE);
        movementPenalty.put(TerrainType.FOREST, 5);
        movementPenalty.put(TerrainType.ROAD, 1);
    }

    public static int getMovementPenalty(TerrainType type) { return movementPenalty.get(type); }

    protected int x, y;
    protected TerrainType type;

    public Tile(int x, int y, TerrainType type){
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public TerrainType getType() { return type; }
    public boolean isLighted() { return isLighted; }

    public void setLighted(boolean lighted) { isLighted = lighted; }

    @Override
    public String toString(){
        return x + ";" + y + ";" + type+";"+isLighted;
    }
}
