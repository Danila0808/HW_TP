package Game;

import Entities.Hero;
import Entities.Lighthouse;
import Entities.Unit;
import FileWorker.FileSystem;
import FileWorker.Reader;
import FileWorker.Writter;
import Map.*;
import PvP.pvpManager;
import Recorder.Recorder;

import java.io.IOException;
import java.util.*;

public class Game {
    private String Name;
    public static final int chance_unlight = 1;
    public static final int MAP_SIZE = 10;
    private Tile[][] map;
    private Hero playerHero, computerHero;
    private Scanner scanner;
    private Random random;
    private Player player, computer;
    private static MapRenderer renderer;
    private CommandHandler commandHandler;
    private AI computerAI;
    public static int turn = 1;
    private Lighthouse lighthouse;
    private String fileName;
    int IND;

    public static final int rangeObserved = 2;

    public Game() {
        Name = "Default";
        scanner = new Scanner(System.in);
        random = new Random();
        player = new Player("Игрок", 10000, 1000);
        computer = new Player("Компьютер", 5000, 1000);
        initGame();
    }

    public Game(String path) throws IOException {
        Reader r = new Reader(path);
        r.recognize();
        Name = r.getName();
        scanner = new Scanner(System.in);
        random = new Random();
        player = r.getPlayer();
        computer = r.getComputer();
        map = r.getMap();
        renderer = r.getRenderer();
        computerHero = computer.getLeaders().getFirst();
        computerAI = new AI(computerHero, computer, r.getComputerCastle(), map);
        lighthouse = r.getLighthouse();
    }

    private void isUnlighted(){
        int chance = random.nextInt(11);
        if(chance <= chance_unlight)
            lighthouse.work(false);
    }

    public void initGame() {
        System.out.println("🔹 Генерация карты...");
        MapGenerator generator = new MapGenerator(player, computer, MAP_SIZE);
        map = generator.getMap();

        System.out.println("🔹 Создание героя...");
        List<Unit> player_units = new ArrayList<>();
        List<Unit> computer_units = new ArrayList<>();
        playerHero = new Hero(0, 0, 200, 100, 5, 1, map, player, player_units);
        computerHero = new Hero(MAP_SIZE-1, MAP_SIZE-1, 200, 100, 5, 1, map, computer, computer_units);
        player.addHero(playerHero);
        computer.addHero(computerHero);

        List<Hero> heroes = new ArrayList<>();
        heroes.add(playerHero);
        heroes.add(computerHero);
        renderer = new MapRenderer(map, heroes);

        LightTile lightTile = generator.getLight();
        this.lighthouse = new Lighthouse(lightTile.getX(), lightTile.getY(), renderer);
        lightTile.setLighthouse(this.lighthouse);

        commandHandler = new CommandHandler(playerHero, player, ((CastleTile) map[0][0]).getCastle(), scanner, map);
        player.addHandler(commandHandler);
        computerAI = new AI(computerHero, computer, ((CastleTile) map[MAP_SIZE-1][MAP_SIZE-1]).getCastle(), map);

        Name = Launcher.Name;
    }

    private void space(){
        for (int i = 0;i<20;i++)
            System.out.println();
    }

    public void renderGame() {
        List<Hero> heroes = new ArrayList<>(player.getLeaders());
        heroes.add(computerHero);
        renderer.setHeroes(heroes);

        System.out.println("🔹 Карта:");
        renderer.render();
    }

    public void start() throws IOException {
        fileName = "NotSavedMap";
        System.out.println("🎮 Игра началась!");
        IND = 0;
        commandHandler = player.getControl().get(IND);

        while (true) {
            renderGame();
            System.out.println("Ход номер №"+turn);
            Console.PrintEvents();

            System.out.println("\uD83D\uDD38 Сейчас ходит герой ...:");

            System.out.println("🔹 Введите команду (W/A/S/D/WA/WD/SA/SD - движение, B - покупка, T - найм, balance - отобразить баланс, army - отобразить армию, E - скип хода, Q - выход): ");
            String command = scanner.nextLine().trim().toUpperCase();

            if (command.equals("Q")) {
                System.out.println("🚪 Выход из игры.");
                scanner.close();
                throw new GameExitException(0, 0, "Пользователь принудительно завершил игру");
            }
            if (command.equals("E")) {
                if(IND==player.getControl().size()-1) {
                    IND=0;
                    player.addGold(player.getGoldIncome());
                    computer.addGold(computer.getGoldIncome());
                    computerHero.startNewTurn();
                    isUnlighted();
                    computerAI.makeMove();
                    commandHandler = player.getControl().get(IND);
                }else{
                    IND++;
                }
            }
            if (command.equals("SAVE")){
                FileSystem.MKdir(FileSystem.pathSaves, Name);
                System.out.print("🔹 Введите название карты: ");
                fileName = scanner.nextLine();
                Writter w = new Writter(FileSystem.pathSaves+"\\" +Name+"\\"+fileName+".txt");
                w.Write(Name, map, player, computer);
                Console.addEvent("Игра сохранена!");
            }

            commandHandler.handleCommand(command);
            space();

            checkBattle();
            isGameOver();
            checkConquer();
            if(IND==player.getControl().size())
                IND=0;
            commandHandler = player.getControl().get(IND);
        }
    }

    private void isGameOver() throws IOException {
        String message;
        if(player.getArmy().size() == 0 && player.getLeaders().size() == 0){
            message = "Игра окончена... Вы проиграли!";
            System.out.println(message);
            scanner.close();
            throw new GameExitException(0, -1, message);
        }
        if(computer.getArmy().size() == 0 && computer.getLeaders().size() == 0){
            message = "Игра окончена... Вы выйграли!";
            System.out.println(message);
            scanner.close();
            Recorder.record(Name+"-"+fileName, player, computer);
            throw new GameExitException(0, 1, message);
        }
    }

    private void checkBattle(){
        for(Hero ph : player.getLeaders()) {
            if (ph.getX() == computerHero.getX() && ph.getY() == computerHero.getY()) {
                pvpManager manager = new pvpManager(ph, player, computerHero, computer, scanner);

                System.out.print("🚪 Битва окончена. Все юниты "+player.getName()+"а мертвы... Герой погиб!");
                if(manager.start()==1){
                    computer.getLeaders().remove(computerHero);
                }else{
                    player.getLeaders().remove(ph);
                    player.getControl().remove(commandHandler);
                }
                return;
//            Battle battle = new Battle(playerHero.getX(), playerHero.getY(), player, computer, playerHero, computerHero);
//            if(battle.Fight()){
//                System.out.println("⚔ Победа Игрока. Игра завершена!");
//                return true;
//            }else{
//                System.out.println("⚔ Поражение Игрока. Игра завершена!");
//                return true;
//            }
            }
        }
    }
    private void checkConquer(){
        for(Hero ph : player.getLeaders()) {
            if (ph.getX() == MAP_SIZE - 1 && ph.getY() == MAP_SIZE - 1) {
                Console.addEvent("Замок компьютера разрушен! Компьютер лишен заработка!");
                computer.addGoldIncome(-computer.getGoldIncome());
            }
        }
        if(computerHero.getX()==0 && computerHero.getY()==0){
            Console.addEvent("Замок игрока разрушен! Игрок лишен заработка!");
            player.addGoldIncome(-player.getGoldIncome());
        }
    }
}