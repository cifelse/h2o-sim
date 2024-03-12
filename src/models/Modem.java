package models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public interface Modem {
    /**
     * The method to broadcast a message to the sockets
     * @param socket - the socket of the receiver
     * @param message - The message to broadcast
     * @param alias - The alias of the user
     * @throws Exception
     */
    default public void broadcast(Socket socket, String message, String alias) throws Exception {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF("[" + alias + "]: " + message);
        out.flush();
    }

    /**
     * The method to broadcast a message to the sockets
     * @param socket - the socket of the receiver
     * @param message - The message to broadcast
     * @throws Exception
     */
    default public void broadcast(Socket socket, String message) throws Exception {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF(message);
        out.flush();
    }

    /**
     * Request an Element to be bonded to the Server
     * @param socket - the socket of the server
     * @param element - The Element
     * @throws Exception
     */
    default public void request(Socket socket, String element) throws Exception {
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF(element);
        out.flush();
    }

    /**
     * Receive the Element requested by the Client
     * @param socket - the socket of the requester
     * @return the Element name
     * @throws Exception
     */
    default public String receive(Socket socket) throws Exception {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        return in.readUTF();
    }
}

