package utils;

import java.util.Scanner;

public class Interfaces {
    public static int createOptions(String title, String[] opts, String output){
        System.out.println("🔹 "+title);
        int i = 1;
        Scanner scanner = new Scanner(System.in);
        for(String opt : opts){
            System.out.println(i+") "+opt);
            i++;
        }
        System.out.println(output);

        String command = scanner.nextLine().trim();
        int ans;
        try{
            ans = Integer.parseInt(command);
        }catch (Exception e){
            System.out.println("❌ Неизвестная комманда!");
            return -1;
        }
        if(ans<1 || ans>opts.length){
            System.out.println("❌ Такого варианта нет!");
            return -1;
        }
        return ans;
    }
}
