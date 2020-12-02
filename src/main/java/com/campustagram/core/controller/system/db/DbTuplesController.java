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
import org.springframework.stereotype.Component;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.persistence.ObjectRepository;
import com.campustagram.core.service.LoggerService;

@ManagedBean(name = "dbTuplesController")
@Scope(value = "session")
@Component(value = "dbTuplesController")
@Join(path = "/dbdeadtupleschecker", to = "/pages/core/systemManagement/db/dbdeadtupleschecker.jsf")
public class DbTuplesController {

	@Autowired
	private Server server;
	@Autowired
	private ObjectRepository objectRepository;
	@Autowired
	private LoggerService loggerService;

	private List<Object[]> dbTableNameAndDeadTuples = new ArrayList<>();
	private String searchKeyword;

	private static final String ACTIVE_CLASS_NAME = "DbTuplesController";

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.START);
		try {

			setDbTableNameAndDeadTuples(objectRepository.getDbDeadTuples());
		} catch (Exception e) {
			loggerService.writeError(ACTIVE_CLASS_NAME, "loadData",
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}

		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.END);
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

	public List<Object[]> getDbTableNameAndDeadTuples() {
		return dbTableNameAndDeadTuples;
	}

	public void setDbTableNameAndDeadTuples(List<Object[]> dbTableNameAndDeadTuples) {
		this.dbTableNameAndDeadTuples = dbTableNameAndDeadTuples;
	}

}
