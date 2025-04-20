package Game;

import Entities.*;
import Map.Tile;
import java.util.Scanner;

public class CommandHandler {
    private Hero hero;
    private Player player;
    private Castle castle;
    private Scanner scanner;
    private Tile[][] map;

    public CommandHandler(Hero hero, Player player, Castle castle, Scanner scanner, Tile[][] map) {
        this.hero = hero;
        this.player = player;
        this.castle = castle;
        this.scanner = scanner;
        this.map = map;
    }

    public void handleCommand(String command) {
        switch (command.toUpperCase()) {
            case "W": // Движение вверх
            case "A": // Движение влево
            case "S": // Движение вниз
            case "D": // Движение вправо
            case "WD": // Движение вправо вверх
            case "SD": // Движение вправо вниз
            case "WA": // Движение влево вверх
            case "SA": // Движение влево вниз
                handlePlayerMove(command);
                break;
            case "B": // Покупка здания
                handleBuyBuilding();
                break;
            case "T": // Наем воинов
                handleRecruitUnit();
                break;
            case "E":
                Game.turn+=1;
                Console.addEvent("Ход завершен! Переход хода компьютеру!");
                hero.startNewTurn();
                break;
            case "ARMY":
                player.showArmy();
                break;
            case "BALANCE":
                Console.addEvent("На данный момент у вас: " + player.getGold());
                break;
            case "ICON":
                handleIcon();
                break;
            case "SAVE":
                break;
            default:
                Console.addEvent("❌ Неизвестная команда: " + command);
                break;
        }
    }

    private void handlePlayerMove(String command) {
        int dx = 0, dy = 0;
        switch (command) {
            case "W": dy = -1; break;
            case "A": dx = -1; break;
            case "S": dy = 1; break;
            case "D": dx = 1; break;
            case "WD": dx = 1; dy = -1; break;
            case "WA": dx = -1; dy = -1; break;
            case "SD": dx = 1; dy = 1; break;
            case "SA": dx = -1; dy = 1; break;
            default:
                System.out.println("❌ Неизвестная команда!");
                return;
        }

        hero.moveHero(map, dx, dy);
    }

    private void handleBuyBuilding() {
        if(hero.getX()!=0 || hero.getY()!=0){
            Console.addEvent("Герой не находится в крепости!");
            return;
        }

        System.out.println("🔹 Доступные здания:");
        System.out.println("1. Сторожевой пост (1000 золота)");
        System.out.println("2. Башня арбалетчиков (1500 золота)");
        System.out.println("3. Оружейная (2000 золота)");
        System.out.println("4. Фортпост (2500 золота)");
        System.out.println("5. Собор (3000 золота)");
        System.out.println("6. Алтарь (5000 золота)");
        System.out.print("🔹 Введите номер здания: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        Building building = null;
        switch (choice) {
            case 1:
                building = new GuardPost();
                break;
            case 2:
                building = new Tower();
                break;
            case 3:
                building = new Armory();
                break;
            case 4:
                building = new FortPost();
                break;
            case 5:
                building = new Cathedral();
                break;
            case 6:
                building = new Altar();
                break;
            default:
                Console.addEvent("❌ Неверный выбор здания.");
                return;
        }

        castle.build(building, player);
    }

    private void handleRecruitUnit() {
        if(hero.getX()!=0 || hero.getY()!=0){
            Console.addEvent("Герой не находится в крепости!");
            return;
        }

        System.out.println("🔹 Доступные юниты:");
        for (PerkType type : player.getAvailableUnits()) {
            System.out.println("- " + type);
        }
        System.out.print("🔹 Введите тип юнита (например, SPEARMAN): ");
        String unitTypeInput = scanner.nextLine().trim().toUpperCase();
        try {
            PerkType unitType = PerkType.valueOf(unitTypeInput);
            if (unitType == PerkType.HERO && player.recruitHero(castle, map)){
                Console.addEvent("🔹 Герой " + " успешно нанят! " + "Денег осталось:"+player.getGold());
            } else if (hero.recruitUnit(unitType, hero.getX(), hero.getY())) {
                Console.addEvent("🔹 Юнит " + unitType + " успешно нанят! " + "Денег осталось:"+player.getGold());
            } else {
                Console.addEvent("❌ Не удалось нанять юнита " + unitType);
            }
        } catch (IllegalArgumentException e) {
            Console.addEvent("❌ Неверный тип юнита!");
        }
    }

    private void handleIcon(){
        System.out.println("🔹 1) (Дефолтный) Гэндальф \uD83E\uDDD9\uD83C\uDFFB\u200D♂\uFE0F ");
        System.out.println("🔹 2) Артас \uD83E\uDDB8\u200D ");
        System.out.println("🔹 3) Арнольд \uD83E\uDDB8\uD83C\uDFFB\u200D♂\uFE0F ");
        System.out.println("🔹 4) Волан-де-морт \uD83E\uDDB9\uD83C\uDFFB\u200D♂\uFE0F ");
        System.out.println("🔹 5) Дракула \uD83E\uDDDB ");
        System.out.println("🔹 6) Зельда \uD83E\uDDB8\uD83C\uDFFC\u200D♀\uFE0F ");
        System.out.println("🔹 7) Лара Крофт \uD83E\uDDB8 ");
        System.out.println("🔹 Введите id иконки игрока: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        if(id<1 || id>7){
            Console.addEvent("❌ Неверный выбор иконки.");
            return;
        }

        Customise.changeIcon(hero, id);
    }
}