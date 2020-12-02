package com.campustagram.landing.controller;

import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@ManagedBean(name = "indexController")
@Scope(value = "session")
@Component(value = "indexController")
@Join(path = "/", to = "/pages/landing/index.jsf")
public class IndexController {
	
}
