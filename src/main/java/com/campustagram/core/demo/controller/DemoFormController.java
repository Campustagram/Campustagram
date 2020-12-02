package com.campustagram.core.demo.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.demo.model.Car;
import com.campustagram.core.demo.service.CarService;

/**
 * created on 2018/09/26
 * 
 * @author Ahmet ŞEN
 *
 */
@Scope(value = "session")
@Component(value = "demoFormController")
@ManagedBean(name = "demoFormController")
@Join(path = "/demoform", to = "/pages/core/demo/demoform.jsf")
public class DemoFormController {
	@Autowired
	private CarService service;

	private List<Car> selectedCars;
	private List<Car> cars6;
	private String searchWord;

	@PostConstruct
	public void init() {
		cars6 = service.createCars(1000);
	}

	public List<Car> getSelectedCars() {
		return selectedCars;
	}

	public void setSelectedCars(List<Car> selectedCars) {
		this.selectedCars = selectedCars;
	}

	public List<Car> getCars6() {
		return cars6;
	}

	public void setCars6(List<Car> cars6) {
		this.cars6 = cars6;
	}

	public String getSearchWord() {
		return searchWord;
	}

	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}

}
