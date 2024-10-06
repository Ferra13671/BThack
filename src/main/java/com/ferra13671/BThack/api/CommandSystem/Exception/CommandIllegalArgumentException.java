package com.ferra13671.BThack.api.CommandSystem.Exception;

public class CommandIllegalArgumentException extends CommandException {
    public CommandIllegalArgumentException() { this(""); }
    public CommandIllegalArgumentException(String message) { super(message); }
}