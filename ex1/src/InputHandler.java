import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
/**
 * HTTP Web Server - Input Handler
 * @author James Goodliffe, 2017
 *
 * Reads an HTTP v1.1 Request from an InputStream
 */
public class InputHandler {
    private Scanner input;

    /**
     * InputHandler Constructor
     * @param socket - the socket from which the input is collected.
     */
    public InputHandler(java.net.Socket socket){
        try {
            InputStream i = socket.getInputStream();
            input = new Scanner(new BufferedInputStream(i), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("I couldn't open: "+socket);
        }

    }

    /**
     * read
     * @return a line of the HTTP Request
     */
    public String read() {
        String line;
        try{
            line = input.nextLine();
        } catch (NoSuchElementException e){
            line = null;
        }
        return line;
    }

    /**
     * close - Closes the input handler.
     */
    public void close(){
        input.close();
    }
}
