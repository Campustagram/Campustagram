package com.campustagram.core.request;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.controller.log.ILogger;

public class PostRequest {

	private static final String ACTIVE_CLASS_NAME = "PostRequest";

	private ILogger logger = new ILogger();

	// Constructors
	public PostRequest() {
		super();
	}

	// Methods
	public ResponseEntity<String> postJson(String json, String uri) throws Exception {
		final String ACTIVE_METHOD_NAME = "postJson";

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		// create request body
		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "Post Request Json: " + json , CommonConstants.INFO);
		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> entity = new HttpEntity<>(json, headers);

		// send request and parse result
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, response.toString(), CommonConstants.INFO);
		return response;
	}

}
