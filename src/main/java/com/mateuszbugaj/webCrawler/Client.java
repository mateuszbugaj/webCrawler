package com.mateuszbugaj.webCrawler;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Client {
    private static Logger logger = LogManager.getLogger(Client.class);
    private TranslatorCollector translator;
    private NewsSearcher newsSearcher;
    private DBManager dbManager;
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

    public Client(){
        logger.debug("Client initialization");
        translator = TranslatorCollector.get().workWithLanguages("English", "Na'vi", "Russian");
        newsSearcher = NewsSearcher.get().timeRange("2020-01-15","2020-01-20",  2).maxHeadlines(1);

        try {
            dbManager = new DBManager();
        } catch (Exception e){
            logger.error("Couldn't establish connection with database");
        }

    }

    public void searchHeadlines(String... words){
        Map<String, List<String >> requestedWordsTranslation = translator.translateWords(words);
        Map<String , List<Headline>> receivedHeadlines = new HashMap<>();
        MultiValuedMap<String ,Headline> translatedReceivedHeadlines = new ArrayListValuedHashMap<>();

        requestedWordsTranslation.forEach((language, wordsList) -> {
            logger.info("Language: " + language + ", words: " + wordsList.toString());
            wordsList.forEach(word -> {
                List<Headline> headlinesForLanguage = newsSearcher.search(word,language);
                receivedHeadlines.put(language, headlinesForLanguage);
            });
        });

        receivedHeadlines.forEach((language, headlines) -> {
            headlines.forEach(headline -> {
                String translatedContent = translator.translateToEnglish(headline.getContent());
                String translatedDescription = translator.translateToEnglish(headline.getDescription());
                Headline translatedHeadline = new Headline(headline.getURL(), translatedContent, translatedDescription, language, headline.getTimeRangeStart(), headline.getTimeRangeStop());
                dbManager.saveToDatabase(translatedHeadline);
                translatedReceivedHeadlines.put(language,translatedHeadline);
            });
        });

    }

    public void clearDatabase(){
        dbManager.clearDatabase();
    }

    public void showDatabaseContent(){
        List<Headline> headlines = dbManager.readFromDatabase();

        System.out.println("Database content:");
        for(Headline headline:headlines){
            if(headline.getTimeRangeStart()!=null){
                System.out.println("Headline from: " + headline.getTimeRangeStart() +" - " + headline.getTimeRangeStop());
            }
            System.out.println("Language:" + headline.getLanguage());
            System.out.println("Content: " + headline.getContent());
            System.out.println("Description: " + headline.getDescription());
        }

    }

    public void stopClient(){
        executor.shutdown();
        newsSearcher.closeDown();
        translator.close();
        dbManager.closeConnection();
    }





}
