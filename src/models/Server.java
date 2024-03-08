package models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.SynchronousQueue;

public class Server {
    public static void main(String[] args) throws IOException {
        // Create a new Server
        new Server();
    }

    // Set the port numbers
    public static final int OXYGEN_PORT = 12345;
    public static final int HYDROGEN_PORT = 8000;

    // Create a new Console
    private final Console console = new Console("Server");

    // Create a Queue for the Oxygens
    private final SynchronousQueue<Socket> oxygen = new SynchronousQueue<Socket>();

    // Create a Queue for the Hydrogens
    private final SynchronousQueue<Socket> hydrogen = new SynchronousQueue<Socket>();

    /**
     * Default Server Constructor
     */
    public Server () {
        try {
            // Start the Oxygen and Hydrogen Handlers
            new Thread(new OxygenHandler()).start();
            new Thread(new HydrogenHandler()).start();
        }
        catch (IOException e) {
            console.log(e);
        }
    }

    /**
     * The Class for handling Oxygen requests
     */
    public class OxygenHandler implements Runnable {
        private final ServerSocket serverSocket;

        public OxygenHandler() throws IOException {
            this.serverSocket = new ServerSocket(Server.OXYGEN_PORT);
            console.log("Listening for Oxygen requests at port " + Server.OXYGEN_PORT);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Socket socket = this.serverSocket.accept();

                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                    // Notify Server of Connection
                    System.out.println(in.readUTF());

                    // Broadcast confirmation
                    out.writeUTF("[Master]: You are successfully connected. Wait for instructions.");
                    out.flush();

                    // Add the slave to the list
                    synchronized (oxygen) {
                        oxygen.add(socket);
                    }
                }
                catch (IOException e) {
                    System.out.println("\n" + e);
                }
            }
        }
    }

    /**
     * The Class for handling Hydrogen requests
     */
    public class HydrogenHandler implements Runnable {
        private final ServerSocket serverSocket;

        public HydrogenHandler() throws IOException {
            this.serverSocket = new ServerSocket(Server.HYDROGEN_PORT);
            console.log("Listening for Hydrogen requests at port " + Server.HYDROGEN_PORT);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Socket socket = this.serverSocket.accept();

                    DataInputStream in = new DataInputStream(socket.getInputStream());
                    DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                    // Notify Server of Connection
                    System.out.println(in.readUTF());

                    // Broadcast confirmation
                    out.writeUTF("[Master]: You are successfully connected. Wait for instructions.");
                    out.flush();

                    // Add the slave to the list
                    synchronized (hydrogen) {
                        hydrogen.add(socket);
                    }
                }
                catch (IOException e) {
                    System.out.println("\n" + e);
                }
            }
        }
    }
}