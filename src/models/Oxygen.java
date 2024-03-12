package models;

import java.net.Socket;

public class Oxygen implements Runnable, Modem {
    // The Hostname of the Server
    public static final String HOSTNAME = "localhost";

    // The Number of Oxygens to send
    private int oxygens;

    /**
     * Create an Oxygen Client with 2^20 Oxygen Molecules.
     */
    public Oxygen() {
        this.oxygens = (int) Math.pow(2, 20);
    }

    /**
     * Create an Oxygen Client with N Oxygen Molecules.
     */
    public Oxygen(int oxygens) {
        this.oxygens = oxygens;
    }

    @Override
    public void run() {
        Console console = new Console("Oyxgen Client");

        try {
            console.log("Connecting to the Server");

            // Connect to the Server
            Socket socket = new Socket(HOSTNAME, Server.OXYGEN_PORT);

            // Listen to any response from the Server
            listen(socket, console);

            // Submit all Oxygens to the Server
            for (int i = 1; i <= this.oxygens; i++) {
                System.out.println("O" + i + ", request, " + console.getTimestamp());
                broadcast(socket, "O" + i);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * The Main Method. The Kicker.
     * @param args - The Command Line Arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // Ask the user for the number of Oxygens
        int oxygens = new Console().input("Enter the number of Oxygen atoms: ").nextInt();

        // Create a new Oxygen Client
        new Oxygen(oxygens).run();
    }
}
