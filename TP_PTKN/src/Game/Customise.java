package Game;

import Entities.Hero;
import Images.Icons;

import java.util.HashMap;
import java.util.Map;

public class Customise {
    public static Map<Integer, Icons> icons = new HashMap<>();

    static {
        icons.put(1, Icons.WIZARD);
        icons.put(2, Icons.HERO_MALE1);
        icons.put(3, Icons.HERO_MALE2);
        icons.put(4, Icons.WILLIAN1);
        icons.put(5, Icons.WILLIAN2);
        icons.put(6, Icons.HERO_FEMALE1);
        icons.put(7, Icons.HERO_FEMALE2);
    }

    public static String getCode(Icons icon){
        return switch (icon) {
            case Icons.WIZARD -> "\uD83E\uDDD9\uD83C\uDFFB\u200D♂\uFE0F";
            case Icons.HERO_MALE1 -> "\uD83E\uDDB8\u200D";
            case Icons.HERO_MALE2 -> "\uD83E\uDDB8\uD83C\uDFFB\u200D♂\uFE0F";
            case Icons.WILLIAN1 -> "\uD83E\uDDB9\uD83C\uDFFB\u200D♂\uFE0F";
            case Icons.WILLIAN2 -> "\uD83E\uDDDB";
            case Icons.HERO_FEMALE1 -> "\uD83E\uDDB8\uD83C\uDFFC\u200D♀\uFE0F";
            case Icons.HERO_FEMALE2 -> "\uD83E\uDDB8";
        };
    }

    public static void changeIcon(Hero hero, int id){
        hero.setIcon( icons.get(id) );
    }
}
