
public class RequestLine {
    public String URI;
    public String method;
    public String status;

    public RequestLine(String uri, String method, String status) {
        this.URI = uri;
        this.method = method;
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public String getURI() {
        return URI;
    }

    public String getStatus() {
        return status;
    }

    public String toString(){
        return URI;
    }
}
