package Entities;

import Game.Console;
import Game.Player;

import java.util.ArrayList;
import java.util.List;

public class Castle {
    private String owner;
    private List<Building> buildings = new ArrayList<>();

    public Castle(String owner) {
        this.owner = owner;
    }

    public void setOwner(String owner) { this.owner = owner; }

    public boolean isBuilded(Building building) {
        for(Building b : buildings){
            if(b.getClass() == building.getClass())
                return true;
        }
        return false;
    }

    public void build(Building building, Player player) {
        if(isBuilded(building)){
            Console.addEvent(building.getName() + " уже есть!");
            return;
        }
        if (player.spendGold(building.getCost())) {
            buildings.add(building);
            building.applyEffect(player);
        } else {
            Console.addEvent("Не хватает золота на " + building.getName());
        }
    }

    public List<Building> getBuildings() { return buildings; }
}