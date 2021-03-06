package org.cubyte.edumon.client;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.WARNING);
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException e) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(e.getMessage());

            System.exit(1);
        }
        final GlobalKeyListener keyListener = new GlobalKeyListener();
        final GlobalMouseListener mouseListener = new GlobalMouseListener();
        GlobalScreen.addNativeKeyListener(keyListener);
        GlobalScreen.addNativeMouseListener(mouseListener);
        GlobalScreen.addNativeMouseMotionListener(mouseListener);

        final GlobalMicListener micListener = new GlobalMicListener();

        final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            int keystrokes;
            int mouseclicks;
            int mousedistance;
            double micLevel;
            @Override
            public void run() {
                keystrokes = keyListener.fetchStrokes();
                mouseclicks = mouseListener.fetchClicks();
                mousedistance = mouseListener.fetchDistance();
                micLevel = micListener.fetchLevel();
                // TODO: send data
                System.out.println("k: " + keystrokes + " c: " + mouseclicks + " d: " + mousedistance + " l: " + micLevel);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }
}
