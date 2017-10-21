import java.util.*;

public class RequestHandler {
    private Scanner input;
    private Map<String, String> headers;
    private String body;
    private RequestLine requestLine;

    public RequestHandler(Map<String, String> headers, String body, RequestLine requestLine){
        this.headers = headers;
        this.body = body;
        this.requestLine = requestLine;
    }

    public Map<String, String> getHeaders(){
        return headers;
    }

    public String getBody(){
        return body;
    }

    public RequestLine getRequestLine(){
        return requestLine;
    }

    public String toString(){
        return "Request Line: "+ getRequestLine() + '\n' + "Headers: " + getHeaders();
    }


    public static RequestHandler parse (List<String> lines){
        String requestLineString = lines.get(0);
        String[] requestLineArray = requestLineString.split(" ");
        String method = requestLineArray[0];
        String URI = requestLineArray[1];
        String status = requestLineArray[2];


        RequestLine requestLine = new RequestLine(URI, method, status);

        List <String> headerList = lines.subList(1, lines.size()-1);
        Map<String, String> headersMap = new HashMap<>();

        for(int i=0; i<headerList.size(); i++){
            String line = headerList.get(i);
            String[] array = line.split(" ");
            headersMap.put(array[0], array[1]);
        }


        return new RequestHandler(headersMap, "", requestLine);
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
