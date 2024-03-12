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
        // Change the message to include the alias
        message = "[" + alias + "]: " + message;

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        out.writeInt(message.getBytes().length);
        out.write(message.getBytes());
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
        
        out.writeInt(message.getBytes().length);
        out.write(message.getBytes());
        out.flush();
    }

    /**
     * Request an Element to be bonded to the Server
     * @param socket - the socket of the server
     * @param element - The Element
     * @throws Exception
     * @return true if the bonding is successful
     */
    default public boolean request(Socket socket, String element) {
        try {
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        
            out.writeInt(element.getBytes().length);
            out.write(element.getBytes());
            out.flush();

            return true;
        }
        catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
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

        int length = in.readInt();
        byte[] data = new byte[length];
        in.readFully(data);

        return new String(data);
    }
}

