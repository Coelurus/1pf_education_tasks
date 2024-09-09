package pfko.vopalensky.filesandcollections;

import pfko.vopalensky.filesandcollections.exceptions.EmptyFileException;
import pfko.vopalensky.filesandcollections.exceptions.InvalidFileFormatException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class FileApp {
    private static final String FILE_PATH =
            "/pfko/vopalensky/filesandcollections/games.csv";
    public static final int COLUMN_COUNT = 5;
    public static final int TITLE_IDX = 0;
    public static final int RELEASED_IDX = 1;
    public static final int DEVELOPERS_IDX = 2;
    public static final int PUBLISHERS_IDX = 3;
    public static final int GENRES_IDX = 4;
    private final List<Game> games = new ArrayList<>();
    private static final PrintStream out = System.out;


    /**
     * Loads data about games from provided file into the FileApp object.
     *
     * @param pathToFile Relative path in resources to file where data
     *                   about games are stored.
     * @throws IOException Exception is thrown when there is a problem
     *                     with input file such as nonexistence, invalid format,
     *                     emptiness.
     */
    public void load(String pathToFile) throws IOException {
        parseInputFile(pathToFile);
    }


    public List<Game> getGames() {
        return games;
    }

    /**
     * Check whether single column contains multiple information
     *
     * @param column Single column from input file
     * @return List of all data inputs from this column.
     */
    private List<String> parseColumn(String column) {
        if (column.isEmpty()) {
            return new ArrayList<>();
        } else if (column.charAt(0) == '"') {
            return Stream.of(column.substring(1, column.length() - 1)
                    .replace("\"", "")
                    .split(",")).map(String::strip).toList();
        } else {
            List<String> columnData = new ArrayList<>();
            columnData.add(column);
            return columnData;
        }
    }

    /**
     * Parses line from input csv file into Game.
     *
     * @param csvLine input line from csv file.
     * @return new Game object
     */
    private Game parseLineToGame(String csvLine) throws InvalidFileFormatException {
        String[] lineFragments =
                csvLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        if (lineFragments.length != COLUMN_COUNT) {
            throw new InvalidFileFormatException();
        }

        String title = lineFragments[TITLE_IDX];
        String released = lineFragments[RELEASED_IDX];
        List<String> developers = parseColumn(lineFragments[DEVELOPERS_IDX]);
        List<String> publishers = parseColumn(lineFragments[PUBLISHERS_IDX]);
        List<String> genres = parseColumn(lineFragments[GENRES_IDX]);
        return new Game(title, released, developers, publishers, genres);
    }

    /**
     * Goes through a file and transforms all lines into Game objects.
     *
     * @param inputFile Path to a file in Resources from where to read.
     */
    private void parseInputFile(String inputFile) throws IOException {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        this.getClass().getResourceAsStream(inputFile))))
        ) {
            String line;

            // Skip headers line
            if ((line = br.readLine()) == null || line.isEmpty()) {
                throw new EmptyFileException();
            }
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    games.add(parseLineToGame(line));
                }
            }
        } catch (EmptyFileException e) {
            out.println("File is empty!");
            throw e;
        } catch (InvalidFileFormatException e) {
            out.println("Invalid file format!");
            throw e;
        } catch (IOException e) {
            out.println("IO exception during file handling");
            throw e;
        } catch (NullPointerException e) {
            out.println("Problem with opening file occurred");
            throw new FileNotFoundException();
        }
    }

    /**
     * Goes through all saved games and returns List of all genres.
     *
     * @return List of unique genres from all games alphabetically ordered.
     */
    public List<String> getAllGenres() {
        Set<String> genres = new HashSet<>();
        for (Game game : games) {
            genres.addAll(game.getGenres());
        }
        List<String> orderedList = new ArrayList<>(genres);
        Collections.sort(orderedList);
        return orderedList;
    }

    /**
     * Get all games that has genre simulator.
     *
     * @return List of simulator games ordered by release.
     */
    public List<Game> getSimulatorGames() {
        return getGameByGenre("Simulator");
    }

    /**
     * Get all games that are in a concrete genre
     *
     * @param genre Genre the games should be in
     * @return List of games with deemed genre ordered by release date.
     */
    public List<Game> getGameByGenre(String genre) {
        return Stream.of(games.toArray(new Game[0]))
                .filter(game -> game.getGenres().contains(genre))
                .sorted(Comparator.comparingInt(
                        a -> (Objects.equals(a.getReleased(), "TBA")
                                ? Integer.MAX_VALUE
                                : Integer.parseInt(a.getReleased()))))
                .toList();
    }

    public List<Map.Entry<String, Integer>> getPublisherCounts() {
        Map<String, Integer> publisherCounts = new HashMap<>();
        for (Game game : games) {
            for (String publisher : game.getPublishers()) {
                publisherCounts.merge(publisher, 1, Integer::sum);
            }
        }

        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(publisherCounts.entrySet());
        list.sort(Comparator.comparingInt(Map.Entry::getValue));
        return list.reversed();
    }
}
