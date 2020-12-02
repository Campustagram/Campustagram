package com.campustagram.core.controller.system.db;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.persistence.ObjectRepository;
import com.campustagram.core.service.LoggerService;

@ManagedBean(name = "dbSizeController")
@Scope(value = "session")
@Component(value = "dbSizeController")
@Join(path = "/dbsizechecker", to = "/pages/core/systemManagement/db/dbsizedbsizechecker.jsf")
public class DbSizeController {

	@Autowired
	private ObjectRepository objectRepository;
	@Autowired
	private Environment env;
	@Autowired
	private LoggerService loggerService;

	private List<Object[]> dbNameAndSizes = new ArrayList<>();
	private List<Object[]> dbTableNameAndSizes = new ArrayList<>();
	private String searchKeyword;

	private static final String ACTIVE_CLASS_NAME = "DbSizeController";

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		final String ACTIVE_METHOD_NAME = "loadData";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		setDbTableNameAndSizes(objectRepository.getDbTableNameAndSizes());

		try {
			String dbInfo = env.getProperty("spring.datasource.url");
			String[] splittedDbInfo = dbInfo.split("/");
			setDbNameAndSizes(objectRepository.getDbNameAndSizes(splittedDbInfo[splittedDbInfo.length - 1]));
		} catch (Exception e) {
			loggerService.writeError(ACTIVE_CLASS_NAME, "loadData",
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);

		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
		return null;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public List<Object[]> getDbTableNameAndSizes() {
		return dbTableNameAndSizes;
	}

	public void setDbTableNameAndSizes(List<Object[]> dbTableNameAndSizes) {
		this.dbTableNameAndSizes = dbTableNameAndSizes;
	}

	public List<Object[]> getDbNameAndSizes() {
		return dbNameAndSizes;
	}

	public void setDbNameAndSizes(List<Object[]> dbNameAndSizes) {
		this.dbNameAndSizes = dbNameAndSizes;
	}

}
