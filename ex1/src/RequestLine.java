/**
 * HTTP Web Server - Request Line
 * @author  Goodliffe, 2017
 *
 * Decodes an HTTP v1.1 RequestLine and returns its constituent components:
 */
public class RequestLine {
    public String URI;
    public String method;
    public String status;

    public RequestLine(String uri, String method, String status) {
        this.URI = uri;
        this.method = method;
        this.status = status;
    }

    /**
     * getMethod
     * @return the Method
     */
    public String getMethod() {
        return method;
    }

    /**
     * getURI
     * @return the URI
     */
    public String getURI() {
        return URI;
    }

    /**
     * getStatus
     * @return the Status
     */
    public String getStatus() {
        return status;
    }

    /**
     * toString
     * @return The URI
     */
    public String toString(){
        return URI;
    }
}
