import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * NETWORKS ASSIGNMENT 1
 * Preparatory Exercise 3
 *
 * A program to parse an HTTP request and request header
 *
 * James Goodliffe 2017
 */




public class server3 {


    static int port;
    ServerSocket Server3;

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
            System.out.println("Started HTTP Parse Server on port: "+port);
            System.out.println("Ready to accept connections..");


            while(true){
                Socket clientSocket = serverSocket.accept(); //Create arraylist of threads...? (to keep track of number of clients)
                System.out.println("Got a new client: "+ clientSocket.getRemoteSocketAddress());

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
                clients.add(new serverThread(clientSocket, clientNumber));
                clients.get(clientNumber).start();

                //Inform the user of the number of clients connected to the server.
                System.out.println("INFORMATION: There are now " + clients.size() + " clients connected to the server! ");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
