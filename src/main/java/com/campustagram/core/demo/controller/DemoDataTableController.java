package com.campustagram.core.demo.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.demo.model.Car;
import com.campustagram.core.demo.persistance.CarRepository;
import com.campustagram.core.demo.service.CarService;
import com.campustagram.core.util.NavigationUtils;

/**
 * created on 2018/09/26
 * 
 * @author Ahmet ŞEN
 *
 */
@Scope(value = "session")
@Component(value = "demoDataTableController")
@ManagedBean(name = "demoDataTableController")
@Join(path = "/demodatatable", to = "/pages/core/demo/demodatatable.jsf")
public class DemoDataTableController {
	@Autowired
	private CarRepository carRepository;

	private List<Car> selectedCars;
	private List<Car> cars6;
	private String searchWord;

	@PostConstruct
	public void init() {

	}

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		cars6 = carRepository.findAll();
	}

	public void deleteCar(Car car) {
		carRepository.delete(car);
		loadData();
	}

	public String saveCar(Car car) {
		carRepository.save(car);
		loadData();
		return NavigationUtils.buildRedirectionString("/demodatatable");
	}

	public String addDemoCars() {
		CarService carService = new CarService();
		List<Car> newCarList = carService.createCars(100000);
		System.out.println("New car count ");
		newCarList.forEach(car -> {
			carRepository.save(car);
			//System.out.println("New car saved: " + car.getBrand() + " => " + car.getColor());
		});
		loadData();
		return NavigationUtils.buildRedirectionString("/demodatatable");
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
