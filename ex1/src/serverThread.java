import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * NETWORKS ASSIGNMENT 1
 * HTTP SERVER
 * Server Thread
 *
 * Instantiated each time the server receives a new request to facilitate multiple requests
 * in parallel.
 *
 * Handles an HTTP v1.1 request and returns a valid HTTP v1.1 response.
 * @author James Goodliffe 2017
 */


public class serverThread extends Thread{

    private Socket clientSocket;
    private int clientNumber;
    private InputHandler in;
    private OutputHandler out;
    private RequestHandler r;
    private BufferedReader bufferedReader;
    private String lineOfData;
    private FileServer filesvr;

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
            //Receive and decode client request:
            List<String> receivedData = new ArrayList<String>();
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            in = new InputHandler(clientSocket);
            out = new OutputHandler(clientSocket);
            filesvr = new FileServer();

            //Read each line of data of the request:
            lineOfData = bufferedReader.readLine();
            while (lineOfData.length()>0) {
                receivedData.add(lineOfData);
                lineOfData = bufferedReader.readLine();
            }

            //Use RequestHandler to parse line of request:
            RequestHandler r = RequestHandler.parse(receivedData);

            //Dealing with a GET request:
            if(r.getRequestLine().getMethod().equals("GET")){

                String requestedFile = r.getRequestLine().URI;

                //If a null url - show the index. Else, look for the requested file.
                switch (requestedFile){
                    case "/":   out.printPage(filesvr.getIndex());
                                break;
                    case "/..": out.printPage(filesvr.get403());
                                break;
                    default:    out.printPage(filesvr.getFile(requestedFile));
                                break;
                }
            }

            //I haven't implemented POST Support.
            if(r.getRequestLine().getMethod().equals("POST")){
                System.err.println("POST Request received.");
            }

            //Finally close all relevant utilities when we've fulfilled the request.
            out.close();
            in.close();
            bufferedReader.close();
            clientSocket.close();
            stop();

        } catch(IOException e){ //Should never reach here.
            out.printPage(filesvr.get500()); //Show 500 Error.
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
