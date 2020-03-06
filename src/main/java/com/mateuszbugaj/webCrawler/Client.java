package com.mateuszbugaj.webCrawler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Client {
    private TranslatorCollector translator;
    private NewsSearcher newsSearcher;
    private FilesManager filesManager;
    final String OUTPUT_FILE = "output";
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

    public Client(){
        translator = new TranslatorCollector();
        newsSearcher = new NewsSearcher();
        filesManager = new FilesManager();
        translator.workWithLanguages("Polish", "German", "English").maxSearchedLanguages(100); // todo: should be builder pattern
        filesManager.clearFiles(OUTPUT_FILE);
    }

    public void searchHeadlines(String... words){
        Map<String, List<String >> requestedWordsTranslation = translator.translateWords(words);

        requestedWordsTranslation.forEach((language, wordsList) -> {
            System.out.println("\nLanguage: " + language);
            System.out.print("  Words: ");
            wordsList.forEach(word -> {
                System.out.printf("%s, ",word);
                newsSearcher.search(word);
            });
        });
    }


    public void stopClient(){
        executor.shutdown();
    }





}
