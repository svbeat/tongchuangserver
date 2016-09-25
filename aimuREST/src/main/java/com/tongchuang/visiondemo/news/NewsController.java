package com.tongchuang.visiondemo.news;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tongchuang.visiondemo.ApplicationConstants;

@RestController
@CrossOrigin

public class NewsController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private NewsService		newsService;
	
	private static final String DEFAULT_MSG = "正在更新，请稍后在试。";
	
	@Autowired
	public NewsController(NewsService newsService) {
		this.newsService = newsService;
	}
	
	@RequestMapping(value = "/news/long", method = RequestMethod.GET)
	public ResponseEntity<String> getNewsLong( 
				@RequestParam("apiKey") String apiKey) {
		
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		}

		String news = newsService.getOptiNewsLong();
		if (news == null) {
			news = DEFAULT_MSG;
		}
		return new ResponseEntity<String>(news, HttpStatus.OK);
	}

	@RequestMapping(value = "/news/short", method = RequestMethod.GET)
	public ResponseEntity<String> getNewsShort( 
				@RequestParam("apiKey") String apiKey) {
		
		if (!ApplicationConstants.SUPER_API_KEY.equals(apiKey)) {
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		}

		String news = newsService.getOptiNewsShort();
		if (news == null) {
			news = DEFAULT_MSG;
		}
		return new ResponseEntity<String>(news, HttpStatus.OK);
	}
	
}
