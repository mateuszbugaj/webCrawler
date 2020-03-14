package com.mateuszbugaj.webCrawler;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Driver;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class SeleniumTest {


    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver","C:/Users/Mateusz/Desktop/chromedriver_win32(2)/chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        String baseUrl = "https://translate.google.com/";
        String tagName = "";

       // /html/body/div[2]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[3]
        // <div class="sl-more tlid-open-source-language-list" aria-label="More" role="button" tabindex="0"></div>

        driver.get(baseUrl);
        WebElement listButton = driver.findElement(By.className("tlid-open-target-language-list")); // CLASSES SEPARATED BY SPACE
        listButton.click();
        //driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]/div[1]/div[3]")).click();

        String attribute = listButton.getAttribute("aria-label");
        System.out.println("List button attribute: " + attribute);

        //<div class="language_list_item language_list_item_language_name" aria-label="">Afrikaans</div>

//        List<String> languagesNames = driver.findElements(By.className("language_list_item_language_name"))//language_list_item_language_name
//                .stream().map(WebElement -> WebElement.getText())
//                .filter(i->!i.equals(""))
//                .collect(Collectors.toList());
//
//        System.out.println("Available languages");
//        for(String name: languagesNames){
//            System.out.println(">> "+name);
//        }

        driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/div[2]/div[2]/div[2]/div/div[2]/div[20]")).click();
        //listButton.click();

        // <div class="language_list_item_wrapper language_list_item_wrapper-cs" onclick="_e(event, 'changeLanguage+0', 'tl_list_cs')" role="button" tabindex="0"><div class="language_list_item_icon tl_list_cs_checkmark"></div><div class="language_list_item language_list_item_language_name" aria-label="">Czech</div></div>


        String wordToTranslate = "disease";
        WebElement source = driver.findElement(By.id("source"));
        //source.click();
        source.sendKeys(wordToTranslate);

        ///html/body/div[2]/div[2]/div[1]/div[2]/div[1]/div[1]/div[2]/div[3]/div[1]/div[2]/div/span[1]/span
        // <span title="" class="">choroba</span>
        // body > div:nth-child(10) > div.frame > div.page.tlid-homepage.homepage.translate-text > div.homepage-content-wrap > div.tlid-source-target.main-header > div.source-target-row > div.tlid-results-container.results-container > div.tlid-result.result-dict-wrapper > div.result.tlid-copy-target > div.text-wrap.tlid-copy-target > div > span.tlid-translation.translation > span
        //document.querySelector("body > div:nth-child(10) > div.frame > div.page.tlid-homepage.homepage.translate-text > div.homepage-content-wrap > div.tlid-source-target.main-header > div.source-target-row > div.tlid-results-container.results-container > div.tlid-result.result-dict-wrapper > div.result.tlid-copy-target > div.text-wrap.tlid-copy-target > div > span.tlid-translation.translation > span")
        // /html/body/div[2]/div[2]/div[1]/div[2]/div[1]/div[1]/div[2]/div[3]/div[1]/div[2]/div/span[1]/span
        // <div class="result-shield-container tlid-copy-target" tabindex="0"><span class="tlid-translation translation" lang="cs"><span title="" class="">choroba</span></span><span class="tlid-translation-gender-indicator translation-gender-indicator"></span><span class="tlid-trans-verified-button trans-verified-button" style="display:none" role="button"></span></div>
        WebElement translation = driver.findElement(By.className("tlid-result-container"));
        //System.out.println("Translation: " + translation);


        //driver.close();

    }

}

class App{
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver","C:/Users/Mateusz/Desktop/chromedriver_win32(2)/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://translate.google.com/?hl=en&tab=TT");
//        WebElement element = driver.findElement(By.xpath("/html/body/div[2]/h2"));
        ///html/body/div[2]/div[2]/div[1]/div[2]/div[1]/div[1]/div[2]
        //WebElement element = driver.findElement(By.xpath("//h2[@class='barone']"));
        // <div role="button" class="goog-inline-block jfk-button jfk-button-standard jfk-button-collapse-left jfk-button-collapse-right jfk-button-checked" tabindex="0" aria-pressed="true" value="en" id="sugg-item-en" style="user-select: none;">English</div>
        //WebElement element = driver.findElement(By.xpath("//div[@class='tlid-results-container']"));
        // <div class="tlid-results-container results-container empty"><div class="error-placeholder placeholder"><span class="tlid-result-error"></span><span class="tlid-result-container-error-button translation-error-button">Try again</span></div><span class="empty-placeholder placeholder">Translation</span><span class="translating-placeholder placeholder">Translating...</span><div class="gendered-translations-header">Translations are gender-specific. <a class="gendered-translations-learn-more" href="https://support.google.com/translate?p=gendered_translations&amp;hl=en" target="_blank">Learn more</a></div></div>
        WebElement element = driver.findElement(By.xpath("//div[@class='tlid-results-container results-container empty']"));
        System.out.println(element.getText());

