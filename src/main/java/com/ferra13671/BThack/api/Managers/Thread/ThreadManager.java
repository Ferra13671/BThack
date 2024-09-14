package com.ferra13671.BThack.api.Managers.Thread;

public class ThreadManager {

    private ThreadManager() {}


    public static void startNewThread(IThread iThread) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                iThread.start(this);
            }
        };

        thread.start();
    }
}
