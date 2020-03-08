package com.mateuszbugaj.webCrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TranslatorCollector {
    private static Logger logger = LogManager.getLogger(TranslatorCollector.class);
    private WebDriver driver = new ChromeDriver();
    private WebElement moreButton;
    private WebElement backGround;
    private WebElement source;
    private List<String> languages;
    private final String TRANSLATOR_URL = "https://translate.google.com";
    private int MAX_LANGUAGES;
    private List<String> selectedLanguages = new ArrayList<>();

    public static TranslatorCollector get(){
        return new TranslatorCollector();
    }
    public TranslatorCollector(){
        init();
    }

    private void init(){
        //driver.manage().window().fullscreen();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        driver.get(TRANSLATOR_URL);
        // todo: figure out how to close pop-ups
        try{
            List<WebElement> close_message = driver.findElements(By.linkText("Close message"));
            close_message.forEach(i->i.click());
        } catch (NoSuchElementException e){

        }
        moreButton = driver.findElement(By.xpath("//div[contains(@class, 'tlid-open-target-language-list')]"));
        backGround = driver.findElement(By.xpath("//div[@class='input-button-container']"));
        source = driver.findElement(By.id("source"));
        languages = collectAvailableLanguages();
        backGround.click();
    }

    public TranslatorCollector workWithLanguages(String... requestedLanguages){
        logger.debug("Requested languages: " + Arrays.toString(requestedLanguages));

        selectedLanguages = languages.stream()
                .filter(i -> Arrays.stream(requestedLanguages)
                        .anyMatch(k -> k.equalsIgnoreCase(i)))
                .collect(Collectors.toList());

        logger.debug("Selected languages: " + selectedLanguages.toString());
        return this;
    }

    public TranslatorCollector maxSearchedLanguages(int amount){
        MAX_LANGUAGES = amount;
        logger.debug("Set max languages to search: " + MAX_LANGUAGES);
        return this;
    }

    private List<String> collectAvailableLanguages(){
        moreButton.click();
        List<String > languagesNames = driver
                .findElements(By.xpath("//div[contains(@class, 'language_list_item_wrapper')]"))
                .stream()
                .filter(i->!i.getText().equals(""))
                .map((WebElement::getText))
                .limit(MAX_LANGUAGES==0?Integer.MAX_VALUE:MAX_LANGUAGES)
                .collect(Collectors.toList());
        return languagesNames;
    }

    public Map<String, List<String >>  translateWords(String... words){
        Map<String, List<String >>  translatedWords = new HashMap<>();
        WebDriverWait driverWait = new WebDriverWait(driver, 5);
        logger.debug("Words to translated: " + Arrays.toString(words));

        if(selectedLanguages.isEmpty()){
            selectedLanguages = languages;
        }

        Iterator<String> iterator = selectedLanguages.iterator();
        while (iterator.hasNext()){
            moreButton.click();
            String currentLanguage = iterator.next();

            // find language button with @currentLanguage as text and click it or throw an exception
            try {
                driver.findElements(By.xpath("//div[contains(@class, 'language_list_item_wrapper')]"))
                        .stream()
                        .filter(i -> i.getText().equals(currentLanguage))
                        .findFirst()
                        .orElseThrow(() -> new Exception("Cannot find language button '"+currentLanguage+"'"))
                        .click();
            } catch (Exception e){
                logger.error(e.getMessage());
                continue;
            }
            logger.debug("Translating to " + currentLanguage);


            ArrayList<String > wordList = new ArrayList<>();
            for(String word:words){
                source.clear();
                source.sendKeys(word);

                // todo: make driver wait until word in source is not like the previous one or if the source has refreshed
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    String translatedWord = driverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='tlid-translation translation']"))).getText();
                    logger.debug(word + " translated to " + translatedWord);
                    wordList.add(translatedWord);
                } catch (Exception e){
                    logger.error(e.getMessage());
                }
            }
            translatedWords.put(currentLanguage, wordList);
        }
        return translatedWords;
    }

    public String translateToEnglish(String sentence){
        String translatedSentence = null;
        WebDriverWait driverWait = new WebDriverWait(driver, 5);
        moreButton.click();

        try {
            driver.findElements(By.xpath("//div[contains(@class, 'language_list_item_wrapper')]"))
                    .stream()
                    .filter(i -> i.getText().equals("English"))
                    .findFirst()
                    .orElseThrow(() -> new Exception("Cannot find language button 'English'"))
                    .click();
        } catch (Exception e){
            logger.error(e.getMessage());
        }

        source.clear();
        source.sendKeys(sentence);

        // todo: make driver wait until word in source is not like the previous one or if the source has refreshed
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            translatedSentence = driverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='tlid-translation translation']"))).getText();
            logger.debug(sentence + " translated to " + translatedSentence);
        } catch (Exception e){
            logger.error(e.getMessage());
        }

        return translatedSentence;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public List<String > getSelectedLanguages() {
        return selectedLanguages;
    }

    public void close(){
        driver.close();
    }
}
