import java.io.*;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.*;

public class ToDoList {
    private static final String SOURCEPATH = System.getProperty("user.home") + "/.local/ToDoList/data/";
    private static String FILENAME = SOURCEPATH + "todolist.txt";
    public static String message = "";
    public static int adjustForHigherNumbers = 0;
    private static boolean subTaskList = false;
    private static boolean hasTasks = false;
    private static int enteredTaskNumber = 0;
    public ArrayList<String> tasks = new ArrayList<>();
    public String filename = "";
    public boolean isSubTaskList;
    public boolean hasTask;

    public ToDoList(ArrayList<String> tasks, String filename, boolean isSubTaskList, boolean hasTasks) {
        this.tasks = tasks;
        this.filename = filename;
        this.isSubTaskList = subTaskList;
        this.hasTask = hasTasks;
    }

    public static void main(String[] args) {
	    File mainDir = new File(System.getProperty("user.home")+"/.local/ToDoList");
	    File data = new File(mainDir, "/data/");
	    if (!data.exists()) data.mkdirs();
        ArrayList<String> tasks = loadTasksFromFile();
        ArrayList<String> headings = loadTasksFromFile();
        Scanner scanner = new Scanner(System.in);


        while (true) {

            clearField();
            printSeparator();
            if (!subTaskList) printTask("To-Do List:", -1);
            else {
                hasTasks = !tasks.isEmpty();
                printTask(headings.get(enteredTaskNumber), -2);
            }
            adjustForHigherNumbers = tasks.size() >= 100 ? 2 : (tasks.size() >= 10 ? 1 : 0);
            for (int i = 0; i < tasks.size(); i++) {
                printTask(tasks.get(i), i);
            }

            printSeparator();
            printTask("Options: ", -1);
            System.out.println("| a - Add Task    | rm - Remove Task | c - Change Task | mv - Move Task |");
            System.out.print("| s - Shift Tasks | ");
            if (!subTaskList) System.out.print("e  - Enter Task  | ");
            else System.out.print("l  - Leave Task  | ");
            System.out.print("h - Help        | ");
            System.out.println("q - Quit       |");
            printSeparator();

            if (!message.isEmpty()) outputMessage();

            String choice = getStringInput(scanner, "Enter your choice: ");
            String argument = "";

            boolean quickInput = false;
            if (choice.length() > 2) {
                quickInput = true;
                argument = choice.substring(choice.indexOf(' ') + 1);
                try {
                    choice = choice.substring(0, choice.indexOf(' '));
                } catch (Exception e) {
                    message = "Invalid choice. Please try again!";
                    continue;
                }
            }

            switch (choice) {
                case "a":
                    String task = "";
                    if (!quickInput) task = getStringInput(scanner, "Enter task: ");
                    else task = argument;
                    if (containsBannedSequences(task)) {
                        message = "Your input contains a sequence that might cause problems. Please try again!";
                        break;
                    }
                    tasks.add(task);
                    if (!subTaskList) headings.add(task);
                    saveTasksToFile(tasks);
                    message = "Task added!";
                    break;
                case "rm":
                    int taskNumber;
                    if (!quickInput) taskNumber = getIntInput(scanner, "Enter task number to remove: ");
                    else taskNumber = Integer.parseInt(argument);
                    if (taskNumber >= 1 && taskNumber <= tasks.size()) {
                        if (subTaskList) {
				removeFile("Task" + taskNumber + ".txt", taskNumber, tasks.size());
			} else headings.remove(taskNumber - 1);
			tasks.remove(taskNumber - 1);

                        saveTasksToFile(tasks);

                        message = "Task removed!";
                    } else {
                        message = "Invalid task number.";
                    }
                    break;
                case "s":
                    int fromIndex;
                    int toIndex;
                    if (!quickInput) {
                        fromIndex = getIntInput(scanner, "Enter task number to move: ") - 1;
                        toIndex = getIntInput(scanner, "Enter new position: ") - 1;
                    } else {
                        fromIndex = Integer.parseInt(argument.substring(0, argument.indexOf(' '))) - 1;
                        toIndex = Integer.parseInt(argument.substring(argument.indexOf(' ') + 1)) - 1;
                    }
                    if (fromIndex >= 0 && fromIndex < tasks.size() && toIndex >= 0 && toIndex < tasks.size()) {
                        String taskToMove = tasks.remove(fromIndex);
                        tasks.add(toIndex, taskToMove);
                        if (!subTaskList) {
                            headings.remove(fromIndex);
                            headings.add(toIndex, taskToMove);
                        }
                        saveTasksToFile(tasks);
                        updateFilesSwitched(fromIndex + 1, toIndex + 1);

                        message = "Task moved!";
                    } else {
                        message = "Invalid task numbers.";
                    }
                    break;
                case "c":
                    int changeNumber;
                    String newTask;
                    if (!quickInput) {
                        changeNumber = getIntInput(scanner, "Enter task number to change: ") - 1;
                        newTask = getStringInput(scanner, "Enter changed task: ");
                    } else {
                        changeNumber = Integer.parseInt(argument.substring(0, argument.indexOf(' '))) - 1;
                        newTask = argument.substring(argument.indexOf(' ') + 1);
                    }
                    if (containsBannedSequences(newTask)) {
                        message = "Your input contains a sequence that might cause problems. Please try again!";
                        break;
                    }
                    if (changeNumber >= 0 && changeNumber <= tasks.size()) {
                        tasks.set(changeNumber, newTask);
                        if (!subTaskList) headings.set(changeNumber, newTask);
                        saveTasksToFile(tasks);
                        message = "Task changed!";
                    } else {
                        message = "Invalid task number.";
                    }
                    break;
                case "e":
                    if (!subTaskList) {
                        int enterNumber;
                        if (!quickInput) enterNumber = getIntInput(scanner, "Enter task number to enter: ") - 1;
                        else enterNumber = Integer.parseInt(argument) - 1;
                        enteredTaskNumber = enterNumber;
                        FILENAME = SOURCEPATH + "Task" + (enterNumber + 1) + ".txt";
                        tasks = loadTasksFromFile();
                        subTaskList = true;
                        System.out.println("Entering...");
                    } else message = "Invalid choice. Please select again.";
                    break;
                case "l":
                    if (subTaskList) {
                        subTaskList = false;
                        FILENAME = SOURCEPATH + "todolist.txt";
                        tasks = loadTasksFromFile();
                        System.out.println("Leaving...");
                    } else message = "Invalid choice. Please select again.";
                    break;
                case "mv":
                    if (subTaskList) break;
                    int fromNumber;
                    int toNumber;
                    if (!quickInput) {
                        fromNumber = getIntInput(scanner, "Enter task number to move: ") - 1;
                        toNumber = getIntInput(scanner, "Enter new position: ") - 1;
                    } else {
                        fromNumber = Integer.parseInt(argument.substring(0, argument.indexOf(' '))) - 1;
                        toNumber = Integer.parseInt(argument.substring(argument.indexOf(' ') + 1)) - 1;
                    }
                    if (fromNumber == toNumber) {
                        message = "Invalid choice. Please select again.";
                        break;
                    }
                    if (fromNumber >= 0 && fromNumber < tasks.size() && toNumber >= 0 && toNumber < tasks.size()) {
                        String taskToMove = tasks.remove(fromNumber);

                        FILENAME = SOURCEPATH + "Task" + (toNumber + 1) + ".txt";

                        File saveTo = new File(FILENAME);
                        if (saveTo.exists()) tasks = loadTasksFromFile();
                        else tasks.clear();
                        tasks.add(taskToMove);
                        saveTasksToFile(tasks);

                        FILENAME = SOURCEPATH + "todolist.txt";
                        tasks = loadTasksFromFile();
                        tasks.remove(fromNumber);

                        saveTasksToFile(tasks);
                        updateFilesRemoved(fromNumber + 1, tasks.size() + 1);

                        message = "Task moved!";
                    } else {
                        message = "Invalid task numbers.";
                    }
                    break;

                case "h":
                    while (true) {
                        clearField();
                        printSeparator();
                        printTask("Help:", -1);
                        printSeparator();
                        printTask("Overview", 0);
                        printTask("Installation", 1);
                        printTask("a  - Add Task", 2);
                        printTask("rm - Remove Task", 3);
                        printTask("c  - Change Task", 4);
                        printTask("mv - Move Task", 5);
                        printTask("s  - Shift Tasks", 6);
                        printTask("e  - Enter Task", 7);
                        printTask("l  - Lave Task", 8);
                        printSeparator();

                        if (!message.isEmpty()) outputMessage();

                        String input = getStringInput(scanner, "Enter the number of your chosen page, use l to go back or q to quit: ").substring(0, 1);
                        try {
                            int intValue = Integer.parseInt(input);
                            if (intValue > 0 && 10 > intValue) TextPrinter.printHelp(intValue);
                            scanner.reset();
                        } catch (NumberFormatException e) {
                            if (input.equals("l")) break;
                            if (input.equals("q")) endProgram(scanner);
                            else message = "Invalid choice, please try again!";
                        }
                    }

                    break;
                case "q":
                    System.out.println("Exiting...");
                    System.out.print("\u001B[0m");
                    scanner.close();
                    System.exit(0);
                default:
                    message = "Invalid choice. Please select again.";
            }
            System.out.println();
        }
    }

