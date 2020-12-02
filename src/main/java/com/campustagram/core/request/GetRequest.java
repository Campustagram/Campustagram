package com.campustagram.core.request;

import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.controller.log.ILogger;

public class GetRequest {

	private static final String ACTIVE_CLASS_NAME = "PostRequest";

	private ILogger logger = new ILogger();

	// Constructors
	public GetRequest() {
		super();
	}

	// Methods
	public String getJson(String uri) throws Exception {
		final String ACTIVE_METHOD_NAME = "getJson";

		RestTemplate restTemplate = new RestTemplate();
		// create response
		HttpEntity<String> entity = restTemplate.getForEntity(uri, String.class);
		String response = entity.getBody();
		logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, response.toString(), CommonConstants.INFO);
		return response;

	}
}
