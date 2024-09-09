package pfko.vopalensky.filesandcollections;

import pfko.vopalensky.filesandcollections.exceptions.EmptyFileException;
import pfko.vopalensky.filesandcollections.exceptions.InvalidFileFormatException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
    private final List<Game> games = new ArrayList<>();
    private static final PrintStream out = System.out;

    // Found inspiration on StackOverflow
    // Refactored (changed * to {0,4}) myself
    // so SonarLint would stop screaming at me
    //
    // Explanatory notes for me
    // ,            ... comma
    // (?=...)      ... positive lookahead
    // [^"]*        ... matches anything that is not double quotes
    // "            ... double quote
    // [^"]*        ... anything that is not double quotes again
    // "            ... double quote
    // {0,5}        ... everything is repeated 0...5 times
    // [^"]*        ... anything that is not double quotes again
    // $            ... end of line
    //,(?=([^"]*"[^"]*"){0,5}[^"]*$)
    private static final int REGEX_THRESHOLD = 5;
    private static final String REGEX_FOR_COLUMN_SEPARATION =
            ",(?=([^\"]*\"[^\"]*\"){0," + REGEX_THRESHOLD + "}[^\"]*$)";
    private static final String DELIMITER = ",";
    private static final String TBA_TAG = "TBA";
    private static final String PROBLEM_ON_CREATING_OUTPUT_MESSAGE =
            "Problem appeared during creating file...";
    private static final String GAME_COUNT = "game_count";

    /**
     * Resolving location of columns inside the input file.
     */
    enum ColumnOrder {
        TITLE(0, "title"),
        RELEASED(1, "released"),
        DEVELOPERS(2, "developers"),
        PUBLISHERS(3, "publishers"),
        GENRES(4, "genres");

        private final int value;
        private final String header;

        ColumnOrder(final int value, final String header) {
            this.value = value;
            this.header = header;
        }

        public int getValue() {
            return value;
        }

        public String getHeader() {
            return header;
        }
    }


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
            return Stream.of(column
                    .replace("\"", "")
                    .split(DELIMITER)).map(String::strip).toList();
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
                csvLine.split(REGEX_FOR_COLUMN_SEPARATION);

        if (lineFragments.length != ColumnOrder.values().length) {
            throw new InvalidFileFormatException();
        }

        String title = lineFragments[ColumnOrder.TITLE.getValue()];
        String released = lineFragments[ColumnOrder.RELEASED.getValue()];
        List<String> developers =
                parseColumn(lineFragments[ColumnOrder.DEVELOPERS.getValue()]);
        List<String> publishers =
                parseColumn(lineFragments[ColumnOrder.PUBLISHERS.getValue()]);
        List<String> genres =
                parseColumn(lineFragments[ColumnOrder.GENRES.getValue()]);
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
            genres.addAll(game.genres());
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
                .filter(game -> game.genres().contains(genre))
                .sorted(Comparator.comparingInt(
                        a -> (Objects.equals(a.released(), TBA_TAG)
                                ? Integer.MAX_VALUE
                                : Integer.parseInt(a.released()))))
                .toList();
    }

    /**
     * Goes through all games saved in app and counts number of published games
     * for each publisher.
     *
     * @return List of entries where key is name of publisher and value
     * is number of published games. List is in descending order.
     */
    public List<Map.Entry<String, Integer>> getPublisherCounts() {
        Map<String, Integer> publisherCounts = new HashMap<>();
        for (Game game : games) {
            for (String publisher : game.publishers()) {
                publisherCounts.merge(publisher, 1, Integer::sum);
            }
        }

        List<Map.Entry<String, Integer>> list =
                new ArrayList<>(publisherCounts.entrySet());
        list.sort(Comparator.comparingInt(Map.Entry::getValue));
        return list.reversed();
    }

    /**
     * Creates file where is comma separated list of all genres from games.
     *
     * @param outputFilePath path to a file where genres should be written
     * @throws IOException When there is a problem with creating or writing
     *                     into new file.
     */
    public void createGenreFile(String outputFilePath) throws IOException {
        try (BufferedWriter bw
                     = new BufferedWriter(new FileWriter(outputFilePath))) {
            String delimiter = "";
            for (String genre : getAllGenres()) {
                bw.write(delimiter + genre);
                delimiter = DELIMITER;
            }
        } catch (IOException e) {
            out.println(PROBLEM_ON_CREATING_OUTPUT_MESSAGE);
            throw e;
        }
    }

    /**
     * Creates a csv file of simulator games with two columns - release, title
     *
     * @param outputFilePath path to a file where simulators should be written
     * @throws IOException When there is a problem with creating or writing
     *                     into new file.
     */
    public void createSimulatorFile(String outputFilePath) throws IOException {
        try (BufferedWriter bw
                     = new BufferedWriter(new FileWriter(outputFilePath))) {
            bw.write(ColumnOrder.RELEASED.getHeader());
            bw.write(DELIMITER);
            bw.write(ColumnOrder.TITLE.getHeader());
            bw.newLine();

            for (Game game : getSimulatorGames()) {
                bw.write(game.released() + DELIMITER + game.title());
                bw.newLine();
            }

        } catch (IOException e) {
            out.println(PROBLEM_ON_CREATING_OUTPUT_MESSAGE);
            throw e;
        }
    }

    /**
     * Creates a csv file of publishers with two columns - publisher name,
     * number of published games
     *
     * @param outputFilePath path to a file where publishers should be written
     * @throws IOException When there is a problem with creating or writing
     *                     into new file.
     */
    public void createPublishersFile(String outputFilePath) throws IOException {
        try (BufferedWriter bw
                     = new BufferedWriter(new FileWriter(outputFilePath))) {
            bw.write(ColumnOrder.PUBLISHERS.getHeader());
            bw.write(DELIMITER);
            bw.write(GAME_COUNT);
            bw.newLine();
            for (Map.Entry<String, Integer> publisher : getPublisherCounts()) {
                bw.write(publisher.getKey());
                bw.write(DELIMITER);
                bw.write(publisher.getValue().toString());
                bw.newLine();
            }

        } catch (IOException e) {
            out.println(PROBLEM_ON_CREATING_OUTPUT_MESSAGE);
            throw e;
        }
    }
}

