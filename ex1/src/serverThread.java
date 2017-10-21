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

//    public void findPage(String requestedFile){
//        //Attempt to look for and get file:
//        if(filesvr.getFile(requestedFile)){
//            out.printPage(filesvr.getFile());
//        }
//        //If the file cannot be found:
//        else{
//            out.printPage(filesvr.get404());
//        }
//    }

    @Override
    public void run() {
        try {
            List<String> receivedData = new ArrayList<String>();
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            in = new InputHandler(clientSocket);
            out = new OutputHandler(clientSocket);
            filesvr = new FileServer();


            lineOfData = bufferedReader.readLine();
            while (lineOfData.length()>0) {
                receivedData.add(lineOfData);
                lineOfData = bufferedReader.readLine();
            }

            RequestHandler r = RequestHandler.parse(receivedData);
            if(r.getRequestLine().getMethod().equals("GET")){
                System.out.println("Valid GET request received.");


                String requestedFile = r.getRequestLine().URI;
                System.out.println("Requested Directory: "+requestedFile);

                switch (requestedFile){
                    case "/":   out.printPage(filesvr.getIndex());
                                break;
                    default:    out.printPage(filesvr.getFile(requestedFile));
                                break;
                }

            }

            if(r.getRequestLine().getMethod().equals("POST")){
                System.out.println("POST Request received.");
            }

            //System.err.println("Closed connection with client "+ clientNumber + " at IP " + clientSocket.getInetAddress());

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
