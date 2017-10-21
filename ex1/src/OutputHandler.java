import java.io.*;
import java.net.Socket;

public class OutputHandler {
    private PrintStream out;
    private OutputStream outMain;

    public OutputHandler (Socket socket){
        try{
            out = new PrintStream(socket.getOutputStream(),true);
            outMain = socket.getOutputStream();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void close(){
            out.close();
    }

    /**
     * GetContentType - Returns the content type for a given page.
     * @param page - the page requested
     * @return "image/gif" or "text/html" dependent on the request.
     */
    public String getContentType(File page) {
        String file = page.toString();
        String type = "";
        if (file.endsWith(".txt")) {
            type = "text/txt";
        } else if (file.endsWith(".html") || file.endsWith("htm")) {
            type = "text/html; Charset=UTF-8";
        } else if (file.endsWith(".jpg")) {
            type = "image/jpg";
        } else if (file.endsWith(".png")) {
            type = "image/png";
        } else if (file.endsWith(".jpeg")) {
            type = "image/jpeg";
        } else if (file.endsWith(".gif")) {
            type = "image/gif";
        } else if (file.endsWith(".pdf")) {
            type = "application/pdf";
        } else if (file.endsWith(".mp3")) {
            type = "audio/mpeg";
        } else if (file.endsWith(".class")) {
            type = "application/octet-stream";
        } else if (file.endsWith(".mp4")) {
            type = "video/mp4";
        }
        return type;
    }

    //todo - add support for image content types.
    // illegal and incomprehensible urls???
    public void printPage(File page) {
        try{
            String contentType = getContentType(page);

            if(!contentType.equals("text/html; Charset=UTF-8") || !contentType.equals("text/txt")){
                System.out.println("NON-HTML DOCUMENT");

                byte[] bytes = new byte[(int)page.length()];
                FileInputStream fis = new FileInputStream(page);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(bytes, 0, bytes.length);

                long contentLength = page.length();

                out.println("HTTP/1.1 200 OK");
                out.println("Content-Length: " + contentLength);
                out.println("Content-Type: "+contentType);
                out.println("");

                outMain.write(bytes,0,bytes.length);
                outMain.close();
                out.flush();
                out.close();
                fis.close();
            } else {

                System.out.println("HTML OR TEXT DOCUMENT");
                int length = 0;
                FileInputStream fileIn = new FileInputStream(page);
                byte[] bytes = new byte[(int) page.length()];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                while ((length = fileIn.read(bytes)) != -1) {
                    bos.write(bytes, 0, length);
                }
                bos.flush();
                bos.close();

                byte[] data1 = bos.toByteArray();
                String dataStr = new String(data1, "UTF-8");

                //String contentType = getContentType(page);
                long contentLength = page.length();

                out.println("HTTP/1.1 200 OK");
                out.println("Content-Length: " + contentLength);
                out.println("Content-Type: " + contentType);
                out.println("");
                out.println(dataStr);

                out.flush();
                out.close();
                fileIn.close();

//            FileReader fileReader = new FileReader(page);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            long length = page.length();
//            String line;
//
//            String contentType = getContentType(page);
//
//            out.write("HTTP/1.1 200 OK\r\n");
//            out.write("Content-Length: " + length + "\r\n");
//            out.write("Content-Type: "+contentType+"\r\n\r\n\r\n");


//            while((line = bufferedReader.readLine()) != null) {
//                out.write(line +"\n");
//                //System.out.println("Printed something");
//            }

            }
        } catch(IOException e){
            e.printStackTrace();
            System.err.println("Failed to open the file");
        }
        out.flush();
    }

}
