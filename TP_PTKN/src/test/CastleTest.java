import Entities.*;
import Game.CommandHandler;
import Game.Console;

import Game.Player;
import Map.CastleTile;
import Map.TerrainTile;
import Map.TerrainType;
import Map.Tile;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

public class CastleTest {
    private static Hero hero, hero2;
    private static Tile[][] testMap;
    private static Player player1, player2;
    private static Castle castle, castle2;
    private static ByteArrayOutputStream outputStream;

    @BeforeEach
    void init(){
        player1 = new Player("Игрок", 10000, 1000);
        player2 = new Player("Компьютер", 10000, 1000);
        testMap = new Tile[10][10];
        for(int y=0;y<10;y++){
            for(int x=0;x<10;x++){
                testMap[y][x] = new TerrainTile(x, y, TerrainType.GRASS);
            }
        }

        hero = new Hero(0, 0, 200, 110, 5, 1, testMap, player1, new ArrayList<>());
        hero2 = new Hero(0, 0, 200, 110, 5, 1, testMap, player2, new ArrayList<>());
        testMap[0][0] = new CastleTile(0, 0, "Noname", player1);
        testMap[1][1] = new CastleTile(0, 0, "Noname", player2);
        castle = ((CastleTile)testMap[0][0]).getCastle();
        castle2 = ((CastleTile)testMap[1][1]).getCastle();

        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Console.clean();
    }

    @Nested
    class TestBuildings{
        @Test
        void test_build_altar(){
            Altar building = new Altar();
            int prev_money = player1.getGold();
            castle.build(building, player1);

            assertTrue(player1.getAvailableUnits().contains(PerkType.HERO));
            assertEquals(prev_money-building.getCost(), player1.getGold());
            assertTrue(castle.getBuildings().contains(building));
        }
        @Test
        void test_build_armory(){
            Armory building = new Armory();
            int prev_money = player1.getGold();
            castle.build(building, player1);

            assertTrue(player1.getAvailableUnits().contains(PerkType.SWORDSMAN));
            assertEquals(prev_money-building.getCost(), player1.getGold());
            assertTrue(castle.getBuildings().contains(building));
        }
        @Test
        void test_build_cathedral(){
            Cathedral building = new Cathedral();
            int prev_money = player1.getGold();
            castle.build(building, player1);

            assertTrue(player1.getAvailableUnits().contains(PerkType.PALADIN));
            assertEquals(prev_money-building.getCost(), player1.getGold());
            assertTrue(castle.getBuildings().contains(building));
        }
        @Test
        void test_build_fortPost(){
            FortPost building = new FortPost();
            int prev_money = player1.getGold();
            castle.build(building, player1);

            assertTrue(player1.getAvailableUnits().contains(PerkType.CAVALRY));
            assertEquals(prev_money-building.getCost(), player1.getGold());
            assertTrue(castle.getBuildings().contains(building));
        }
        @Test
        void test_build_guardPost(){
            GuardPost building = new GuardPost();
            int prev_money = player1.getGold();
            castle.build(building, player1);

            assertTrue(player1.getAvailableUnits().contains(PerkType.SPEARMAN));
            assertEquals(prev_money-building.getCost(), player1.getGold());
            assertTrue(castle.getBuildings().contains(building));
        }
        @Test
        void test_build_tower(){
            Tower building = new Tower();
            int prev_money = player1.getGold();
            castle.build(building, player1);

            assertTrue(player1.getAvailableUnits().contains(PerkType.CROSSBOMEN));
            assertEquals(prev_money-building.getCost(), player1.getGold());
            assertTrue(castle.getBuildings().contains(building));
        }
        @Test
        void test_not_enough(){
            player1.addGold(-9000);
            Tower building = new Tower();
            castle.build(building, player1);

            Console.PrintEvents();
            String consoleOutput = outputStream.toString().trim();

            assertEquals("1) Не хватает золота на "+building.getName(), consoleOutput);
        }
        @Test
        void test_builded_already(){
            Tower building = new Tower();
            castle.build(building, player1);
            Console.clean();
            castle.build(building, player1);

            Console.PrintEvents();
            String consoleOutput = outputStream.toString().trim();

            assertEquals("1) "+building.getName()+" уже есть!", consoleOutput);
        }
        @Test
        void test_not_in_castle(){
            hero.moveHero(testMap, 1, 1);
            Console.clean();
            CommandHandler handler = new CommandHandler(hero, player1, castle, null, testMap);
            handler.handleCommand("B");

            Console.PrintEvents();
            String consoleOutput = outputStream.toString().trim();

            assertEquals("1) Герой не находится в крепости!", consoleOutput);
        }
    }

