package com.campustagram.core.controller;

import javax.faces.bean.ManagedBean;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "errorPageController")
@Scope(value = "request")
@Component(value = "errorPageController")
@Join(path = "/errorpage", to = "/pages/core/errorpage/errorpage.jsf")
@Controller
public class ErrorPageController implements ErrorController {

	@Autowired
	private Server server;
	@Autowired
	private LoggerService loggerService;
	
	private Integer statusCode;
	private String statusMessage;

	private static final String ACTIVE_CLASS_NAME = "ErrorPageController";

	public ErrorPageController() {
		super();
	}

	@Override
	public String getErrorPath() {
		return "/errorpage";
	}

	@RequestMapping("/error")
	public String handleError(HttpServletRequest request) throws Exception {
		final String ACTIVE_METHOD_NAME = "handleError";

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, "handleError: " + status.toString(),
				CommonConstants.ERROR);

		statusCode = Integer.valueOf(status.toString());
		if (statusCode == HttpStatus.NOT_FOUND.value()) {
			setStatusMessage("404_messages");
		} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
			setStatusMessage("403_messages");
		} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
			setStatusMessage("500_messages");
		} else if (statusCode == HttpStatus.BAD_GATEWAY.value()) {
			setStatusMessage("502_messages");
		} else if (statusCode == HttpStatus.SERVICE_UNAVAILABLE.value()) {
			setStatusMessage("503_messages");
		} else if (statusCode == HttpStatus.GATEWAY_TIMEOUT.value()) {
			setStatusMessage("504_messages");
		} else if (statusCode == HttpStatus.LOOP_DETECTED.value()) {
			setStatusMessage("508_messages");
		}

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, statusCode.toString(), CommonConstants.END);
		return "/errorpage";
	}

	public String redirectToLanding() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "redirectToLanding", null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "redirectToLanding", null, CommonConstants.END);
		return NavigationUtils.buildRedirectionString("/index");
	}

	public void writeLog() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "writeLog", null, CommonConstants.START);
		server.createPageVisitLog(null, "error" + getStatusCode(), CommonConstants.LOG_DANGER);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "writeLog", null, CommonConstants.END);
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

}