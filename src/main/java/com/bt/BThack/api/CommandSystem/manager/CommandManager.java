package com.bt.BThack.api.CommandSystem.manager;



import com.bt.BThack.api.CommandSystem.Exception.CommandException;
import com.bt.BThack.api.CommandSystem.command.ICommand;

import java.util.ArrayList;
import java.util.Arrays;

public final class CommandManager {
    /**
     * all the commands added
     */
    public static final ArrayList<ICommand> commands = new ArrayList<>();

    /**
     * indicates whether we should use {@link String#equals(Object)} or {@link String#equalsIgnoreCase(String)}
     */
    public static boolean ignoresCases = true;

    /**
     * indicates whether we should print out exceptions to the console
     */
    public static boolean debug = false;

    /**
     * @return all the commands added
     */
    public static ArrayList<ICommand> getCommands() { return commands; }

    /**
     * adds commands to {@link #commands}
     * @param commands the commands to add
     */
    public static void addCommands(ICommand... commands) { CommandManager.commands.addAll(Arrays.asList(commands)); }

    /**
     * parses the given lines and executes all of them that match the requirements
     * @param lines the lines to parse
     */
    public static void parseCommand(String... lines) {
        for (String line : lines) {
            String[] split = line.split(" ");
            String cmdName = split[0];
            String[] args = Arrays.copyOfRange(split, 1, split.length);

            for (ICommand command : commands) {
                for (String alias : command.getAliases()) {
                    if (ignoresCases ? cmdName.equalsIgnoreCase(alias) : cmdName.equals(alias)) {
                        try {
                            command.execute(args);
                        } catch (CommandException e) {
                            if (debug) e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}