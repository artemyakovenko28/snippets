package http

import groovy.json.JsonBuilder
import groovy.json.JsonOutput
import spock.lang.Specification

class HttpRequestTest extends Specification {

    private static final BASE_URL = "https://my-json-server.typicode.com/typicode/demo"

    static String encodeQueryParams(String target) {
        return URLEncoder.encode(target, "UTF-8")
    }

    def "get request"() {
        HttpURLConnection get = "${BASE_URL}/posts".toURL()
                .openConnection() as HttpURLConnection
        int responseCode = get.getResponseCode()
        String responseBody = get.getInputStream().getText()

        expect:
        responseCode == 200
        responseBody
    }

    def "post request"() {
        String requestBody = new JsonBuilder([["id": 4, "title": "Post 4"]]).toPrettyString()

        HttpURLConnection post = "${BASE_URL}/posts".toURL()
                .openConnection() as HttpURLConnection
        post.setRequestMethod("POST")
        post.setDoOutput(true)
        post.setRequestProperty("Content-Type", "application/json")
        post.getOutputStream().write(requestBody.getBytes("UTF-8"))

        post.connect()

        int responseCode = post.getResponseCode()
        String responseBody = JsonOutput.prettyPrint(post.getInputStream().getText())

        expect:
        responseCode == 201
        requestBody == responseBody
    }

    def "put request"() {
        String requestBody = new JsonBuilder(["id": 3, "title": "Putted post"]).toPrettyString()

        HttpURLConnection put = "${BASE_URL}/posts/3".toURL()
                .openConnection() as HttpURLConnection
        put.setRequestMethod("PUT")
        put.setDoOutput(true)
        put.setRequestProperty("Content-Type", "application/json")
        put.getOutputStream().write(requestBody.getBytes("UTF-8"))

        int responseCode = put.getResponseCode()
        String responseBody = JsonOutput.prettyPrint(put.getInputStream().getText())

        expect:
        responseCode == 200
        requestBody == responseBody
    }

    def "patch request"() {
        String requestBody = new JsonBuilder(["title": "Patched post"]).toPrettyString()
        String expectedResponseBody = new JsonBuilder(["title": "Patched post", "id": 3]).toPrettyString()

        HttpURLConnection patch = "${BASE_URL}/posts/3".toURL()
                .openConnection() as HttpURLConnection
        patch.setRequestMethod("PUT")
        patch.setDoOutput(true)
        patch.setRequestProperty("Content-Type", "application/json")
        patch.getOutputStream().write(requestBody.getBytes("UTF-8"))

        int responseCode = patch.getResponseCode()
        String responseBody = JsonOutput.prettyPrint(patch.getInputStream().getText())

        expect:
        responseCode == 200
        responseBody == expectedResponseBody
    }

    def "delete request"() {
        String expectedResponseBody = "{}"

        HttpURLConnection delete = "${BASE_URL}/posts/3".toURL()
                .openConnection() as HttpURLConnection
        delete.setRequestMethod("DELETE")

        int responseCode = delete.getResponseCode()
        String responseBody = delete.getInputStream().getText()

        expect:
        responseCode == 200
        responseBody == expectedResponseBody
    }
}
