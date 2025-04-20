package PvP;

import Entities.Hero;
import Entities.Tomb;
import Entities.Unit;

import java.util.ArrayList;
import java.util.List;

public class pvpMapRender {
    public static final int SIZE = 10;
    private Unit[][] field = new Unit[SIZE][SIZE];
    private Hero playerHero, computerHero;
    private List<Tomb> tombs = new ArrayList<>();

    public pvpMapRender(Hero playerHero, Hero computerHero) {
        this.playerHero = playerHero;
        this.computerHero = computerHero;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                field[i][j] = null;
            }
        }
    }

    public Unit getField(int x, int y){ return field[y][x]; }

    public void setField(Unit unit, int x, int y){
        unit.setX(x);
        unit.setY(y);
    }

    public void addTomb(int x, int y){
        Tomb t = new Tomb(x, y);
        tombs.add(t);
    }

    public boolean isTomb(int x, int y){
        for(Tomb t : tombs){
            if(x == t.getX() && y == t.getY()){
                return true;
            }
        }
        return false;
    }

    public void placeUnits(){
        for(Unit u : playerHero.getUnits()){
            field[u.getY()][u.getX()] = u;
        }
        for(Unit u : computerHero.getUnits()){
            field[u.getY()][u.getX()] = u;
        }
    }

    public void placePlayerUnit(Unit unit, int x, int y) {
        if (x >= 0 && x < 3 && y >= 0 && y < SIZE) {
            setField(unit, x, y);
        } else {
            System.out.println("❌ Нельзя разместить юнита за пределами своей зоны!");
        }
    }

    public void moveUnit(Unit unit, int newX, int newY){
        field[unit.getY()][unit.getX()] = null;
        setField(unit, newX, newY);
    }

    public void render() {
        placeUnits();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (isTomb(j, i)){
                    System.out.print("❌");
                } else if (field[i][j] != null) {
                    System.out.print(getSymbol(field[i][j]));
                } else if (j == 2 || j==7) {
                    System.out.print("\uD83E\uDDF1");
                } else {
                    System.out.print("\uD83C\uDF3F");
                }
            }
            System.out.println();
        }
    }

    private String getSymbol(Unit unit){
        return switch (unit.getType()){
            case SPEARMAN ->  "\uD83D\uDEE1\uFE0F";
            case CROSSBOMEN -> "🏹";
            case SWORDSMAN -> "⚔\uFE0F";
            case CAVALRY -> "\uD83D\uDC0E";
            case PALADIN -> "\uD83E\uDDDD\u200D♀\uFE0F";
            case HERO -> "";
        };
    }
}
