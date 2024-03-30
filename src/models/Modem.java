package models;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public interface Modem {
    /**
     * The method to broadcast a message to the sockets
     * @param socket - the socket of the receiver
     * @param message - The message to broadcast
     * @param alias - The alias of the user
     * @throws Exception
     */
    default public void broadcast(Socket socket, String message, String alias) {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            String fullMessage = "[" + alias + "]: " + message;
            byte[] bytes = fullMessage.getBytes(StandardCharsets.UTF_8);
            out.writeInt(bytes.length); // Write the length of the message
            out.write(bytes);
            out.flush();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * The method to broadcast a message to the sockets
     * @param socket - the socket of the receiver
     * @param message - The message to broadcast
     * @throws Exception
     */
    default public void broadcast(Socket socket, String message) {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
            out.writeInt(bytes.length); // Write the length of the message
            out.write(bytes);
            out.flush();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Request an Element to be bonded to the Server
     * @param socket - the socket of the server
     * @param element - The Element
     * @throws Exception
     */
    default public boolean request(Socket socket, String element) {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            byte[] bytes = element.getBytes(StandardCharsets.UTF_8);
            out.writeInt(bytes.length); // Write the length of the element
            out.write(bytes);
            out.flush();
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    /**
     * Receive the Element requested by the Client
     * @param socket - the socket of the requester
     * @return the Element name
     * @throws Exception
     */
    default public String receive(Socket socket) throws Exception {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        int length = in.readInt(); // Read the length of the incoming message
        byte[] bytes = new byte[length];
        in.readFully(bytes); // Read the bytes into the byte array
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
