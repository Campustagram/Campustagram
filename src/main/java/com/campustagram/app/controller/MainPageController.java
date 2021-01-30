package com.campustagram.app.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.app.model.DeviceFilter;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.demo.model.Car;
import com.campustagram.core.demo.service.CarService;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.service.LoggerService;

@ManagedBean(name = "mainPageController")
@Scope(value = "session")
@Component(value = "mainPageController")
@Join(path = "/mainpage", to = "/pages/app/mainpage.jsf")
public class MainPageController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LoggerService loggerService;

	@Autowired
	private CarService service;
	private static final String ACTIVE_CLASS_NAME = "UserListController";

	private List<Car> cars;

	private Car selectedCar;
	private boolean value1;

	private TreeNode root;
	private TreeNode[] selectedNodes;

	@PostConstruct
	public void init() {
		cars = service.createCars(48);
	}

	public void releaseDevice() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.START);
		System.out.println("releaseDevice clicked");
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.END);
	}

	public TreeNode createDocuments() {
		TreeNode root = new CheckboxTreeNode(new DeviceFilter("Root"), null);

		TreeNode versions = (TreeNode) new CheckboxTreeNode(new DeviceFilter("version"), root);
		TreeNode brands = (TreeNode) new CheckboxTreeNode(new DeviceFilter("brand"), root);
		TreeNode resolutions = (TreeNode) new CheckboxTreeNode(new DeviceFilter("resolution"), root);

		// Versions
		TreeNode version1 = new CheckboxTreeNode("version", new DeviceFilter("6.4"), versions);
		TreeNode version2 = new CheckboxTreeNode("version", new DeviceFilter("9.0"), versions);
		TreeNode version3 = new CheckboxTreeNode("version", new DeviceFilter("9.1"), versions);

		// Brands
		TreeNode brand1 = new CheckboxTreeNode("brand", new DeviceFilter("Samsung"), brands);
		TreeNode brand2 = new CheckboxTreeNode("brand", new DeviceFilter("Huawei"), brands);
		TreeNode brand3 = new CheckboxTreeNode("brand", new DeviceFilter("Lg"), brands);

		// Resolutions
		TreeNode resolution1 = new CheckboxTreeNode("resolution", new DeviceFilter("1920*1080"), resolutions);
		TreeNode resolution2 = new CheckboxTreeNode("resolution", new DeviceFilter("1080*768"), resolutions);
		TreeNode resolution3 = new CheckboxTreeNode("resolution", new DeviceFilter("1024*768"), resolutions);

		return root;

	}

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.START);

		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.END);
	}

	public void startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);
		root = createDocuments();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
	}

	public void addMessage() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "addMessage", null, CommonConstants.START);

		String summary = value1 ? "Checked" : "Unchecked";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "addMessage", null, CommonConstants.END);

	}

	public List<Car> getCars() {
		return cars;
	}

	public void setService(CarService service) {
		this.service = service;
	}

	public Car getSelectedCar() {
		return selectedCar;
	}

	public void setSelectedCar(Car selectedCar) {
		this.selectedCar = selectedCar;
	}

	public boolean isValue1() {
		return value1;
	}

	public void setValue1(boolean value1) {
		this.value1 = value1;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

}
