package models;

import java.net.Socket;

/**
 * A special type of ArrayList made for Elements or Atoms
 */
public class Element implements Modem {
    // Element Symbol
    private final String name;

    // The Requester
    private Socket requester;

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

    public String bond(String timestamp) {
        String message = "[Server]: " + name + ", bonded, " + timestamp;
        try {
            broadcast(requester, message);
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return message;
    }
}
