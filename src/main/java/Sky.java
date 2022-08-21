import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Sky {
    private List<Task> taskList = new ArrayList<>();
    private static final String PATH_NAME_FOR_DATA = "data/sky.txt";

    public void greetUser() {
        System.out.println("  ____________________________________________________________");
        System.out.println("  Hello from Sky!\n  Your heavenly chatbot to help you track your things.");
        System.out.println("  ____________________________________________________________");
    }

    public void endConvo() {
        System.out.println("  Bye. May all your endeavours fly high!");
        System.out.println("  ____________________________________________________________");
    }

    public void printsList() {
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println("  " + (i + 1) + "." + taskList.get(i));
        }
    }

    public void markATask(String userInput) {
        try {
            String taskNumInString = userInput.substring(5);
            // Minus one as arrayList is zero-indexed
            int taskNum = Integer.parseInt(taskNumInString) - 1;
            Task task = taskList.get(taskNum);
            task.markAsDone();
            this.reWriteDataFile(PATH_NAME_FOR_DATA);
            System.out.println("  Wow... who would have thought you had it in you... I've marked this task as done: \n" +
                    "    " + task);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("  You have either not entered any number to indicate which task I should mark, \n" +
                    "  or you entered an invalid task number.");
        } catch (NumberFormatException e) {
            System.out.println("  Are you new? Enter a number after typing mark.");
        }
    }

    public void unmarkATask(String userInput) {
        try {
            String taskNumInString = userInput.substring(7);
            // Minus one as arrayList is zero-indexed
            int taskNum = Integer.parseInt(taskNumInString) - 1;
            Task task = taskList.get(taskNum);
            task.markAsUndone();
            this.reWriteDataFile(PATH_NAME_FOR_DATA);
            System.out.println("  Well, that's disappointing. I've marked this task as undone: \n" +
                    "    " + task);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("  You have either not entered any number to indicate which task I should unmark, \n" +
                    "  or you entered an invalid task number.");
        } catch (NumberFormatException e) {
            System.out.println("  Are you new? Enter a number after typing unmark.");
        }
    }

    public void addToDo(String userInput) {
        try {
            String taskToDo = userInput.substring(5);
            Task task = new ToDo(taskToDo);
            taskList.add(task);
            // Add task into data file
            this.appendToFile(PATH_NAME_FOR_DATA, task.toString());
            System.out.println("  Got it. I've added this task: \n" +
                    "    " + task +
                    "\n  Now you have " + taskList.size() +
                    (taskList.size() <= 1 ? " task in the list.": " tasks in the list."));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("  Are you new? Specify a task after typing todo.");
        }
    }

    public void addDeadLine(String userInput) {
        try {
            String taskDeadline = userInput.substring(9);
            int indexOfSlash = -1;
            for (int i = 0; i < taskDeadline.length(); i++) {
                if (taskDeadline.charAt(i) == '/') {
                    indexOfSlash = i;
                }
            }
            String taskDescription = taskDeadline.substring(0, indexOfSlash - 1);
            String taskBy = taskDeadline.substring(indexOfSlash + 1);
            Task task = new Deadline(taskDescription, taskBy);
            taskList.add(task);
            // Add task into data file.
            this.appendToFile(PATH_NAME_FOR_DATA, task.toString());
            System.out.println("  Got it. I've added this task: \n" +
                    "    " + task +
                    "\n  Now you have " + taskList.size() +
                    (taskList.size() <= 1 ? " task in the list.": " tasks in the list."));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("  You have either not entered any text after typing deadline, \n" +
                    "  or you have positioned your slash wrongly.");
        }
    }

    public void addEvent(String userInput) {
        try {
            String taskEvent = userInput.substring(6);
            int indexOfSlash = -1;
            for (int i = 0; i < taskEvent.length(); i++) {
                if (taskEvent.charAt(i) == '/') {
                    indexOfSlash = i;
                }
            }
            String taskDescription = taskEvent.substring(0, indexOfSlash - 1);
            String taskDuration = taskEvent.substring(indexOfSlash + 1);
            Task task = new Event(taskDescription, taskDuration);
            taskList.add(task);
            // Add task into data file
            this.appendToFile(PATH_NAME_FOR_DATA, task.toString());
            System.out.println("  Got it. I've added this task: \n" +
                    "    " + task +
                    "\n  Now you have " + taskList.size() +
                    (taskList.size() <= 1 ? " task in the list.": " tasks in the list."));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("  You have either not entered any text after typing event, \n" +
                    "  or you have positioned your slash wrongly.");
        }
    }

    public void handleInvalidInput() {
        try {
            throw new TextNoMeaningException("  Are you new? Type a command that I actually know.");
        } catch (TextNoMeaningException e) {
            System.out.println(e);
        }
    }

    public void deleteTask(String userInput) {
        try {
            String taskNumInString = userInput.substring(7);
            // Minus one as arrayList is zero-indexed
            int taskNum = Integer.parseInt(taskNumInString) - 1;
            Task task = taskList.get(taskNum);
            taskList.remove(task);
            this.reWriteDataFile(PATH_NAME_FOR_DATA);
            System.out.println("  Splendid. I've removed this task: \n" +
                    "    " + task +
                    "\n  Now you have " + taskList.size() +
                    (taskList.size() <= 1 ? " task in the list.": " tasks in the list."));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("  You have either not entered any number to indicate which task I should delete, \n" +
                    "  or you entered an invalid task number.");
        } catch (NumberFormatException e) {
            System.out.println("  Are you new? Enter a number after typing delete.");
        }
    }

    public void appendToFile(String filePath, String textToAdd) {
        File file = new File(filePath);
        this.createFileIfNecessary(file);

        // Append to the file
        try {
            FileWriter fw = new FileWriter(filePath, true);
            fw.write(textToAdd + "\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    public void writeToFile(String filePath, String textToWrite) {
        File file = new File(filePath);
        this.createFileIfNecessary(file);

        // Write to the file
        try {
            FileWriter fw = new FileWriter(filePath);
            fw.write(textToWrite + "\n");
            fw.close();
        } catch (IOException e) {
            System.out.println("Something went wrong: " + e.getMessage());
        }
    }

    public void readFromFile(String filePath) {
        File file = new File(filePath);
        this.createFileIfNecessary(file);

        // Read and print the file's content.
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()) {
                System.out.println(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unable to detect file: " + e.getMessage());
        }
    }

    // If the file does not yet exist, we create all the necessary parent directories and file.
    public void createFileIfNecessary(File file) {
        if (!file.exists()) {
            // If the file doesn't exist yet, mkdirs() will assume everything specified is a
            // directory and creates it as such. By using getParentFile(), we leave the creation of
            // the file itself to createNewFile().
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error with creating a file at specified path name: " + e.getMessage());
            }
        }
    }

    // Re-write the entire data file
    public void reWriteDataFile(String pathName) {
        for (int i = 0; i < taskList.size(); i++) {
            if (i == 0) {
                this.writeToFile(pathName, taskList.get(i).toString());
            } else {
                this.appendToFile(pathName, taskList.get(i).toString());
            }
        }
    }

    public void addTasksFromDataFileToTaskList(String pathName) {
        File file = new File(pathName);
        if (!file.exists()) {
            return;
        }
        try {
            Scanner scanner = new Scanner(file);
            while(scanner.hasNext()) {
                String textLine = scanner.nextLine();
                char taskType = textLine.charAt(1);
                boolean isMarked = textLine.charAt(4) == 'X';
                String description = textLine.substring(7);

                if (taskType == 'T') {
                    Task task = new ToDo(description);
                    taskList.add(task);
                } else if (taskType == 'D') {
                    int indexOfBracket = -1;
                    for (int i = 0; i < description.length(); i++) {
                        if (description.charAt(i) == '(') {
                            indexOfBracket = i;
                        }
                    }
                    String taskDescription = description.substring(0, indexOfBracket - 1);
                    String taskBy = description.substring(indexOfBracket + 2, description.length() - 1);
                    Task task = new Deadline(taskDescription, taskBy);
                    taskList.add(task);
                } else {
                    int indexOfBracket = -1;
                    for (int i = 0; i < description.length(); i++) {
                        if (description.charAt(i) == '(') {
                            indexOfBracket = i;
                        }
                    }
                    String taskDescription = description.substring(0, indexOfBracket - 1);
                    String taskDuration = description.substring(indexOfBracket + 2, description.length() - 1);
                    Task task = new Event(taskDescription, taskDuration);
                    taskList.add(task);
                }
                if (isMarked) {
                    taskList.get(taskList.size() - 1).markAsDone();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Unable to detect file: " + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error parsing data from data file.");
        }
    }

    public static void main(String[] args) {
        Sky sky = new Sky();
        sky.greetUser();
        sky.addTasksFromDataFileToTaskList(PATH_NAME_FOR_DATA);
        Scanner in = new Scanner(System.in);

        while (true) {
            String userInput = in.nextLine();
            System.out.println("  ____________________________________________________________");
            if (userInput.startsWith("bye")) {
                sky.endConvo();
                break;
            } else if (userInput.startsWith("list")) {
                sky.printsList();
            } else if (userInput.startsWith("mark")) {
                sky.markATask(userInput);
            } else if (userInput.startsWith("unmark")) {
                sky.unmarkATask(userInput);
            } else if (userInput.startsWith("todo")) {
                sky.addToDo(userInput);
            } else if (userInput.startsWith("deadline")) {
                sky.addDeadLine(userInput);
            } else if (userInput.startsWith("event")) {
                sky.addEvent(userInput);
            } else if (userInput.startsWith("delete")) {
                sky.deleteTask(userInput);
            } else {
                sky.handleInvalidInput();
            }
            System.out.println("  ____________________________________________________________");
        }
    }
}