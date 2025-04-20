package PvP;

import Entities.Hero;
import Entities.Unit;
import Game.Console;
import Game.Player;

import java.util.List;
import java.util.Scanner;

public class pvpManager {
    private Hero playerHero;
    private Hero computerHero;
    private Player player;
    private Player computer;
    private pvpMapRender renderer;
    private pvpHandler playerHandler;
    private pvpAI computerAI;
    private Scanner scanner;

    private void space(){
        for (int i = 0;i<20;i++)
            System.out.println();
    }

    public pvpManager(Hero playerHero, Player player, Hero computerHero, Player computer, Scanner scanner) {
        this.playerHero = playerHero;
        this.computerHero = computerHero;
        this.player = player;
        this.computer = computer;

        this.scanner = scanner;
        this.renderer = new pvpMapRender(playerHero, computerHero);
        this.playerHandler = new pvpHandler(playerHero, computerHero, player, scanner, renderer);
        this.computerAI = new pvpAI(computerHero, playerHero, computer, renderer);

        space();
        System.out.println("\uD83C\uDFAE Начало PvP-режима!");
        initPVP();
    }

    public void initPVP(){
        System.out.println("\uD83D\uDD39 Этап размещения юнитов!");
        playerHandler.placePlayerUnits();

        System.out.println("🔹 Компьютер размещает своих юнитов...");
        computerAI.placeUnits();

        space();

        System.out.println("🔹 Карта:");
        renderer.render();
    }

    private void render(){
        space();
        System.out.println("🔹 Карта:");
        renderer.render();
        Console.PrintEvents();
    }

    public int start(){
        System.out.println("🔹 Основной игровой цикл!");
        int pvp;

        while (true) {
            System.out.println("🔹 Ваш ход!");
            playerTurn();

            pvp = isGameOver();
            if (pvp != 0) { return pvp; }

            System.out.println("🔹 Ход компьютера!");
            computerAI.makeMove();

            render();

            pvp = isGameOver();
            if (pvp!=0) { return pvp; }
        }
    }

    private void playerTurn() {
        while (true) {
            System.out.println("🔹 Введите команду (SELECT, MOVE, ATTACK, ARMY, END): ");
            String command = scanner.nextLine().trim().toUpperCase();

            if (command.equals("END")) {
                playerHandler.endTurn();
                break;
            } else {
                playerHandler.handleCommand(command);
            }

            render();
        }
    }

    private int isGameOver() {
        if (playerHero.getUnits().isEmpty()) {
            Console.addEvent("🔹 Компьютер победил! Все ваши юниты уничтожены.");
            return -1;
        }
        if (computerHero.getUnits().isEmpty()) {
            Console.addEvent("🔹 Вы победили! Все юниты компьютера уничтожены.");
            return 1;
        }
        return 0;
    }
}
