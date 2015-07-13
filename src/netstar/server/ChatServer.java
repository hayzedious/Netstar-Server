package netstar.server;

/**
 * @author AZEEZ TAIWO
 * @version 1.0
 */
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
import javax.naming.BinaryRefAddr;

//This is the chatServer backend logic for the wakakum chat client
public class ChatServer {

    public static ArrayList<Socket> allClientSockets = new ArrayList<>(); //an arrayList of clients' socket
    public static ArrayList<String> currentUsers = new ArrayList<>(); // an arraylist of online users(a static member)
    PrintWriter output = null;
    ServerSocket serverSocket = null;
    final int PORT = 10007;
    static String username = "";

    public void startConnection() throws IOException {
        serverSocket = new ServerSocket(PORT); // a new server Socket
        System.out.println("Listening for client connections...");
        while (true) {
            try {
                Socket clientSock = serverSocket.accept(); //this serverSockect method returns a plain old socket representing a client socket
                allClientSockets.add(clientSock); // adding to the arraylist of sockets
                Scanner input = new Scanner(clientSock.getInputStream());
                username = input.nextLine();
                System.out.println(username + " just connected ");
                addUserName(username);
                ChatServer_Two chat = new ChatServer_Two(clientSock, username);
                Thread x = new Thread(chat); //you allocat each client connection to a thread of execution
                x.start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void addUserName(String x) throws IOException {
        currentUsers.add(x); //adds user to the arraylist of Users
        for (int i = 0; i < allClientSockets.size(); ++i) {
            Socket temp_sock = (Socket) allClientSockets.get(i);
            PrintWriter out = new PrintWriter(temp_sock.getOutputStream());
            //we use "#?:" because its an arraylist and not normal string, so when the client sees that, it knows its
            //normal string but an arraylist
            out.println("#?:" + currentUsers); //prints the list of online users to each client
            out.flush();
        }
    }
}