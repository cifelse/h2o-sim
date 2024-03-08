package models;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface Modem {
    /**
     * The method to broadcast a message to the sockets
     * @param out - The DataOutputStream
     * @param alias - The alias of the user
     * @param message - The message to broadcast
     * @throws Exception
     */
    default public void broadcast(DataOutputStream out, String alias, String message) throws Exception {
        out.writeUTF("[" + alias + "]: " + message);
        out.flush();
    }

    default public void broadcast(DataOutputStream out, String message) throws Exception {
        out.writeUTF("[Anonymous]: " + message);
        out.flush();
    }

    default public void broadcast(DataOutputStream out, int number) throws Exception {
        out.writeInt(number);
        out.flush();
    }

    /**
     * The method to receive a message from the sockets
     * @param in - The DataInputStream
     * @return Byte
     * @throws Exception
     */
    default public String receive(DataInputStream in) throws Exception {
        return in.readUTF();
    }
}