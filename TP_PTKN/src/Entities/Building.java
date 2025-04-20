package Entities;

import Game.Console;
import Game.Player;

import static Entities.PerkType.*;

enum BuildingEffectType {
    UNIT_TRAINING,
    RESOURCE_BOOST,
    DEFENSE_BONUS,
    HERO_SPAWNER
}

abstract public class Building {
    protected String name;
    protected int cost;
    protected BuildingEffectType effectType;

    public Building(String name, int cost, BuildingEffectType effectType){
        this.name = name;
        this.cost = cost;
        this.effectType = effectType;
    }

    public String getName() { return name; }
    public int getCost() { return cost; }
    public BuildingEffectType getEffectType() { return effectType; }

    public abstract void applyEffect(Player player);
}


//Добавить Алтарь(возрождение героя), рынок(кони, оружие, доспехи)
//Добавить Гос.Предприятие (Увеличивает income)
//Добавить Военная база (увеличивает кол-во макс юнитов)