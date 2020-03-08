package com.mateuszbugaj.webCrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class NewsSearcher{
    private static Logger logger = LogManager.getLogger(NewsSearcher.class);
    private WebElement searchTextArea;
    private WebDriver driver;// = new ChromeDriver();
    private WebDriverWait driverWait;
    private final String TRANSLATOR_URL = "https://google.com";
    private WebElement textArea;
    private ArrayList<String > queueOfWords = new ArrayList<>();

    public NewsSearcher (){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=en-ca"); // to enable english version
        driver = new ChromeDriver(options);
        driverWait = new WebDriverWait(driver, 5);
        //driver.manage().window().fullscreen();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.get(TRANSLATOR_URL);
        textArea = driver.findElement(By.name("q"));
        textArea.sendKeys("Web crawler");
        textArea.submit();
        new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(By.linkText("News"))).click();
        searchTextArea = driver.findElement(By.name("q"));
    }

    public List<Headline> search(String word){
        List<Headline> headlines = new ArrayList<>();
        searchTextArea = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("q")));
        searchTextArea.clear();
        searchTextArea.sendKeys(word);
        searchTextArea.submit();

        List<WebElement> headlineLinks = driver.findElements(By.className("gG0TJc"));
        for (WebElement link:headlineLinks){
            String URL = link.findElement(By.className("dO0Ag")).findElement(By.className("lLrAF")).getAttribute("href");
            String content = link.findElement(By.className("dO0Ag")).getText();
            String description = link.findElement(By.className("st")).getText();



            logger.debug("@" + URL);
            logger.debug("  " + content);
            logger.debug("      " + description); // todo: get rid of dots at the end of description by going it website and looking for words that appear after the lats dot in the description. Then collect words that appear after those words until the first dot. Then append them to the description in the place of three dots.

            headlines.add(new Headline(URL, content, description));
        }
        logger.info("Received headlines: \n" + headlines.toString());

        return headlines;
    }

    public void closeDown(){
        driver.close();
    }
}

/**
 * Below is the syntax you can use for Chrome Browser :
 *
 * System.setProperty("webdriver.chrome.driver","D:/.../chromedriver.exe");
 *
 * ChromeOptions options = new ChromeOptions();
 *
 * options.addArguments("-lang= sl");
 *
 * ChromeDriver driver = new ChromeDriver(options);
 *
 * driver.get("http://www.google.com);
 *
 *
 *
 * Here are few samples for different languages:
 *
 * //options.AddArgument("--lang=es"); //espanol
 *
 * //options.AddArgument("--lang=es-mx"); //espanol (Latinoamerica), espanol
 *
 * //options.AddArgument("--lang=en-ca"); //english (UK), english (us), english
 *
 * //options.AddArgument("--lang=en-au"); //english (UK), english (us), english
 *
 * //options.AddArgument("--lang=en-nz"); //english (UK), english (us), english
 *
 * //options.AddArgument("--lang=zh"); //english (us), english
 *
 * //options.AddArgument("--lang=zh-tw"); //Chinese (Traditional Chinese), Chinese, english (us), english
 *
 * //options.AddArgument("--lang=zh-hk"); //Chinese (Traditional Chinese), Chinese, english (us), english
 *
 * //options.AddArgument("--lang=zh-cn"); //Chinese (Simplified Chinese), Chinese, english (us), english
 *
 * options.AddArgument("--lang=fr"); //Francais (France), Francais, english (us), english
 *
 * //options.AddArgument("--lang=fr-ca"); //Francais (France), Francais, english (us), english
 *
 * //options.AddArgument("--lang=aus"); //Francais (France), Francais, english (us), english
 *
 *
 *
 * Read more: https://softwaretestingboard.com/q2a/2347/how-to-change-the-language-of-the-browser-selenium-webdriver#ixzz6FO8NNsne
 */
