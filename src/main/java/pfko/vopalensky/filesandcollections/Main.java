package pfko.vopalensky.filesandcollections;

import java.io.IOException;

public class Main {

    /**
     * Runs example of FileApp object.
     *
     * @param args None
     * @throws IOException On any problem when working with files
     */
    public static void main(String[] args) throws IOException {
        FileApp fa = new FileApp();
        fa.load("/pfko/vopalensky/filesandcollections/games.csv");
        fa.createGenreFile("genres.txt");
        fa.createSimulatorFile("simulator_games.csv");
        fa.createPublishersFile("game_publishers.csv");
    }
}
