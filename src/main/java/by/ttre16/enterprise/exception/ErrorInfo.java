package by.ttre16.enterprise.exception;

public class ErrorInfo {
    private final String url;
    private final ErrorType errorType;
    private final String details;

    public ErrorInfo(CharSequence url, ErrorType errorType,
            CharSequence details) {
        this.url = url.toString();
        this.errorType = errorType;
        this.details = details.toString();
    }

    @Override
    public String toString() {
        return "ErrorInfo{" +
                "url='" + url + '\'' +
                ", errorType=" + errorType +
                ", detail='" + details + '\'' +
                '}';
    }
}
