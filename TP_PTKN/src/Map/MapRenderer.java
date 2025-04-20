package Map;

import Entities.Hero;
import Game.Game;
import Game.Customise;

import java.util.List;

public class MapRenderer {
    private Tile[][] map;
    private List<Hero> heroes;

    public MapRenderer(Tile[][] map, List<Hero> heroes) {
        this.map = map;
        this.heroes = heroes;
    }

    public Tile[][] getMap() { return map; }
    public void setHeroes(List<Hero> heroes) { this.heroes = heroes; }
    public void setHero(){}

    private void lightObj(Tile obj){
        int posX = obj.getX();
        int posY = obj.getY();

        for(int y=posY-Game.rangeObserved; y<=posY+Game.rangeObserved; y++){
            for(int x=posX-Game.rangeObserved; x<=posX+Game.rangeObserved; x++){
                if(x>=0 && y>=0 && x<Game.MAP_SIZE && y<Game.MAP_SIZE){
                    map[y][x].setLighted(true);
                }
            }
        }
    }

    public void render(){
        lightObj(map[0][0]);
        for(Hero hero : heroes){
            if(hero.getOwner().getName() == "Игрок"){
                lightObj(map[hero.getY()][hero.getX()]);
            }
        }

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                System.out.print(getTileSymbol(x, y) + " ");
            }
            System.out.println();
        }
    }

    private String getTileSymbol(int x, int y) {
        Tile tile = map[y][x];

        if(!tile.isLighted()) { return "❓"; }

        if (tile instanceof CastleTile) return "🏰";

        for (Hero hero : heroes) {
            if (hero.getX() == x && hero.getY() == y) {
                return Customise.getCode(hero.getIcon());
            }
        }

        if (tile instanceof TreasureTile){
            if(((TreasureTile) tile).isCollected())
                return "🌿";
            else
                return "💰";
        }

        if (tile instanceof TerrainTile) {
            TerrainTile terrain = (TerrainTile) tile;
            return switch (terrain.getType()) {
                case GRASS -> "🌿";
                case FOREST -> "🌲";
                case MOUNTAIN -> "⛰️";
                case WATER -> "🌊";
                case ROAD -> "🛤️";
            };
        }

        if (tile instanceof LightTile){
            return "\uD83D\uDC88";
        }

        return "❓";
    }
}
