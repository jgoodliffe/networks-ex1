import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
    private RequestHandler r;
    private BufferedReader bufferedReader;
    private String lineOfData;

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
            List<String> receivedData = new ArrayList<String>();
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            in = new InputHandler(clientSocket);
            out = new OutputHandler(clientSocket);

            //out.printLine("Welcome to the multithreaded message server. You are client "+clientNumber);


            lineOfData = bufferedReader.readLine();
            while (lineOfData.length()>0) {
                receivedData.add(lineOfData);
                lineOfData = bufferedReader.readLine();
                //System.out.println("Received this from client "+ clientNumber + ": " );
            }

            //bufferedReader.readLine();
            //System.out.println("HELLO MY NAME IS JAMIE :D");

//            String bodyLine = bufferedReader.readLine();
//            while(bodyLine != null && bodyLine.length() >0){
//                out.printLine(bodyLine);
//                bodyLine = bufferedReader.readLine();
//            }

            RequestHandler r = RequestHandler.parse(receivedData);
            if(r.getRequestLine().getMethod().equals("GET")){
                out.printLine("HTTP/1.1 200 OK");
                System.out.println("HTTP/1.1 200 OK");
                out.printLine("CONTENT TYPE: text/HTML");
                System.out.println("CONTENT TYPE: text/HTML");
                out.printLine("\r\n ");
                System.out.println(" ");
                out.printLine("<p>GET REQUEST: </p><p>" + r + "</p>");
                System.out.println("<p>GET REQUEST: </p><p>" + r + "</p>");

            }

            if(r.getRequestLine().getMethod().equals("POST")){
                out.printLine("HTTP/1.1 200 OK");
                System.out.println("HTTP/1.1 200 OK");
                out.printLine("CONTENT TYPE: text/HTML");
                System.out.println("CONTENT TYPE: text/HTML");
                out.printLine(" \r\n");
                System.out.println(" ");
                out.printLine("<p>POST REQUEST: </p><p>" + r + "</p>");
                System.out.println("<p>POST REQUEST: </p><p>" + r + "</p>");
            }


            System.err.println("Closed connection with client "+ clientNumber + " at IP " + clientSocket.getInetAddress());
            out.close();
            in.close();
            bufferedReader.close();
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
