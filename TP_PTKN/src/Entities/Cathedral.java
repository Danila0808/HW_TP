package Entities;

import Game.Console;
import Game.Player;

import static Entities.PerkType.PALADIN;

public class Cathedral extends Building {
    public Cathedral() {
        super("Собор", 3000, BuildingEffectType.UNIT_TRAINING);
    }

    @Override
    public void applyEffect(Player player) {
        player.addUnitType(PALADIN);
        Console.addEvent("Собор построен. Теперь доступны для найма паладины!");
    }
}
