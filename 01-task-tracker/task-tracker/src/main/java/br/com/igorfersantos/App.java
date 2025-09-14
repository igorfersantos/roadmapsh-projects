package br.com.igorfersantos;

import java.io.IOException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class App {
    private static final String USAGE_TEXT = """
            Usage: task-cli [-h | --help] <command> [<args>]
            
            Commands:
            
            Adding a new task
                add <task-description>
            
            Listing tasks
                list
                list [todo|in-progress|done]
            
            Updating tasks
                update <task-id> <task-description>
            
            Deleting tasks
                delete <task-id>
            
            Marking a task
                mark-in-progress <task-id>
                mark-done <task-id>
            
            
            Examples:
            
            Adding a new task
            task-cli add "Buy groceries"
            
            Updating and deleting tasks
            task-cli update 1 "Buy groceries and cook dinner"
            task-cli delete 1
            
            Marking a task as in progress or done
            task-cli mark-in-progress 1
            task-cli mark-done 1
            
            Listing all tasks
            task-cli list
            
            Listing tasks by status
            task-cli list done
            task-cli list todo
            task-cli list in-progress
            """;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println(USAGE_TEXT);
            return;
        }
        final Database database = new Database();

        if (!database.checkDatabaseExists()) {
            System.out.println("The database will be created");
            try {
                database.initializeDatabase();
                database.loadDatabase();
            } catch (IOException e) {
                System.out.println(
                        "There was an error while trying to create the tasks file: " +
                                e.getMessage());
                System.out.println("The application will not proceed.");
                return;
            }
        }

        // do the stuff

        try {
            database.persistDatabase();
        } catch (IOException e) {
            System.out.println("There was an error while persisting the tasks: " +
                    e.getMessage());
        }
    }
}