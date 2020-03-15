package com.mateuszbugaj.webCrawler;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.*;
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

        translator = TranslatorCollector.get()
                .workWithLanguages(LanguageCodes.English.getName(),
                        LanguageCodes.Italian.getName(),
                        LanguageCodes.Chinese.getName(),
                        LanguageCodes.Korean.getName(),
                        LanguageCodes.Vietnamese.getName(),
                        LanguageCodes.Polish.getName());
        newsSearcher = NewsSearcher.get().
                timeRange("2019-12-15","2020-03-15",  14)
                .maxHeadlines(3);

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
        LocalDate previousDate = null;
        LocalDate currentDate;
        String previousLanguage = null;
        String currentLanguage;


        headlines.sort(Comparator.comparing(Headline::getLanguage));
        headlines.sort(Comparator.comparing(Headline::getTimeRangeStart));

        for(Headline headline:headlines){
            currentDate = headline.getTimeRangeStart();
            currentLanguage = headline.getLanguage();
            if(previousDate==null || !currentDate.isEqual(previousDate)){
                System.out.println("Date: " + currentDate);
            }
            if(!currentLanguage.equalsIgnoreCase(previousLanguage)){
                System.out.println("  #" + currentLanguage);
            }

            System.out.println("  -" + headline.getContent());
            //System.out.println("   " + headline.getDescription());

            previousDate = currentDate;
            previousLanguage = currentLanguage;
        }

    }

    public void stopClient(){
        executor.shutdown();
        newsSearcher.closeDown();
        translator.close();
        dbManager.closeConnection();
    }





}
