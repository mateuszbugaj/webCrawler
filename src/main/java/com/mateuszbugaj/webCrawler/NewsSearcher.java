package com.mateuszbugaj.webCrawler;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NewsSearcher{
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
        driver.manage().window().fullscreen();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.get(TRANSLATOR_URL);
        textArea = driver.findElement(By.name("q"));
        textArea.sendKeys("Web crawler");
        textArea.submit();
        new WebDriverWait(driver, 20).until(ExpectedConditions.elementToBeClickable(By.linkText("News"))).click();
        searchTextArea = driver.findElement(By.name("q"));
    }



    public void /*todo: should return some kind of content */ search(String word){
        searchTextArea = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("q")));
        searchTextArea.clear();
        searchTextArea.sendKeys(word);
        searchTextArea.submit();
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
