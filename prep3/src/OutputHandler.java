import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class OutputHandler {
    private PrintWriter out;

    public OutputHandler (Socket socket){
        try{
            out = new PrintWriter(socket.getOutputStream(),true);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void close(){
        out.close();
    }

    public void printLine(Object x){
        out.println(x);
        out.flush();
    }
}
