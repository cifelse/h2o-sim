package models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Oxygen implements Runnable, Modem {
    // The Hostname of the Server
    public static final String HOSTNAME = "localhost";

    // The Number of Oxygens to send
    private int oxygens;

    // Create a new Console
    private Console console;

    /**
     * Default Oxygen Constructor
     */
    public Oxygen(int oxygens) {
        this.oxygens = oxygens;
        this.console = new Console("Oxygen Client");
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
            broadcast(out, this.oxygens);

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

        // Ask the user for the number of Oxygens
        int oxygens = console.input("Enter the number of Oxygen atoms: ").nextInt();

        // Create a new Oxygen Client
        new Thread(new Oxygen(oxygens)).start();
    }
}
