package pfko.vopalensky.object_array;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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
                                
                You have following options:\s
                    [1] Create new array
                    [2] Choose existing array
                    [3] Switch to array with max sum
                    [4] Switch to array with min sum
                    [5] End program""");

        if (Objects.isNull(currentArray)) {
            out.println("You do not have any chosen array at the moment.");
        } else {
            out.print("And your current array is: ");
            currentArray.print();
        }
        out.print("What do you do: ");
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
        out.println("""
                You have these options:\s
                    [1] Fill your array with random numbers.
                    [2] Define your own numbers to be added.""");

        Scanner in = new Scanner(System.in);
        String choice = in.nextLine();
        PFArray newArray;
        if (Objects.equals(choice, "2")) {
            out.println("Write contents of your array separated by commas.");
            newArray = new PFArray(in.nextLine());
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
        out.println("Write number of array you want to choose: ");
    }

    private static void chooseArray() {
        printArrayList();
        Scanner in = new Scanner(System.in);
        int chosenArrayIdx = in.nextInt();
        currentArray = arrayList.get(chosenArrayIdx - 1);
    }

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
            default:
                out.println("Invalid option...");
                break;
        }
    }

    /**
     * Ends the program.
     */
    private static void exit() {
        System.exit(0);
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
