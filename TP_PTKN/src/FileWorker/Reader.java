package FileWorker;

import Entities.*;
import Game.*;
import Images.Icons;
import Map.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Reader {
    private String name;
    private Tile[][] map;
    private int MAP_SIZE = Game.MAP_SIZE;
    private String SEP = ";";
    private Player player, computer;
    private Boolean isLighted;
    private List<Hero> heroes;
    private Castle playerCastle, computerCastle;
    private LightTile lightTile;
    private MapRenderer renderer;

    private BufferedReader bufferedReader;

    public Reader(String pathFile){
        heroes = new ArrayList<>();
        map = new Tile[MAP_SIZE][MAP_SIZE];
        player = new Player("Игрок", 10000, 1000);
        computer = new Player("Компьютер", 10000, 1000);

        try {
            FileReader r = new FileReader(pathFile);
            bufferedReader = new BufferedReader(r);
        }catch (IOException e){
            System.err.println("Ошибка при работе с файлом " + pathFile+" : "+e.getMessage());
        }
    }

    public void recognize() throws IOException {
        String line = readLine();
        while(line!=null){
            String[] arr = line.split(SEP);
            switch (arr[0]){
                case "Player":
                    readPlayer(arr);
                    break;
                case "TerrainTile", "TreasureTile", "CastleTile", "LightTile":
                    readTile(arr);
                    break;
                case "Hero":
                    readHero(arr);
                    break;
                case "Unit":
                    readUnit(arr);
                    break;
                case "Name":
                    name = arr[1];
                    break;
            }

            line = readLine();
        }
        for(Hero hero : heroes){
            Player owner = hero.getOwner();
            if(Objects.equals(owner.getName(), "Игрок"))
                player.addHandler(new CommandHandler(hero, player, playerCastle, new Scanner(System.in), map));
        }
        for(PerkType type : player.getAvailableUnits())
            playerCastle.getBuildings().add(getBuildingByType(type));

        for(PerkType type : computer.getAvailableUnits())
            computerCastle.getBuildings().add(getBuildingByType(type));

        renderer = new MapRenderer(map, heroes);

        if(lightTile != null) {
            Lighthouse lighthouse = new Lighthouse(lightTile.getX(), lightTile.getY(), renderer);
            lightTile.setLighthouse(lighthouse);
        }
    }

    private Building getBuildingByType(PerkType type){
        Building building = null;
        switch (type){
            case SPEARMAN:
                building = new GuardPost();
                break;
            case CROSSBOMEN:
                building = new Tower();
                break;
            case SWORDSMAN:
                building = new Armory();
                break;
            case CAVALRY:
                building = new FortPost();
                break;
            case PALADIN:
                building = new Cathedral();
                break;
            case HERO:
                building = new Altar();
                break;
        }
        return building;
    }

    private void readTile(String[] arr){
        int x=0, y=0;
        TerrainType type;

        if(Objects.equals(arr[0], "TerrainTile")){
            x = Integer.parseInt(arr[1]);
            y = Integer.parseInt(arr[2]);
            try { type = TerrainType.valueOf(arr[3]);
            }catch (Exception e){ type = null; }
            map[y][x] = new TerrainTile(x, y, type);
        }else if(Objects.equals(arr[0], "TreasureTile")){
            x = Integer.parseInt(arr[1]);
            y = Integer.parseInt(arr[2]);
            int value = Integer.parseInt(arr[5]);
            boolean isCollected = Boolean.parseBoolean(arr[6]);

            TreasureTile tile = new TreasureTile(x, y, value);
            if(isCollected)
                tile.setCollected();

            map[y][x] = tile;
        }else if(Objects.equals(arr[0], "CastleTile")){
            x = Integer.parseInt(arr[1]);
            y = Integer.parseInt(arr[2]);
            String name = arr[5];

            CastleTile tile = null;
            if(!Objects.equals(name, "Компьютер")) {
                tile = new CastleTile(x, y, name, player);
                playerCastle = tile.getCastle();
            } else {
                tile = new CastleTile(x, y, name, computer);
                computerCastle = tile.getCastle();
            }

            map[y][x] = tile;
        }else if(Objects.equals(arr[0], "LightTile")){
            x = Integer.parseInt(arr[1]);
            y = Integer.parseInt(arr[2]);
            try { type = TerrainType.valueOf(arr[3]);
            }catch (Exception e){ type = null; }
            isLighted = Boolean.parseBoolean(arr[5]);

            map[y][x] = new LightTile(x, y, type, null);
            lightTile = (LightTile) map[y][x];
        }

        map[y][x].setLighted(Boolean.parseBoolean(arr[4]));
    }

    private void readPlayer(String[] arr){
        String name = arr[1];
        int gold = Integer.parseInt(arr[2]);
        int goldIncome = Integer.parseInt(arr[3]);

        Player current;
        if(!Objects.equals(name, "Компьютер"))
            current = player;
        else
            current = computer;

        current.addGold(-current.getGold());
        current.addGoldIncome(-current.getGoldIncome());
        current.addGold(gold);
        current.addGoldIncome(goldIncome);

        if (arr.length<=4)
            return;
        String[] subarr = arr[4].split("-");
        for(String type : subarr){
            PerkType t = PerkType.valueOf(type);
            current.getAvailableUnits().add(t);
        }
    }

    private void readHero(String[] arr){
        int x, y, xp, damage, moveDistance, attackRange;
        String name;
        try {
            x = Integer.parseInt(arr[1]);
            y = Integer.parseInt(arr[2]);
            xp = Integer.parseInt(arr[3]);
            damage = Integer.parseInt(arr[4]);
            moveDistance = Integer.parseInt(arr[5]);
            attackRange = Integer.parseInt(arr[6]);
            name = arr[7];
        }catch (Exception e){ return; }

        Hero hero = null;
        if (!Objects.equals(name, "Компьютер")) {
            hero = new Hero(x, y, xp, damage, moveDistance, attackRange, map, player, new ArrayList<>());
            try { hero.setIcon(Icons.valueOf(arr[9])); }
            catch (Exception _) {  }
            player.addHero(hero);
        }else{
            hero = new Hero(x, y, xp, damage, moveDistance, attackRange, map, computer, new ArrayList<>());
            computer.addHero(hero);
        }

        heroes.add(hero);
    }

    private void readUnit(String[] arr){
        int x = Integer.parseInt(arr[1]);
        int y = Integer.parseInt(arr[2]);
        int xp = Integer.parseInt(arr[3]);
        int damage = Integer.parseInt(arr[4]);
        int moveDistance = Integer.parseInt(arr[5]);
        int attackRange = Integer.parseInt(arr[6]);
        String name = arr[7];
        int cost = Integer.parseInt(arr[8]);
        PerkType perk;
        try { perk = PerkType.valueOf(arr[9]);
        }catch (Exception e){ perk = null; }

        Unit unit;
        if(!Objects.equals(name, "Компьютер")){
            unit = new Unit(x, y, xp, damage, moveDistance, attackRange, map, player, perk, cost);
            player.addUnit(unit);
        }else{
            unit = new Unit(x, y, xp, damage, moveDistance, attackRange, map, computer, perk, cost);
            computer.addUnit(unit);
        }

        heroes.getLast().addUnit(unit);
    }

    public Tile[][] getMap() { return map; }
    public Player getPlayer() { return player; }
    public Player getComputer() { return computer; }
    public List<Hero> getHeroes() { return heroes; }
    public Castle getComputerCastle() { return computerCastle; }
    public MapRenderer getRenderer() { return renderer; }
    public String getName() { return name; }
    public Lighthouse getLighthouse() { return lightTile.getLighthouse(); }

    public String readLine() throws IOException { return bufferedReader.readLine(); }
}
