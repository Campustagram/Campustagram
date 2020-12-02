package com.campustagram.core.exceptionhandling;

import java.util.Iterator;
import java.util.Map;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import org.primefaces.PrimeFaces;
import org.springframework.security.access.AccessDeniedException;

public class CustomExceptionHandler extends ExceptionHandlerWrapper {
	private ExceptionHandler exceptionHandler;

	public CustomExceptionHandler(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return exceptionHandler;
	}

	@Override
	public void handle() throws FacesException {
		final Iterator<ExceptionQueuedEvent> queue = getUnhandledExceptionQueuedEvents().iterator();

		while (queue.hasNext()) {
			ExceptionQueuedEvent item = queue.next();
			ExceptionQueuedEventContext exceptionQueuedEventContext = (ExceptionQueuedEventContext) item.getSource();

			try {
				Throwable throwable = exceptionQueuedEventContext.getException();
				System.err.println("Exception: " + throwable.getMessage());

				System.err.println("x:" + throwable.getClass());

				FacesContext context = FacesContext.getCurrentInstance();
				Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
				NavigationHandler nav = context.getApplication().getNavigationHandler();

				requestMap.put("error-message", throwable.getMessage());
				requestMap.put("error-stack", throwable.getStackTrace());
				
				if(throwable instanceof AccessDeniedException || throwable instanceof IllegalStateException) {
					PrimeFaces.current().executeScript("PF('timeRunDialog').show();");
					nav.handleNavigation(context , null , null);
				}

				context.renderResponse();
			} finally {
				queue.remove();
			}
		}
	}
}
