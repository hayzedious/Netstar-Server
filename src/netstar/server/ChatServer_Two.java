package netstar.server;

/**
 * @author AZEEZ TAIWO
 * @version 1.0
 */
import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatServer_Two implements Runnable {

    /**
     * This class is a server class to respond to the need of a particular
     * client(in this case , a client thread)
     */
    static Connection conn=null;
    static PreparedStatement ps=null;
    Socket sock, onlineSocket;
    private Scanner input;
    private PrintWriter out;
    String message = "";
    String username = "";
    ChatServer server = new ChatServer();

    public ChatServer_Two(Socket x, String uName) {
        sock = x;
        username = uName;
        connectDB();
    }

    @Override
    public void run() {
        try {
            try {
                input = new Scanner(sock.getInputStream()); //this socket represents a client socket
                out = new PrintWriter(sock.getOutputStream());
                while (true) {
                    if (!input.hasNext()) {
                        return;
                    }
                    message = input.nextLine();
                    System.out.println("It reached here");
                    if(message.contains("PM")){
                        System.out.println(message);
                    String pm_mesg=message.substring(2);
                    String[] x=pm_mesg.split("&");
                    String actor=x[0];
                    String chat=x[1];
                        try {
                            //now insert the data to the database
                            String sql="insert into chat_record (actors, conversation) values(?,?)";
                            ps=conn.prepareStatement(sql);
                            ps.setString(1, actor);
                            ps.setString(2, chat);
                            ps.executeUpdate();
                            System.out.println("Chat has been saved successfully");
                        } catch (SQLException ex) {
                            Logger.getLogger(ChatServer_Two.class.getName()).log(Level.SEVERE, null, ex);
                            System.out.println("Cannot save chat");
                        }
                    }else{
                    System.out.println(username + " " + message);
                    for (int i = 0; i < server.allClientSockets.size(); ++i) {
                        Socket temp_sock = (Socket) server.allClientSockets.get(i);
                        PrintWriter temp_out = new PrintWriter(temp_sock.getOutputStream());
                        temp_out.println(username + ": " + message); //prints the message to the clients
                        temp_out.flush();
                        System.out.println("Sent for " + temp_sock.getLocalAddress().getHostName());
                    }
                    }
                }
            } finally {
                //removedSocket.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    public static void connectDB(){
    try{
    String username="root";
    String password="";
    String url="jdbc:mysql://localhost/netstar_db";
    Class.forName("com.mysql.jdbc.Driver").newInstance();
    conn=DriverManager.getConnection(url, username, password);
        System.out.println("Established connection to the database");
    }catch(Exception ex){
        System.out.println("Can't establish a connection with the database");
    }
    }
}
