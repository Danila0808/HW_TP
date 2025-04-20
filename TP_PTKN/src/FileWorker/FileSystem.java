package FileWorker;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileSystem {
    public static final String pathBase = System.getProperty("user.dir")+"\\src\\";
    public static final String pathSaves = pathBase + "Storage\\saves";
    public static final String pathMaps = pathBase + "Storage\\maps";
    public static final String pathRecord = pathBase + "Storage\\records\\main.txt";

    public static List<String> load(String p){
        List<String> result = new ArrayList<>();

        try (Stream<Path> paths = Files.list(Paths.get(p))) {
            paths.forEach(path -> result.add(String.valueOf(path)));
        } catch (Exception e) {
            System.out.println("Ошибка при чтении папки: " + e.getMessage());
        }

        return result;
    }

    public static List<String> loadSaves(){ return load(pathSaves); }
    public static List<String> loadMaps(){ return load(pathMaps); }

    public static String getFileName(String path){
        String[] arr = path.split("\\\\");
        return arr[arr.length-1];
    }

    public static void printNames(List<String> arr){
        int i = 1;
        for(String path : arr)
            System.out.println(i++ + ") " + getFileName(path));
    }

    public static void printNames(String choice){
        switch (choice){
            case "saves":
                printNames(loadSaves());
            case "maps":
                printNames(loadMaps());
        }
    }

    public static boolean MKdir(String path, String dirName){
        File dir = new File(path+"\\"+dirName);
        return dir.mkdir();
    }

    public static boolean delete(String path){
        File file = new File(path);
        try {
            file.delete();
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
