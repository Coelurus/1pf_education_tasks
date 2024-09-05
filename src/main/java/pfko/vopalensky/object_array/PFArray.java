package pfko.vopalensky.object_array;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Random;


public class PFArray {
    private static final int MAX_RANDOM_ARRAY_LENGTH = 20;
    private static final int MAX_RANDOM_ARRAY_VALUE = 20;
    private static final Random random = new Random();
    private static final String DELIMITER = ", ";
    private static final PrintStream out = System.out;
    private static final int EXTEND_COEFFICIENT = 2;

    /**
     * Array of numbers stored by user
     */
    private int[] data;
    /**
     * Current size of inner array
     */
    private int length;
    /**
     * Count of stored numbers
     */
    private int count;

    /**
     * Generates random number in interval of {@code -MAX_RANDOM_ARRAY_VALUE}
     * and {@code MAX_RANDOM_ARRAY_VALUE}
     *
     * @return random number
     */
    private int generateRandomNumber() {
        return random.nextInt(-MAX_RANDOM_ARRAY_VALUE,
                MAX_RANDOM_ARRAY_VALUE + 1);
    }

    /**
     * Randomly generates a new array.
     */
    public PFArray() {
        count = random.nextInt(1, MAX_RANDOM_ARRAY_LENGTH + 1);
        length = count;
        data = new int[count];
        for (int i = 0; i < count; i++) {
            data[i] = generateRandomNumber();
        }
    }

    /**
     * Creates new array based on user input
     *
     * @param input line of numbers divided by commas to be saved in array
     */
    public PFArray(String input) {
        String[] numbers = input.replaceAll("\\s", "").split(",");
        count = numbers.length;
        length = count;
        data = new int[length];
        for (int i = 0; i < count; i++) {
            data[i] = Integer.parseInt(numbers[i]);
        }
    }

    /**
     * Prints out elements of array.
     */
    public void print() {
        for (int i = 0; i < count; i++) {
            out.print(data[i]);
            if (i != count - 1) {
                out.print(DELIMITER);
            } else {
                out.println();
            }
        }
    }

    /**
     * Enlarge the array where numbers are saved so more numbers can fit in.
     */
    private void extend() {
        length *= EXTEND_COEFFICIENT;
        data = Arrays.copyOf(data, length);
    }


    /**
     * Add new number to the array
     *
     * @param number New number to be added at the end of array.
     * @return reference to this array
     */
    public PFArray add(int number) {
        if (count == length) {
            extend();
        }
        data[count] = number;
        count++;
        return this;
    }

    /**
     * Remove number(s) from array. Removes only one or all instances.
     *
     * @param number Number to be removed from an array.
     * @return reference to this array
     */
    public PFArray remove(int number, boolean removeAll) {
        for (int i = 0; i < count; i++) {
            while (i < count && data[i] == number) {
                System.arraycopy(data, i + 1,
                        data, i, data.length - i - 1);
                count--;
                if (!removeAll) {
                    return this;
                }
            }
        }
        return this;
    }

    /**
     * Finds the largest number in array.
     *
     * @return the largest number if array contains at least one element
     * otherwise {@code Exception} is thrown
     * @throws IndexOutOfBoundsException when there is no element in array
     */
    public int max() {
        if (count == 0) {
            throw new IndexOutOfBoundsException();
        }
        int max = data[0];
        for (int i = 0; i < count; i++) {
            max = Math.max(max, data[i]);
        }
        return max;
    }

    /**
     * Finds the largest number in array.
     *
     * @return the largest number if array contains at least one element
     * otherwise {@code Exception} is thrown
     * @throws IndexOutOfBoundsException when there is no element in array
     */
    public int min() {
        if (count == 0) {
            throw new IndexOutOfBoundsException();
        }
        int min = data[0];
        for (int i = 0; i < count; i++) {
            min = Math.min(min, data[i]);
        }
        return min;
    }

    /**
     * Counts sum of all numbers in array
     *
     * @return sum of all numbers in array
     */
    public int sum() {
        int sum = 0;
        for (int i = 0; i < count; i++) {
            sum += data[i];
        }
        return sum;
    }

    /**
     * Deletes whole array and all elements in it.
     *
     * @return reference to this array which is deleted. Thus, it is null.
     */
    public PFArray clear() {
        count = 0;
        return null;
    }

    /**
     * Changes all numbers in array to a random different number.
     *
     * @return reference to this array
     */
    public PFArray regenerate() {
        for (int i = 0; i < count; i++) {
            data[i] = generateRandomNumber();
        }
        return this;
    }

    /**
     * Returns number of elements inside of array.
     *
     * @return count of numbers in array
     */
    public int size() {
        return count;
    }

    /**
     * Returns element from array at given index
     *
     * @param idx of element to return
     * @return element at given index
     * @throws IndexOutOfBoundsException when {@code idx} is larger than count
     */
    public int at(int idx) {
        if (idx >= count) {
            throw new IndexOutOfBoundsException();
        }
        return data[idx];
    }

    /**
     * Returns array of numbers filled with numbers stored in PFArray object
     *
     * @return classic Java array of numbers
     */
    public int[] toArray() {
        return Arrays.copyOf(data, count);
    }

    /**
     * Starts and controls the communication between program and user.
     */
    public static void run() {
        PFArray pfa = new PFArray("-33, 1,5,7, 33");
        pfa.add(8);
        pfa.add(7).add(8).remove(7, true);
        pfa.remove(8, true);
        pfa.print();
        pfa.regenerate().print();

    }
}
