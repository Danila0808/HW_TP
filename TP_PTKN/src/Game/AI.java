package Game;

import Entities.*;
import Map.Tile;

import static Entities.PerkType.*;

public class AI {
    private Hero hero;
    private Player player;
    private Castle castle;
    private Tile[][] map;

    public AI(Hero hero, Player player, Castle castle, Tile[][] map) {
        this.hero = hero;
        this.player = player;
        this.castle = castle;
        this.map = map;
    }

    public void makeMove() {
        buyBuildings();

        if(hero.getX()==map.length-1 && hero.getY()==map.length-1) {
            recruitUnits();
        }

        moveTowardsPlayerCastle();
    }

    private void buyBuildings() {
        if (player.getGold() >= 1000 && !castle.isBuilded(new GuardPost())) {
            castle.build(new GuardPost(), player);
        }
        if (player.getGold() >= 1500 && !castle.isBuilded(new Tower())) {
            castle.build(new Tower(), player);
        }
        if (player.getGold() >= 2000 && !castle.isBuilded(new Armory())) {
            castle.build(new Armory(), player);
        }
        if (player.getGold() >= 2500 && !castle.isBuilded(new FortPost())) {
            castle.build(new FortPost(), player);
        }
        if (player.getGold() >= 3000 && !castle.isBuilded(new Cathedral())) {
            castle.build(new Cathedral(), player);
        }
    }

    private void recruitUnits() {
        while (player.getArmy().size() < player.getMaxArmySize()) {
            int flag = 0;
            if(!hero.recruitUnit(PALADIN, hero.getX(), hero.getY()))
                flag+=1;
            if(!hero.recruitUnit(CAVALRY, hero.getX(), hero.getY()))
                flag+=1;
            if(!hero.recruitUnit(SWORDSMAN, hero.getX(), hero.getY()))
                flag+=1;
            if(!hero.recruitUnit(CROSSBOMEN, hero.getX(), hero.getY()))
                flag+=1;
            if(!hero.recruitUnit(SPEARMAN, hero.getX(), hero.getY()))
                flag+=1;
            if(flag==5)
                break;
        }
    }

    private void moveTowardsPlayerCastle() {
        if(hero.getX()==0 && hero.getY()==0)
            return;

        Tile target = map[hero.getY()-1][hero.getX()-1];
        int pentalty = target.getMovementPenalty(target.getType());
        while(hero.getMoveDistance()>pentalty && !(hero.getX()==0 && hero.getY()==0)){
            Console.PrintEvents();
            hero.moveHero(map, -1, -1);
            pentalty = target.getMovementPenalty(target.getType());
        }
    }

}
