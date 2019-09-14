package http

import groovy.json.JsonBuilder

class MessageAssertionBuilder {

    private prefix

    MessageAssertionBuilder(prefix) {
        this.prefix = prefix
    }

    void assertMessage(String path) {
        String content = new File("${prefix}${path}").getText()
        println new JsonBuilder(content).toString()
    }

    public static void main(String[] args) {
        String path = "example"
        String content = new File("${path}.json.groovy").getText()
        println new JsonBuilder(content).toString()
    }
}
