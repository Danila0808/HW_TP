import Entities.*;
import Game.*;
import Images.Icons;
import Map.TerrainTile;
import Map.TerrainType;
import Map.Tile;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class HandlerTest {
    private static Hero hero;
    private static Player player;
    private static Castle castle;
    private static Tile[][] map;
    private static CommandHandler commandHandler;
    private static ByteArrayInputStream inputStream;
    private static ByteArrayOutputStream outputStream;
    private static Scanner scanner;
    private static int prev, prevX, prevY;

    void setInput(String input){
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        scanner = new Scanner(inputStream);
        commandHandler = new CommandHandler(hero, player, castle, scanner, map);
    }

    String getOutput(){
        return outputStream.toString().trim();
    }

    String getLine(){
        String[] arr = getOutput().split("\n");
        return arr[arr.length - 1];
    }

    String getLine(int ind){
        String[] arr = getOutput().split("\n");
        return arr[arr.length - 1 - ind];
    }

    @BeforeEach
    void setUp() {
        player = new Player("Игрок", 10000, 0);
        hero = new Hero(4, 4, 200, 110, 5, 1, new Tile[10][10], player, new ArrayList<>());
        player.addHero(hero);
        castle = new Castle("Замок");
        map = new Tile[10][10];
        for(int y=0;y<10;y++)
            for(int x=0;x<10;x++)
                map[y][x] = new TerrainTile(x, y, TerrainType.GRASS);

        String initInput = "initial text";
        inputStream = new ByteArrayInputStream(initInput.getBytes());
        scanner = new Scanner(inputStream);

        commandHandler = new CommandHandler(hero, player, castle, scanner, map);

        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Console.clean();
    }

    @AfterAll
    static void returnInitState(){
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Nested
    class testHandleCommandMove{
        @AfterEach
        void newTurn(){
            hero.startNewTurn();
        }
        @BeforeEach
        void initPrev(){
            prevX = hero.getX();
            prevY = hero.getY();
        }

        @Test
        void testW(){
            commandHandler.handleCommand("W");
            assertEquals(prevY-1, hero.getY());
        }
        @Test
        void testA(){
            commandHandler.handleCommand("A");
            assertEquals(prevX-1, hero.getX());
        }
        @Test
        void testS(){
            commandHandler.handleCommand("S");
            assertEquals(prevY+1, hero.getY());
        }
        @Test
        void testD(){
            commandHandler.handleCommand("D");
            assertEquals(prevX+1, hero.getX());
        }
        @Test
        void testWA(){
            commandHandler.handleCommand("WA");
            assertEquals(prevX-1, hero.getX());
            assertEquals(prevY-1, hero.getY());
        }
        @Test
        void testWD(){
            commandHandler.handleCommand("WD");
            assertEquals(prevX+1, hero.getX());
            assertEquals(prevY-1, hero.getY());
        }
        @Test
        void testSA(){
            commandHandler.handleCommand("SA");
            assertEquals(prevX-1, hero.getX());
            assertEquals(prevY+1, hero.getY());
        }
        @Test
        void testSD(){
            commandHandler.handleCommand("SD");
            assertEquals(prevX+1, hero.getX());
            assertEquals(prevY+1, hero.getY());
        }
    }

    @Nested
    class testHandleCommandBuyBuilding{
        @Test
        void testFailNotInCastle(){
            commandHandler.handleCommand("B");
            Console.PrintEvents();
            assertEquals("1) Герой не находится в крепости!", getOutput());
        }
        @Test
        void testFail(){
            hero.moveHero(map, -4, -4);
            setInput("0\n");
            Console.clean();
            commandHandler.handleCommand("B");
            Console.PrintEvents();
            assertEquals("\uD83D\uDD39 Введите номер здания: 1) ❌ Неверный выбор здания.", getLine());
        }
        @Test
        void testOK(){
            hero.moveHero(map, -4, -4);
            setInput("1\n");
            int prev_money = player.getGold();
            commandHandler.handleCommand("B");
            GuardPost building = new GuardPost();

            assertTrue(player.getAvailableUnits().contains(PerkType.SPEARMAN));
            assertEquals(prev_money-building.getCost(), player.getGold());
            assertInstanceOf(GuardPost.class, castle.getBuildings().getLast());
        }
    }

    @Nested
    class testHandleCommandRecruitUnit{
        @Test
        void testFailNotInCastle(){
            commandHandler.handleCommand("T");
            Console.PrintEvents();
            assertEquals("1) Герой не находится в крепости!", getOutput());
        }
        @Test
        void testFail(){
            hero.moveHero(map, -4, -4);
            setInput("ABOBA\n");
            commandHandler.handleCommand("T");
            Console.PrintEvents();
            assertEquals("2) ❌ Неверный тип юнита!", getLine());
        }
        @Test
        void testOK_unit(){
            hero.moveHero(map, -4, -4);
            castle.build(new GuardPost(), player);
            setInput("SPEARMAN\n");
            int prev_money = player.getGold();
            commandHandler.handleCommand("T");

            assertSame(player.getArmy().getLast().getType(), PerkType.SPEARMAN);
            assertEquals(prev_money-500, player.getGold());
            assertSame(hero.getUnits().getLast().getType(), PerkType.SPEARMAN);
        }
        @Test
        void testOK_hero(){
            hero.moveHero(map, -4, -4);
            player.addGold(10000);
            castle.build(new Altar(), player);
            setInput("HERO\n");
            int prev_money = player.getGold();
            int prevLeaders = player.getLeaders().size();
            commandHandler.handleCommand("T");

            assertEquals(prevLeaders+1, player.getLeaders().size());
            assertEquals(prev_money-hero.getCost(), player.getGold());
        }
    }

    @Test
    void testHandleCommandEndTurn() {
        int prevPrevMove = hero.getMoveDistance();
        hero.moveHero(map, 0, -1);
        commandHandler.handleCommand("E");
        assertEquals(2, Game.turn);
        assertEquals(prevPrevMove, hero.getMoveDistance());
    }

    @Nested
    class testHandleCommandShowArmy{
        @Test
        void testFail(){
            commandHandler.handleCommand("ARMY");
            Console.PrintEvents();
            assertEquals("1) ❌ У вас нет армии!", getOutput());
        }
        @Test
        void testOK(){
            castle.build(new GuardPost(), player);
            hero.recruitUnit(PerkType.SPEARMAN, 0, 0);
            hero.recruitUnit(PerkType.SPEARMAN, 0, 0);
            hero.recruitUnit(PerkType.SPEARMAN, 0, 0);
            commandHandler.handleCommand("ARMY");

            Console.PrintEvents();

            assertEquals("4) SPEARMAN XP: 100\r", getLine(2));
            assertEquals("5) SPEARMAN XP: 100\r", getLine(1));
            assertEquals("6) SPEARMAN XP: 100", getLine());
        }
    }

    @Test
    void testHandleCommandShowBalance() {
        commandHandler.handleCommand("BALANCE");
        Console.PrintEvents();
        assertEquals("1) На данный момент у вас: "+player.getGold(), getOutput());
    }

    @Nested
    class testHandleCommandChangeIcon{
        @Test
        void testOK(){
            setInput("3\n");
            commandHandler.handleCommand("ICON");
            assertEquals(Icons.HERO_MALE2, hero.getIcon());
        }
        @Test
        void testFail(){
            setInput("8\n");
            commandHandler.handleCommand("ICON");
            Console.PrintEvents();
            assertEquals("1) ❌ Неверный выбор иконки.", getLine());
        }
    }

    @Test
    void testHandleCommandUnknown() {
        commandHandler.handleCommand("UNKNOWN");
        Console.PrintEvents();
        assertEquals("1) ❌ Неизвестная команда: " + "UNKNOWN", getOutput());
    }
}
//w a s d wa wd sa sd (main) +
//B +
//T +
//E +
//Icon +
//army +
//balance +