package pfko.vopalensky.filesndcollections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pfko.vopalensky.filesandcollections.Game;
import pfko.vopalensky.filesandcollections.exceptions.EmptyFileException;
import pfko.vopalensky.filesandcollections.FileApp;
import pfko.vopalensky.filesandcollections.exceptions.InvalidFileFormatException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

class FileAppTest {

    @Test
    void testLength() throws IOException {
        FileApp fa = new FileApp();
        fa.load("/pfko/vopalensky/filesandcollections/happy.csv");
        Assertions.assertEquals(3, fa.getGames().size());
    }

    @Test
    void getGenres() throws IOException {
        FileApp fa = new FileApp();
        fa.load("/pfko/vopalensky/filesandcollections/happy.csv");

        Assertions.assertArrayEquals(
                new String[]{"Adventure", "Hack and slash/Beat em up", "Indie",
                        "Role-playing (RPG)", "Simulator", "Strategy",
                        "Tactical", "Turn-based strategy (TBS)",
                }, fa.getAllGenres().toArray());
    }

    @Test
    void getOrderedStrategies() throws IOException {
        FileApp fa = new FileApp();
        fa.load("/pfko/vopalensky/filesandcollections/happy.csv");
        Assertions.assertEquals(
                "Fallout Tactics: Brotherhood of Steel",
                fa.getGameByGenre("Strategy").get(0).getTitle());
        Assertions.assertEquals(
                "Mount & Blade: Warband",
                fa.getGameByGenre("Strategy").get(1).getTitle());

    }

    @Test
    void getPublishers() throws IOException {
        FileApp fa = new FileApp();
        fa.load("/pfko/vopalensky/filesandcollections/publishers.csv");
        List<Map.Entry<String, Integer>> publisherCounts =
                fa.getPublisherCounts();
        Assertions.assertEquals(5, publisherCounts.get(0).getValue());
        Assertions.assertEquals("Interplay Entertainment", publisherCounts.get(1).getKey());
    }

    @Test
    void nonexistentFile() {
        Assertions.assertThrows(FileNotFoundException.class, () -> {
            FileApp fa = new FileApp();
            fa.load("/pfko/vopalensky/filesandcollections/nonexistent.csv");
        });
    }

    @Test
    void emptyFile() {
        Assertions.assertThrows(EmptyFileException.class, () -> {
            FileApp fa = new FileApp();
            fa.load("/pfko/vopalensky/filesandcollections/empty.csv");
        });
    }

    @Test
    void invalidFile() {
        Assertions.assertThrows(InvalidFileFormatException.class, () -> {
            FileApp fa = new FileApp();
            fa.load("/pfko/vopalensky/filesandcollections/invalid.csv");
        });
    }

    @Test
    void fileWithOnlyHeaders() throws IOException {
        FileApp fa = new FileApp();
        fa.load("/pfko/vopalensky/filesandcollections/onlyheaders.csv");
        Assertions.assertEquals(0, fa.getGames().size());
    }

    @Test
    void fileWithTBA() throws IOException {
        FileApp fa = new FileApp();
        fa.load("/pfko/vopalensky/filesandcollections/TBA.csv");
        List<Game> simulators = fa.getSimulatorGames();
        Assertions.assertEquals(
                "Mount & Blade: Warband",
                simulators.get(0).getTitle());
        Assertions.assertEquals(
                "KnightOut",
                simulators.get(1).getTitle());
    }
}
