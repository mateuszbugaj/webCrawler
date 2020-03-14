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
        newsSearcher = NewsSearcher.get().timeRange("2020-1-15", "2020-1-20").maxHeadlines(5);
        try {
            dbManager = new DBManager();
        } catch (Exception e){

        }
        translator = TranslatorCollector.get().workWithLanguages("English", "Umpalumpa", "Russian");
    }

    public void searchHeadlines(String... words){
        Map<String, List<String >> requestedWordsTranslation = translator.translateWords(words);
        Map<String , List<Headline>> receivedHeadlines = new HashMap<>();
        MultiValuedMap<String ,Headline> translatedReceivedHeadlines = new ArrayListValuedHashMap<>();

        // todo: collect this to the data base

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
                Headline translatedHeadline = new Headline(headline.getURL(), translatedContent, translatedDescription);
                dbManager.saveToDatabase(translatedHeadline);
                translatedReceivedHeadlines.put(language,translatedHeadline);
            });
        });

        translatedReceivedHeadlines.asMap().forEach((language, headlines) -> {
            System.out.println("\nLanguage: " + language);
            System.out.println("Headline: ");
            headlines.forEach(headline -> {
                System.out.printf(">> %s \n",headline.getContent());
            });
        });



    }

    public void clearDatabase(){
        dbManager.clearDatabase();
    }
    public void stopClient(){
        executor.shutdown();
        newsSearcher.closeDown();
        translator.close();
        dbManager.closeConnection();
    }





}
