package com.tongchuang.visiondemo.news;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NewsService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final String NEWS_URL="http://news.baidu.com/ns?word=%E7%9C%BC%E7%A7%91&tn=newsfcu&from=news&rn=NEWS_COUNT&cl=2&ct=0";
	private static final String COUNT_LONG = "10";
	private static final String COUNT_SHORT = "5";
	private String optiNewsShort = null;
	private String optiNewsLong	= null;

	@Scheduled(fixedDelay = 60*60*1000, initialDelay = 1000)
	public void refreshNews() {
		logger.info("refresh news start ...");
		
		String newsUrl = NEWS_URL.replace("NEWS_COUNT", COUNT_SHORT);
		String news = fetchNews(newsUrl);
		if (news != null) {
			optiNewsShort = news;
		}
		
		newsUrl = NEWS_URL.replace("NEWS_COUNT", COUNT_LONG);
		
		news = fetchNews(newsUrl);
		if (news != null) {
			optiNewsLong = news;
		}
		logger.info("refresh news long end ...");
		

		logger.info("refresh news end ...");
	}
	
	
	private String fetchNews(String newsUrl) {

		String news = null;
		try {

			URL url = new URL(newsUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			//conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				logger.error("Fetching News Failed : HTTP error code : " + conn.getResponseCode());
				return null;
			}

			StringBuilder strBlder = new StringBuilder();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "GBK"));
			//BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				strBlder.append(output);
			}

			conn.disconnect();
			news = strBlder.toString();
			int firstDiv = news.indexOf("</div>");
			int lastDiv = news.lastIndexOf("</div>");
			news = news.substring(firstDiv+"</div>".length(), lastDiv);
			logger.info("news:"+news);
		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return news;
	}


	public String getOptiNewsShort() {
		return optiNewsShort;
	}


	public String getOptiNewsLong() {
		return optiNewsLong;
	}

	
}
