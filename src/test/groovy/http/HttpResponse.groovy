package http

class HttpResponse {

    private HttpURLConnection connection

    int getResponseCode() {
        return connection.getResponseCode()
    }

    String getResponseText() {
        return connection.getInputStream().getText()
    }
}
