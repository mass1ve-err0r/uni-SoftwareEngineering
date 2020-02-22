package de.techfak.gse.mbaig;

/*
 * GSERadio - A (wannabe) Festify.rocks copycat powered by VLCJ4
 * @author Mirza Saadat Baig
 */

import java.io.IOException;
import java.util.*;

public final class GSERadio {
    // objs
    static MPlayer player;
    static StreamingServer server;
    // globals
    private static Scanner inputChecker = new Scanner(System.in);
    private static final String BRAND = "===GSERadio 1.0===\n";
    private static final String ARG_GUI_1 = "--gui";
    private static final String ARG_GUI_2 = "-g";
    private static final String ARG_SERVER = "--server";
    private static final String ARG_CLIENT = "--client";
    private static final String IDENTIFIER_PORT = "--streaming=";
    private static final String IDENTIFIER_REST = "--port=";
    private static final String ERROR_CARG = "Contradicting arguments passed! Aborting...";
    private static final String LOCAL_DIR = System.getProperty("user.dir");
    private static final String REGEX_CHAR = "=";
    private static final int STD_PORT = 9000;
    private static final int STD_REST = 8080;
    private static final int PORT_MIN = 1024;
    private static final int PORT_MAX = 49151;
    private static final int ERROR_ARGUMENT = 103;
    private static final int ERROR_PORT = 102;
    private static final int SERVER_ARGS_3 = 3;
    private static final int SERVER_ARGS_4 = 4;
    private static final int ARG3 = 3;

    // empty struct to satisfy checkstyle
    protected GSERadio() { }

    /**
     * main - Der Einstieg in das SE-Project.
     * @param args - Die CMD params
     */
    public static void main(String[] args) throws IOException {
        // multi-arg check through determining length of given args
        if (args.length == 0) {
            // by definition we are launching now in a directory, no additional checks needed
            System.out.println("No folder has been passed!\nLaunching GSERadio in current directory...\n\n");
            player = new MPlayer(LOCAL_DIR, 1);
            System.out.println(BRAND);
            player.playMusic(0);
            uInputManager();
        } else if (args.length == 1) {
            // local GUI
            if (args[0].equals(ARG_GUI_1) || args[0].equals(ARG_GUI_2)) {
                System.out.println("Launching GUI in local folder...");
                GSERadioGUI.main(args, LOCAL_DIR);
                // local server
            } else if (args[0].equals(ARG_SERVER)) {
                server = new StreamingServer(STD_PORT, STD_REST, LOCAL_DIR);
            } else if (args[0].equals(ARG_CLIENT)) {
                GSERadioClientGUI.main(args);
            } else {
                player = new MPlayer(args[0], 1);
                System.out.println(BRAND);
                player.playMusic(0);
                uInputManager();
            }
        } else if (args.length == 2) {
            if (args[0].equals(ARG_GUI_1) || args[0].equals(ARG_GUI_2)) {
                // start GUI with custom path
                System.out.println("Launching GUI with custom path...");
                GSERadioGUI.main(args);
            } else if (args[0].equals(ARG_SERVER)) {
                if (args[1].equals(ARG_CLIENT)) {
                    System.err.println(ERROR_CARG);
                    exitWithCode(ERROR_ARGUMENT);
                } else if (args[1].contains(IDENTIFIER_PORT)) {
                    // server + port, local files
                    String[] content = args[1].split(REGEX_CHAR);
                    int port = Integer.parseInt(content[content.length - 1]);
                    if (port >= PORT_MIN && port <= PORT_MAX) {
                        server = new StreamingServer(port, STD_REST, LOCAL_DIR);
                    } else {
                        exitWithCode(ERROR_PORT);
                    }
                    // parse arg 2 as path
                } else {
                    // push MPlayer into Server just for the ease of it
                    server = new StreamingServer(STD_PORT, STD_REST, args[1]);
                }
            }
        } else if (args.length == SERVER_ARGS_3) {
            if (args[0].equals(ARG_SERVER) && args[1].equals(ARG_CLIENT)) {
                System.err.println(ERROR_CARG);
                exitWithCode(ERROR_ARGUMENT);
            }
            if (args[0].equals(ARG_SERVER) && args[1].contains(IDENTIFIER_PORT)) {
                if (args[2].contains(IDENTIFIER_REST)) {
                    int portREST = Integer.parseInt(args[2].split(REGEX_CHAR)[1]);
                    int port = Integer.parseInt(args[1].split(REGEX_CHAR)[1]);
                    if ((portREST >= PORT_MIN && portREST <= PORT_MAX) && (port >= PORT_MIN && port <= PORT_MAX)) {
                        server = new StreamingServer(port, portREST, LOCAL_DIR);
                    }
                } else {
                    int port = Integer.parseInt(args[1].split(REGEX_CHAR)[1]);
                    if (port >= PORT_MIN && port <= PORT_MAX) {
                        server = new StreamingServer(port, STD_REST, args[2]);
                    }
                }
                //
            }
        } else if (args.length == SERVER_ARGS_4) {
            if (args[0].equals(ARG_SERVER) && args[1].equals(ARG_CLIENT)) {
                System.err.println(ERROR_CARG);
                exitWithCode(ERROR_ARGUMENT);
            } else {
                int port = Integer.parseInt(args[1].split(REGEX_CHAR)[1]);
                int portREST = Integer.parseInt(args[2].split(REGEX_CHAR)[1]);
                if ((portREST >= PORT_MIN && portREST <= PORT_MAX) && (port >= PORT_MIN && port <= PORT_MAX)) {
                    server = new StreamingServer(port, portREST, args[ARG3]);
                } else {
                    exitWithCode(ERROR_PORT);
                }
            }
        } else {
            // arg -err case
            exitWithCode(-1);
        }
    }

    /**
     * uInputManager - The UserInputManager loop for System.in/ stdin.
     */
    private static void uInputManager() {
        //boolean loop = true;
        while (true) {
            String uInput = inputChecker.nextLine();
            switch (uInput) {
                case "exit":
                    player.stopPlayer();
                    //loop = false;
                    exitWithCode(0);
                    break;
                case "song":
                    player.playlist.getSongInfo(0);
                    break;
                case "playlist":
                    player.playlist.getCurrentPlaylist();
                    break;
                default:
                    System.out.println("\n\n-----:GSERadio:-----\n"
                        + "Available commands:\n"
                        + "song - Display current information about the song\n"
                        + "playlist - Show the current running song + the remaining playlist\n"
                        + "exit - Exits the application\n\n"
                    );
                    break;
            }
        }
    }

    static void exitWithCode(int exitCode) {
        System.exit(exitCode);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Additional Debug Functions
    ///////////////////////////////////////////////////////////////////////////


}
