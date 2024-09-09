package pfko.vopalensky.objectarray;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Takes care of communicating with user and let him work with arrays.
 */
public class ArrayApp {
    private static final List<PFArray> arrayList = new ArrayList<>();
    private static final PrintStream out = System.out;
    private static PFArray currentArray;

    private ArrayApp() {
    }

    /**
     * Prints out information about possible commands.
     */
    private static void printHelp() {
        out.println("""
                                
                You have following options:
                \t[0] End program
                \t[1] Create new array""");

        if (!arrayList.isEmpty()) {
            out.println("""
                    \t[2] Choose existing array
                    \t[3] Switch to array with max sum
                    \t[4] Switch to array with min sum""");
        }

        if (Objects.isNull(currentArray)) {
            out.println("You do not have any chosen array at the moment.");
        } else {
            out.print("""
                    Or you can work with current array:
                    \t[a] Add number
                    \t[b] Remove first chosen number
                    \t[c] Remove all chosen numbers
                    \t[d] Regenerate
                    \t[e] Delete array
                    And your current array is:
                        """);
            currentArray.print();
        }
        out.print("\nWhat do you do: ");
    }

    /**
     * Generates new array either randomly or manually based on users choice.
     *
     * @return newly created array or null if user passes invalid input
     */
    private static PFArray createNewArray() {
        out.print("""
                You have these options:
                    [1] Fill your array with random numbers.
                    [2] Define your own numbers to be added.
                Which do you choose:\s""");

        Scanner in = new Scanner(System.in);
        String choice = in.nextLine();
        PFArray newArray;
        if (Objects.equals(choice, "2")) {
            out.println("Write contents of your array separated by commas.");
            try {
                newArray = new PFArray(in.nextLine());
            } catch (Exception ignored) {
                out.println("Invalid input!");
                newArray = null;
            }

        } else {
            newArray = new PFArray();
        }
        return newArray;
    }

    /**
     * Prints out information about current state of arrays.
     */
    private static void printArrayList() {
        out.println("There are following arrays: ");
        for (int i = 0; i < arrayList.size(); i++) {
            out.print("\t[" + (i + 1) + "] ");
            arrayList.get(i).print();
        }
        out.print("Which do you want to choose: ");
    }

    /**
     * Lets user choose array that will be set as a current one.
     *
     * @return Chosen array or null if user chooses invalid index of array.
     */
    private static PFArray chooseArray() {
        printArrayList();
        Scanner in = new Scanner(System.in);
        try {
            int chosenArrayIdx = in.nextInt();
            return arrayList.get(chosenArrayIdx - 1);
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Find and sets the current array to the one with the biggest sum.
     *
     * @return array with max sum
     */
    private static PFArray findMaxSumArray() {
        PFArray maxSum = arrayList.get(0);
        for (PFArray pfa : arrayList) {
            if (pfa.sum() > maxSum.sum()) {
                maxSum = pfa;
            }
        }
        return maxSum;
    }

    /**
     * Find and sets the current array to the one with the smallest sum.
     *
     * @return array with min sum
     */
    private static PFArray findMinSumArray() {
        PFArray minSum = arrayList.get(0);
        for (PFArray pfa : arrayList) {
            if (pfa.sum() < minSum.sum()) {
                minSum = pfa;
            }
        }
        return minSum;
    }

    /**
     * Resolves user's command for adding number into current array.
     */
    private static void addNumber() {
        Scanner in = new Scanner(System.in);
        out.print("Which number do you want to add: ");
        try {
            currentArray.add(in.nextInt());
        } catch (InputMismatchException ignored) {
            out.println("That is not a number!");
        }
    }

    /**
     * Resolves user's command for removing number(s) from current array.
     */
    private static void removeNumber(boolean removeAll) {
        Scanner in = new Scanner(System.in);
        out.print("Which number do you want to delete: ");
        try {
            currentArray.remove(in.nextInt(), removeAll);
        } catch (InputMismatchException ignored) {
            out.println("That is not a number!");
        }
    }

    /**
     * Resolves user's command for regenerating current array.
     */
    private static void regenerateArray() {
        currentArray.regenerate();
    }

    /**
     * Resolves user's command for deleting current array. Setting it to null.
     *
     * @return null to symbolize deleted array
     */
    private static PFArray deleteArray() {
        return currentArray.clear();
    }

    /**
     * Decides which action user has chosen and calls corresponding method.
     *
     * @return true if user wants to end the program
     */
    private static boolean resolveMainMenuChoice() {
        Scanner in = new Scanner(System.in);
        String option = in.nextLine();

        if (resolveNoArraysCommand(option)) {
            return Boolean.TRUE;
        }

        if (arrayList.isEmpty()) {
            return Boolean.FALSE;
        } else {
            resolveNoCurrentArrayCommand(option);
        }

        if (Objects.isNull(currentArray)) {
            return Boolean.FALSE;
        } else {
            resolveCurrentArrayCommand(option);
        }
        return Boolean.FALSE;
    }

    /**
     * Resolves commands that are runnable only with current array set.
     *
     * @param option users input
     */
    private static void resolveCurrentArrayCommand(String option) {
        switch (option) {
            case "a":
                addNumber();
                break;
            case "b":
                removeNumber(false);
                break;
            case "c":
                removeNumber(true);
                break;
            case "d":
                regenerateArray();
                break;
            case "e":
                arrayList.remove(currentArray);
                currentArray = deleteArray();
                break;
            default:
                break;
        }
    }

    /**
     * Resolves commands that are runnable when some arrays exist.
     *
     * @param option users input
     */
    private static void resolveNoCurrentArrayCommand(String option) {
        switch (option) {
            case "2":
                PFArray chosenArray = chooseArray();
                if (!Objects.isNull(chosenArray)) {
                    currentArray = chosenArray;
                } else {
                    out.println("Invalid option! Current array has not changed.");
                }
                break;
            case "3":
                currentArray = findMaxSumArray();
                out.print("This is the array with max sum: ");
                currentArray.print();
                break;
            case "4":
                currentArray = findMinSumArray();
                out.print("This is the array with min sum: ");
                currentArray.print();
                break;
            default:
                break;
        }
    }

    /**
     * Resolves commands that are runnable in every situation.
     *
     * @param option users input
     * @return true if user wants to end the program
     */
    private static boolean resolveNoArraysCommand(String option) {
        switch (option) {
            case "1":
                PFArray newlyCreated = createNewArray();
                if (!Objects.isNull(newlyCreated)) {
                    currentArray = newlyCreated;
                    arrayList.add(newlyCreated);
                } else {
                    out.println("Array has not been created.");
                }
                break;
            case "0":
                return true;
            default:
                break;
        }
        return false;
    }

    /**
     * Starts and controls the communication between program and user.
     */
    public static void run() {
        boolean endProgram = false;
        while (!endProgram) {
            printHelp();
            endProgram = resolveMainMenuChoice();
        }
    }
}
