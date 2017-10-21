import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputHandler {
    private Scanner input;

    public InputHandler(java.net.Socket socket){
        try {
            InputStream i = socket.getInputStream();
            input = new Scanner(new BufferedInputStream(i), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("I couldn't open: "+socket);
        }

    }

    public String read() {
        String line;
        try{
            line = input.nextLine();
        } catch (NoSuchElementException e){
            line = null;
        }
        return line;
    }

    public void close(){
        input.close();
    }
}
