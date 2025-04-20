package Editor;

import Entities.Hero;
import Entities.PerkType;
import Entities.Unit;
import FileWorker.FileSystem;
import Game.Game;
import Game.Console;
import Game.Launcher;
import Game.GameExitException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class EditorMenu {
    private static Scanner scanner;
    private static Editor editor;

    public static void Menu() throws IOException {
        String temp = "1) Загрузить карту\r\n2) Создать новую\r\n3) Удалить карту\r\n4) Играть\r\n5) Назад\r\n";
        System.out.println(temp);
        scanner = new Scanner(System.in);
        System.out.print("Введите число [1;5]: ");
        String choice = scanner.nextLine();

        space();
        switch (choice){
            case "1":
                load();
                break;
            case "2":
                create();
                break;
            case "3":
                delete();
                break;
            case "4":
                start();
                break;
            case "5":
                return;
            default:
                System.out.println("❌ Неизвестная команда");
                Menu();
                break;
        }
    }

    private static void start() throws IOException {
        List<String> paths = FileSystem.loadMaps();
        FileSystem.printNames(paths);
        System.out.println((paths.size()+1)+") Назад");
        String c = scanner.nextLine();
        if(Objects.equals(c, (paths.size()+1) + "")){
            space();
            Menu();
        }else{
            int ind=0;
            try {
                ind = Integer.parseInt(c);
            }catch (NumberFormatException e){
                System.out.println("❌ Неизвестная команда");
                space();
                Menu();
            }
            if(ind<1 || ind>paths.size()){
                System.out.println("❌ Неизвестная команда");
                space();
                Menu();
            }
            space();

            Game game = new Game(paths.get(ind-1));
            try {
                game.start();
            } catch (GameExitException _){ System.exit(0); }
        }
    }

    private static void load() throws IOException {
        List<String> paths = FileSystem.loadMaps();
        FileSystem.printNames(paths);
        System.out.println((paths.size()+1)+") Назад");
        String c = scanner.nextLine();
        if(Objects.equals(c, (paths.size()+1) + "")){
            space();
            Menu();
        }else{
            int ind=0;
            try {
                ind = Integer.parseInt(c);
            }catch (NumberFormatException e){
                System.out.println("❌ Неизвестная команда");
                space();
                Menu();
            }
            if(ind<1 || ind>paths.size()){
                System.out.println("❌ Неизвестная команда");
                space();
                Menu();
            }
            space();

            editor = new Editor(paths.get(ind-1));
            edit();
        }
    }

    private static void create() throws IOException {
        editor = new Editor();
        edit();
    }

    private static void delete() throws IOException {
        List<String> paths = FileSystem.loadMaps();
        FileSystem.printNames(paths);
        System.out.println((paths.size()+1)+") Назад");
        String c = scanner.nextLine();
        if(Objects.equals(c, (paths.size()+1) + "")){
            space();
            Menu();
        }else{
            int ind=0;
            try {
                ind = Integer.parseInt(c);
            }catch (NumberFormatException e){
                System.out.println("❌ Неизвестная команда");
                space();
                Menu();
            }
            if(ind<1 || ind>paths.size()){
                System.out.println("❌ Неизвестная команда");
                space();
                Menu();
            }
            space();

            if(FileSystem.delete(paths.get(ind-1)))
                System.out.println("Карта успешно удалена");
            else
                System.out.println("Ошибка удаления...");
            space();
            Menu();
        }
    }

    private static void edit() throws IOException {
        while (true){
            space();
            System.out.println("🔹 Карта: ");
            editor.render();
            System.out.println("🔹 Доступные комманды: \r\nINSERT - вставить или изменить ячейку\r\nBALANCE - изменить" +
                    " денежный баланс у игрока или компьютера\r\nNEW - добавить героя или юнита\r\nDELETE - удалить героя или юнита" +
                    "\r\nSAVE - сохранить и выйти\r\nQUIT - выйти из редактора");
            Console.PrintEvents();
            String command = scanner.nextLine().trim().toUpperCase();

            switch (command) {
                case "INSERT":
                    System.out.println("🔹 Выберете тип местности: \r\n" +
                            "1) Ландшафт\r\n" +
                            "2) Маяк\r\n" +
                            "3) Сокровища\r\n" +
                            "4) Назад");
                    command = scanner.nextLine().trim();
                    if(command.equals("4"))
                        break;
                    insert(command);
                    break;
                case "BALANCE":
                    insertBalance();
                    break;
                case "NEW":
                    insertNew();
                    break;
                case "DELETE":
                    deleteEntity();
                    break;
                case "SAVE":
                    System.out.print("🔹 Введите название карты: ");
                    String fileName = scanner.nextLine();
                    editor.save(Launcher.Name+"-"+fileName);
                    return;
                case "QUIT":
                    return;
                default:
                    Console.addEvent("❌ Неизвестная команда: " + command);
                    break;
            }
        }
    }

    private static int[] validator(String command, boolean flag) throws IOException {
        int[] xy = new int[2];
        String[] arr = command.split(" ");
        try{
            xy[0] = Integer.parseInt(arr[0]);
            xy[1] = Integer.parseInt(arr[1]);
        }catch(Exception e){
            Console.addEvent("❌ Неизвестная комманда!");
            return null;
        }
        if(xy[0] < 0 || xy[0] >= Game.MAP_SIZE || xy[1] < 0 || xy[1] >= Game.MAP_SIZE) {
            Console.addEvent("❌ Выход за пределы карты!");
            return null;
        }if(flag && xy[0]==xy[1] && (xy[0] == 0 || xy[0] == Game.MAP_SIZE-1)) {
            Console.addEvent("❌ Замок нельзя сломать!");
            return null;
        }
        return xy;
    }

    private static int convert(String num){
        try {
            return Integer.parseInt(num);
        }catch (Exception e){
            Console.addEvent("❌ Недопутимое значение!");
            return -1;
        }
    }

    private static void insert(String command) throws IOException {
        space();
        System.out.print("🔹 Введите координаты: ");
        int[] result = validator(scanner.nextLine(), true);
        if(result == null)
            return;
        int x = result[0];
        int y = result[1];
        String com;
        space();

        switch (command){
            case "1":
                System.out.println("🔹 Выберите тип ландшафта: \r\n" +
                        "1) Трава\r\n" +
                        "2) Лес\r\n" +
                        "3) Вода\r\n" +
                        "4) Гора\r\n" +
                        "5) Тропа\r\n" +
                        "6) Назад");
                System.out.print("Введите число 1-6: ");
                com = scanner.nextLine();
                if(Objects.equals(com, "6")) return;
                insertTerrain(com, x, y);
                break;
            case "2":
                editor.setLight(x, y);
                break;
            case "3":
                System.out.print("Введите размер сокровищ: ");
                com = scanner.nextLine();
                int value = 0;
                try {
                    value = Integer.parseInt(com);
                }catch (Exception e){
                    Console.addEvent("❌ Недопутимое значение!");
                    break;
                }
                editor.setTreasure(x, y, value);
                break;
            default:
                Console.addEvent("❌ Неизвестная комманда!");
                break;
        }
    }

    private static void insertTerrain(String command, int x, int y){
        switch (command){
            case "1":
                editor.setGrass(x, y);
                break;
            case "2":
                editor.setForest(x, y);
                break;
            case "3":
                editor.setWater(x, y);
                break;
            case "4":
                editor.setMoutain(x, y);
                break;
            case "5":
                editor.setRoad(x, y);
                break;
            default:
                Console.addEvent("❌ Неизвестная комманда!");
                break;
        }
    }

    private static void insertBalance(){
        space();
        System.out.println("1) Игрок: золото - "+editor.player.getGold() + "; доход - " + editor.player.getGoldIncome());
        System.out.println("2) Компьютер: золото - "+editor.computer.getGold() + "; доход - " + editor.computer.getGoldIncome());
        System.out.println("3) Назад");
        String command = scanner.nextLine().trim();
        int value, gold, income;

        switch (command){
            case "1":
                System.out.print("Введите золото: ");
                value = convert(scanner.nextLine().trim());
                if(value == -1)
                    return;
                gold = value;

                System.out.print("Введите доход: ");
                value = convert(scanner.nextLine().trim());
                if(value == -1)
                    return;
                income = value;

                editor.setBalancePlayer(gold, income);
                break;
            case "2":
                System.out.print("Введите золото: ");
                value = convert(scanner.nextLine().trim());
                if(value == -1)
                    return;
                gold = value;

                System.out.print("Введите доход: ");
                value = convert(scanner.nextLine().trim());
                if(value == -1)
                    return;
                income = value;

                editor.setBalanceEnemy(gold, income);
                break;
            case "3":
                break;
            default:
                Console.addEvent("❌ Неизвестная комманда!");
                break;
        }
    }

    private static void insertNew() throws IOException {
        space();
        System.out.println("1) Герой\r\n2) Юнит\r\n3) Назад");
        String command = scanner.nextLine().trim();
        System.out.println();
        switch (command){
            case "1":
                setHero();
                break;
            case "2":
                setUnit();
                break;
            case "3":
                break;
            default:
                Console.addEvent("❌ Неизвестная комманда!");
                break;
        }
    }

    private static void setHero() throws IOException {
        space();
        System.out.print("🔹 Введите координаты: ");
        int[] result = validator(scanner.nextLine(), false);
        if(result == null)
            return;
        int x = result[0];
        int y = result[1];
        System.out.println();

        System.out.println("1) Игрок\r\n2) Комьютер");
        String command = scanner.nextLine().trim();
        switch (command){
            case "1":
                editor.setHero(x, y);
                break;
            case "2":
                editor.setEnemy(x, y);
                break;
            default:
                Console.addEvent("❌ Неизвестная комманда!");
                break;
        }
    }

    private static void setUnit() throws IOException {
        space();
        System.out.print("🔹 Введите координаты: ");
        int[] result = validator(scanner.nextLine(), false);
        if(result == null)
            return;
        int x = result[0];
        int y = result[1];
        System.out.println();

        List<Hero> heroes = editor.getHeroes();
        int i = 1;
        for (Hero hero : heroes) {
            System.out.println(i + ") " + hero.getOwner().getName() + " X: " + hero.getX() + " Y: " + hero.getY());
            i++;
        }
        System.out.print("Введите значение 1-"+heroes.size()+": ");
        int value = convert(scanner.nextLine().trim());
        if (value==-1)
            return;
        System.out.println();

        System.out.println("Введите тип война:\r\n" +
                "1) Копейщик\r\n" +
                "2) Арбалетчик\r\n" +
                "3) Рыцарь\r\n" +
                "4) Конница\r\n" +
                "5) Паладин");
        switch (scanner.nextLine().trim()) {
            case "1":
                editor.setUnit(value - 1, x, y, PerkType.SPEARMAN);
                break;
            case "2":
                editor.setUnit(value - 1, x, y, PerkType.CROSSBOMEN);
                break;
            case "3":
                editor.setUnit(value - 1, x, y, PerkType.SWORDSMAN);
                break;
            case "4":
                editor.setUnit(value - 1, x, y, PerkType.CAVALRY);
                break;
            case "5":
                editor.setUnit(value - 1, x, y, PerkType.PALADIN);
                break;
            default:
                Console.addEvent("❌ Неизвестная комманда!");
                break;
        }
    }

    private static void deleteEntity(){
        space();
        System.out.println("1) Герой\r\n2) Юнит\r\n3) Назад");
        String command = scanner.nextLine().trim();
        switch (command){
            case "1":
                deleteHero();
                break;
            case "2":
                deleteUnit();
                break;
            case "3":
                break;
            default:
                Console.addEvent("❌ Неизвестная комманда!");
                break;
        }
    }

    private static void deleteHero(){
        space();
        List<Hero> heroes = editor.getHeroes();
        if(heroes.isEmpty()){
            Console.addEvent("❌ Героев нет");
            return;
        }
        int i = 1;
        for (Hero hero : heroes) {
            System.out.println(i + ") " + hero.getOwner().getName() + " X: " + hero.getX() + " Y: " + hero.getY());
            i++;
        }
        System.out.print("Введите значение 1-"+heroes.size()+": ");
        int value = convert(scanner.nextLine().trim());
        if (value==-1)
            return;

        editor.deleteHero(value-1);
    }

    private static void deleteUnit(){
        space();
        List<Hero> heroes = editor.getHeroes();
        if(heroes.isEmpty()){
            Console.addEvent("❌ Героев нет");
            return;
        }
        int i = 1;
        for (Hero hero : heroes) {
            System.out.println(i + ") " + hero.getOwner().getName() + " X: " + hero.getX() + " Y: " + hero.getY());
            i++;
        }
        System.out.print("Введите значение 1-"+heroes.size()+": ");
        int ind = convert(scanner.nextLine().trim());
        if (ind==-1)
            return;
        System.out.println();

        if(heroes.get(ind-1).getUnits().isEmpty()){
            Console.addEvent("❌ Юнитов у героя нет");
            return;
        }
        i=1;
        for(Unit u : heroes.get(ind-1).getUnits()) {
            System.out.println(i + ") " + u.getType() + " X: " + u.getX() + " Y: " + u.getY());
            i++;
        }
        System.out.print("Введите значение 1-"+heroes.size()+": ");
        int value = convert(scanner.nextLine().trim());
        if (value==-1)
            return;

        editor.deleteUnit(heroes.get(ind-1), value-1);
    }

    private static void space(){
        for (int i = 0;i<20;i++)
            System.out.println();
    }
}
