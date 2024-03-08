package models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Hydrogen implements Runnable, Modem {
    // The Hostname of the Server
    public static final String HOSTNAME = "localhost";

    // The Number of Hydrogens to send
    private int hydrogens;

    // Create a new Console
    private Console console;

    /**
     * Default Hydrogen Constructor
     */
    public Hydrogen(int hydrogens) {
        this.hydrogens = hydrogens;
        this.console = new Console("Hydrogen Client");
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(HOSTNAME, Server.HYDROGEN_PORT);

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            // Receive Connection Confirmation
            String message = receive(in);

            // Notify the Server how many Hydrogens are there
            broadcast(out, this.hydrogens);

            // Log all messages sent by the Server until EOM
            while (!message.contains("EOM")) {
                console.log(message);

                // Receive the next message
                message = receive(in);
            }

            socket.close();
        }
        catch (Exception e) {
            console.log("Server died. Error found: " + e.getMessage());
        }
    }

    /**
     * The Main Method. The Kicker.
     * @param args - The Command Line Arguments
     * @throws Exception
     */
    public static void main(String[] args) throws IOException {
        Console console = new Console();

        // Ask the user for the number of Hydrogens
        int hydrogens = console.input("Enter the number of Hydrogen atoms: ").nextInt();

        // Create a new Hydrogen Client
        new Thread(new Hydrogen(hydrogens)).start();
    }
}
