package com.mateuszbugaj.webCrawler;

import java.time.LocalDate;

public class Headline {
    private String URL;
    private String content;
    private String description;
    private String language;
    private LocalDate timeRangeStart;
    private LocalDate timeRangeStop;

    public Headline(String URL, String content, String description, String language) {
        this(URL, content, description, language, null,null );
    }

    public Headline(String URL, String content, String description, String language, LocalDate timeRangeStart, LocalDate timeRangeStop) {
        this.URL = URL;
        this.content = content;
        this.description = description;
        this.language = language;
        this.timeRangeStart = timeRangeStart;
        this.timeRangeStop = timeRangeStop;
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

    public String getLanguage() {
        return language;
    }

    public LocalDate getTimeRangeStart() {
        return timeRangeStart;
    }

    public LocalDate getTimeRangeStop() {
        return timeRangeStop;
    }

    @Override
    public String toString() {
        return "\n"+content;
    }

}
