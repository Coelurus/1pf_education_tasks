package pfko.vopalensky.static_array;

/**
 * Static class for working with arrays.
 *
 * @author Filip Vopalensky
 * @version 1.0.0 02 September 2024
 */
public class StaticArray {
    private static final int MAX_RANDOM_ARRAY_LENGTH = 20;
    private static final int MAX_RANDOM_ARRAY_VALUE = 20;

    private static int getRandomArrayValue() {
        return (int) ((Math.random() - 0.5) * 2 * MAX_RANDOM_ARRAY_VALUE);
    }

    public static int[] createRandomArray() {
        int array_length = (int) (Math.random() * MAX_RANDOM_ARRAY_LENGTH + 1);
        int[] array = new int[array_length];
        for (int i = 0; i < array_length; i++) {
            array[i] = getRandomArrayValue();
        }
        return array;
    }

    public static int[] createDefinedArray(String inputLine) {
        String[] numbers = inputLine.replaceAll("\\s", "").split(",");
        int[] array = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            array[i] = Integer.parseInt(numbers[i]);
        }
        return array;
    }

    public static void print(int[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i != array.length - 1) {
                System.out.print(", ");
            } else {
                System.out.println();
            }
        }
    }
}
