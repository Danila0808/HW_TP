package Entities;

import Game.Console;
import Game.Player;

import static Entities.PerkType.SWORDSMAN;

public class Armory extends Building {
    public Armory() {
        super("Оружейная", 2000, BuildingEffectType.UNIT_TRAINING);
    }

    @Override
    public void applyEffect(Player player) {
        player.addUnitType(SWORDSMAN);
        Console.addEvent("Оружейная построена. Теперь доступны для найма мечники!");
    }
}
