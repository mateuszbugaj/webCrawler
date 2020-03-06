package com.mateuszbugaj.webCrawler;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TranslatorCollector {
    private WebDriver driver = new ChromeDriver();
    private WebElement moreButton;
    private WebElement backGround;
    private WebElement source;
    private List<String> languages;
    private List<WebElement> languageButtons;
    private final String TRANSLATOR_URL = "https://translate.google.com";
    private int MAX_LANGUAGES;
    //private List<WebElement> selectedLanguagesButtons = new ArrayList<>();
    private List<String> selectedLanguages = new ArrayList<>();

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
        //todo: figure out how to throw exception when there is no language like requested

        selectedLanguages = languages.stream()
                .filter(i ->
                        Arrays.stream(requestedLanguages)
                        .anyMatch(k -> k.equals(i)))
                .collect(Collectors.toList());

        System.out.println("Selected languages: ");
        //selectedLanguagesButtons.stream().forEach(i-> System.out.println(i.getText()));
        selectedLanguages.stream().forEach(i -> System.out.println(i));
        return this;
    }

    public TranslatorCollector maxSearchedLanguages(int amount){
        MAX_LANGUAGES = amount;
        System.out.println("Searched languages: " + MAX_LANGUAGES);
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

        if(selectedLanguages.isEmpty()){
            selectedLanguages = languages;
        }

        Iterator<String> iterator = selectedLanguages.iterator();
        while (iterator.hasNext()){
            moreButton.click();
            String currentLanguage = iterator.next();
            //WebElement languageButton = driverWait.until(ExpectedConditions.presenceOfElementLocated();
            WebElement languageButton = driver.findElements(By.xpath("//div[contains(@class, 'language_list_item_wrapper')]"))
                    .stream()
                    .filter(i -> i.getText().equals(currentLanguage))
                    .findFirst().get(); // todo: make it return exception when can't find

            languageButton.click();

            ArrayList<String > wordList = new ArrayList<>();
            System.out.println("words: "+ Arrays.toString(words));
            for(String word:words){
                source.clear();

                System.out.println("Word: " + word);
                source.sendKeys(word);

                // todo: make driver wait until word in source is not like the previous one or if the source has refreshed
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String translatedWord = driverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='tlid-translation translation']"))).getText();
                System.out.println("Translated word: " + translatedWord);
                wordList.add(translatedWord);
            }

            translatedWords.put(currentLanguage, wordList);

        }

        return translatedWords;

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
