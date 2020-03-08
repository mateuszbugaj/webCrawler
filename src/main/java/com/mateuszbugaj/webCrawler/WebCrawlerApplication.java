package com.mateuszbugaj.webCrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebCrawlerApplication {
	private static Logger logger = LogManager.getLogger(WebCrawlerApplication.class); // https://www.scalyr.com/blog/maven-log4j2-project/

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver","C:/Users/Mateusz/Desktop/chromedriver_win32(2)/chromedriver.exe");
		logger.debug("Start of application");
		Client client = new Client();
		client.searchHeadlines("world");
		client.stopClient();
	}



}
