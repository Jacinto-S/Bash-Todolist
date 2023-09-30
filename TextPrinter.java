import java.util.Scanner;

public class TextPrinter {

    public static void printHelp(int input) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            ToDoList.clearField();
            ToDoList.printSeparator();
            switch (input) {
                case 1 -> printOverview();
                case 2 -> printInstallation();
                case 3 -> printAdd();
                case 4 -> printRemove();
                case 5 -> printChange();
                case 6 -> printMove();
                case 7 -> printShift();
                case 8 -> printEnter();
                case 9 -> printLeave();
            }
            ToDoList.printSeparator();

            if (!ToDoList.message.isEmpty()) ToDoList.outputMessage();
            String newInput = getStringInput(scanner, "Enter l to continue or q to quit: ").substring(0,1);
            if (newInput.equals("l")) {
                break;
            } else if (newInput.equals("q")) {
                ToDoList.endProgram(scanner);
            } else ToDoList.message = "Invalid choice. Please enter q to continue.";
        }
    }

    public static void printOverview() {
        ToDoList.printTask("Overview:", -1);
        printLineBreak();
        ToDoList.printTask("This is a simple Todo-List application intended to be used in Bash.", -1);
        printLineBreak();
        ToDoList.printTask("Its purpose is to provide a minimalistic way to manage a Todo-List without the excess weight of an additional app.", -1);
        printLineBreak();
        ToDoList.printTask("Currently, this program allows its users to add, remove or change tasks, change their relative positions and enter them in order to create subtasks.", -1);
        ToDoList.printTask("Additionally, normal tasks can be made subtasks by moving them into the task they're supposed to be a subtask of.", -1);
        printLineBreak();
        ToDoList.printTask("Since this app was developed to be as minimalistic as possible, its optimized to be used in a minimized format.", -1);
        ToDoList.printTask("This is because on default, all Linux distributions known to me will open bash in this way.", -1);
        printLineBreak();
        ToDoList.printTask("Credit: This program was developed by Jacinto Schwarzwälder. If you want to use it, consider this open source by the standard MIT License", -1);
    }

    public static void printInstallation() {
        ToDoList.printTask("Installation:", -1);
        printLineBreak();
        ToDoList.printTask("To run this application, you need to have Java 19 (or higher) installed.", -1);
        printLineBreak();
        ToDoList.printTask("To install this program it, move the java file to the desired pathway. From there, execute:", -1);
        ToDoList.printTask("> javac ToDoList.java", -1);
        printLineBreak();
        ToDoList.printTask("You should now be able to run the program by running:", -1);
        ToDoList.printTask("> java ToDoList", -1);
        ToDoList.printTask("from the location of your file.", -1);
        printLineBreak();
        ToDoList.printTask("You can make the program available from anywhere in bash by adding the following script to you .bashrc file:", -1);
        ToDoList.printTask("> alias todo='java -cp your/path/to/the/file ToDoList'", -1);
        ToDoList.printTask("After doing so, update the .bashrc file with:", -1);
        ToDoList.printTask("> source .bashrc", -1);
        printLineBreak();
        ToDoList.printTask("If you encounter any bugs, please let me know at schwarzwaelder.jacinto@gmail.com", -1);
    }

    public static void printAdd() {
        ToDoList.printTask("Add Task:", -1);
        printLineBreak();
        ToDoList.printTask("To add a task, simply write \"a (number of your task your task)\".", -1);
        ToDoList.printTask("Alternatively, you can enter 'a'. After pressing enter, you will be prompted to enter your task-number.", -1);
        printLineBreak();
        ToDoList.printTask("Your task will be automatically added to the bottom.", -1);
        printLineBreak();
        ToDoList.printTask("There is no option to add it to a specific location. If so desired, please use the 's' option to shift your task to the desired location.", -1);
    }

    public static void printRemove() {
        ToDoList.printTask("Remove Task:", -1);
        printLineBreak();
        ToDoList.printTask("To remove a task, input \"rm (number of your task)\"", -1);
        ToDoList.printTask("Alternatively, you can enter 'rm'. After pressing enter, you will be prompted to enter your task-number.", -1);
        printLineBreak();
        ToDoList.printTask("Your task and any subtasks will automatically be removed from the list.", -1);
    }

    public static void printChange() {
        ToDoList.printTask("Change Task:", -1);
        printLineBreak();
        ToDoList.printTask("To change a task, input \"c (number of your task) (updated task)\"", -1);
        ToDoList.printTask("Alternatively, you can enter 'c'. After pressing enter, you will be prompted to enter your task-number and then your changed task.", -1);
        printLineBreak();
        ToDoList.printTask("Your task will automatically be replaced by the one you entered.", -1);
    }

    public static void printMove() {
        ToDoList.printTask("Move Task:", -1);
        printLineBreak();
        ToDoList.printTask("This feature allows you to move a task into a subtask-list.", -1);
        printLineBreak();
        ToDoList.printTask("To move a task, input \"mv (number of new subtask) (number of parent-task)\"", -1);
        ToDoList.printTask("Alternatively, you can enter 'mv'. After pressing enter, you will be prompted to enter the task-number of the subtask and then the parent-task.", -1);
        printLineBreak();
        ToDoList.printTask("For example, you might have the following tasks:", -1);
        ToDoList.printTask("Go shopping", 0);
        ToDoList.printTask("Buy milk", 1);
        ToDoList.printTask("You might realize that buying milk is supposed to be a subtask of shopping. You can make it one by entering: \"mv 2 1\".", -1);
        printLineBreak();
        ToDoList.printTask("Your task will automatically be made a subtask..", -1);
    }

    public static void printShift() {
        ToDoList.printTask("Shift Task:", -1);
        printLineBreak();
        ToDoList.printTask("To change the position of two task, input \"s (number of first task) (number of second task)\"", -1);
        ToDoList.printTask("Alternatively, you can enter 's'. After pressing enter, you will be prompted to enter both task-numbers, one after another.", -1);
        printLineBreak();
        ToDoList.printTask("The tasks will automatically switch places.", -1);
    }

    public static void printEnter() {
        ToDoList.printTask("Enter Task:", -1);
        printLineBreak();
        ToDoList.printTask("Entering a task allows you to see and add subtasks to a given task. This is useful for tasks that have multiple items to get done, like shopping or programming something. " +
                "Inside a task, you will find a new To-Do List that contains all subtasks that you ad to it.", -1);
        printLineBreak();
        ToDoList.printTask("To enter a task, input \"e (number of task)\"", -1);
        ToDoList.printTask("Alternatively, you can enter 'e'. After pressing enter, you will be prompted to input the number of the task that you want to fill with subtasks.", -1);
        printLineBreak();
        ToDoList.printTask("The tasks will automatically switch places.", -1);
    }

    public static void printLeave() {
        ToDoList.printTask("Leave Task:", -1);
        printLineBreak();
        ToDoList.printTask("After you have entered a task, you can leave it by inputting \"l\"", -1);
        printLineBreak();
        ToDoList.printTask("You will then be taken back to the main Todo-List.", -1);
    }

    private static String getStringInput(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static void printLineBreak() {
        System.out.println("|" + ToDoList.printSpace(71) + "|");
    }
}
