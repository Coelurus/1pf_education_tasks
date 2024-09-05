package pfko.vopalensky.static_array;

import java.io.PrintStream;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

/**
 * Static class for working with arrays.
 *
 * @author Filip Vopalensky
 * @version 1.0.0 02 September 2024
 */
public class StaticArray {
    private static final int MAX_RANDOM_ARRAY_LENGTH = 20;
    private static final int MAX_RANDOM_ARRAY_VALUE = 20;
    private static final SecureRandom random = new SecureRandom();
    private static final PrintStream out = System.out;
    private static final String PRINT_DELIMITER = ", ";
    public static final String AGREE_ON_MULTIPLE = "YES";
    public static final String NOT_AGREE_ON_MULTIPLE = "NO";

    private StaticArray() {
    }

    /**
     * Returns new randomly generated array of numbers.
     *
     * @return a pseudorandomly long array filled with pseudorandom {@code int} numbers
     */
    public static int[] createRandomArray() {
        int count = random.nextInt(1, MAX_RANDOM_ARRAY_LENGTH + 1);
        int[] array = new int[count];
        for (int i = 0; i < count; i++) {
            array[i] = random.nextInt(-MAX_RANDOM_ARRAY_VALUE,
                    MAX_RANDOM_ARRAY_VALUE + 1);
        }
        return array;
    }

    /**
     * Returns new array filled with numbers by user's choice.
     *
     * @param inputLine User input line defining numbers that should be
     *                  contained in array where numbers are separated by commas
     * @return array filled with user defined numbers
     */
    public static int[] createDefinedArray(String inputLine) {
        String[] numbers = inputLine.replaceAll("\\s", "").split(",");
        int[] data = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            data[i] = Integer.parseInt(numbers[i]);
        }
        return data;
    }

    /**
     * Prints out content of an array.
     *
     * @param array Array of numbers whose elements are to be printed out.
     */
    public static void print(int[] array) {
        for (int i = 0; i < array.length; i++) {
            out.print(array[i]);
            if (i != array.length - 1) {
                out.print(PRINT_DELIMITER);
            } else {
                out.println();
            }
        }
    }

    /**
     * Insert an additional element at the end of an array.
     *
     * @param originalArray Array to which the element should be added.
     * @param number        New number to be added into the array.
     * @return New array consisting of the original with new number at the end.
     */
    public static int[] add(int[] originalArray, int number) {
        int[] largerArray = new int[originalArray.length + 1];
        System.arraycopy(originalArray, 0, largerArray, 0, originalArray.length);
        largerArray[largerArray.length - 1] = number;
        return largerArray;
    }

    /**
     * Sets all elements in array to 0.
     *
     * @param array Array to be deleted.
     * @return Returns null to present deleted array.
     */
    public static int[] clear(int[] array) {
        Arrays.fill(array, 0);
        return null;
    }

    /**
     * Finds the largest number in an array.
     *
     * @param array Array to find the largest number in.
     * @return The largest number in array if array contains at least
     * one element
     * @throws ArrayIndexOutOfBoundsException when the array is empty
     */
    public static int max(int[] array) {
        if (array.length == 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int max = array[0];
        for (int number : array) {
            max = Math.max(number, max);
        }
        return max;
    }

    /**
     * Finds the smallest number in an array.
     *
     * @param array Array to find the smallest number in.
     * @return The smallest number in array if array contains at least
     * one element
     * @throws ArrayIndexOutOfBoundsException when the array is empty
     */
    public static int min(int[] array) {
        if (array.length == 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int min = array[0];
        for (int number : array) {
            min = Math.min(number, min);
        }
        return min;
    }

    /**
     * Removes just one or all occurrences of a number in an array based
     * on user choice
     *
     * @param array  Original array to delete numbers from.
     * @param number Number to be deleted
     * @return New array without deleted elements
     */
    public static int[] remove(int[] array, int number) {
        boolean firstDeleted = false;
        boolean deleteAll = false;
        for (int i = 0; i < array.length; i++) {
            while (i < array.length && array[i] == number) {
                if (!deleteAll && firstDeleted) {
                    out.println("Do you wish to delete all occurrences? ["
                            + AGREE_ON_MULTIPLE + "/"
                            + NOT_AGREE_ON_MULTIPLE + "]");
                    Scanner in = new Scanner(System.in);

                    if (Objects.equals(in.nextLine(), AGREE_ON_MULTIPLE)) {
                        deleteAll = true;
                    } else {
                        return array;
                    }
                }

                int[] shortArray = new int[array.length - 1];
                System.arraycopy(array, 0,
                        shortArray, 0, i);
                System.arraycopy(array, i + 1,
                        shortArray, i, array.length - i - 1);
                array = shortArray;

                firstDeleted = true;
            }
        }
        return array;
    }

    private static void exit() {
        System.exit(0);
    }

    /**
     * Gives user a choice to create an array.
     *
     * @return Newly created array
     */
    private static int[] createArrayUI() {
        out.println("""
                You have no elements in your array at the moment.\s
                So you have these options:
                    [1] Fill your array with random numbers.
                    [2] Define your own numbers to be added.""");

        Scanner in = new Scanner(System.in);
        String choice = in.nextLine();
        if (Objects.equals(choice, "2")) {
            out.println("Write contents of your array separated by commas.");
            return createDefinedArray(in.nextLine());
        } else {
            return createRandomArray();
        }
    }

    /**
     * Prints out all possible actions that user can carry out.
     */
    private static void printHelp() {
        out.println("""
                                
                You have following options:\s
                    [1] Print content of array
                    [2] Add number to array
                    [3] Find max number in array
                    [4] Find min number in array
                    [5] Remove number from array
                    [6] Delete whole array
                    [7] End program
                    """);
    }

    /**
     * Communicates with user about which number to add.
     *
     * @param array Original array to which new element will be added.
     * @return New array with added element.
     */
    private static int[] resolveAdd(int[] array) {
        Scanner in = new Scanner(System.in);
        out.println("Which number do you want to add?");
        array = add(array, in.nextInt());
        return array;
    }

    /**
     * Communicates with user about which number(s) to remove.
     *
     * @param array Original array from which chosen number will be removed.
     * @return New array without removed elements.
     */
    private static int[] resolveRemove(int[] array) {
        Scanner in = new Scanner(System.in);
        out.println("Which number do you want to remove?");
        array = remove(array, in.nextInt());
        return array;
    }

    /**
     * Resolves which action user has chosen.
     *
     * @param array Original state of array before action.
     * @return New state of array after being processed by action chosen
     * by user.
     */
    private static int[] resolveAction(int[] array) {
        Scanner in = new Scanner(System.in);
        switch (in.nextLine()) {
            case "1":
                print(array);
                break;
            case "2":
                array = resolveAdd(array);
                break;
            case "3":
                out.print("The largest number is ");
                out.println(max(array));
                break;
            case "4":
                out.print("The smallest number is ");
                out.println(min(array));
                break;
            case "5":
                array = resolveRemove(array);
                break;
            case "6":
                array = clear(array);
                break;
            case "7":
                exit();
                break;
            default:
                out.println("Invalid option...");
                break;
        }
        return array;
    }

    /**
     * Runs and operates the console UI communicating with user.
     */
    public static void run() {
        out.println("Welcome to this awesome array application!");
        out.println();

        int[] array = createArrayUI();
        while (true) {
            if (Objects.equals(array, null) || array.length == 0) {
                array = createArrayUI();
            } else {
                printHelp();
                array = resolveAction(array);
            }
        }
    }
}
