package pfko.vopalensky.filesandcollections;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        FileApp fa = new FileApp();
        fa.load("x");
        fa.getPublisherCounts();
    }
}
