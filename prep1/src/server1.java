import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * NETWORKS ASSIGNMENT 1
 * Preparatory Exercise 1
 *
 * A program which accepts a single incoming connection,
 * using listen() and accept(),
 * displays all received data on the screen.
 *
 * James Goodliffe 2017
 */




public class server1 {

    static int port;
    ServerSocket Server1;

    public static void main(String[] args){

        if(args.length != 1){
            System.err.println("Error: An incorrect number of arguments were supplied. Make sure you provide a Port Number!");
            System.exit(1);
        }

        port = Integer.parseInt(args[0]);

        try {
            ServerSocket serverSocket = new ServerSocket(port,50,  InetAddress.getByAddress(new byte[] {0x7f, 0x00, 0x00, 0x01}));
            System.out.println("Started Server on port: "+port);


            while(true){
                System.out.println("Waiting for a connection..");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Got a new client: "+ clientSocket.getRemoteSocketAddress());

                InputHandler in = new InputHandler(clientSocket);
                OutputHandler out = new OutputHandler(clientSocket);

                String data;
                while((data = in.read()) != null){
                    out.printLine(data);
                    System.out.println("Received this from client: "+ data);
                }

                System.err.println("Closed connection with client: " + clientSocket.getInetAddress());
                out.close();
                in.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
