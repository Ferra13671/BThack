package com.ferra13671.BThack.Events.MegaEvents;

/**
 * @author Bebra_tyan(Blue_tyan)
 * @version 1
 */

public class MegaEvents {
    private static final String Name = "MegaEvents";
    private static final String Version = "1.0";
    public static final String Logger = "[" + Name + " " + Version + "]";

    public static void log(String message) {
        System.out.println(Logger + " " + message);
    }
}
