import Entities.Hero;
import Entities.Lighthouse;
import Game.Console;

import Game.Player;
import Map.*;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

public class ActionTest {
    private static ByteArrayOutputStream outputStream;
    private static Hero hero;
    private static Tile[][] testMap;
    private static Player player1;
    private static MapRenderer renderer;

    @BeforeEach
    void init(){
        player1 = new Player("Игрок", 0, 0);
        testMap = new Tile[10][10];
        for(int y=0;y<10;y++){
            for(int x=0;x<10;x++){
                testMap[y][x] = new TerrainTile(x, y, TerrainType.GRASS);
            }
        }

        hero = new Hero(0, 0, 200, 110, 5, 1, testMap, player1, new ArrayList<>());
        List<Hero> heroes = new ArrayList<>();
        heroes.add(hero);
        renderer = new MapRenderer(testMap, heroes);

        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Console.clean();
    }

    @Test
    void test_collect_treasure(){
        int prev_balance = player1.getGold();
        testMap[0][1] = new TreasureTile(1, 0, 100);
        hero.moveHero(testMap, 1, 0);
        Console.clean(-1);
        Console.PrintEvents();

        String consoleOutput = outputStream.toString().trim();

        assertEquals("1) Сундук на [1,0] собран! Получено: 100", consoleOutput);
        assertEquals(prev_balance+100, player1.getGold());
        assertTrue(((TreasureTile)testMap[0][1]).isCollected());
    }

    @Test
    void test_clearing_fog(){
        renderer.render();
        Console.clean();

        for(int y=0;y<10;y++){
            for(int x=0;x<10;x++){

                if(y - hero.getY() <= 2 && x - hero.getX() <= 2)
                    assertTrue(testMap[y][x].isLighted());
                else
                    assertFalse(testMap[y][x].isLighted());
            }
        }
    }

    @Test
    void test_light_work(){
        Lighthouse lighthouse = new Lighthouse(0, 1, renderer);
        testMap[1][0] = new LightTile(0, 1, TerrainType.GRASS, lighthouse);

        hero.moveHero(testMap, 0, 1);
        for(int y=0;y<10;y++)
            for(int x=0;x<10;x++)
                assertTrue(testMap[y][x].isLighted());
    }

    @Test
    void test_light_unwork(){
        Lighthouse lighthouse = new Lighthouse(0, 1, renderer);
        testMap[1][0] = new LightTile(0, 1, TerrainType.GRASS, lighthouse);

        renderer.render();
        hero.moveHero(testMap, 0, 1);
        renderer.render();
        lighthouse.work(false);
        renderer.render();


        for(int y=0;y<10;y++){
            for(int x=0;x<10;x++){

                if(y - hero.getY() <= 2 && x - hero.getX() <= 2)
                    assertTrue(testMap[y][x].isLighted());
                else
                    assertFalse(testMap[y][x].isLighted());
            }
        }
    }

    @AfterAll
    static void returnInitState(){
        System.setOut(System.out);
    }
}
//Подбор денег (увеличение баланса, пропадание сокровища) +
//Тушение маяка (Вся карта навеяна туманом) +
//Зажжение маяка (Вся карта без тумана) +
//Исчезновение тумана +