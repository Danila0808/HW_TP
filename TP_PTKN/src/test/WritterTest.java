import Entities.*;
import FileWorker.Writter;
import Game.Player;
import Map.*;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class WriterTest {
    private static final String TEST_FILE = "test_output.txt";
    private Writter writer;

    @BeforeEach
    void setUp() {
        writer = new Writter(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
    }

    @Test
    void write_ShouldSaveTerrainTile() throws IOException {
        writer.write(new TerrainTile(1, 2, TerrainType.GRASS));
        String content = Files.readString(Path.of(TEST_FILE));
        assertTrue(content.contains("TerrainTile;1;2;GRASS;false"));
    }

    @Test
    void write_ShouldSaveCastleTile() throws IOException {
        Player player = new Player("Player", 1000, 50);
        writer.write(new CastleTile(3, 4, "Player", player));
        String content = Files.readString(Path.of(TEST_FILE));
        assertTrue(content.contains("CastleTile;3;4;GRASS;false;Player"));
    }

    @Test
    void clear_ShouldDeleteContent() throws IOException {
        writer.write("Test");
        writer.clear();
        assertEquals("", Files.readString(Path.of(TEST_FILE)));
    }

    @Test
    void write_ShouldHandleNullTile() throws IOException {
        writer.write((Tile) null);
        assertEquals("\n", Files.readString(Path.of(TEST_FILE)));  // Ничего не записалось
    }
}

