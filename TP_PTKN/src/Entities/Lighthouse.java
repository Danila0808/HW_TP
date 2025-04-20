package Entities;

import Game.Game;
import Map.MapRenderer;

import Game.Console;

public class Lighthouse {
    private int x, y;
    private boolean isLigthed;
    private MapRenderer map;

    public Lighthouse(int x, int y, MapRenderer map){
        this.x = x;
        this.y = y;
        this.isLigthed = false;
        this.map = map;
    }

    public int getX() { return x; }
    public boolean isLigthed() { return isLigthed; }
    public int getY() { return y; }

    public void work(boolean l){
        for(int y = 0; y < Game.MAP_SIZE; y++){
            for(int x = 0; x < Game.MAP_SIZE; x++){
                map.getMap()[y][x].setLighted(l);
            }
        }
        if(l)
            Console.addEvent("Маяк зажжен! Карта освещена!");
        else
            Console.addEvent("Ветер потушил маяк! Туман войны навеял карту!");
    }
}
