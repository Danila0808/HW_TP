package Entities;

import Game.Player;
import Map.Tile;

public abstract class Creature {
    private static final int MAP_SIZE = 10;

    protected Player owner;
    protected int x, y;
    protected int xp;
    protected int damage;
    protected int moveDistance;
    protected int baseMove;
    protected int attackRange;
    protected Tile[][] map;
    protected int cost;

    public Creature(int x, int y, int xp, int damage, int moveDistance, int attackRange, Tile[][] map, Player owner, int cost){
        this.x = x;
        this.y = y;
        this.xp = xp;
        this.damage = damage;
        this.moveDistance = moveDistance;
        this.attackRange = attackRange;
        this.map = map;
        this.owner = owner;
        this.cost = cost;

        this.baseMove = moveDistance;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getXp() { return xp; }
    public int getDamage() { return damage; }
    public int getMoveDistance() { return moveDistance; }
    public int getAttackRange() { return attackRange; }
    public int getCost(){ return cost; }
    public Player getOwner() { return owner; }

    public void setX(int newX) { x = newX; }
    public void setY(int newY) { y = newY; }
    public void setXp(int newXP) { xp = newXP; }
    public void setDamage(int newDamage) { damage = newDamage; }
    public void setMoveDistance(int newDistance) { moveDistance = newDistance; }
    public void setAttackRange(int newAttackRange) { attackRange = newAttackRange; }
    public void setCost(int cost) { this.cost = cost; }

    @Override
    public String toString() {
        return x+";"+y+";"+xp+";"+damage+";"+moveDistance+";"+attackRange+";"+owner.getName()+";"+cost;
    }
}
