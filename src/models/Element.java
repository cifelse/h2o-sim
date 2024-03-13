package models;

import java.net.Socket;

/**
 * The Element class for containing either Hydrogen or Oxygen with their respective client sockets
 */
public class Element implements Modem {
    // Element Symbol
    private final String name;

    // The Requester
    private final Socket requester;

    /**
     * Create an element with its name and requesters
     * @param name
     */
    public Element(String name, Socket requester) {
        this.name = name;
        this.requester = requester;
    }

    public String getName() {
        return this.name;
    }

    public Socket getRequester() {
        return this.requester;
    }

    public String bond() {
        String message = this.name + ", bonded, " + Console.getTimeStamp();

        try {
            broadcast(this.requester, message, Server.NAME);
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return message;
    }
}