    public static int getIntInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                message = "Invalid input. Please enter a number.";
            }
        }
    }

    private static String getStringInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static ArrayList<String> loadTasksFromFile() {
        ArrayList<String> tasks = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(FILENAME)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                tasks.add(line);
            }
        } catch (IOException e) {
            // File does not exist or other IO error
        }
        return tasks;
    }

    public static void saveTasksToFile(ArrayList<String> tasks) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(FILENAME)))) {
            for (String task : tasks) {
                writer.write(task);
                writer.newLine();
            }
        } catch (IOException e) {
            // Error while writing to file
            e.printStackTrace();
        }
    }

    public static void printSeparator() {
        String line = "-";
        System.out.println(line.repeat(73));
    }

    public static void clearField() {
        String newLine = "\n";
        System.out.println(newLine.repeat(20));
    }

    public static String printSpace(int count) {
        String space = " ";
        return space.repeat(count);
    }

    public static String printRightLine(String given) {
        int indent = 72 - given.length();
        return printSpace(indent - 1) + " |";
    }

    /**
     * Prints a given String so that it fits the design of the application.
     *
     * @param task   String to print
     * @param number Number of the task. <br /> Use -1 for tasks without one.
     */
    public static void printTask(String task, int number) {
        boolean isTask = !(number < 0);
        String adjustment = isTask ? giveBackAdjustment(number + 1) : "";  //Adjustment by "  " for better readability when some tasks have multiple digits
        boolean isHeading = number == -2;
        String toPrint = "|" + " ".repeat(1) + (isTask ? (number + 1) + ". " : "") + adjustment + task;

        //int lineEnd = (isTask ? 66 : 69) - offset;
        int lineEnd = 71;
        if (toPrint.length() < lineEnd) System.out.println(toPrint + printRightLine(toPrint));
        else {
            int endCorrect = lineEnd;
            if (toPrint.charAt(lineEnd-1)!=' ') endCorrect=wordEnd(toPrint, lineEnd);
            String line = toPrint.substring(0, endCorrect);
            System.out.println(line + printRightLine(line));
            int start = endCorrect;
            if (toPrint.charAt(endCorrect)==' ') start+=1;
            lineEnd -= (5+adjustForHigherNumbers);
            int end = start + lineEnd;
            while (end < toPrint.length()) {
                if (toPrint.charAt(end-1)!=' ') end=wordEnd(toPrint, end);     //Break earlier if word would be cut in two otherwise
                line = "|" + " ".repeat(4+adjustForHigherNumbers) + toPrint.substring(start, end);
                System.out.println(line + printRightLine(line));
                start = end;
                if (toPrint.charAt(end)==' ') start+=1;     //correct empty spaces from breaking
                end = start + lineEnd;
            }
            line = "|" + " ".repeat(4+adjustForHigherNumbers) + toPrint.substring(start);
            System.out.println(line + printRightLine(line));
        }
        if (isHeading && hasTasks) printSeparator();
    }

    private static String giveBackAdjustment(int number) {
        int distance = adjustForHigherNumbers + 1 - Integer.toString(number).length();
        return distance == 0 ? "" : " ".repeat(distance);
    }

    public static int wordEnd(String given, int lineEnd) {
        //String sentence = given.substring(0, lineEnd);
        char[] items = given.toCharArray();
        for (int i = lineEnd; i > 0; i--) {
            if (items[i] == ' ') {
                lineEnd = i;
                break;
            }
        }
        return lineEnd;
    }

    public static void removeFile(String searchName, int taskNumber, int taskCount) {
        File directory = new File(SOURCEPATH);
        File fileToDelete = new File(directory, searchName);
        if (fileToDelete.delete()) /*System.out.println("Success")*/ ;
        else message = "There was an error with your todo-files. Please in your/directory/for/this/program/data";
        updateFilesRemoved(taskNumber + 1, taskCount);
    }

    public static void updateFilesRemoved(int startNumber, int endNumber) {
        File directory = new File(SOURCEPATH);
        for (int i = startNumber; i <= endNumber; i++) {
            File file = new File(directory, "Task" + i + ".txt");
            File file1 = new File(directory, "Task" + (i - 1) + ".txt");
            file.renameTo(file1);
        }
    }

    public static void updateFilesSwitched(int firstNumber, int secondNumber) {
        File directory = new File(SOURCEPATH);
        String secondName = "waiting_list" + ".txt";

        File file1 = new File(directory, "Task" + firstNumber + ".txt");
        File file2 = new File(directory, "Task" + secondNumber + ".txt");
        File secondFile = new File(directory, "Task" + secondNumber + ".txt");
        File placeHolder = new File(directory, secondName);

        file1.renameTo(placeHolder);
        file2.renameTo(file1);
        placeHolder.renameTo(secondFile);
    }

    public static void endProgram(Scanner scanner) {
        System.out.println("Exiting...");
        scanner.close();
        System.exit(0);
    }

    public static void outputMessage() {
        System.out.println(message);
        message = "";
    }

    public static boolean containsBannedSequences(String input) {
        return input.contains("\u001B")
                || input.contains("\n");
    }

}
