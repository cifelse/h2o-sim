package models;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.SynchronousQueue;

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
    private final Console console = new Console(NAME);

    // Create a Queue for the Oxygens
    private final SynchronousQueue<Socket> oxygen = new SynchronousQueue<Socket>();

    // Create a Queue for the Hydrogens
    private final SynchronousQueue<Socket> hydrogen = new SynchronousQueue<Socket>();

    /**
     * Default Server Constructor
     */
    public Server () throws Exception {
        // Start the Oxygen Handler
        new Thread(new OxygenHandler()).start();
        // Start the Hydrogen Handler
        new Thread(new HydrogenHandler()).start();
    }

    public void bond() {
        try {
            // Get the Oxygen and Hydrogen atoms
            Socket oxygen = this.oxygen.take();
            Socket hydrogen = this.hydrogen.take();
            Socket hydrogen2 = this.hydrogen.take();

            // Notify the Oxygen and Hydrogen atoms
            DataOutputStream out = new DataOutputStream(oxygen.getOutputStream());
            out.writeUTF("[Master]: You are successfully connected. Wait for instructions.");
            out.flush();

            out = new DataOutputStream(hydrogen.getOutputStream());
            out.writeUTF("[Master]: You are successfully connected. Wait for instructions.");
            out.flush();

            // Notify the Server
            console.log("Oxygen and Hydrogen atoms are successfully connected.");
        }
        catch (IOException | InterruptedException e) {
            console.log(e);
        }
    }

    /**
     * The Class for handling Oxygen requests
     */
    private class OxygenHandler implements Runnable {
        // Own Server Socket at Port 12345
        private ServerSocket serverSocket;

        /**
         * Default OxygenHandler Constructor
         * @throws Exception
         */
        public OxygenHandler() throws Exception {
            this.serverSocket = new ServerSocket(Server.OXYGEN_PORT);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    console.log("Listening for Oxygen atoms at port %d.", Server.OXYGEN_PORT);

                    Socket socket = this.serverSocket.accept();

                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                    broadcast(out, NAME, "Request received. Wait for bonding.");

                    // Add the oxygen to the queue
                    synchronized (oxygen) {
                        oxygen.add(socket);
                    }
                }
                catch (Exception e) {
                    console.log(e);
                }
            }
        }
    }

    /**
     * The Class for handling Hydrogen requests
     */
    private class HydrogenHandler implements Runnable {
        // Own Server Socket at Port 8000
        private ServerSocket serverSocket;

        /**
         * Default HydrogenHandler Constructor
         * @throws Exception
         */
        public HydrogenHandler() throws Exception {
            this.serverSocket = new ServerSocket(Server.HYDROGEN_PORT);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    console.log("Listening for Hydrogen atoms at port %d.", Server.HYDROGEN_PORT);

                    Socket socket = this.serverSocket.accept();

                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                    broadcast(out, NAME, "Request received. Wait for bonding.");

                    // Add the hydrogen to the queue
                    synchronized (hydrogen) {
                        hydrogen.add(socket);
                    }
                }
                catch (Exception e) {
                    console.log(e);
                }
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