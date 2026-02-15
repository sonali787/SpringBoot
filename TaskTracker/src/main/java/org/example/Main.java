package org.example;

import java.util.Objects;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws Exception{



        if (args.length == 0) {
            System.out.println("No argument is provided");
        }

        String cmd = args[0];
        TaskManager taskManager = new TaskManager();

        switch (cmd) {
            case "add":
                taskManager.add(args[1]);
                break;

            case "update":
                taskManager.update(args[1],args[2]);
                break;

            case "delete":
                taskManager.delete(args[1]);
                break;

            case "mark-in-progress":
                taskManager.markInProgress(args[1]);
                break;

            case "mark-done":
                taskManager.markDone(args[1]);
                break;

            case "list":
                if (args.length == 2)
                    taskManager.list(args[1]);
                else
                    taskManager.list(null);
                break;


            default:
                System.out.println("Unknown command");
        }

    }
}
