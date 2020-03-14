package com.mateuszbugaj.webCrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebCrawlerApplication {
	private static Logger logger = LogManager.getLogger(WebCrawlerApplication.class); // https://www.scalyr.com/blog/maven-log4j2-project/

	public static void main(String[] args) {
		String browserDriverPath = "chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", browserDriverPath);
		logger.debug("Start of application");


		Client client = new Client();
		client.searchHeadlines("virus");
		client.showDatabaseContent();
		client.clearDatabase();
		client.stopClient();
	}



}
