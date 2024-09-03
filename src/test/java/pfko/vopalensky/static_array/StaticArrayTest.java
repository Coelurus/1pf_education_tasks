package pfko.vopalensky.static_array;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void deleteAll() {
        int[] array = StaticArray.createDefinedArray("1");
        array = StaticArray.remove(array, 1);
        assertNotNull(array);
    }

    @Test
    void maxOnEmpty() {
        int[] array = StaticArray.remove(StaticArray.createDefinedArray("1"), 1);
        assertThrows(IndexOutOfBoundsException.class, () -> StaticArray.max(array));
    }

    @Test
    void minOnEmpty() {
        int[] array = StaticArray.remove(StaticArray.createDefinedArray("4"), 4);
        assertThrows(IndexOutOfBoundsException.class, () -> StaticArray.min(array));
    }

    @Test
    void removeNonExisting() {
        int[] array = StaticArray.createDefinedArray("1,2,4");
        array = StaticArray.remove(array, 3);
        assertArrayEquals(new int[]{1, 2, 4}, array);
    }

    void simulateInput(String line) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(line.getBytes());
        System.setIn(testIn);
    }

    @Test
    void removeMultiple() {
        simulateInput(StaticArray.AGREE_ON_MULTIPLE);
        int[] array = StaticArray.createDefinedArray("1,2,2,4");
        array = StaticArray.remove(array, 2);
        assertArrayEquals(new int[]{1, 4}, array);
    }

    @Test
    void doNotRemoveMultiple() {
        simulateInput(StaticArray.NOT_AGREE_ON_MULTIPLE);
        int[] array = StaticArray.createDefinedArray("1,2,2,4");
        array = StaticArray.remove(array, 2);
        assertArrayEquals(new int[]{1, 2, 4}, array);
    }
}
