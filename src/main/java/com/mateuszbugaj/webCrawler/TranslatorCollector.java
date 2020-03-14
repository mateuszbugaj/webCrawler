package com.mateuszbugaj.webCrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TranslatorCollector {
    private static Logger logger = LogManager.getLogger(TranslatorCollector.class);
    private WebDriver driver = new ChromeDriver();
    private WebDriverWait driverWait;
    private WebElement moreButton;
    private WebElement source;
    private Set<String> availableLanguages;
    private final String TRANSLATOR_URL = "https://translate.google.com";
    private Set<String> selectedLanguages = new HashSet<>();
    private String previousTranslatedWord;

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
        driverWait = new WebDriverWait(driver, 5);
        // todo: figure out how to close pop-ups
        try{
            List<WebElement> close_message = driver.findElements(By.linkText("Close message"));
            close_message.forEach(i->i.click());
        } catch (NoSuchElementException e){

        }
        moreButton = driver.findElement(By.xpath("//div[contains(@class, 'tlid-open-target-language-list')]"));
        source = driver.findElement(By.id("source"));
        availableLanguages = Arrays.stream(LanguageCodes.values()).map(LanguageCodes::getName).collect(Collectors.toSet());
        selectedLanguages.add("English"); // default language

    }

    /**
     * Select available languages from requested and notify if language is not an option
     * @param requestedLanguages - list of languages requested by user
     * @return
     */
    public TranslatorCollector workWithLanguages(String... requestedLanguages){
        selectedLanguages = Arrays.stream(requestedLanguages)
                .filter(requested -> {
                    if(Arrays.stream(LanguageCodes.values()).map(LanguageCodes::getName).anyMatch(name -> name.equalsIgnoreCase(requested))){
                        return true;
                    } else {
                        logger.error("Language " + requested + " is not available");
                        return false;
                    }
                })
                .collect(Collectors.toSet());

        logger.debug("Requested languages: " + Arrays.toString(requestedLanguages)+"; Selected languages: " + selectedLanguages.toString());
        return this;
    }

    /**
     * Translate words passed as the parameter to all selected languages.
     * Collect results in HashMap with language being key and list of translated words the value.
     * @param words - requested words to translate
     * @return HashMap with list of translated words as values and languages as keys
     */
    public Map<String, List<String >>  translateWords(String... words){
        Map<String, List<String >>  translatedWords = new HashMap<>();
        logger.debug("Words to translate: " + Arrays.toString(words));

        if(selectedLanguages.isEmpty()){
            selectedLanguages = availableLanguages;
        }

        Iterator<String> iterator = selectedLanguages.iterator();
        while (iterator.hasNext()){
            String currentLanguage = iterator.next();
            logger.debug("Translating to " + currentLanguage);
            ArrayList<String > wordList = new ArrayList<>();
            for(String word:words){
                wordList.add(useTranslator(driverWait,word,currentLanguage));
            }
            translatedWords.put(currentLanguage, wordList);
        }
        return translatedWords;
    }

    /**
     * Used to translate content and description of headlines to english to make them uniform and understandable.
     * Language is set to english by default
     * @param sentence - sentence to translate
     * @return - translated sentence to english
     */
    public String translateToEnglish(String sentence){
        String translatedSentence;
        translatedSentence = useTranslator(driverWait, sentence, "English");
        return translatedSentence;
    }

    /**
     * Translate 'word' to 'language'.
     * 1) Clear source (text area with sentence to translate)
     * 2) Check if requested language is already selected to reduce time
     *      - If not, open list of languages and search for the right button and click it
     * 3) Type 'word' in source and submit
     * 4) Wait until value of translated sentence change and grab it as translated word.
     * 5) Return translated word.
     * @param driverWait
     * @param word - requested fraze to translate
     * @param language - requested language
     * @return - translated fraze
     */
    private String useTranslator(WebDriverWait driverWait,String word, String language){
        String translatedWord;
        source.clear();

        List<WebElement> suggestedButtons = driver.findElements(By.xpath("//div[@class='tl-wrap']/div[@class='tl-sugg']/div[@class='sl-sugg-button-container']/div[@role='button']"));
        boolean isLanguageAlreadySelected = suggestedButtons
                .stream()
                .anyMatch(button-> button.getText().equalsIgnoreCase(language) && button.getAttribute("aria-pressed").equalsIgnoreCase("true"));

        if(!isLanguageAlreadySelected) {
            moreButton.click();
            // find language button with @currentLanguage as text and click it or throw an exception
            try {
                driver.findElements(By.xpath("//div[contains(@class, 'language_list_item_wrapper')]"))
                        .stream()
                        .filter(i -> i.getText().equals(language))
                        .findFirst()
                        .orElseThrow(() -> new Exception("Cannot find language button '" + language + "'"))
                        .click();
            } catch (Exception e) {
                logger.error(e.getMessage());
                return null;
            }
        }

        source.sendKeys(word);

        try {
            if(previousTranslatedWord!=null) {
                driverWait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(driver.findElement(By.xpath("//span[@class='tlid-translation translation']")), previousTranslatedWord)));
            }
            translatedWord = driver.findElement(By.xpath("//span[@class='tlid-translation translation']")).getText();
            
            previousTranslatedWord = translatedWord;
            logger.debug(word + " translated to " + translatedWord);
            return translatedWord;
        } catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    public Set<String> getAvailableLanguages() {
        return availableLanguages;
    }

    public void close(){
        driver.close();
    }
}

/*
Search Language Codes

lr=lang_af    Afrikaans
lr=lang_ar    Arabic
lr=lang_hy    Armenian
lr=lang_be    Belarusian
lr=lang_bg    Bulgarian
lr=lang_ca    Catalan
lr=lang_zh-CN Chinese (Simplified)
lr=lang_zh-TW Chinese (Traditional)
lr=lang_hr    Croatian
lr=lang_cs    Czech
lr=lang_da    Danish
lr=lang_nl    Dutch
lr=lang_en    English
lr=lang_eo    Esperanto
lr=lang_et    Estonian
lr=lang_tl    Filipino
lr=lang_fi    Finnish
lr=lang_fr    French
lr=lang_de    German
lr=lang_el    Greek
lr=lang_iw    Hebrew
lr=lang_hi    Hindi
lr=lang_hu    Hungarian
lr=lang_is    Icelandic
lr=lang_id    Indonesian
lr=lang_it    Italian
lr=lang_ja    Japanese
lr=lang_ko    Korean
lr=lang_lv    Latvian
lr=lang_lt    Lithuanian
lr=lang_no    Norwegian
lr=lang_fa    Persian
lr=lang_pl    Polish
lr=lang_pt    Portuguese
lr=lang_ro    Romanian
lr=lang_ru    Russian
lr=lang_sr    Serbian
lr=lang_sk    Slovak
lr=lang_sl    Slovenian
lr=lang_es    Spanish
lr=lang_sw    Swahili
lr=lang_sv    Swedish
lr=lang_th    Thai
lr=lang_tr    Turkish
lr=lang_uk    Ukrainian
lr=lang_vi    Vietnamese
 */