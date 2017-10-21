import java.io.IOException;
import java.net.Socket;

/**
 *
 * NETWORKS ASSIGNMENT 1
 * Preparatory Exercise 2
 * Server Thread
 *
 *
 * Extend this program so that it will accept multiple connections,
 * in parallel, and display all received traffic on the screen.
 *
 * James Goodliffe 2017
 */


public class serverThread extends Thread{

    private Socket clientSocket;
    private int clientNumber;
    private InputHandler in;
    private OutputHandler out;

    /**
     * Constructor for serverThread
     * @param clientSocket the client socket interface
     * @param clientNumber the number of the client connected to the server
     */
    public serverThread(Socket clientSocket, int clientNumber) {
        this.clientSocket = clientSocket;
        this.clientNumber = clientNumber;
    }


    @Override
    public void run() {
        try {
            in = new InputHandler(clientSocket);
            out = new OutputHandler(clientSocket);

            out.printLine("Welcome to the multithreaded message server. You are client "+clientNumber);


            String data;
            while ((data = in.read()) != null) {
                out.printLine(data);
                System.out.println("Received this from client "+ clientNumber + ": " + data);
            }

            System.err.println("Closed connection with client "+ clientNumber + " at IP " + clientSocket.getInetAddress());
            out.close();
            in.close();
            clientSocket.close();
            stop();

        } catch(IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Used to set a new client ID in the event of a client connecting or disconnecting.
     * @param number - the new client ID
     */
    public void setID(int number) {
        this.clientNumber = number;
    }
}
