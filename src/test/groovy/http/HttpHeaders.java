package http;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HttpHeaders {

    private final Map<String, List<String>> headers;

    public HttpHeaders() {
        this(new LinkedHashMap<>());
    }

    public HttpHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public void add(String headerName, String headerValue) {
        List<String> headerValues = this.headers.computeIfAbsent(headerName, k -> new LinkedList<>());
        headerValues.add(headerValue);
    }

    public void set(String headerName, String headerValue) {
        List<String> headerValues = new LinkedList<>();
        headerValues.add(headerValue);
        this.headers.put(headerName, headerValues);
    }
}
