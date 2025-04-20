package Editor;

import Entities.Hero;
import Entities.Lighthouse;
import Entities.PerkType;
import Entities.Unit;
import FileWorker.FileSystem;
import FileWorker.Reader;
import FileWorker.Writter;
import Game.Game;
import Game.Player;
import Images.Icons;
import Map.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Editor {
    private Tile[][] map;
    Player player, computer;
    private int MAP_SIZE = Game.MAP_SIZE;
    List<Hero> heroes;
    LightTile lighthouse;

    public Editor(){
        map = new Tile[MAP_SIZE][MAP_SIZE];
        heroes = new ArrayList<>();

        for(int y=0;y<MAP_SIZE;y++)
            for (int x = 0; x < MAP_SIZE; x++)
                map[y][x] = new TerrainTile(x, y, TerrainType.GRASS);

        initMap();
    }
    public Editor(String path) throws IOException {
        Reader r = new Reader(path);
        r.recognize();

        this.map = r.getMap();
        this.player = r.getPlayer();
        this.computer = r.getComputer();

        map[0][0] = new CastleTile(0, 0, "NoName", player);
        map[MAP_SIZE-1][MAP_SIZE-1] = new CastleTile(MAP_SIZE-1, MAP_SIZE-1, "Noname", computer);

        this.heroes = new ArrayList<>(player.getLeaders());
        this.heroes.addAll(computer.getLeaders());
    }

    private void initMap(){
        player = new Player("Игрок", 10000, 1000);
        computer = new Player("Компьютер", 10000, 1000);

        map[0][0] = new CastleTile(0, 0, "NoName", player);
        map[MAP_SIZE-1][MAP_SIZE-1] = new CastleTile(MAP_SIZE-1, MAP_SIZE-1, "Noname", computer);
    }

    private void space(){
        for (int i = 0;i<20;i++)
            System.out.println();
    }

    public void setGrass(int x, int y){ map[y][x] = new TerrainTile(x, y, TerrainType.GRASS); }
    public void setWater(int x, int y){ map[y][x] = new TerrainTile(x, y, TerrainType.WATER); }
    public void setMoutain(int x, int y){ map[y][x] = new TerrainTile(x, y, TerrainType.MOUNTAIN); }
    public void setForest(int x, int y){ map[y][x] = new TerrainTile(x, y, TerrainType.FOREST); }
    public void setRoad(int x, int y){ map[y][x] = new TerrainTile(x, y, TerrainType.ROAD); }

    public void setTreasure(int x, int y, int value){ map[y][x] = new TreasureTile(x, y, value); }
    public void setLight(int x, int y){
        map[y][x] = new LightTile(x, y, map[y][x].getType(), null);
        lighthouse = (LightTile) map[y][x];
    }

    public void setBalancePlayer(int gold, int income){
        player.addGold(gold-player.getGold());
        player.addGoldIncome(income-player.getGoldIncome());
    }

    public void setBalanceEnemy(int gold, int income){
        computer.addGold(gold-computer.getGold());
        computer.addGoldIncome(income-computer.getGoldIncome());
    }

    public void setHero(int x, int y){
        Hero hero = new Hero(x, y, 250, 101, 5, 1, map, player, new ArrayList<>());
        heroes.add(hero);
        player.addHero(hero);
    }

    public void setEnemy(int x, int y){
        Hero hero = new Hero(x, y, 250, 101, 5, 1, map, computer, new ArrayList<>());
        heroes.add(hero);
        computer.addHero(hero);
    }

    public void setUnit(int ind, int x, int y, PerkType type) {
        Unit u = heroes.get(ind).createUnit(type, x, y);
        heroes.get(ind).getUnits().add(u);
        heroes.get(ind).getOwner().addUnit(u);
    }

    public void deleteHero(int ind){
        Hero hero = heroes.get(ind);
        if(hero.getOwner() == player) {
            for(Unit u : hero.getUnits())
                player.getArmy().remove(u);
            player.getLeaders().remove(hero);
        } else {
            for(Unit u : hero.getUnits())
                computer.getArmy().remove(u);
            computer.getLeaders().remove(hero);
        }
        heroes.remove(hero);
    }

    public void deleteUnit(Hero hero, int ind){
        Unit u = hero.getUnits().get(ind);
        player.getArmy().remove(u);
        hero.getUnits().remove(u);
    }

    private void unlight(){
        for(int y=0;y<MAP_SIZE;y++)
            for (int x = 0; x < MAP_SIZE; x++)
                map[y][x].setLighted(true);
    }

    public void render(){
        unlight();
        MapRenderer renderer = new MapRenderer(map, heroes);
        renderer.render();
    }

    public void save(String fileName){
        if (lighthouse != null)
            lighthouse.setLighthouse(new Lighthouse(lighthouse.getX(), lighthouse.getY(), new MapRenderer(map, heroes)));

        for(int y=0;y<MAP_SIZE;y++)
            for (int x = 0; x < MAP_SIZE; x++)
                map[y][x].setLighted(false);



        Writter w = new Writter(FileSystem.pathMaps+"\\"+fileName+".txt");
        w.Write(fileName, map, player, computer);
    }

    public List<Hero> getHeroes(){ return heroes; }
}
