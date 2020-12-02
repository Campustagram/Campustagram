package com.campustagram.core.controller.user.login;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.stereotype.Controller;

@ManagedBean(name = "logoutController")
@Scope(value = "request")
@Controller(value = "logoutController")
public class LogoutController {

	public void logout(HttpServletRequest request , HttpServletResponse response) throws IOException{
		new DefaultRedirectStrategy().sendRedirect(request, response, "/logout");
	}
}