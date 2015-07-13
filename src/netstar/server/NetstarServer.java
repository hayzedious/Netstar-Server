package netstar.server;

import java.io.IOException;

/**
 *
 * @author AZEEZ TAIWO
 */
public class NetstarServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //This starts the chat server 
        new ChatServer().startConnection();
    }

}