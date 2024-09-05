package pfko.vopalensky.object_array;

import java.io.PrintStream;
import java.util.*;

public class ArrayController {
    private static final List<PFArray> arrayList = new ArrayList<>();
    private static final PrintStream out = System.out;
    private static PFArray currentArray;
    private static final String INVALID_OPTION_MESSAGE = "Invalid option...";
    private static final boolean END_PROGRAM = true;

    private ArrayController() {

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
     * Prints out information about not having saved any arrays.
     */
    private static void printEmptyList() {
        out.println("You have no arrays...");
    }

    /**
     * Generates new array either randomly or manually based on users choice.
     */
    private static void createNewArray() {
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
                return;
            }

        } else {
            newArray = new PFArray();
        }
        arrayList.add(newArray);
        currentArray = newArray;
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
     */
    private static void chooseArray() {
        printArrayList();
        Scanner in = new Scanner(System.in);
        try {
            int chosenArrayIdx = in.nextInt();
            currentArray = arrayList.get(chosenArrayIdx - 1);
        } catch (Exception ignored) {
            out.println("Invalid option! Current array has not changed");
        }
    }

    /**
     * Find and sets the current array to the one with the biggest sum.
     */
    private static void findMaxSumArray() {
        PFArray maxSum = arrayList.get(0);
        for (PFArray pfa : arrayList) {
            if (pfa.sum() > maxSum.sum()) {
                maxSum = pfa;
            }
        }
        currentArray = maxSum;
    }

    /**
     * Find and sets the current array to the one with the smallest sum.
     */
    private static void findMinSumArray() {
        PFArray minSum = arrayList.get(0);
        for (PFArray pfa : arrayList) {
            if (pfa.sum() < minSum.sum()) {
                minSum = pfa;
            }
        }
        currentArray = minSum;
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
     * Resolves user's command for deleting current array.
     */
    private static void deleteArray() {
        arrayList.remove(currentArray);
        currentArray = currentArray.clear();
    }

    /**
     * Decides which action user has chosen and calls corresponding method.
     *
     * @return true if user wants to end the program
     */
    private static boolean resolveMainMenuChoice() {
        Scanner in = new Scanner(System.in);
        String option = in.nextLine();
        if (arrayList.isEmpty()) {
            if (resolveNoArraysCommand(option)) return END_PROGRAM;
        } else if (Objects.isNull(currentArray)) {
            resolveNoCurrentArrayCommand(option);
            if (resolveNoArraysCommand(option)) return END_PROGRAM;
        } else {
            resolveCurrentArrayCommand(option);
            resolveNoCurrentArrayCommand(option);
            if (resolveNoArraysCommand(option)) return END_PROGRAM;

        }
        return !END_PROGRAM;
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
                deleteArray();
                break;
            default:
1                break;
        }
    }

    /**
     * Resolves commands that are runnable when some arrays exists.
     *
     * @param option users input
     */
    private static void resolveNoCurrentArrayCommand(String option) {
        switch (option) {
            case "2":
                chooseArray();
                break;
            case "3":
                findMaxSumArray();
                out.print("This is the array with max sum: ");
                currentArray.print();
                break;
            case "4":
                findMinSumArray();
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
                createNewArray();
                break;
            case "0":
                return true;
            default:
                printEmptyList();
                break;
        }
        return false;
    }

    /**
     * Starts and controls the communication between program and user.
     */
    public static void run() {
        boolean kill = false;
        while (!kill) {
            printHelp();
            kill = resolveMainMenuChoice();
        }
    }
}
