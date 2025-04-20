package FileWorker;
import Game.Player;
import Map.Tile;
import org.junit.jupiter.api.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileSystemTest {
    private static final String TEST_DIR = "test_dir";
    private static final String TEST_FILE = "test_file.txt";

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(Paths.get(TEST_DIR));
        Files.createFile(Paths.get(TEST_DIR, TEST_FILE));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.walk(Paths.get(TEST_DIR))
                .map(Path::toFile)
                .forEach(File::delete);
        Files.deleteIfExists(Paths.get(TEST_DIR));
    }

    @Test
    void load_ShouldReturnFileList_WhenDirectoryExists() {
        List<String> files = FileSystem.load(TEST_DIR);

        assertFalse(files.isEmpty());
        assertTrue(files.get(0).contains(TEST_FILE));
    }

    @Test
    void load_ShouldReturnEmptyList_WhenDirectoryDoesNotExist() {
        List<String> files = FileSystem.load("non_existent_dir");

        assertTrue(files.isEmpty());
    }

    @Test
    void getFileName_ShouldExtractFileNameFromPath() {
        String path = "C:\\folder\\subfolder\\file.txt";
        String fileName = FileSystem.getFileName(path);

        assertEquals("file.txt", fileName);
    }

    @Test
    void MKdir_ShouldCreateDirectory_IfNotExists() {
        String newDir = "new_dir";
        boolean result = FileSystem.MKdir(TEST_DIR, newDir);

        assertTrue(result);
        assertTrue(Files.exists(Paths.get(TEST_DIR, newDir)));
    }

    @Test
    void delete_ShouldRemoveFile_IfExists() throws IOException {
        Path filePath = Paths.get(TEST_DIR, "to_delete.txt");
        Files.createFile(filePath);

        boolean result = FileSystem.delete(filePath.toString());

        assertTrue(result);
        assertFalse(Files.exists(filePath));
    }
}