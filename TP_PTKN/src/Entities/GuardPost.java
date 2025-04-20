package Entities;

import Game.Console;
import Game.Player;

import static Entities.PerkType.SPEARMAN;

public class GuardPost extends Building {
    public GuardPost() {
        super("Сторожевой пост", 1000, BuildingEffectType.UNIT_TRAINING);
    }

    @Override
    public void applyEffect(Player player){
        player.addUnitType(SPEARMAN);
        Console.addEvent("Потрачено " + cost + ". Осталось " + player.getGold());
        Console.addEvent("Здание сторожевого поста построено. Теперь доступны для найма копейщики!");
    }
}
