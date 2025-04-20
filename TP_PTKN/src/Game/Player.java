package Game;

import Entities.Castle;
import Entities.Hero;
import Entities.PerkType;
import Entities.Unit;
import Map.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player {
    private String name;
    private int gold;
    private int goldIncome;
    private List<PerkType> availableUnits;
    private List<Unit> army;
    private List<Hero> leaders;
    private List<CommandHandler> control;
    private int maxArmySize = 10;

    public Player(String name, int gold, int goldIncome){
        this.name = name;
        this.gold = gold;
        this.goldIncome = goldIncome;

        this.availableUnits = new ArrayList<>();
        this.army = new ArrayList<>();
        this.leaders = new ArrayList<>();
        this.control = new ArrayList<>();
    }

    public String getName() { return name; }
    public int getGold() { return gold; }
    public int getGoldIncome() { return goldIncome; }
    public List<PerkType> getAvailableUnits() { return availableUnits; }
    public List<Unit> getArmy() { return army; }
    public List<Hero> getLeaders() { return leaders; }
    public List<CommandHandler> getControl() { return control; }

    public int getMaxArmySize() { return maxArmySize; }

    public void setMaxArmySize(int size) { this.maxArmySize = size; }
    public void setArmy(List<Unit> army) { this.army = army; }
    public void killHero(Hero hero){ leaders.remove(hero); }

    public void addHero(Hero hero) { this.leaders.add(hero); }
    public void addHandler(CommandHandler commandHandler) { this.control.add(commandHandler); }
    public void addUnit(Unit u) { this.army.add(u); }

    public boolean spendGold(int amount){
        if(gold > amount){
            gold-=amount;
            return true;
        }else{
            return false;
        }
    }

    public void addGold(int amount){ gold+=amount; }
    public void addGoldIncome(int amount) { goldIncome+=amount; }
    public void addUnitType(PerkType unit) { availableUnits.add(unit); }

    public boolean canRecruitUnit(PerkType unitType) { return availableUnits.contains(unitType); }
    public boolean recruitUnit(Unit unit) {
        if (army.size() < maxArmySize && canRecruitUnit(unit.getType()) && unit.getCost() < gold) {
            gold-=unit.getCost();
            army.add(unit);
            return true;
        }
        return false;
    }

    public boolean recruitHero(Castle castle, Tile[][] map){
        Hero hero = new Hero(0, 0, 250, 101, 5, 1, map, this, new ArrayList<>());
        if(canRecruitUnit(PerkType.HERO) && gold > hero.getCost()){ //COST OF HERO
            gold-=hero.getCost();
            leaders.add(hero);

            CommandHandler newCommandHandler = new CommandHandler(hero, this, castle, new Scanner(System.in), map);
            control.add(newCommandHandler);

            return true;
        }
        hero = null;
        return false;
    }

    public void showArmy(){
        if(army.size()==0) {
            Console.addEvent("❌ У вас нет армии!");
            return;
        }
        for(Hero h : leaders){
            Console.addEvent("\r\nHERO "+h.getIcon()+": ["+h.getX()+"; "+h.getY()+"]");
            for(Unit u : h.getUnits())
                Console.addEvent(u.getType()+ " XP: " + u.getXp());
        }
    }

    @Override
    public String toString() {
        return name+";"+gold+";"+goldIncome;
    }
}

//Армия, население, расходы