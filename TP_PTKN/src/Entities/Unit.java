package Entities;

import Game.Player;
import Map.Tile;

public class Unit extends Creature {
    private PerkType type;

    public Unit(int x, int y, int xp, int damage, int moveDistance, int attackRange, Tile[][] map, Player owner, PerkType type, int cost){
        super(x, y, xp, damage, moveDistance, attackRange, map, owner, cost);
        this.type = type;
    }

    public void startNewTurn() {
        setMoveDistance(baseMove);
    }

    public PerkType getType() { return type; }
}
