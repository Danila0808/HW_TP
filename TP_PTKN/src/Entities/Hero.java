package Entities;

import Game.Console;
import Game.Player;
import Images.Icons;
import Map.LightTile;
import Map.TerrainTile;
import Map.Tile;
import Map.TreasureTile;

import java.util.ArrayList;
import java.util.List;

public class Hero extends Creature {
    private List<Unit> units;
    private Icons icon;

    public Hero(int x, int y, int xp, int damage, int moveDistance, int attackRange, Tile[][] map, Player owner, List<Unit> units){
        super(x, y, xp, damage, moveDistance, attackRange, map, owner, 3000);
        this.units = units;
        this.icon = Icons.WIZARD;
    }

    public Icons getIcon(){ return icon; }
    public List<Unit> getUnits(){ return units; }

    public void setIcon(Icons icon) { this.icon = icon; }
    public void addUnit(Unit u) { this.units.add(u); }

    public void startNewTurn() {
        setMoveDistance(baseMove);
    }

    public void moveHero(Tile[][] map, int dx, int dy){
        int newX = x + dx;
        int newY = y + dy;

        if (newX < 0 || newX >= map.length || newY < 0 || newY >= map[0].length) {
            Console.addEvent("❌ Нельзя выйти за границы карты!");
            return;
        }

        Tile targetTile = map[newY][newX];
        int penalty = (targetTile).getMovementPenalty(targetTile.getType());

        if(penalty > 5){
            Console.addEvent("🚧 Нельзя пройти на " + targetTile.getType() + "!");
            return;
        }

        if(moveDistance < penalty){
            Console.addEvent("⚠ Недостаточно очков движения! Нужно: " + penalty);
            return;
        }

        if(targetTile instanceof TreasureTile){
            ((TreasureTile) targetTile).collect(owner);
        }
        if(targetTile instanceof LightTile){
            ((LightTile) targetTile).getLighthouse().work(true);
        }

        x = newX;
        y = newY;
        moveDistance -= penalty;
        Console.addEvent("🚶‍♂️ Герой " + owner.getName() + " переместился в (" + x + "," + y + "), осталось " + moveDistance + " очков хода.");
    }

    public boolean recruitUnit(PerkType unitType, int x, int y) {
        if (owner.canRecruitUnit(unitType)) {
            Unit newUnit = createUnit(unitType, x, y);
            if (newUnit != null && owner.recruitUnit(newUnit)) {
                units.add(newUnit);
                return true;
            }else{
                Console.addEvent("❌ Невозможно нанять юнита " + unitType + ". Денег не хватает или армия полная!");
            }
        } else {
            Console.addEvent("❌ Невозможно нанять юнита " + unitType + ". Здание не построено!");
        }
        return false;
    }

    public Unit createUnit(PerkType unitType, int x, int y) {
        switch (unitType) {
            case SPEARMAN:
                return new Unit(x, y, 100, 66, 4, 1, map, owner, PerkType.SPEARMAN, 500);
            case CROSSBOMEN:
                return new Unit(x, y, 80, 80, 4, 5, map, owner, PerkType.CROSSBOMEN, 1000);
            case SWORDSMAN:
                return new Unit(x, y, 140, 75, 3, 1, map, owner, PerkType.SWORDSMAN, 1500);
            case CAVALRY:
                return new Unit(x, y, 160, 70, 6, 1, map, owner, PerkType.CAVALRY, 2000);
            case PALADIN:
                return new Unit(x, y, 200, 99, 4, 1, map, owner, PerkType.PALADIN, 2500);
            default:
                return null;
        }
    }
}
