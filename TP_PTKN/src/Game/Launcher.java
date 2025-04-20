package Game;

import Editor.EditorMenu;
import FileWorker.FileSystem;
import Recorder.Recorder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Launcher {
    private static Game game;
    public static String Name;

    private static void space(){
        for (int i = 0;i<20;i++)
            System.out.println();
    }

    private static void changeName() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("🔹 Введите ваш никнейм: ");
        Name = scanner.nextLine();
        space();
        Menu();
    }

    public static void Menu() throws IOException {
        if(Name==null) changeName();

        String temp = "1) Редактор карт\r\n2) Рекорды\r\n3) Начать новую игру\r\n4) Загрузить\r\n5) Поменять имя\r\n6) Выход\r\n";
        System.out.print(temp);
        System.out.print("Введите число [1;6]: ");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();

        space();
        switch (choice){
            case "1":
                EditorMenu.Menu();
                space();
                Menu();
                break;
            case "2":
                Recorder.printRecords();
                System.out.println("\r\n");
                Menu();
                break;
            case "3":
                space();
                game = new Game();
                try {
                    game.start();
                } catch (GameExitException e){
                    System.exit(e.getStatus());
                }
                break;
            case "4":
                List<String> accounts = FileSystem.loadSaves();
                List<String> paths = new ArrayList<>();
                if(accounts.contains(FileSystem.pathSaves+"\\"+Name))
                    paths = FileSystem.load(FileSystem.pathSaves+"\\"+Name);
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
                    game = new Game(paths.get(ind-1));
                    try {
                        game.start();
                    } catch (GameExitException e){
                        System.exit(e.getStatus());
                    }
                }

                break;
            case "5":
                changeName();
                break;
            case "6":
                throw new GameExitException(0, 0, "Игра завершена коммандой лаунчера!");
            default:
                System.out.println("❌ Неизвестная команда");
                Menu();
                break;
        }
    }
}
