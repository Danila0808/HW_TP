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


import static Map.MapGenerator.TREASURE_COUNT;
import static org.junit.jupiter.api.Assertions.*;

public class MapTest {
    private static ByteArrayOutputStream outputStream;
    private static Tile[][] testMap;
    private static Player player1, player2;


    @BeforeEach
    void init(){
        player1 = new Player("Игрок", 0, 0);
        player2 = new Player("Компьютер", 0, 0);
        testMap = new MapGenerator(player1, player2, 10).getMap();

        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Console.clean();
    }

    @Nested
    class Test_map_generator{
        @Test
        void test_2_castles(){
            int res = 0;

            for(int y=0;y<10;y++)
                for(int x=0;x<10;x++)
                    if(testMap[y][x] instanceof CastleTile)
                        res++;

            assertEquals(2, res);
        }

        @Test
        void test_road(){
            for(int y=0;y<10;y++)
                for(int x=0;x<10;x++)
                    if(y==x && y!=0 && y!=9 && !(testMap[y][x] instanceof TreasureTile))
                        assertSame(testMap[y][x].getType(), TerrainType.ROAD, x+" "+y);
                    else
                        assertNotSame(testMap[y][x].getType(), TerrainType.ROAD);
        }

        @Test
        void test_treasure(){
            int res = 0;

            for(int y=0;y<10;y++)
                for(int x=0;x<10;x++)
                    if(testMap[y][x] instanceof TreasureTile)
                        res++;

            assertEquals(TREASURE_COUNT, res);
        }

        @Test
        void test_all_grounds(){
            int res = 0;
            List<TerrainType> grounds = new ArrayList<>();

            for(int y=0;y<10;y++)
                for(int x=0;x<10;x++)
                    if(!grounds.contains(testMap[y][x].getType()))
                        grounds.add(testMap[y][x].getType());

            assertEquals(TerrainType.values().length, grounds.size());
        }

        @Test
        void test_lighthouse(){
            int res = 0;

            for(int y=0;y<10;y++)
                for(int x=0;x<10;x++)
                    if(testMap[y][x] instanceof LightTile)
                        res++;

            assertEquals(1, res);
        }

        @RepeatedTest(100)
        void test_generator(){
            testMap = new MapGenerator(player1, player2, 10).getMap();
            assertNotNull(testMap);
        }
    }

    @Test
    void test_render(){
        Tile[][] map = new Tile[3][3];
        map[0][0] = new CastleTile(0, 0, "Noname", player1);
        map[0][1] = new TerrainTile(1, 0, TerrainType.GRASS);
        map[0][2] = new LightTile(2, 0, TerrainType.GRASS, null);
        map[1][0] = new TreasureTile(0, 1, 100);
        map[1][1] = new TerrainTile(1, 1, TerrainType.ROAD);
        map[1][2] = new TerrainTile(2, 1, TerrainType.MOUNTAIN);
        map[2][0] = new TerrainTile(0, 2, TerrainType.FOREST);
        map[2][1] = new TerrainTile(1, 2, TerrainType.WATER);
        map[2][2] = new TerrainTile(2, 2, TerrainType.GRASS);

        Hero hero1 = new Hero(0, 0, 200, 110, 5, 1, map, player1, new ArrayList<>());
        Hero hero2 = new Hero(2, 2, 200, 110, 5, 1, map, player2, new ArrayList<>());
        List<Hero> heroes = new ArrayList<>();
        heroes.add(hero1);
        heroes.add(hero2);
        MapRenderer renderer = new MapRenderer(map, heroes);
        renderer.render();

        String consoleOutput = outputStream.toString().trim();
        assertEquals("\uD83C\uDFF0 \uD83C\uDF3F \uD83D\uDC88 \r\n" +
                        "\uD83D\uDCB0 \uD83D\uDEE4\uFE0F ⛰\uFE0F \r\n" +
                        "\uD83C\uDF32 \uD83C\uDF0A \uD83E\uDDD9\uD83C\uDFFB\u200D♂\uFE0F"
                , consoleOutput);
    }

    @AfterAll
    static void returnInitState(){
        System.setOut(System.out);
    }
}
//[Map Generator]
//2 замка +
//Дорога между ними +
//Сокровища +
//Есть все типы местности +
//Маяк +
//Нет ошибок при генерации 100 раз +

//Cоответствие карте +