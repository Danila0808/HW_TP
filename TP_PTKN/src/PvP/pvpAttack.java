package PvP;

import Entities.Hero;
import Entities.Unit;
import Game.Console;
import Game.Game;

public class pvpAttack {
    private Unit attacker;
    private Hero targetHero;
    private int targetX, targetY;
    private pvpMapRender map;

    public pvpAttack(Unit attacker, Hero targetHero, int targetX, int targetY, pvpMapRender map) {
        this.attacker = attacker;
        this.targetHero = targetHero;
        this.targetX = targetX;
        this.targetY = targetY;
        this.map = map;

        execute();
    }

    public void execute() {
        if (attacker.getMoveDistance() == 0){
            Console.addEvent("❌ Нет очков хода!");
            return;
        }

        if (!isTargetInRange()) {
            Console.addEvent("❌ Цель вне зоны атаки!");
            return;
        }

        Unit target = getUnitAt(targetX, targetY);
        if (target == null) {
            Console.addEvent("❌ На указанных координатах нет юнита!");
            return;
        }

        if (target.getOwner().getName().equals(attacker.getOwner().getName())){
            Console.addEvent("❌ Friendly fire запрещен!");
            return;
        }

        int damage = attacker.getDamage();
        target.setXp(target.getXp() - damage);
        Console.addEvent("⚔ " + attacker.getType() + " атаковал " + target.getType() + " и нанес " + damage + " урона!");

        if (target.getXp() <= 0) {
            handleUnitDeath(target);
        }

        attacker.setMoveDistance(0);
    }

    private boolean isTargetInRange() {
        if(targetX<0 || targetX>= Game.MAP_SIZE || targetY<0 || targetY>=Game.MAP_SIZE)
            return false;

        int dx = Math.abs(attacker.getX() - targetX);
        int dy = Math.abs(attacker.getY() - targetY);
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance <= attacker.getAttackRange();
    }

    public Unit getUnitAt(int x, int y) { return map.getField(x, y); }

    private void handleUnitDeath(Unit target) {
        Console.addEvent("☠ " + target.getType() + " побежден!");

        map.addTomb(targetX, targetY);

        if (target.getOwner().getArmy().contains(target)) {
            target.getOwner().getArmy().remove(target);
            targetHero.getUnits().remove(target);
        }
    }
}
