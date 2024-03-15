package models;

import java.io.EOFException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/**
 * The Server Class that is responsible in handling the Oxygen and Hydrogen atoms
 * and bonding them together to form a water molecule.
 */
public class Server implements Modem {
    public static final String NAME = "Server";

    // Set the port numbers
    public static final int OXYGEN_PORT = 12345;
    public static final int HYDROGEN_PORT = 8000;
    public static final int THREADS = 32;

    // Create a new Console
    private final Console console;

    // Create a Queue for the Oxygens
    private Queue<Element> oxygens;

    // Create a Queue for the Hydrogens
    private Queue<Element> hydrogens;

    /**
     * Default Server Constructor
     */
    public Server () throws Exception {
        // Create a new Console
        this.console = new Console(NAME);

        // Instantiate Elements
        this.hydrogens = new LinkedList<Element>();
        this.oxygens = new LinkedList<Element>();

        // Start the Oxygen Handler
        new Thread(new OxygenHandler(Server.OXYGEN_PORT)).start();
        // Start the Hydrogen Handler
        new Thread(new HydrogenHandler(Server.HYDROGEN_PORT)).start();
    }

    /**
     * The Centralized Function to Bond Hydrogens and Oxygens
     * @throws Exception
     */
    public void bond() {
        synchronized(this.hydrogens) {
            synchronized(this.oxygens) {
                // If Hydrogens and Oxygens are not enough, abort
                if (this.hydrogens.size() < 2 || this.oxygens.size() < 1) return;
            
                Element h1 = this.hydrogens.poll();
                console.log(h1.bond());

                Element h2 = this.hydrogens.poll();
                console.log(h2.bond());

                Element o = this.oxygens.poll();
                console.log(o.bond());
            }        
        }
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
            this.serverSocket = new ServerSocket(this.port);
        }

        @Override
        public void run() {
            while (true) try {
                // Accept new Oxygen Clients
                console.log("Listening for new Oxygen atoms at port %d.", this.port);

                Socket socket = this.serverSocket.accept();

                // Send Confirmation
                broadcast(socket, "You are connected. Listening for elements.", Server.NAME);
                
                while (!socket.isClosed()) try {
                    // Receive the Element name
                    String element = receive(socket);

                    // Add the element to the ArrayList
                    oxygens.add(new Element(element, socket));

                    // Log the Request
                    console.log(element + ", request, " + console.getTimestamp());

                    // Check if bonding is possible
                    bond();
                }
                catch (Exception e) {
                    if (e instanceof EOFException) 
                        console.log("Oxygen Client disconnected.");
                    else 
                        console.log(e);

                    if (!socket.isClosed()) socket.close();
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
        public HydrogenHandler(int port) throws Exception {
            this.port = port;
            this.serverSocket = new ServerSocket(this.port);
        }

        @Override
        public void run() {
            while (true) try {
                // Accept new Hydrogen Clients
                console.log("Listening for new Hydrogen atoms at port %d.", this.port);

                Socket socket = this.serverSocket.accept();

                // Send Confirmation
                broadcast(socket, "You are connected. Listening for elements.", Server.NAME);

                while (!socket.isClosed()) try {
                    // Receive the Element name
                    String element = receive(socket);

                    // Add the element to the ArrayList
                    hydrogens.add(new Element(element, socket));

                    // Log the Request
                    console.log(element + ", request, " + console.getTimestamp());

                    bond();
                }
                catch (Exception e) {
                    if (e instanceof EOFException) 
                        console.log("Hydrogen Client disconnected.");
                    else 
                        console.log(e);

                    if (!socket.isClosed()) socket.close();
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