    @Nested
    class TestReqruit{
        @Test
        void test_reqruit_spearman(){
            GuardPost building = new GuardPost();
            int capHero = hero.getUnits().size();
            int capPlayer = player1.getArmy().size();
            castle.build(building, player1);
            int prevMoney = player1.getGold();
            Console.clean();

            hero.recruitUnit(PerkType.SPEARMAN, 0, 0);
            assertEquals(capHero+1, hero.getUnits().size());
            assertEquals(capPlayer+1, player1.getArmy().size());
            assertEquals(prevMoney-500, player1.getGold());
        }
        @Test
        void test_reqruit_crossbomen(){
            Tower building = new Tower();
            int capHero = hero.getUnits().size();
            int capPlayer = player1.getArmy().size();
            castle.build(building, player1);
            int prevMoney = player1.getGold();
            Console.clean();

            hero.recruitUnit(PerkType.CROSSBOMEN, 0, 0);
            assertEquals(capHero+1, hero.getUnits().size());
            assertEquals(capPlayer+1, player1.getArmy().size());
            assertEquals(prevMoney-1000, player1.getGold());
        }
        @Test
        void test_reqruit_cavalary(){
            FortPost building = new FortPost();
            int capHero = hero.getUnits().size();
            int capPlayer = player1.getArmy().size();
            castle.build(building, player1);
            int prevMoney = player1.getGold();
            Console.clean();

            hero.recruitUnit(PerkType.CAVALRY, 0, 0);
            assertEquals(capHero+1, hero.getUnits().size());
            assertEquals(capPlayer+1, player1.getArmy().size());
            assertEquals(prevMoney-2000, player1.getGold());
        }
        @Test
        void test_reqruit_paladin(){
            Cathedral building = new Cathedral();
            int capHero = hero.getUnits().size();
            int capPlayer = player1.getArmy().size();
            castle.build(building, player1);
            int prevMoney = player1.getGold();
            Console.clean();

            hero.recruitUnit(PerkType.PALADIN, 0, 0);
            assertEquals(capHero+1, hero.getUnits().size());
            assertEquals(capPlayer+1, player1.getArmy().size());
            assertEquals(prevMoney-2500, player1.getGold());
        }
        @Test
        void test_reqruit_swordsman(){
            Armory building = new Armory();
            int capHero = hero.getUnits().size();
            int capPlayer = player1.getArmy().size();
            castle.build(building, player1);
            int prevMoney = player1.getGold();
            Console.clean();

            hero.recruitUnit(PerkType.SWORDSMAN, 0, 0);
            assertEquals(capHero+1, hero.getUnits().size());
            assertEquals(capPlayer+1, player1.getArmy().size());
            assertEquals(prevMoney-1500, player1.getGold());
        }
        @Test
        void test_reqruit_hero(){
            Altar building = new Altar();
            castle.build(building, player1);
            int prevMoney = player1.getGold();
            Console.clean();

            player1.recruitHero(castle, testMap);
            assertEquals(1, player1.getLeaders().size());
            assertEquals(prevMoney-3000, player1.getGold());
        }
        @Test
        void test_no_building(){
            hero.recruitUnit(PerkType.SPEARMAN, 0, 0);

            Console.PrintEvents();
            String consoleOutput = outputStream.toString().trim();

            assertEquals("1) ❌ Невозможно нанять юнита SPEARMAN. Здание не построено!", consoleOutput);
        }
        @Test
        void test_no_money(){
            player1.addGold(-8999);

            GuardPost building = new GuardPost();
            castle.build(building, player1);
            Console.clean();

            hero.recruitUnit(PerkType.SPEARMAN, 0, 0);
            Console.PrintEvents();
            String consoleOutput = outputStream.toString().trim();

            assertEquals("1) ❌ Невозможно нанять юнита SPEARMAN. Денег не хватает или армия полная!", consoleOutput);
        }
        @Test
        void test_not_in_castle(){
            hero.moveHero(testMap, 1, 1);
            Console.clean();
            CommandHandler handler = new CommandHandler(hero, player1, castle, null, testMap);
            handler.handleCommand("T");

            Console.PrintEvents();
            String consoleOutput = outputStream.toString().trim();

            assertEquals("1) Герой не находится в крепости!", consoleOutput);
        }
    }

    @AfterAll
    static void returnInitState(){
        System.setOut(System.out);
    }
}
//Постройка здания (всех) +
//Здание уже построено +
//Денег не хватает +
//Герой не в замке +

//Покупка юнита (всех) +
//Нет здания для постройки +
//Денег не хватает +
//Армия полная +
//Герой не в замке +