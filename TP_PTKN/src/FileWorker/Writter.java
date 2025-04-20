package FileWorker;

import Entities.PerkType;
import Entities.Unit;
import Game.Player;
import Game.Game;
import Map.*;
import Entities.Hero;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Writter {
    private String pathFile;
    private Boolean status;

    private final String SEP = ";";
    private final String END = "\n";

    public void setPath(String pathFile){ this.pathFile = pathFile; }
    public void setStatus(Boolean status){ this.status = status; }

    public Writter(){}
    public Writter(String path){
        setPath(path);
        clear();
        setStatus(true);
    }

    public void write(String content){
        try (FileWriter w = new FileWriter(pathFile, status)){
            w.write(content+END);
        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлом " + pathFile+" : "+e.getMessage());
        }
    }

    public void clear(){
        try (FileWriter w = new FileWriter(pathFile, false)){
            w.write("");
        } catch (IOException e) {
            System.err.println("Ошибка при работе с файлом " + pathFile+" : "+e.getMessage());
        }
    }

    public void write(Tile tile){
        String content = "";

        if(tile instanceof TerrainTile){
            content = "TerrainTile"+SEP+tile;
        }else if(tile instanceof TreasureTile){
            content = "TreasureTile"+SEP+tile+SEP+((TreasureTile) tile).getValue()+ SEP + ((TreasureTile) tile).isCollected();
        }else if(tile instanceof CastleTile){
            content = "CastleTile"+SEP+tile + SEP + ((CastleTile) tile).getOwner().getName();
        }else if(tile instanceof LightTile){
            try {
                content = "LightTile"+SEP+tile + SEP + ((LightTile) tile).getLighthouse().isLigthed();
            }catch(NullPointerException e){
                content = "LightTile"+SEP+tile+SEP+null;
            }

        } else {
            System.err.println("Неизвестный тип ячейки");
        }

        write(content);
    }

    public void write(Hero hero){
        String content = "";

        content = "Hero"+SEP+hero+SEP+hero.getIcon();

        write(content);
        for(Unit u : hero.getUnits())
            write(u);
    }

    public void write(Unit unit){
        String content = "";

        content = "Unit"+SEP+unit+SEP+unit.getType();

        write(content);
    }

    public void write(Player player){
        String content = "";

        content = "Player"+SEP+player+";";
        for(PerkType t : player.getAvailableUnits())
            content += t+"-";
        content = content.substring(0, content.length()-1);

        write(content);
        for(Hero h : player.getLeaders())
            write(h);
    }

    public void write(Tile[][] map){
        for(int y=0; y < Game.MAP_SIZE; y++)
            for(int x=0; x < Game.MAP_SIZE; x++)
                write(map[y][x]);
    }

    public void Write(String name, Tile[][] map, Player player, Player computer){
        write("Name"+SEP+name);
        write(map);
        write(player);
        write(computer);
    }
}
