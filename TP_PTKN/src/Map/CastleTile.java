package Map;

import Entities.Castle;
import Game.Player;

public class CastleTile extends Tile {
    private Player owner;
    private Castle castle;

    public CastleTile(int x, int y, String castleName, Player owner){
        super(x, y, TerrainType.GRASS);
        this.owner = owner;
        this.castle = new Castle(castleName);
    }

    public Castle getCastle() { return castle; }
    public Player getOwner() { return owner; }
}
