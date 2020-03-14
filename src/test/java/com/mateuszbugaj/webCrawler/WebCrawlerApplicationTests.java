package com.mateuszbugaj.webCrawler;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.InvalidSelectorException;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebCrawlerApplicationTests {

	@BeforeAll
	static void initOfSelenium(){
		System.setProperty("webdriver.chrome.driver","C:/Users/Mateusz/Desktop/chromedriver_win32(2)/chromedriver.exe");
	}

	@Test
	void testOfInitializationOfNewsSearcher(){
		boolean initializationComplete;
		try {
			NewsSearcher newsSearcher = new NewsSearcher();
			initializationComplete = true;
		} catch (InvalidSelectorException e){
			initializationComplete = false;
		}

		assert initializationComplete;

	}

	@Test
	void testOfSearchWithNewsSearcher(){
		NewsSearcher newsSearcher = new NewsSearcher();

		boolean searchComplete;
		try {
			newsSearcher.search("Good");
			searchComplete = true;
		} catch (InvalidSelectorException e){
			searchComplete = false;
		}

		assert searchComplete;
	}

	@Test
	void selectLanguagesForTranslatorToUse(){
		TranslatorCollector translatorCollector = new TranslatorCollector();
		String language = "Polish";
		translatorCollector.getAvailableLanguages().stream().forEach(i-> System.out.println(i));
		translatorCollector.workWithLanguages(language);


		//assert translatorCollector.getSelectedLanguagesButtons().get(0).getText().equals(language);
		translatorCollector.close();
	}


}
