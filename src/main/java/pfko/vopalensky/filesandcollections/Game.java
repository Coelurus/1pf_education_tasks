package pfko.vopalensky.filesandcollections;

import java.util.List;

public record Game(String title, String released, List<String> developers,
                   List<String> publishers, List<String> genres) {
}
