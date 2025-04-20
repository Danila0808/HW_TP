package Map;

import Entities.Lighthouse;
import Game.Player;
import Game.Game;

import java.util.Random;

public class MapGenerator {
    private static int MAP_SIZE = 10;
    public static final int TREASURE_COUNT = 3;
    private LightTile lightTile;
    private static final TerrainType[] TERRAIN_TYPES = {
            TerrainType.GRASS, TerrainType.FOREST, TerrainType.MOUNTAIN, TerrainType.WATER
    };

    private Player player1, player2;
    private Tile[][] map;
    private Random random;

    public MapGenerator(Player p1, Player p2, int MAP_SIZE){
        this.MAP_SIZE = MAP_SIZE;
        this.map = new Tile[MAP_SIZE][MAP_SIZE];
        this.random = new Random();
        this.player1 = p1;
        this.player2 = p2;

        generateMap();
    }

    public Tile[][] getMap() { return map; }
    public LightTile getLight(){ return lightTile; }

    private void placeCastles(){
        map[0][0] = new CastleTile(0, 0, player1.getName(), player1);
        map[MAP_SIZE - 1][MAP_SIZE - 1] = new CastleTile(MAP_SIZE - 1, MAP_SIZE - 1, player2.getName(), player2);
    }

    private void createRoadBetweenCastles() {
        for (int i = 0; i < MAP_SIZE; i++) {
            map[i][i] = new TerrainTile(i, i, TerrainType.ROAD);
        }
    }

    private void placeTreasures() {
        int placed = 0;
        while (placed < TREASURE_COUNT) {
            int x = random.nextInt(MAP_SIZE);
            int y = random.nextInt(MAP_SIZE);

            if (map[y][x] instanceof TerrainTile) {
                map[y][x] = new TreasureTile(x, y, random.nextInt(10000));
                placed++;
            }
        }
    }

    private void placeLighthouse(){
        int x = random.nextInt(MAP_SIZE);
        int y = random.nextInt(MAP_SIZE);
        while (x!=0 && y!=0 && x!=Game.MAP_SIZE && y!=Game.MAP_SIZE){
            x = random.nextInt(MAP_SIZE);
            y = random.nextInt(MAP_SIZE);
        }

        this.lightTile = new LightTile(x, y, TerrainType.GRASS, null);
        map[y][x] = this.lightTile;
    }

    private void generateMap(){
        for(int y = 0; y < MAP_SIZE; y++){
            for(int x = 0; x < MAP_SIZE; x++){
                TerrainType randomTerrain = TERRAIN_TYPES[random.nextInt(TERRAIN_TYPES.length)];
                map[y][x] = new TerrainTile(x, y, randomTerrain);
            }
        }

        createRoadBetweenCastles();
        placeTreasures();
        placeCastles();
        placeLighthouse();
    }
}
