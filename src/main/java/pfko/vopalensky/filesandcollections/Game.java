package pfko.vopalensky.filesandcollections;

import java.util.List;

public class Game {
    private final String title;
    private final String released;
    private final List<String> developers;
    private final List<String> publishers;
    private final List<String> genres;

    public Game(String title,
                String released,
                List<String> developers,
                List<String> publishers,
                List<String> genres) {
        this.title = title;
        this.released = released;
        this.developers = developers;
        this.publishers = publishers;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public String getReleased() {
        return released;
    }

    public List<String> getDevelopers() {
        return developers;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public List<String> getGenres() {
        return genres;
    }
}
