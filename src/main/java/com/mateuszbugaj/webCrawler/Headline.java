package com.mateuszbugaj.webCrawler;

public class Headline {
    //todo: should it have also information about key word and language?
    private String URL;
    private String content;
    private String description;

    public Headline(String URL, String content, String description) {
        this.URL = URL;
        this.content = content;
        this.description = description;
    }

    public String getURL() {
        return URL;
    }

    public String getContent() {
        return content;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return content;
    }
}
