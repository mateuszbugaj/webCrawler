package com.mateuszbugaj.webCrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NewsSearcher{
    private static Logger logger = LogManager.getLogger(NewsSearcher.class);
    private WebDriver driver;
    private int maxHeadlines = 10; // default value
    private LocalDate timeRangeStart;
    private LocalDate timeRangeStop;
    private Period interval;

    public static NewsSearcher get(){
        return new NewsSearcher();
    }

    public NewsSearcher (){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=en-ca"/*,"headless"*/); // to enable english version, to enable mode without window
        driver = new ChromeDriver(options);
        //driver.manage().window().fullscreen();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    /**
     * Set time range to search in specific time periods by intervals
     * @param after
     * @param before
     * @param intervalInDays
     * @return
     */
    public NewsSearcher timeRange(String after, String before, int intervalInDays){
        interval = Period.ofDays(intervalInDays);
        logger.debug("Set interval: " + interval.getDays() + " days");
        return timeRange(before, after);
    }

    public NewsSearcher timeRange(String before, String after){
        this.timeRangeStart = LocalDate.parse(after);
        this.timeRangeStop = LocalDate.parse(before);
        logger.debug("Set range: " + timeRangeStart + " - " + timeRangeStop);
        return this;
    }

    public NewsSearcher maxHeadlines(int max){
        maxHeadlines = max;
        logger.debug("Set max headlines per search: " + maxHeadlines);
        return this;
    }

    public List<Headline> search(String word, String language){
        String languageCode = LanguageCodes.valueOf(language).getCode();
        List<Headline> headlines = new ArrayList<>();
        String timeRangeAnnotation;
        String customURL;

        if(interval!=null) {
            logger.debug("Headlines from " + timeRangeStart + " to " + timeRangeStop + " with interval equal " + interval.getDays() + " days");
            LocalDate newTimeRangeStart = timeRangeStart;
            LocalDate newTimeRangeStop = timeRangeStop.plus(interval);
            do {
                logger.debug("Headlines from " + newTimeRangeStart + " to " + newTimeRangeStop);
                timeRangeAnnotation = String.format("%%20before:%s%%20after:%s", newTimeRangeStart, newTimeRangeStop);
                customURL = createCustomUrl(word, languageCode, timeRangeAnnotation); //todo: make case for multiple words
                List<Headline> headlinesFromThisTimePeriod = getHeadlinesFromURL(customURL, language, newTimeRangeStart, newTimeRangeStop);
                headlines.addAll(headlinesFromThisTimePeriod);
                newTimeRangeStart = newTimeRangeStart.plus(interval);
                newTimeRangeStop = newTimeRangeStop.plus(interval);
            } while (newTimeRangeStop.isBefore(timeRangeStop));

        } else {
            if(timeRangeStart==null){
                customURL = createCustomUrl(word,languageCode);
            } else {
                timeRangeAnnotation = timeRangeStart==null?"":String.format("%%20before:%s%%20after:%s", timeRangeStart, timeRangeStop);
                customURL = createCustomUrl(word,languageCode, timeRangeAnnotation);
            }
            headlines = getHeadlinesFromURL(customURL, language, timeRangeStart, timeRangeStop);
        }

        logger.info("Received headlines: \n" + headlines.toString());

        return headlines;
    }

    private List<Headline> getHeadlinesFromURL(String URL, String language, LocalDate timeRangeStart, LocalDate timeRangeStop){
        List<Headline> headlines = new ArrayList<>();

        driver.get(URL);
        logger.info("Searching with URL: " + URL);


        List<WebElement> headlineLinks = driver.findElements(By.className("rc")).stream().limit(maxHeadlines).collect(Collectors.toList());
        for (WebElement link : headlineLinks) {
            String headlineURL = link.findElement(By.className("r")).findElement(By.partialLinkText("news.google.com")).getAttribute("href");
            String content = link.findElement(By.className("r")).findElement(By.className("DKV0Md")).getText();
            String description = link.findElement(By.className("st")).getText();


            logger.debug("\n@ " + URL + "\n" +
                    "> " + content + "\n" +
                    ">> " + description);

            headlines.add(new Headline(headlineURL, content, description, language, timeRangeStart, timeRangeStop));
        }

        return headlines;
    }

    private String createCustomUrl(String word,String languageCode){
        return String.join("", "https://google.com", "/search?q=", word, "%20site:news.google.com&", languageCode);
    }

    private String createCustomUrl(String word, String languageCode, String timeRangeAnnotation){
        return String.join("", "https://google.com", "/search?q=", word,timeRangeAnnotation==null?"":timeRangeAnnotation, "%20site:news.google.com&", languageCode);
    }

    public void closeDown(){
        driver.close();
    }
}