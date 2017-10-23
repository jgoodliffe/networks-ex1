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
    public Boolean gzip;

    public RequestLine(String uri, String method, String status, Boolean gzip) {
        this.URI = uri;
        this.method = method;
        this.status = status;
        this.gzip = gzip;
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
     * getGzip
     * @return gzip status
     */
    public Boolean getGzip(){
        return gzip;
    }

    /**
     * toString
     * @return The URI
     */
    public String toString(){
        return URI;
    }
}
