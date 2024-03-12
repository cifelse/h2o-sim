package models;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * The Server Class that is responsible in handling the Oxygen and Hydrogen atoms
 * and bonding them together to form a water molecule.
 */
public class Server implements Modem {
    public static final String NAME = "Server";

    // Set the port numbers
    public static final int OXYGEN_PORT = 12345;
    public static final int HYDROGEN_PORT = 8000;

    // Create a new Console
    private final Console console;

    // Create a Queue for the Oxygens
    private ArrayList<Element> oxygens;

    // Create a Queue for the Hydrogens
    private ArrayList<Element> hydrogens;

    /**
     * Default Server Constructor
     */
    public Server () throws Exception {
        // Create a new Console
        this.console = new Console(NAME);

        // Instantiate Elements
        this.hydrogens = new ArrayList<Element>();
        this.oxygens = new ArrayList<Element>();

        // Start the Oxygen Handler
        new Thread(new OxygenHandler(Server.OXYGEN_PORT)).start();
        // Start the Hydrogen Handler
        new Thread(new HydrogenHandler(Server.HYDROGEN_PORT)).start();
    }

    /**
     * The Class for handling Oxygen requests
     */
    private class OxygenHandler implements Runnable {
        // Server Socket
        private ServerSocket serverSocket;
        // Port
        private int port;

        /**
         * Default OxygenHandler Constructor
         * @throws Exception
         */
        public OxygenHandler(int port) throws Exception {
            this.port = port;
        }

        @Override
        public void run() {
            try {
                this.serverSocket = new ServerSocket(this.port);

                // Accept new Hydrogen Clients
                console.log("Listening for new Oxygen atoms at port %d.", this.port);

                Socket socket = this.serverSocket.accept();

                // Send Confirmation
                broadcast(socket, "You are connected. Listening for elements.", Server.NAME);
                
                while (true) {
                    // Receive the Element name
                    String element = receive(socket);

                    // Add the element to the ArrayList
                    oxygens.add(new Element(element));

                    // Log the Request
                    console.log(element + ", request, " + console.getTimestamp());
                }
            }
            catch (Exception e) {
                console.log(e);
            }
        }
    }

    /**
     * The Class for handling Hydrogen requests
     */
    private class HydrogenHandler implements Runnable {
        // Server Socket
        private ServerSocket serverSocket;
        // Port
        private int port;

        /**
         * Default HydrogenHandler Constructor
         */
        public HydrogenHandler(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try {
                this.serverSocket = new ServerSocket(this.port);

                // Accept new Hydrogen Clients
                console.log("Listening for new Hydrogen atoms at port %d.", this.port);

                Socket socket = this.serverSocket.accept();

                // Send Confirmation
                broadcast(socket, "You are connected. Listening for elements.", Server.NAME);

                while (true) {
                    // Receive the Element name
                    String element = receive(socket);

                    // Add the element to the ArrayList
                    hydrogens.add(new Element(element));

                    // Log the Request
                    console.log(element + ", request, " + console.getTimestamp());
                }
            }
            catch (Exception e) {
                console.log(e);
            }
        }
    }

    /**
     * The Main Method. The Kicker.
     * @param args - The Command Line Arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // Create a new Server
        new Server();
    }
}