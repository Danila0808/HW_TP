package PvP;

import Entities.Hero;
import Entities.Unit;
import Game.Console;
import Game.Game;
import Game.Player;

import java.util.List;
import java.util.Scanner;

public class pvpHandler {
    private Hero hero;
    private Hero computerHero;
    private Player player;
    private Scanner scanner;
    private pvpMapRender renderer;
    private List<Unit> playerUnits;
    private Unit selectedUnit;
    private pvpAttack attack;

    public pvpHandler(Hero hero, Hero computerHero, Player player, Scanner scanner, pvpMapRender renderer) {
        this.hero = hero;
        this.computerHero = computerHero;
        this.player = player;
        this.scanner = scanner;
        this.renderer = renderer;

        this.playerUnits = hero.getUnits();
    }

    public void handleCommand(String command) {
        switch (command.toUpperCase()) {
            case "SELECT":
                selectUnit();
                break;
            case "W": // Движение вверх
            case "A": // Движение влево
            case "S": // Движение вниз
            case "D": // Движение вправо
            case "WD": // Движение вправо вверх
            case "SD": // Движение вправо вниз
            case "WA": // Движение влево вверх
            case "SA": // Движение влево вниз
                handleMove(command);
                break;
            case "ATTACK":
                handleAttack();
                break;
            case "ARMY":
                showArmy();
                break;
            case "END":
                endTurn();
                break;
            default:
                Console.addEvent("❌ Неизвестная команда: " + command);
                break;
        }
    }

    private void selectUnit() {
        System.out.println("🔹 Доступные юниты:");
        for (int i = 0; i < playerUnits.size(); i++) {
            Unit unit = playerUnits.get(i);
            System.out.println((i + 1) + ". " + unit.getType() + " (X: " + unit.getX() + ", Y: " + unit.getY() + ")");
        }
        System.out.print("🔹 Введите номер юнита: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice > 0 && choice <= playerUnits.size()) {
            selectedUnit = playerUnits.get(choice - 1);
            System.out.println("🔹 Выбран юнит: " + selectedUnit.getType());
        } else {
            System.out.println("❌ Неверный выбор!");
        }
    }

    private void handleMove(String command) {
        if (selectedUnit == null) {
            Console.addEvent("❌ Сначала выберите юнита!");
            return;
        }

        int dx = 0, dy = 0;
        switch (command.toUpperCase()) {
            case "W": dy = -1; break;
            case "A": dx = -1; break;
            case "S": dy = 1; break;
            case "D": dx = 1; break;
            case "WD": dx = 1; dy = -1; break;
            case "WA": dx = -1; dy = -1; break;
            case "SD": dx = 1; dy = 1; break;
            case "SA": dx = -1; dy = 1; break;
            default:
                Console.addEvent("❌ Неверное направление!");
                return;
        }

        if(selectedUnit.getMoveDistance() <= 0){
            Console.addEvent("❌ У данного юнита нет очков хода!");
            return;
        }

        int newX = selectedUnit.getX() + dx;
        int newY = selectedUnit.getY() + dy;

        if(newX<0 || newY<0 || newX >= Game.MAP_SIZE || newY >= Game.MAP_SIZE){
            Console.addEvent("❌ Выход за пределы карты!");
            return;
        }

        renderer.moveUnit(selectedUnit, newX, newY);
        selectedUnit.setMoveDistance(selectedUnit.getMoveDistance() - 1);
        Console.addEvent("🚶‍♂️ Юнит " + selectedUnit.getType() + " переместился на (" + newX + ", " + newY + ").");
    }

    private void handleAttack() {
        if (selectedUnit == null) {
            Console.addEvent("❌ Сначала выберите юнита!");
            return;
        }

        System.out.println("🔹 Введите координаты цели (X Y): ");
        int targetX = scanner.nextInt();
        int targetY = scanner.nextInt();
        scanner.nextLine();

        attack = new pvpAttack(selectedUnit, computerHero, targetX, targetY, renderer);
    }

    private void showArmy() {
        Console.addEvent("🔹 Ваша армия:");
        for (Unit unit : playerUnits) {
            Console.addEvent("- " + unit.getType() + " (X: " + unit.getX() + ", Y: " + unit.getY() + ") " + "XP: " + unit.getXp());
        }
    }

    public void endTurn() {
        Console.addEvent("🔹 Ход завершен! Переход хода противнику.");
        for (Unit unit : playerUnits) {
            unit.startNewTurn();
        }
        selectedUnit = null;
    }

    public void placePlayerUnits(){
        int ind = 1;
        int end_ind = playerUnits.size();
        for(Unit u : playerUnits) {
            System.out.print("🔹 №" + ind + ") Размещаем юнита. Осталось воинов: " + (end_ind-ind+1));
            System.out.print("🔹 Введите координаты для " + u.getType() +" (X Y): ");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            scanner.nextLine();

            renderer.placePlayerUnit(u, x, y);
            ind++;
        }
    }
}