        driver.close();
    }
}

class App2{
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver","C:/Users/Mateusz/Desktop/chromedriver_win32(2)/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        String url = "https://translate.google.com";
        String wordToTranslate = "class";
        driver.get(url);
        WebElement source = driver.findElement(By.id("source"));
        source.sendKeys(wordToTranslate);
        String translatedText = driver.findElement(By.xpath("//span[@class='tlid-translation translation']")).getText(); //<div class="result tlid-copy-target"><div class="result-header"><div class="starbutton jfk-button-flat jfk-button unstarred" aria-label="Star translation" data-tooltip="Star translation" role="button" tabindex="0" data-tooltip-align="t,c" style="user-select: none;"><div class="jfk-button-img"></div></div></div><div class="text-wrap tlid-copy-target"><div class="result-shield-container tlid-copy-target" tabindex="0"><span class="tlid-translation translation" lang="cs"><span title="" class="">dfsdf</span></span><span class="tlid-translation-gender-indicator translation-gender-indicator"></span><span class="tlid-trans-verified-button trans-verified-button" style="display:none" role="button"></span></div></div><div class="tlid-result-transliteration-container result-transliteration-container transliteration-container"><div class="tlid-transliteration-content transliteration-content full"></div><div class="tlid-show-more-link truncate-link" style="display:none">Show more</div><div class="tlid-show-less-link truncate-link" style="display:none">Show less</div></div><div class="result-footer source-or-target-footer tlid-copy-target"><div class="tlid-share-translation-button share-translation-button jfk-button-flat source-or-target-footer-button right-positioned jfk-button" aria-label="Share translation" data-tooltip="Share translation" role="button" tabindex="0" data-tooltip-align="t,c" style="user-select: none;"><div class="jfk-button-img"></div></div><div class="tlid-suggest-edit-button suggest-edit-button jfk-button-flat source-or-target-footer-button right-positioned jfk-button" aria-label="Suggest an edit" data-tooltip="Suggest an edit" role="button" tabindex="0" data-tooltip-align="t,c" style="user-select: none;"><div class="jfk-button-img"></div></div><div class="more-wrapper"><div class="morebutton jfk-button-flat source-or-target-footer-button tlid-result-footer-more-button right-positioned goog-inline-block goog-menu-button" data-tooltip="More" role="button" aria-expanded="false" tabindex="0" aria-haspopup="true" aria-label="More" data-tooltip-align="t,c" style="user-select: none;"><div class="goog-inline-block goog-menu-button-outer-box"><div class="goog-inline-block goog-menu-button-inner-box"><div class="goog-inline-block goog-menu-button-caption"><div class="jfk-button-img"></div></div><div class="goog-inline-block goog-menu-button-dropdown">&nbsp;</div></div></div></div><div class="moremenu goog-menu" role="menu" aria-haspopup="true" style="user-select: none; display: none;"><div class="goog-menuitem tlid-suggest-edit-menu-item" role="menuitem" id=":7" style="user-select: none;"><div class="goog-menuitem-content">Suggest an edit</div></div><div class="goog-menuitem tlid-share-translation-menu-item" role="menuitem" id=":8" style="user-select: none;"><div class="goog-menuitem-content">Share translation</div></div></div></div><div class="tlid-copy-translation-button copybutton jfk-button-flat source-or-target-footer-button right-positioned jfk-button" aria-label="Copy translation" data-tooltip="Copy translation" role="button" tabindex="0" data-tooltip-align="t,c" style="user-select: none;"><div class="jfk-button-img"></div></div><div class="res-tts ttsbutton-res left-positioned ttsbutton jfk-button-flat source-or-target-footer-button jfk-button" aria-label="Listen" data-tooltip="Listen" aria-pressed="false" role="button" tabindex="0" data-tooltip-align="t,c" style="user-select: none;"><div class="jfk-button-img"></div></div></div></div>
        System.out.println("Translated text>> "+translatedText);

        //driver.close();
    }
}

class App3{
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver","C:/Users/Mateusz/Desktop/chromedriver_win32(2)/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        String url = "https://translate.google.com";
        String wordToTranslate = "class";
        driver.get(url);
        driver.manage().window().fullscreen();
//        WebElement moreButton = driver.findElement(By.xpath("//div[@aria-label='More']"));
        WebElement moreButton = driver.findElement(By.xpath("//div[contains(@class, 'tlid-open-target-language-list')]"));
        WebElement source = driver.findElement(By.id("source"));
        moreButton.click();
        List<WebElement> languageButtons = driver.findElements(By.xpath("//div[contains(@class, 'language_list_item_wrapper')]")).stream().filter(i->!i.getText().equals("")).collect(Collectors.toList());
        System.out.println(languageButtons.size());


