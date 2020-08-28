package by.ttre16.enterprise.exception;

public class ErrorInfo {
    private final String url;
    private final ErrorType errorType;
    private final String detail;

    public ErrorInfo(CharSequence url, ErrorType errorType,
            Throwable exception) {
        this.url = url.toString();
        this.errorType = errorType;
        this.detail = exception.getMessage();
    }

    @Override
    public String toString() {
        return "ErrorInfo{" +
                "url='" + url + '\'' +
                ", errorType=" + errorType +
                ", detail='" + detail + '\'' +
                '}';
    }
}
