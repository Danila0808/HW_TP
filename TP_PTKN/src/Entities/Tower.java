package Entities;

import Game.Console;
import Game.Player;

import static Entities.PerkType.CROSSBOMEN;

public class Tower extends Building {
    public Tower() {
        super("Башня арбалетчиков", 1500, BuildingEffectType.UNIT_TRAINING);
    }

    @Override
    public void applyEffect(Player player){
        player.addUnitType(CROSSBOMEN);
        Console.addEvent("Здание башни арбалетчиков построено. Теперь доступны для найма арбалетчики!");
    }
}
