import Entities.Hero;
import Entities.PerkType;
import Entities.Unit;
import Game.Console;

import Game.Player;
import Map.Tile;
import PvP.pvpHandler;
import PvP.pvpMapRender;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import static org.junit.jupiter.api.Assertions.*;

public class PVPTest {
    private Hero hero;
    private Hero computerHero;
    private Player player;
    private Scanner scanner;
    private pvpMapRender renderer;
    private static pvpHandler handler;
    private static ByteArrayInputStream inputStream;
    private static ByteArrayOutputStream outputStream;
    private static int prevX, prevY;
    private static List<Unit> playerArmy, computerArmy;
    private static Unit selected;

    void setInput(String input){
        inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
        scanner = new Scanner(inputStream);
        handler = new pvpHandler(hero, computerHero, player, scanner, renderer);
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
    void init(){
        player = new Player("Игрок", 10000, 0);
        Player computer = new Player("Компьютер", 10000, 0);
        Tile[][] map = new Tile[10][10];

        playerArmy = new ArrayList<>();
        playerArmy.add(new Unit(1, 1, 80, 80, 4, 5, map, player, PerkType.CROSSBOMEN, 1000));
        playerArmy.add(new Unit(1, 3, 80, 80, 4, 5, map, player, PerkType.CROSSBOMEN, 1000));

        computerArmy = new ArrayList<>();
        computerArmy.add(new Unit(8, 1, 80, 80, 4, 5, map, computer, PerkType.CROSSBOMEN, 1000));
        computerArmy.add(new Unit(8, 3, 80, 80, 4, 5, map, computer, PerkType.CROSSBOMEN, 1000));

        hero = new Hero(4, 4, 200, 110, 5, 1, map, player, playerArmy);
        computerHero = new Hero(4, 4, 200, 110, 5, 1, map, computer, computerArmy);
        renderer = new pvpMapRender(hero, computerHero);

        String initInput = "initial text";
        inputStream = new ByteArrayInputStream(initInput.getBytes());
        scanner = new Scanner(inputStream);

        handler = new pvpHandler(hero, computerHero, player, scanner, renderer);

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
    class testSelectUnit{
        @Test
        void testOK(){
            setInput("1\n");
            handler.handleCommand("SELECT");
            Console.PrintEvents();
            assertEquals("\uD83D\uDD39 Введите номер юнита: \uD83D\uDD39 Выбран юнит: CROSSBOMEN", getLine());
        }
        @Test
        void testFail(){
            setInput("0\n");
            handler.handleCommand("SELECT");
            Console.PrintEvents();
            assertEquals("\uD83D\uDD39 Введите номер юнита: ❌ Неверный выбор!", getLine());
        }
    }

    @Test
    void testFail(){
        handler.handleCommand("W");
        Console.PrintEvents();
        assertEquals("1) ❌ Сначала выберите юнита!", getOutput());
    }

    @Nested
    class testHandleCommandMove{
        @BeforeEach
        void select(){
            setInput("1\n");
            handler.handleCommand("SELECT");
            Console.clean();
            selected = playerArmy.getFirst();

            prevX = selected.getX();
            prevY = selected.getY();
        }
        @AfterEach
        void newTurn(){
            selected.startNewTurn();
        }

        @Test
        void testW(){
            handler.handleCommand("W");
            assertEquals(prevY-1, selected.getY());
        }
        @Test
        void testA(){
            handler.handleCommand("A");
            assertEquals(prevX-1, selected.getX());
        }
        @Test
        void testS(){
            handler.handleCommand("S");
            assertEquals(prevY+1, selected.getY());
        }
        @Test
        void testD(){
            handler.handleCommand("D");
            assertEquals(prevX+1, selected.getX());
        }
        @Test
        void testWA(){
            handler.handleCommand("WA");
            assertEquals(prevX-1, selected.getX());
            assertEquals(prevY-1, selected.getY());
        }
        @Test
        void testWD(){
            handler.handleCommand("WD");
            assertEquals(prevX+1, selected.getX());
            assertEquals(prevY-1, selected.getY());
        }
        @Test
        void testSA(){
            handler.handleCommand("SA");
            assertEquals(prevX-1, selected.getX());
            assertEquals(prevY+1, selected.getY());
        }
        @Test
        void testSD(){
            handler.handleCommand("SD");
            assertEquals(prevX+1, selected.getX());
            assertEquals(prevY+1, selected.getY());
        }
        @Test
        void testOutOfWorld(){
            handler.handleCommand("A");
            Console.clean();
            handler.handleCommand("A");
            Console.PrintEvents();
            assertEquals("1) ❌ Выход за пределы карты!", getLine());
        }
        @Test
        void testTired(){
            for(int i=0;i<6;i++)
                handler.handleCommand("D");
            Console.PrintEvents();
            assertEquals("6) ❌ У данного юнита нет очков хода!", getLine());
        }
    }

    @Test
    void testShowArmy(){
        handler.handleCommand("ARMY");
        Console.PrintEvents();
        assertEquals("1) \uD83D\uDD39 Ваша армия:\r", getLine(2));
        assertEquals("2) - CROSSBOMEN (X: 1, Y: 1) XP: 80\r", getLine(1));
        assertEquals("3) - CROSSBOMEN (X: 1, Y: 3) XP: 80", getLine(0));
    }

    @Test
    void testEndTurn(){
        List<Integer> points = new ArrayList<>();

        for(int i=1;i<=playerArmy.size();i++){
            points.add(playerArmy.get(i-1).getMoveDistance());
            setInput(i+"\n");
            handler.handleCommand("SELECT");
            handler.handleCommand("D");
        }

        handler.endTurn();

        for(int i=1;i<=playerArmy.size();i++)
            assertEquals(points.get(i-1), playerArmy.get(i-1).getMoveDistance());
    }

    @Test
    void testPlacePlayerUnits(){
        setInput("0\n0\n1\n1\n");
        handler.placePlayerUnits();
        assertEquals(playerArmy.getFirst().getX(), 0);
        assertEquals(playerArmy.getFirst().getY(), 0);
        assertEquals(playerArmy.getLast().getX(), 1);
        assertEquals(playerArmy.getLast().getY(), 1);
    }

    @Test
    void testHandleAttack(){
        setInput("1\n8\n1\n");
        handler.handleCommand("SELECT");
        for(int i=0;i<3;i++)
            handler.handleCommand("D");

        renderer.setField(computerArmy.getFirst(), 8, 1);
        renderer.placeUnits();

        handler.handleCommand("ATTACK");
        Console.PrintEvents();
        assertEquals("4) ⚔ CROSSBOMEN атаковал CROSSBOMEN и нанес 80 урона!\r", getLine(1));
        assertEquals("5) ☠ CROSSBOMEN побежден!", getLine());
    }
}

//Границы карты +
//Устал +

//w a s d wa wd sa sd (pvp) +
//END +
//Attack +
//army +
//select +
//place +