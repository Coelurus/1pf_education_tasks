import org.junit.jupiter.api.Test;
import pfko.vopalensky.static_array.StaticArray;

import static org.junit.jupiter.api.Assertions.*;

public class StaticArrayTest {

    @Test
    public void randomFillsArray() {
        assertTrue(StaticArray.createRandomArray().length > 0);
    }

    @Test
    public void fillWithString() {
        assertArrayEquals(StaticArray.createDefinedArray(
                "1,-2,3,6"), new int[]{1, -2, 3, 6});

    }
}
