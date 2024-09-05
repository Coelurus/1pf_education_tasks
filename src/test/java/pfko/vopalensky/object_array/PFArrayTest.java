package pfko.vopalensky.object_array;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
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
    void emptyLineGenerate() {
        assertThrows(NumberFormatException.class, () -> new PFArray(""));
    }

    @Test
    void invalidNumbersInLineGenerate() {
        assertThrows(NumberFormatException.class, () -> new PFArray("1,4,p,6"));
    }

    @Test
    void emptyNumbersInLineGenerate() {
        assertThrows(NumberFormatException.class, () -> new PFArray("1,,7,60"));
    }

    @Test
    void addNums() {
        PFArray pfa = new PFArray("1,5,6");
        pfa.add(7).add(-9).add(42);
        assertArrayEquals(new int[]{1, 5, 6, 7, -9, 42}, pfa.toArray());
    }

    @Test
    void addToDeleted() {
        PFArray pfa = new PFArray();
        assertThrows(NullPointerException.class, (() -> pfa.clear().add(4)));
    }

    @Test
    void removeNums() {
        PFArray pfa = new PFArray("1,5,6, 7 , -9, 42");
        pfa.remove(7, true).remove(-9, false).remove(1, true);
        assertArrayEquals(new int[]{5, 6, 42}, pfa.toArray());
    }

    @Test
    void removeFromDeleted() {
        PFArray pfa = new PFArray();
        PFArray finalPfa = pfa.clear();
        assertThrows(NullPointerException.class, () -> finalPfa.remove(9));
    }

    @Test
    void removeFromEmpty() {
        PFArray pfa = new PFArray();
        pfa.clear();
        assertDoesNotThrow(() -> {
            pfa.remove(4);
        });
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

    @Test
    void properExtend() throws NoSuchFieldException, IllegalAccessException {
        PFArray pfa = new PFArray("1,2,3");
        pfa.add(9);
        Field privateField = PFArray.class.getDeclaredField("length");
        privateField.setAccessible(true);
        int length = (int) privateField.get(pfa);
        assertEquals(6, length);
    }

    @Test
    void getMax() {
        PFArray pfa = new PFArray("1,6,7,-9,3");
        assertEquals(7, pfa.max());
    }

    @Test
    void getMin() {
        PFArray pfa = new PFArray("1,6,7,-9,3");
        assertEquals(-9, pfa.min());
    }

    @Test
    void minInEmpty() {
        PFArray pfa = new PFArray("8");
        pfa.remove(8, true);
        assertThrows(IndexOutOfBoundsException.class, pfa::min);
    }

    @Test
    void maxInEmpty() {
        PFArray pfa = new PFArray("99");
        pfa.remove(99);
        assertThrows(IndexOutOfBoundsException.class, pfa::max);
    }

    @Test
    void sum() {
        PFArray pfa = new PFArray("1,6,7,-9,3");
        assertEquals(8, pfa.sum());
    }

    @Test
    void sumOnEmpty() {
        PFArray pfa = new PFArray("3");
        pfa.remove(3);
        assertEquals(0, pfa.sum());
    }

    @Test
    void clearArray() {
        PFArray pfa = new PFArray("3,90,-9");
        assertNull(pfa.clear());
    }

    @Test
    void clearArrayIsEmpty() {
        PFArray pfa = new PFArray("3,90,-9");
        pfa.clear();
        assertEquals(0, pfa.size());
    }

    @Test
    void print() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        PFArray pfa = new PFArray("3,90,-9");
        pfa.print();

        assertEquals("", outContent.toString()
                .trim());

        System.setOut(originalOut);
    }
}

