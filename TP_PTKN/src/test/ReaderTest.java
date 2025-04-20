import Entities.*;
import Game.Player;
import Map.*;
import FileWorker.Reader;

import org.junit.jupiter.api.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {
    private static final String TEST_FILE = "test_save.txt";
    private static final String EMPTY_FILE = "empty_file.txt";
    private static final String INVALID_FILE = "invalid_data.txt";

    @BeforeEach
    void setUp() throws IOException {
        // 1. Корректный файл
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEST_FILE))) {
            writer.write("Name;TestMap\n");
            writer.write("TerrainTile;1;2;GRASS;false\n");
            writer.write("CastleTile;3;4;GRASS;false;Игрок\n");
            writer.write("Hero;5;6;100;10;2;1;Игрок;3000;WIZARD\n");
        }

        // 2. Пустой файл
        new File(EMPTY_FILE).createNewFile();

        // 3. Файл с битыми данными
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INVALID_FILE))) {
            writer.write("TerrainTile;1;2;UNKNOWN_TYPE;false\n");  // Несуществующий TerrainType
            writer.write("Hero;X;Y;100;10;2;1;Player;ARCHER\n");   // X и Y - не числа
        }
    }

    @AfterEach
    void tearDown() {
        new File(TEST_FILE).delete();
        new File(EMPTY_FILE).delete();
        new File(INVALID_FILE).delete();
    }

    @Test
    void recognize_ShouldParseMapName() throws IOException {
        Reader reader = new Reader(TEST_FILE);
        reader.recognize();
        assertEquals("TestMap", reader.getName());
    }

    @Test
    void recognize_ShouldParseHero() throws IOException {
        Reader reader = new Reader(TEST_FILE);
        reader.recognize();
        assertEquals(1, reader.getHeroes().size());
        assertEquals(100, reader.getHeroes().get(0).getXp());
    }

    @Test
    void recognize_ShouldHandleEmptyFile() throws IOException {
        Reader reader = new Reader(EMPTY_FILE);
        reader.recognize();
        assertNull(reader.getName());
        assertTrue(reader.getHeroes().isEmpty());
    }

    @Test
    void recognize_ShouldSkipInvalidLines() throws IOException {
        Reader reader = new Reader(INVALID_FILE);
        reader.recognize();
        assertNull(reader.getMap()[2][1].getType());
        assertTrue(reader.getHeroes().isEmpty());
    }

    // --- Несуществующий файл ---
    @Test
    void constructor_ShouldSetNullReader_IfFileNotFound() {
        Reader reader = new Reader("non_existent_file.txt");
        assertThrows(NullPointerException.class, reader::recognize);
    }
}
