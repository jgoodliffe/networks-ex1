import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpUtils;

import javax.xml.ws.spi.http.HttpHandler;
import java.util.*;

/**
 * HTTP Web Server - Request Handler
 * @author James Goodliffe, 2017
 *
 * Decodes an HTTP v1.1 Request and returns its constituent components.
 */

public class RequestHandler {
    private Scanner input;
    private Map<String, String> headers;
    private String body;
    private RequestLine requestLine;

    /**
     * RequestHandler
     * @param headers the request headers
     * @param body the request body
     * @param requestLine the RequestLine (with URI etc.)
     */
    public RequestHandler(Map<String, String> headers, String body, RequestLine requestLine){
        this.headers = headers;
        this.body = body;
        this.requestLine = requestLine;
    }

    /**
     * getHeaders
     * @return the HTTP Headers
     */
    public Map<String, String> getHeaders(){
        return headers;
    }

    /**
     * getBody
     * @return the HTTP Body
     */
    public String getBody(){
        return body;
    }

    /**
     * getRequestLine
     * @return The HTTP Request Line
     */
    public RequestLine getRequestLine(){
        return requestLine;
    }

    /**
     * toString
     * @return A string of the HTTP Request
     */
    public String toString(){
        return "Request Line: "+ getRequestLine() + '\n' + "Headers: " + getHeaders();
    }

    /**
     * RequestHandler
     * @param lines The HTTP Request
     * @return a RequestHandler formatted request.
     */
    public static RequestHandler parse (List<String> lines){
        //System.out.println("NEW REQUEST");
        String requestLineString = lines.get(0);
        String accept = "";
        for(int i=0; i<lines.size(); i++){
            if (lines.get(i).contains("Accept-Encoding:")){
                accept = lines.get(i);
            }
        }
        //System.out.println(accept);

        String[] requestLineArray = requestLineString.split(" ");
        String method = requestLineArray[0];
        String URI = requestLineArray[1];
        String status = requestLineArray[2];

        //System.out.println("NEW:"+acceptedEncodings);
        boolean gzip = false;
        if(accept.contains("gzip")){
            gzip = true;
        }

        System.out.println(gzip);


        //Create new request line:
        RequestLine requestLine = new RequestLine(URI, method, status, gzip);

        List <String> headerList = lines.subList(1, lines.size()-1);
        Map<String, String> headersMap = new HashMap<>();

        for(int i=0; i<headerList.size(); i++){
            String line = headerList.get(i);
            String[] array = line.split(" ");
            headersMap.put(array[0], array[1]);
        }


        return new RequestHandler(headersMap, "", requestLine);
    }


    /**
     * read
     * @return A line of the HTTP Request.
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
     * close
     * Close the Request handler.
     */
    public void close(){
        input.close();
    }

}
