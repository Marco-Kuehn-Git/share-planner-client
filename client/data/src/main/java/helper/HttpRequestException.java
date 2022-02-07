//Marc Beyer//
package helper;

public class HttpRequestException extends Exception{
    private int status;

    public HttpRequestException(String message, int status) {
        super(message);
        this.status = status;
    }

    public HttpRequestException(Tuple<Integer, String> response) {
        super(response.getValue());
        this.status = response.getKey();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
