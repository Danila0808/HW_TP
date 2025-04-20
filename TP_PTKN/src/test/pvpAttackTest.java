import Entities.Hero;
import Entities.PerkType;
import Entities.Unit;
import Game.Console;
import Game.Player;
import Map.Tile;
import PvP.pvpAttack;
import PvP.pvpMapRender;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class pvpAttackTest {
    private static Unit attacker;
    private static Hero targetHero;
    private static pvpMapRender renderer;
    private static ByteArrayOutputStream outputStream;

    private Hero hero;
    private Hero computerHero;
    private Player player, computer;
    private static List<Unit> playerArmy, computerArmy;

    String getOutput() {
        return outputStream.toString().trim();
    }

    String getLine() {
        String[] arr = getOutput().split("\n");
        return arr[arr.length - 1];
    }

    String getLine(int ind) {
        String[] arr = getOutput().split("\n");
        return arr[arr.length - 1 - ind];
    }

    @BeforeEach
    void setUp() {
        player = new Player("Игрок", 10000, 0);
        computer = new Player("Компьютер", 10000, 0);
        Tile[][] map = new Tile[10][10];

        playerArmy = new ArrayList<>();
        playerArmy.add(new Unit(5, 1, 80, 80, 4, 5, map, player, PerkType.CROSSBOMEN, 1000));
        playerArmy.add(new Unit(1, 3, 80, 80, 4, 5, map, player, PerkType.CROSSBOMEN, 1000));

        computerArmy = new ArrayList<>();
        computerArmy.add(new Unit(8, 1, 80, 80, 4, 5, map, computer, PerkType.CROSSBOMEN, 1000));
        computerArmy.add(new Unit(8, 3, 80, 80, 4, 5, map, computer, PerkType.CROSSBOMEN, 1000));

        hero = new Hero(4, 4, 200, 110, 5, 1, map, player, playerArmy);
        computerHero = new Hero(4, 4, 200, 110, 5, 1, map, computer, computerArmy);
        renderer = new pvpMapRender(hero, computerHero);
        renderer.placeUnits();

        attacker = playerArmy.getFirst();
        targetHero = computerHero;

        player.getArmy().addAll(playerArmy);
        computer.getArmy().addAll(computerArmy);

        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @AfterAll
    static void returnInitState() {
        System.setOut(System.out);
    }

    @Test
    void testOK() {
        Unit target = renderer.getField(8, 1);
        assertNotNull(target);
        int prevXP = target.getXp();

        new pvpAttack(attacker, targetHero, 8, 1, renderer);
        assertEquals(prevXP - attacker.getDamage(), target.getXp());

        Console.PrintEvents();
        assertTrue(getOutput().contains("атаковал"));
        assertEquals(0, attacker.getMoveDistance());
    }

    @Test
    void testOK_Death() {
        Unit target = renderer.getField(8, 1);
        assertNotNull(target);

        int prevArmyHero = computerHero.getUnits().size();
        int prevArmyPlayer = computer.getArmy().size();

        new pvpAttack(attacker, targetHero, 8, 1, renderer);
        assertTrue(renderer.isTomb(8, 1));
        assertEquals(prevArmyHero-1, computerHero.getUnits().size());
        assertEquals(prevArmyPlayer-1, computer.getArmy().size());

        Console.PrintEvents();
        assertTrue(getOutput().contains("побежден"));
    }

    @Test
    void testOutOfRange() {
        new pvpAttack(attacker, targetHero, 9, 9, renderer);

        Console.PrintEvents();
        assertEquals("1) ❌ Цель вне зоны атаки!", getLine());
    }

    @Test
    void testOutOfMap() {
        new pvpAttack(attacker, targetHero, 5, -1, renderer);

        Console.PrintEvents();
        assertEquals("1) ❌ Цель вне зоны атаки!", getLine());
    }

    @Test
    void testNoUnitAtTarget() {
        new pvpAttack(attacker, targetHero, 5, 5, renderer);

        Console.PrintEvents();
        assertEquals("1) ❌ На указанных координатах нет юнита!", getLine());
    }

    @Test
    void testFriendlyFire() {
        new pvpAttack(attacker, targetHero, 1, 3, renderer);

        Console.PrintEvents();
        assertEquals("1) ❌ Friendly fire запрещен!", getLine());
    }

    @Test
    void testTired() {
        attacker.setMoveDistance(0);
        new pvpAttack(attacker, targetHero, 1, 3, renderer);

        Console.PrintEvents();
        assertEquals("1) ❌ Нет очков хода!", getLine());
    }
}
//Смерть +
//Friendly Fire +
//Дальность атаки +
//Урон +
//Усталость +
//Вне карты +
