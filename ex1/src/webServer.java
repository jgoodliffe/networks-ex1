import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * NETWORKS ASSIGNMENT 1
 * MAIN EXERCISE
 *
 * A a web server which interacts correctly with a web browser,
 * serving content from the file system.
 * An index page is provided when visited with a null URI.
 * Handles subsequent requests arising from clicking on the items in the index.
 *
 * @author James Goodliffe 2017
 */

public class webServer {


    static int port;
    ServerSocket webServer;

    /**
     * Server Main Method
     * Launches the server - Handles multiple connections in parallel via Threading.
     * <p>
     *     Server Threads are stored in an ArrayList so that it is possible to keep track of the number of users connected to the server.
     * </p>
     * @param args
     */
    public static void main(String[] args){
        ArrayList<serverThread> clients;

        //Validation to check against correct number of arguments.
        if(args.length != 1){
            System.err.println("Error: An incorrect number of arguments were supplied. Make sure you provide a Port Number!");
            System.exit(1);
        }

        //Set port number from Argument supplied to program.
        port = Integer.parseInt(args[0]);

        try {
            clients = new ArrayList<>();
            ServerSocket serverSocket = new ServerSocket(port,50,  InetAddress.getByAddress(new byte[] {0x7f, 0x00, 0x00, 0x01}));
            System.out.println("Started Web Server on port: "+port);


            while(true){
                Socket clientSocket = serverSocket.accept();
                //System.out.println("NEW REQUEST FROM: "+ clientSocket.getRemoteSocketAddress());

                //Removing inactive serverThread instances each time a new client attempts to connect.
                for(int i=0; i<clients.size(); i++){
                    if(!clients.get(i).isAlive()){
                        clients.remove(i);
                    }
                }
                //Update respective serverThread IDs
                for(int i=0; i<clients.size(); i++){
                    clients.get(i).setID(i);
                }

                //Keeping track of the number of clients connected.
                int clientNumber = clients.size();

                //Start a new thread to handle the client:
                clients.add(new serverThread(clientSocket, clientNumber));
                clients.get(clientNumber).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
