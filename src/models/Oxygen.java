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

            // Wait for Confirmation first
            console.log(receive(socket));
            
            // Listen to any response from the Server
            console.listen(socket, this.oxygens);

            // Send the Overall Count first
            broadcast(socket, this.oxygens);

            boolean hasError = false;
            
            // Submit all Oxygens to the Server
            for (int i = 1; i <= this.oxygens && !hasError; i++) {
                String element = "O" + i;
                
                // Log each request on the Client side
                console.log(element + ", request, " + console.getTimestamp());

                // Request for bonding to the Server through the Modem
                hasError = request(socket, element);
            }

            // Send EOF to the Server
            if (!hasError) broadcast(socket, "EOF");

            else {
                console.log("An error occurred. Closing the connection.");
                if (!socket.isClosed()) socket.close();
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

        // Use Default Count if -1
        if (oxygens == -1) new Oxygen().run();

        // Create a new Oxygen Client
        else new Oxygen(oxygens).run();
    }
}
