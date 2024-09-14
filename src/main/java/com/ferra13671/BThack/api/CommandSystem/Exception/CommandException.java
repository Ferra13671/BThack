package com.ferra13671.BThack.api.CommandSystem.Exception;

public class CommandException extends Exception {
    /**
     * the message left from the place throwing the exception
     */
    private final String message;

    public CommandException() {
        this("");
    }

    public CommandException(String message) {
        this.message = message;
    }

    /**
     * @return the message
     */
    public final String getMessage() { return message; }
}