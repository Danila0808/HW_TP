import Entities.Hero;
import Game.Console;
import Game.Player;
import Map.TerrainTile;
import Map.TerrainType;
import Map.Tile;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MovementTest {
    private static Hero hero;
    private static Tile[][] testMap;
    private static Player player1;
    private static ByteArrayOutputStream outputStream;

    @BeforeEach
    void init(){
        player1 = new Player("1", 0, 0);
        testMap = new Tile[10][10];
        for(int y=0;y<10;y++){
            for(int x=0;x<10;x++){
                testMap[y][x] = new TerrainTile(x, y, TerrainType.GRASS);
            }
        }

        hero = new Hero(0, 0, 200, 110, 5, 1, testMap, player1, new ArrayList<>());

        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Console.clean();
    }

    @Test
    void test_OK_move(){
        hero.moveHero(testMap, 1, 0);

        assertEquals(1, hero.getX());
        assertEquals(0, hero.getY());
    }

    @Test
    void test_FAIL_move(){
        testMap[0][1] = new Tile(1, 0, TerrainType.MOUNTAIN);
        hero.moveHero(testMap, 1, 0);

        Console.PrintEvents();

        String consoleOutput = outputStream.toString().trim();
        assertEquals("1) 🚧 Нельзя пройти на MOUNTAIN!", consoleOutput);
    }

    @Test
    void test_FAIL_move_beyond_map(){
        hero.moveHero(testMap, 0, -1);

        Console.PrintEvents();

        String consoleOutput = outputStream.toString().trim();
        assertEquals("1) ❌ Нельзя выйти за границы карты!", consoleOutput);
    }

    @Test
    void test_FAIL_move_penalty(){
        hero.moveHero(testMap, 0, 1);
        Console.clean();
        hero.moveHero(testMap, 0, 1);

        Console.PrintEvents();

        String consoleOutput = outputStream.toString().trim();
        assertEquals("1) ⚠ Недостаточно очков движения! Нужно: 3", consoleOutput);
    }

    @AfterAll
    static void returnInitState(){
        System.setOut(System.out);
    }
}
//Движение ОК +
//За границы +
//Усталость +
//Непреодолимая преграда +

