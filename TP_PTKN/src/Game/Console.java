package Game;

import java.util.ArrayList;
import java.util.List;

public class Console {
    private static final List<String> events = new ArrayList<>();

    public static void addEvent(String event){
        events.add(event);
    }

    public static void PrintEvents(){
        for(int i=1;i<=events.size();i++){
            System.out.println(i + ") " + events.get(i-1));
        }
        events.clear();
    }

    public static void clean(){
        events.clear();
    }
    public static void clean(int ind){
        if(ind<0) {
            events.remove(events.size()+ind);
            return;
        }
        events.remove(ind);
    }
}