        Map<String, String > translatedWords = new HashMap<>();

//        for(WebElement button:languageButtons){
//            //String languageName = button.findElement(By.xpath("//div[contains(@class, 'language_list_item')]")).getText();//.getAttribute("aria-label");
//            String languageName = button.getText();
//            System.out.println(languageName);
//            try {
//                button.click();
//                source.clear();
//                source.sendKeys(wordToTranslate);
//                Thread.sleep(200);
//
//                String translatedWord = driver.findElement(By.xpath("//span[@class='tlid-translation translation']")).getText();
//
//                translatedWords.put(languageName, translatedWord);
//
//                moreButton.click();
//            } catch (ElementClickInterceptedException e){
//
//            } catch (InterruptedException e){
//
//            }
//        }

        Iterator<WebElement> iterator = languageButtons.iterator();
        boolean clicked = true;
        WebElement button = null;
        while (iterator.hasNext()){
            if(clicked) {
                button = iterator.next();
            }

            String languageName = button.getText();
            System.out.println(languageName);

            try {
                button.click();
                source.clear();
                source.sendKeys(wordToTranslate);
                Thread.sleep(200);

                String translatedWord = driver.findElement(By.xpath("//span[@class='tlid-translation translation']")).getText();

                translatedWords.put(languageName, translatedWord);

                moreButton.click();
            } catch (ElementClickInterceptedException e){
                clicked = false;
            } catch (InterruptedException e){

            } catch (StaleElementReferenceException e){

            } catch (ElementNotInteractableException e){

            } finally{
                clicked = true;
            }
        }

        for(WebElement element:languageButtons){
            String languageName = element.getText();
            System.out.println(languageName +" >> "+translatedWords.get(languageName));
        }



        driver.close();

    }

}

class App4{
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver","C:/Users/Mateusz/Desktop/chromedriver_win32(2)/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.google.com");
        WebElement element = driver.findElement(By.name("q"));
        element.sendKeys("Cheese!\n"); // send also a "\n"
        element.submit();

    }
}

class App5{
    public static void main(String[] args) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        Waiter waiter1 = new Waiter(1);
        Waiter waiter2 = new Waiter(2);
        Waiter waiter3 = new Waiter(3);
        executor.submit(waiter1::executeTask);
        executor.submit(waiter1::executeTask);
        executor.submit(waiter1::executeTask);
        executor.shutdown();
    }

}

class Waiter{
    private int index;
    int value;

    public Waiter(int index) {
        this.index = index;
        try {
            Thread.sleep(1000 + index*100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Waiter " + index + " initialized.");
    }

    public void executeTask(){
        value++;
        System.out.println("Second task executed. " + value);
    }

}

class App6{
    public static void main(String[] args) {
        String[] list1 = new String[]{"Two", "Three", "Six"};
        List<String > list2 = Arrays.asList("One", "Two", "Three", "Four", "Five");
        List<String > list3;

        list3 = list2.stream().filter(element -> Arrays.stream(list1).anyMatch(i -> i.equals(element))).collect(Collectors.toList());
        System.out.println(list3.toString());
    }
}

class App7{
    public static void main(String[] args) {
        List<String > list1 = Arrays.asList("One", "Two", "Three", "Four", "Five");
        List<String > list2 = Arrays.asList("Six", "Seven", "Eight", "Nine", "Ten");
        String key = "key";

        LinkedHashMap<String , String > map = new LinkedHashMap<>();
        map.put(key, "One");
        map.put(key, "Two");
        map.put(key, "Three");

        map.forEach((k,v) -> {
            System.out.println("k = " + k);
            System.out.println("v = " + v.toString());
        });
    }
}

class App8{
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("codes"));
            String line = reader.readLine();
            while (line!=null){
                String code = line.substring(0,line.indexOf(" "));
                String language = line.substring(line.lastIndexOf(" ")+1);
                //System.out.println("Code: " + code + ", language: " + language);
                line = reader.readLine();
                //codes.put("Afrikaans","lr=lang_af");
                System.out.println("codes.put(\""+language+"\",\""+code+"\");");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

/*
Afrikaans{
        @Override
        public String getCode() {
            return "lr=lang_af";
        }

        @Override
        public String getName() {
            return "Afrikaans";
        }
    };
 */

class App9{
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("codes"));
            String line = reader.readLine();
            while (line!=null){
                String code = line.substring(0,line.indexOf(" "));
                String language = line.substring(line.lastIndexOf(" ")+1);
                line = reader.readLine();

                System.out.println(language + "{\n" +
                        "@Override\n" +
                        "public String getCode() {\n" +
                        "            return \""+code+"\";\n" +
                        "        }" +
                        "@Override\n" +
                        "        public String getName() {\n" +
                        "            return \""+language+"\";\n" +
                        "        }\n" +
                        "    },");


                //System.out.println("codes.put(\""+language+"\",\""+code+"\");");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}


