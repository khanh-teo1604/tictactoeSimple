package vgu.pe2026.ttt.basic.HTTP;

public class ServiceResult {

    private final int statusCode;
    private final String responseBody;

    public ServiceResult(
            int statusCode,
            String responseBody) {

        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseBody() {
        return responseBody;
    }
}