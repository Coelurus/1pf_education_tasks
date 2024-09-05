package pfko.vopalensky.object_array;

import java.io.PrintStream;
import java.util.*;

public class ArrayController {
    private static List<PFArray> arrayList = new ArrayList<>();
    private static final PrintStream out = System.out;
    private static PFArray currentArray;


    private ArrayController() {

    }


    /**
     * Prints out information about possible commands.
     */
    private static void printHelp() {
        out.println("""
                                
                You have following options:
                    [1] Create new array
                    [2] Choose existing array
                    [3] Switch to array with max sum
                    [4] Switch to array with min sum
                    [5] End program""");

        if (Objects.isNull(currentArray)) {
            out.println("You do not have any chosen array at the moment.");
        } else {
            out.print("""
                    Or you can work with current array:
                        [a] Add number
                        [b] Remove first chosen number
                        [c] Remove all chosen numbers
                        [d] Regenerate
                        [e] Delete array
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
        if (arrayList.isEmpty()) {
            printEmptyList();
            return;
        }
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
        if (arrayList.isEmpty()) {
            printEmptyList();
            return;
        }
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
        if (arrayList.isEmpty()) {
            printEmptyList();
            return;
        }
        PFArray minSum = arrayList.get(0);
        for (PFArray pfa : arrayList) {
            if (pfa.sum() < minSum.sum()) {
                minSum = pfa;
            }
        }
        currentArray = minSum;
    }

    /**
     * Ends the program.
     */
    private static void exit() {
        System.exit(0);
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
     */
    private static void resolveMainMenuChoice() {
        Scanner in = new Scanner(System.in);
        switch (in.nextLine()) {
            case "1":
                createNewArray();
                break;
            case "2":
                chooseArray();
                break;
            case "3":
                findMaxSumArray();
                if (!Objects.isNull(currentArray)) {
                    out.print("This is the array with max sum: ");
                    currentArray.print();
                }
                break;
            case "4":
                findMinSumArray();
                if (!Objects.isNull(currentArray)) {
                    out.print("This is the array with min sum: ");
                    currentArray.print();
                }
                break;
            case "5":
                exit();
                break;
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
                out.println("Invalid option...");
                break;
        }
    }

    /**
     * Starts and controls the communication between program and user.
     */
    public static void run() {
        while (true) {
            printHelp();
            resolveMainMenuChoice();
        }
    }
}
