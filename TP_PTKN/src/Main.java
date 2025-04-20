import FileWorker.FileSystem;
import Game.GameExitException;
import Game.Launcher;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try {
            Launcher.Menu();
        }catch (GameExitException _){}
    }
}