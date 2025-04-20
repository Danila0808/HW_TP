package Entities;

import Game.Console;
import Game.Player;

import static Entities.PerkType.HERO;
import static Entities.PerkType.PALADIN;

public class Altar extends Building {
    public Altar() {
        super("Aлтарь", 5000, BuildingEffectType.HERO_SPAWNER);
    }

    @Override
    public void applyEffect(Player player) {
        //Логика
        player.addUnitType(HERO);
        Console.addEvent("Алтарь построен. Теперь доступны для найма Герои!");
    }
}
