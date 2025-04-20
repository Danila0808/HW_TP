package Map;

import Game.Console;
import Game.Player;

public class TreasureTile extends Tile {
    private int value;
    private boolean isCollected;

    public TreasureTile(int x, int y, int value){
        super(x, y, TerrainType.GRASS);
        this.value = value;
    }

    public int getValue() { return value; }
    public void setCollected() { isCollected = true; }
    public boolean isCollected() { return isCollected; }

    public void collect(Player player){
        if(!isCollected){
            isCollected = true;
            player.addGold(value);
            Console.addEvent("Сундук на [" + x + "," + y + "] собран! Получено: " + value);
        }
    }
}
