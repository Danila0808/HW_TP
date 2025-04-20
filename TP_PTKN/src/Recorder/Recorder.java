package Recorder;

import Entities.Hero;
import Entities.Unit;
import FileWorker.FileSystem;
import FileWorker.Reader;
import FileWorker.Writter;
import Game.Player;

import java.io.IOException;
import java.util.*;

public class Recorder {
    private static String path = FileSystem.pathRecord;

    public static void printRecords() throws IOException {
        Reader r = new Reader(path);
        for(int i=1;i<=3;i++) {
            String[] arr = r.readLine().split(";");
            System.out.println(i + ") " + arr[0]+": "+arr[1]);
        }
    }

    public static void clean(){
        Writter w = new Writter(path);

        for(int i=1;i<=3;i++)
            w.write("Noname"+i+";-9999");
    }

    public static void record(String Name, Player player, Player computer) throws IOException {
        Reader r = new Reader(path);

        Map<String, Integer> records = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            String[] arr = r.readLine().split(";");
            records.put(arr[0], Integer.parseInt(arr[1]));
        }

        int value = calculatePoints(player, computer);
        records.put(Name, value);

        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(records.entrySet());
        sortedEntries.sort((e1, e2) -> e2.getValue().compareTo(e1.getValue()));

        Writter w = new Writter(path);
        for (int i = 0; i < Math.min(3, sortedEntries.size()); i++) {
            Map.Entry<String, Integer> entry = sortedEntries.get(i);
            w.write(entry.getKey() + ";" + entry.getValue());
        }
    }

    public static int calculatePoints(Player player, Player computer){
        int result = 0;

        for(Unit u : player.getArmy())
            result += u.getCost();

        for(Hero h : player.getLeaders())
            result += h.getCost();

        for(Unit u : computer.getArmy())
            result -= u.getCost();

        for(Hero h : computer.getLeaders())
            result -= h.getCost();

        result += player.getGold() + player.getGoldIncome() - computer.getGold() - computer.getGoldIncome();

        return result;
    }
}
