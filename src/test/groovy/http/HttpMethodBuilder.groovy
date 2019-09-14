package http

class HttpMethodBuilder {

    HeadersBodyBuilder get(String url) {
        method(HttpMethod.GET, url)
    }

    HeadersBodyBuilder post(String url) {
        method(HttpMethod.POST, url)
    }

    HeadersBodyBuilder put(String url) {
        method(HttpMethod.PUT, url)
    }

    HeadersBodyBuilder patch(String url) {
        method(HttpMethod.PATCH, url)
    }

    HeadersBodyBuilder delete(String url) {
        method(HttpMethod.DELETE, url)
    }

    HeadersBodyBuilder method(HttpMethod method, String url) {
        assert url
        return new HeadersBodyBuilder(url.toURL(), method)
    }
}
