package pfko.vopalensky.static_array;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StaticArrayTest {

    @Test
    void randomFillsArray() {
        assertTrue(StaticArray.createRandomArray().length > 0);
    }

    @Test
    void fillWithString() {
        assertArrayEquals(new int[]{1, -2, 3, 6},
                StaticArray.createDefinedArray("1,-2,3,6"));

    }

    @Test
    void addNumber() {
        int[] array = StaticArray.createDefinedArray("1, -2,   3, 6 ");
        array = StaticArray.add(array, 22);
        assertArrayEquals(new int[]{1, -2, 3, 6, 22}, array);
    }

    @Test
    void getMax() {
        int[] array = StaticArray.createDefinedArray("1, -2, 27,  3, 6 ");
        assertEquals(27, StaticArray.max(array));
    }

    @Test
    void getMin() {
        int[] array = StaticArray.createDefinedArray("1, -2, 27,  3, 6 ");
        assertEquals(-2, StaticArray.min(array));
    }

    @Test
    void remove() {
        int[] array = StaticArray.createDefinedArray("1, -2,  3, 6 ");
        array = StaticArray.remove(array, 3);
        assertArrayEquals(new int[]{1, -2, 6}, array);
    }

    @Test
    void clear() {
        int[] array = StaticArray.createDefinedArray("1, -2,  3, 6 ");
        array = StaticArray.clear(array);
        assertNull(array);
    }
}
