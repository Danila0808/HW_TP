package Game;

import Entities.Hero;
import Entities.Unit;

import java.util.List;
import java.util.Random;

public class Battle {
    int x, y;
    private Player player, computer;
    private Hero playerHero, computerHero;
    private int pointsPlayer, pointsComputer, points;
    private Random random;

    public Battle(int x, int y, Player p, Player c, Hero ph, Hero ch){
        this.x = x;
        this.y = y;
        this.player = p;
        this.computer = c;
        this.playerHero = ph;
        this.computerHero = ch;
        random = new Random();
    }

    private int sumPointsPlayer(){
        int answer = 0;
        List<Unit> army = player.getArmy();
        for(Unit u : army){
            answer+=u.getXp()+u.getDamage()+u.getAttackRange();
        }
        answer+=playerHero.getXp()+playerHero.getDamage()+playerHero.getAttackRange();

        return answer;
    }

    private int sumPointsComputer(){
        int answer = 0;
        List<Unit> army = computer.getArmy();
        for(Unit u : army){
            answer+=u.getXp()+u.getDamage()+u.getAttackRange();
        }
        answer+=computerHero.getXp()+computerHero.getDamage()+computerHero.getAttackRange();

        return answer;
    }

    boolean Fight(){
        pointsPlayer = sumPointsPlayer();
        pointsComputer = sumPointsComputer();
        points = pointsPlayer + pointsComputer;

        double chancePlayer = 100.0 * pointsPlayer/points;

        int event = random.nextInt(101);
        System.out.println("ШАНС ПОБЕДИТЬ ИГРОКА: " + chancePlayer+"%");
        if(event<chancePlayer){
            return true;
        }else{
            return false;
        }
    }
}
