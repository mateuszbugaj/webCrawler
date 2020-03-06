package com.mateuszbugaj.webCrawler;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebCrawlerApplication {
	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver","C:/Users/Mateusz/Desktop/chromedriver_win32(2)/chromedriver.exe");
		//Logger logger = LogManager.getLogger(WebCrawlerApplication.class);

		//SpringApplication.run(WebCrawlerApplication.class, args);
		//SeleniumTest seleniumTest = new SeleniumTest();
		//seleniumTest

		// Test with two treads
//		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
//
//		Runnable task = new Runnable() {
//			@Override
//			public void run() {
//				TranslatorCollector translator = new TranslatorCollector();
//				FilesManager filesManager = new FilesManager();
//				final String OUTPUT_FILE = "output";
//				filesManager.clearFiles(OUTPUT_FILE);
//
//				for(String language:translator.getLanguages()){
//					System.out.println(language);
//				}
//
//				//todo: make syntax for writing content better
//				translator.translateWords("Good").forEach((k,v) -> {
//					System.out.println("\nLanguage: " + k);
//					filesManager.writeToFile(OUTPUT_FILE, "\nLanguage: " + k);
//					System.out.print(">> Words: ");
//					filesManager.writeToFile(OUTPUT_FILE, ">> Words: ");
//					v.forEach(i-> System.out.print(i+", "));
//					v.forEach(i-> filesManager.writeToFile(OUTPUT_FILE, "  "+i));
//				});
//			}
//		};
//
//		executor.submit(task);
//		executor.submit(task);


//		TranslatorCollector translator = new TranslatorCollector();
//		FilesManager filesManager = new FilesManager();
//		//NewsSearcher newsSearcher = new NewsSearcher();
//		final String OUTPUT_FILE = "output";
//		filesManager.clearFiles(OUTPUT_FILE);
//
//		for(String language:translator.getLanguages()){
//			System.out.println(language);
//		}
//
//		//todo: make syntax for writing content better
//		translator.translateWords("Good").forEach((k,v) -> {
//			System.out.println("\nLanguage: " + k);
//			//logger.debug("\nLanguage: " + k);
//			filesManager.writeToFile(OUTPUT_FILE, "\nLanguage: " + k);
//			System.out.print(">> Words: ");
//			filesManager.writeToFile(OUTPUT_FILE, ">> Words: ");
//			v.forEach(i-> System.out.print(i+", "));
//			v.forEach(i-> filesManager.writeToFile(OUTPUT_FILE, "  "+i));
//		});

		Client client = new Client();
		client.searchHeadlines("world");
		client.stopClient();
	}



}
