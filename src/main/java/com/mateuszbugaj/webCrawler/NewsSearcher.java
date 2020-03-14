package com.mateuszbugaj.webCrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NewsSearcher{
    private static Logger logger = LogManager.getLogger(NewsSearcher.class);
    private WebDriver driver;
    private WebDriverWait driverWait;
    private final String WEBSITE_URL = "https://google.com";
    private int maxHeadlines = 10;
    private String timeRange = "";

    public static NewsSearcher get(){
        return new NewsSearcher();
    }

    public NewsSearcher (){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=en-ca"/*,"headless"*/); // to enable english version
        driver = new ChromeDriver(options);
        driverWait = new WebDriverWait(driver, 5);
        //driver.manage().window().fullscreen();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    public NewsSearcher timeRange(String before, String after){
        timeRange = String.join(" ", before, after);
        return this;
    }

    public NewsSearcher maxHeadlines(int max){
        maxHeadlines = max;
        return this;
    }


    public List<Headline> search(String word){
        return search(word, "English");
    }

    public List<Headline> search(String word, String language){
        String languageCode = LanguageCodes.valueOf(language).getCode();
        String customURLForLanguage =
                        WEBSITE_URL
                        + "/search?q="
                        + word
                        + "%20site:news.google.com&"
                        + languageCode;

        driver.get(customURLForLanguage);
        logger.info("Searching with URL: " + customURLForLanguage);
        List<Headline> headlines = new ArrayList<>();

        List<WebElement> headlineLinks = driver.findElements(By.className("rc")).stream().limit(maxHeadlines).collect(Collectors.toList());
        for (WebElement link:headlineLinks){
            String URL = link.findElement(By.className("r")).findElement(By.partialLinkText("news.google.com")).getAttribute("href");
            String content = link.findElement(By.className("r")).findElement(By.className("DKV0Md")).getText();
            String description = link.findElement(By.className("st")).getText();


            logger.debug("\n@ " + URL + "\n" +
                    "> " + content + "\n" +
                    ">> " + description);

//            if(description.endsWith("...")){
//                driver.get(URL);
//                //driver.findElement(By.xpath("//p[contains(@text, '"+description.replace(" ...","").substring(30)+"')]"));
//                driver.findElements(By.tagName("p")).stream().filter(p -> p.getAttribute("text").contains(description.replace(" ...","").substring(30))).findFirst();
//                driver.navigate().back();
//            }

            headlines.add(new Headline(URL, content, description));
        }

        logger.info("Received headlines: \n" + headlines.toString());

        return headlines;
    }

    public void closeDown(){
        driver.close();
    }
}