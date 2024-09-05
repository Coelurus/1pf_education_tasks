package pfko.vopalensky.static_array;

import org.junit.jupiter.api.Test;
import pfko.vopalensky.object_array.PFArray;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PFArrayTest {

    @Test
    void randomGenerate() {
        assertTrue(new PFArray().size() > 0);
    }

    @Test
    void lineGenerate() {
        PFArray pfa = new PFArray("1,5,6");
        assertArrayEquals(new int[]{1, 5, 6}, pfa.toArray());
    }

    @Test
    void addNums() {
        PFArray pfa = new PFArray("1,5,6");
        pfa.add(7).add(-9).add(42);
        assertArrayEquals(new int[]{1, 5, 6, 7, -9, 42}, pfa.toArray());
    }

    @Test
    void removeNums() {
        PFArray pfa = new PFArray("1,5,6, 7 , -9, 42");
        pfa.remove(7, true).remove(-9, false).remove(1, true);
        assertArrayEquals(new int[]{5, 6, 42}, pfa.toArray());
    }

    @Test
    void regenerate() {
        PFArray pfa = new PFArray("1,1,1,1,1,1,1,1,1,6,1,1,1,1,1,5,1,1,1,1,1,3,1,1");
        PFArray pfaExpected = new PFArray("1,1,1,1,1,1,1,1,1,6,1,1,1,1,1,5,1,1,1,1,1,3,1,1");
        pfa.regenerate();
        assertEquals(pfa.size(), pfaExpected.size());

        boolean difference = false;
        for (int i = 0; i < pfa.size(); i++) {
            if (!Objects.equals(pfa.at(i), pfaExpected.at(i))) {
                difference = true;
            }
        }
        assertTrue(difference);
    }

}

