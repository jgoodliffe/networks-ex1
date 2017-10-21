import com.sun.deploy.net.HttpResponse;
import sun.net.www.http.HttpClient;

import java.io.*;
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


    //todo - add support for image content types.
    // illegal and incomprehensible urls???
    public void printPage(File page) {
        try{

            FileReader fileReader = new FileReader(page);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            long length = page.length();
            String line;
            out.write("HTTP/1.1 200 OK\r\n");
            out.write("Content-Length: " + length + "\r\n");
            out.write("Content-Type: "+"*/*"+"\r\n\r\n\r\n");
            while((line = bufferedReader.readLine()) != null) {
                out.write(line +"\n");
                //System.out.println("Printed something");
            }
        } catch(IOException e){
            e.printStackTrace();
            System.err.println("Failed to open the file");
        }
        out.flush();
    }
}
