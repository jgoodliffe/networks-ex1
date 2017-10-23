/**
 * HTTP Web Server - Output Handler
 * @author James Goodliffe, 2017
 *
 * Handles all output related tasks.
 */

import java.io.*;
import java.net.Socket;
import java.util.zip.GZIPOutputStream;

public class OutputHandler {

    //Variable declaration:
    private PrintStream out;
    private OutputStream outMain;

    /**
     * OutputHandler
     * Handles all output related things - actually returning the relevant data to the client!
     * @param socket The client socket to be actioned on.
     */
    public OutputHandler (Socket socket){
        try{
            out = new PrintStream(socket.getOutputStream(),true);
            outMain = socket.getOutputStream();

        } catch (IOException e){ //Should never reach here but just in case
            e.printStackTrace();
        }
    }

    private static byte[] gzipCompress(byte[] uncompressedData) {
        byte[] result = new byte[]{};
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(uncompressedData.length);
             GZIPOutputStream gzipOS = new GZIPOutputStream(bos)) {
            gzipOS.write(uncompressedData);
            gzipOS.close();
            result = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String responseType (File page){
        if (page.getName().contains("404.html")){
            return "HTTP/1.1 404 NOT FOUND";
        }
        if(page.getName().contains("403.html")){
            return "HTTP/1.1 403 FORBIDDEN";
        }
        if(page.getName().contains("500.html")){
            return "HTTP/1.1 500 INTERNAL SERVER ERROR";
        }
        return "HTTP/1.1 200 OK";
    }


    /**
     * Closes the PrintStream.
     */
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

    /**
     * Print Page - Responsible for writing RAW output to the client, actually serving the request.
     * @param page - the page file to be printed
     */
    public void printPage(File page) {
        try{

            //Get content type:
            String contentType = getContentType(page);

            //Check what type of file the document is:
            if(!contentType.equals("text/html; Charset=UTF-8")){
                //If File isn't an HTML/Text Document:
                //System.out.println("NON-HTML DOCUMENT");

                //Set up byte array and Buffered input stream
                //Read from file into byte array:
                byte[] bytes = new byte[(int)page.length()];
                FileInputStream fis = new FileInputStream(page);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(bytes, 0, bytes.length);

                //Acquire length of page:
                long contentLength = page.length();


                //Write the HTTP Header via the PrintStream:
                out.println(responseType(page));
                out.println("Content-Length: " + contentLength);
                out.println("Content-Type: "+contentType);
                out.println("");

                //Write document directly to Output stream:
                outMain.write(bytes,0,bytes.length);

                //Close + Exit:
                outMain.close();
                out.flush();
                out.close();
                fis.close();

            } else { //If file is HTML or TEXT document:

                //Set up byte array and input handling:
                int length = 0;
                FileInputStream fileIn = new FileInputStream(page);
                byte[] bytes = new byte[(int) page.length()];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                //Read the bytes of the file into a byte array:
                while ((length = fileIn.read(bytes)) != -1) {
                    bos.write(bytes, 0, length);
                }
                bos.flush();
                bos.close();

                //Write the byte array to a single string which can be printed:
                byte[] data1 = bos.toByteArray();
                String dataStr = new String(data1, "UTF-8");

                //Acquire length of content.
                long contentLength = page.length();


                //Returning the HTTP Header:
                out.println(responseType(page));
                out.println("Content-Length: " + contentLength);
                out.println("Content-Type: " + contentType);
                out.println("");

                //Print page content:
                out.println(dataStr);

                //Finally flushing output before closing the output.
                out.flush();
                out.close();
                fileIn.close();

            }
        } catch(IOException e){ //Shouldn't ever reach here but just in case!
            System.err.println("Error opening file to be sent: ");
            e.printStackTrace();

            //Return a 500 Error:
            File file = new File("ex1/web/500.html");
            printPage(file);
        }
        out.flush();
    }


    //Dealing with clients that are able to support GZip:

    /**
     * printPageGZip
     * Serving a page to a client that is able to support gZip compression
     * @param page the requested page.
     */
    public void printPageGZip(File page) {
        try{
            //Get content type:
            String contentType = getContentType(page);

            //Check what type of file the document is:
            if(!contentType.equals("text/html; Charset=UTF-8")){
                //If File isn't an HTML/Text Document:


                //Set up byte array and Buffered input stream
                //Read from file into byte array:
                byte[] bytes = new byte[(int)page.length()];
                FileInputStream fis = new FileInputStream(page);
                BufferedInputStream bis = new BufferedInputStream(fis);
                bis.read(bytes, 0, bytes.length);

                //As we're outputting in GZip, compress the content:
                byte[] compressedBytes = gzipCompress(bytes);

                //Acquire length of compressed page:
                long contentLength = compressedBytes.length;

                //Write the HTTP Header via the PrintStream:
                out.println(responseType(page));
                out.println("Content-Length: " + contentLength);
                out.println("Content-Type: "+contentType);
                out.println("Content-Encoding: gzip");
                out.println("");

                //Write document directly to Output stream:
                outMain.write(compressedBytes,0,compressedBytes.length);

                //Close + Exit:
                outMain.close();
                out.flush();
                out.close();


            } else { //If file is HTML or TEXT document:

                //Set up byte array and input handling:
                int length = 0;
                FileInputStream fileIn = new FileInputStream(page);
                byte[] bytes = new byte[(int) page.length()];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                //Read the bytes of the file into a byte array:
                while ((length = fileIn.read(bytes)) != -1) {
                    bos.write(bytes, 0, length);
                }

                //As we're outputting in GZip, output the compressed page:
                byte[] compressedBytes = gzipCompress(bytes);

                //Acquire length of compressed content.
                long contentLength = compressedBytes.length;

                //Returning the HTTP Header:
                out.println(responseType(page));
                out.println("Content-Length: " + contentLength);
                out.println("Content-Type: " + contentType);
                out.println("Content-Encoding: gzip");
                out.println("");

                //Print page content
                outMain.write(compressedBytes);

                //Finally flushing output before closing the output.
                outMain.close();
                out.flush();
                out.close();
                fileIn.close();

            }
        } catch(IOException e){ //Shouldn't ever reach here but just in case!
            System.err.println("Error opening file to be sent: ");
            e.printStackTrace();

            //Return a 500 Error:
            File file = new File("ex1/web/500.html");
            printPage(file);
        }
        out.flush();
    }
}
