package PvP;

import Entities.Hero;
import Entities.Unit;
import Game.Player;

import java.util.List;
import java.util.Scanner;

public class pvpAI {
    private Hero hero;
    private Hero playerHero;
    private Player computer;
    private pvpMapRender renderer;
    private List<Unit> computerUnits;
    private pvpAttack attack;
    private final int SIZE = pvpMapRender.SIZE;

    public pvpAI(Hero hero, Hero playerHero, Player computer, pvpMapRender renderer) {
        this.hero = hero;
        this.playerHero = playerHero;
        this.computer = computer;
        this.renderer = renderer;

        this.computerUnits = hero.getUnits();
    }

    public void placeUnits() {
        int startX = 8;
        int spacing = SIZE / computerUnits.size();

        for (int i = 0; i < computerUnits.size(); i++) {
            int y = spacing * (i + 1) - 1;
            renderer.setField(computerUnits.get(i), startX, y);
        }
        System.out.println("🔹 Компьютер разместил своих юнитов.");
    }

    public void makeMove() {
        for (Unit unit : computerUnits) {
            while (unit.getMoveDistance()>0 && unit.getX()>0) {
                attackIfPossible(unit);
                moveUnit(unit);
            }
            unit.startNewTurn();
        }
    }

    private void moveUnit(Unit unit) {
        int newX = unit.getX() - 1;
        if (newX >= 0 && renderer.getField(newX, unit.getY()) == null && unit.getMoveDistance()>0) {
            renderer.moveUnit(unit, newX, unit.getY());
            unit.setMoveDistance(unit.getMoveDistance()-1);
            System.out.println("🚶‍♂️ Юнит " + unit.getType() + " двинулся влево.");
        }
    }

    private boolean isTargetInRange(Unit attacker, int targetX, int targetY) {
        int dx = Math.abs(attacker.getX() - targetX);
        int dy = Math.abs(attacker.getY() - targetY);
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= attacker.getAttackRange();
    }

    private Unit getUnitAt(int x, int y) { return renderer.getField(x, y); }

    private void attackIfPossible(Unit unit) {
        int attackRange = unit.getAttackRange();
        for (int dx = -attackRange; dx <= attackRange; dx++) {
            for (int dy = -attackRange; dy <= attackRange; dy++) {
                if (dx == 0 && dy == 0) continue;

                int targetX = unit.getX() + dx;
                int targetY = unit.getY() + dy;

                if (targetX >= 0 && targetX < SIZE && targetY >= 0 && targetY < SIZE && getUnitAt(targetX, targetY) != null && isTargetInRange(unit, targetX, targetY)) {
                    Unit target = getUnitAt(targetX, targetY);
                    if(target.getOwner().getName() == "Игрок" && unit.getMoveDistance()>0){
                        attack = new pvpAttack(unit, playerHero, targetX, targetY, renderer);
                    }
                }
            }
        }
    }
}
