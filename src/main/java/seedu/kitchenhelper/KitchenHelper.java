package seedu.kitchenhelper;

import seedu.kitchenhelper.command.Command;
import seedu.kitchenhelper.command.CommandResult;
import seedu.kitchenhelper.command.ExitCommand;
import seedu.kitchenhelper.storage.Storage;
import seedu.kitchenhelper.exception.KitchenHelperException;
import seedu.kitchenhelper.object.Chore;
import seedu.kitchenhelper.object.Recipe;
import seedu.kitchenhelper.object.ingredient.Ingredient;
import seedu.kitchenhelper.parser.Parser;
import seedu.kitchenhelper.ui.Ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.*;

public class KitchenHelper {
    public final static Logger LOGS = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public ArrayList<Ingredient> ingredientList = new ArrayList<>();
    public ArrayList<Recipe> recipeList = new ArrayList<>();
    public ArrayList<Chore> choreList = new ArrayList<>();
    /* Hi pls, look at this main program.
     * https://github.com/nus-cs2113-AY1920S2/personbook/blob/master/src/main/java/seedu/personbook/Main.java */
    
    private Ui ui;
    private Storage storage;
  
    private void start() {
        ui = new Ui();
        ui.showWelcomeMessage();
        storage = new Storage("output.txt");
    }
    
    private void run() throws KitchenHelperException {
        setUpLogger();
        start();
        runCommandLoopUntilExitCommand();
        exit();
    }
    
    private void exit() {
        System.exit(0);
    }

    private void setUpLogger() throws KitchenHelperException {
        /*
        Output to console only when a serious failure has caused normal
        execution of the program
         */
        LogManager.getLogManager().reset();
        ConsoleHandler consoleOutput = new ConsoleHandler();
        consoleOutput.setLevel(Level.SEVERE);
        LOGS.addHandler(consoleOutput);

        /*
        Logs all information above the Level.Fine to a log file
         */
        try {
            FileHandler logFile = new FileHandler("KitchenLogs.log");
            logFile.setFormatter(new SimpleFormatter());
            logFile.setLevel(Level.FINE);
            LOGS.addHandler(logFile);
        } catch (IOException e) {
            throw new KitchenHelperException("Error in Logging");
        }
    }
    
    private void runCommandLoopUntilExitCommand() {
        Command command;
        String userCommandInput = "";
        
        do {
            try {
                // takes in the user's input
                userCommandInput = ui.getUserCommand();
                // parse input to return obj of the corresponding
                // type of command (i.e add/ delete/ list/ help / exit)
                command = new Parser().parseUserCommand(userCommandInput);
                CommandResult result = executeCommand(command);
                ui.showResultToUser(result);
                ui.printDivider();
            } catch (KitchenHelperException e) {
                ui.printInvalidCmd();
                ui.print(e.getMessage());
                ui.printDivider();
            }
        } while (!userCommandInput.equalsIgnoreCase(ExitCommand.COMMAND_WORD));
        
    }
    
    public static void main(String[] args) throws KitchenHelperException {
        new KitchenHelper().run();
    }
    
    /**
     * Executes the command and return result.
     *
     * @param command the command being executed.
     * @return the return message of the command.
     */
    public CommandResult executeCommand(Command command) {
        try {
            // to check if you get the right object
            // System.out.println(command.getClass().getName());
            CommandResult result = command.execute(ingredientList, recipeList, choreList);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
