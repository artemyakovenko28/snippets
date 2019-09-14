package http

class HeadersBodyBuilder {

    private URL url
    private HttpMethod httpMethod
    private HttpHeaders httpHeaders = new HttpHeaders()

    HeadersBodyBuilder(URL url, HttpMethod httpMethod) {
        this.url = url
        this.httpMethod = httpMethod
    }

    HeadersBodyBuilder header(String name, String... values) {
        values.each {
            httpHeaders.add(name, it)
        }
        return this
    }

    HttpResponse body(String body) {

    }

    HttpResponse noBody() {

    }
}