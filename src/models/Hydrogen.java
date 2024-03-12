package models;

import java.net.Socket;

public class Hydrogen implements Runnable, Modem {
    // The Hostname of the Server
    public static final String HOSTNAME = "localhost";

    // The Number of Hydrogens to send
    private int hydrogens;

    /**
     * Create a Hydrogen Client with 2 * 2^20 Hydrogen Molecules.
     */
    public Hydrogen() {
        this.hydrogens = 2 * (int) Math.pow(2, 20);
    }

    /**
     * Create an Hydrogen Client with N Hydrogen Molecules.
     */
    public Hydrogen(int hydrogens) {
        this.hydrogens = hydrogens;
    }

    @Override
    public void run() {
        Console console = new Console("Hydrogen Client");

        try {
            console.log("Connecting to the Server.");

            // Connect to the Server
            Socket socket = new Socket(HOSTNAME, Server.HYDROGEN_PORT);

            // Listen to any response from the Server
            console.listen(socket);

            // Submit all Hydrogens to the Server
            for (int i = 1; i <= this.hydrogens; i++) {
                String element = "H" + i;

                // Request for bonding to the Server through the Modem
                request(socket, element);
                
                // Log each request on the Client side
                console.log(element + ", request, " + console.getTimestamp());
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
        // Ask the user for the number of Hydrogens
        int hydrogens = new Console().input("Enter the number of Hydrogen atoms: ").nextInt();

        // Create a new Hydrogen Client
        new Hydrogen(hydrogens).run();
    }
}
