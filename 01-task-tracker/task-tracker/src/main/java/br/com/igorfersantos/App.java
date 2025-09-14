package br.com.igorfersantos;

import br.com.igorfersantos.domain.task.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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
        }

//        if (!Database.checkDatabaseExists()) {
//            System.out.println("The database will be created");
//            Database.initializeDatabase();
//        }
//        Database.loadDatabase();
//
//        // do the stuff
//
//        final boolean hasBeenSucessefullyPersisted = Database.persistDatabase();
    }
}