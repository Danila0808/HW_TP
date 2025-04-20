package Entities;

import Game.Console;
import Game.Player;

import static Entities.PerkType.CAVALRY;

public class FortPost extends Building {
    public FortPost() {
        super("Фортпост", 2500, BuildingEffectType.UNIT_TRAINING);
    }

    @Override
    public void applyEffect(Player player) {
        player.addUnitType(CAVALRY);
        Console.addEvent("Фортпост построен. Теперь доступны для найма кавалеристы!");
    }
}
