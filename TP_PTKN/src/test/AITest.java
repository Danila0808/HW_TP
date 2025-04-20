import Entities.*;
import Game.AI;
import Game.Player;
import Map.CastleTile;
import Map.TerrainTile;
import Map.TerrainType;
import Map.Tile;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AITest {
    private Hero hero;
    private Player player;
    private Castle castle;
    private Tile[][] map;
    private AI ai;

    @BeforeEach
    void init() {
        // Инициализация игровых объектов
        player = new Player("Компьютер", 10000, 0);
        hero = new Hero(5, 5, 200, 110, 5, 1, new Tile[10][10], player, new ArrayList<>());
        castle = new Castle("Noname");
        map = new Tile[10][10];
        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                map[y][x] = new TerrainTile(x, y, TerrainType.GRASS);
            }
        }

        ai = new AI(hero, player, castle, map);
    }

    @AfterAll
    static void returnInitState() {
        System.setOut(System.out);
    }

    @Test
    void testBuyBuildings() {
        player.addGold(10000);

        ai.makeMove();

        assertTrue(castle.isBuilded(new GuardPost()));
        assertTrue(castle.isBuilded(new Tower()));
        assertTrue(castle.isBuilded(new Armory()));
        assertTrue(castle.isBuilded(new FortPost()));
        assertTrue(castle.isBuilded(new Cathedral()));
    }

    @Test
    void testRecruitUnits() {
        hero.setX(map.length - 1);
        hero.setY(map.length - 1);

        player.addGold(10000);

        ai.makeMove();

        assertFalse(player.getArmy().isEmpty());
    }

    @Test
    void testMoveTowardsPlayerCastle() {
        hero.setX(5);
        hero.setY(5);

        hero.setMoveDistance(10);

        ai.makeMove();

        assertTrue(hero.getX() < 5 && hero.getY() < 5);
    }

    @Test
    void testCaptureCastle() {
        map[0][0] = new CastleTile(0, 0, "NoName", new Player("Игрок", 10000, 1000));
        hero.setX(1);
        hero.setY(1);

        ai.makeMove();

        assertEquals(0, player.getGoldIncome());
    }
}
//Покупка +
//Найм +
//Движение +
//Захват Замка +
