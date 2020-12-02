package com.campustagram.core.config;

import javax.faces.bean.ManagedBean;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * created on 2018/09/26
 * 
 * @author Ahmet ŞEN
 *
 */
@ManagedBean(name = "coreConfig")
@Scope(value = "session")
@Component(value = "coreConfig")

public class CoreConfig {
	public static final String LOADING_IMAGE = "img/system/lg.square-grid-loader.gif";
	public static String TEMPLATE_SKIN = "skin-blue";
	private String VERTICAL_ADMIN_TEMPLATE_PATH = "admin.xhtml";
	private String HORIZONTAL_ADMIN_TEMPLATE_PATH = "admin-top.xhtml";
	public String SYSTEM_NAME = "Campustagram";
	public String SYSTEM_REPLY_MAIL_ADDRESS = "dodgehellcat3478@gmail.com";

	private boolean verticalTopMenu = false;
	private boolean showDatatableExport = false;

	public String getAdminTemplate() {
		if (verticalTopMenu) {
			return VERTICAL_ADMIN_TEMPLATE_PATH;
		} else {
			return HORIZONTAL_ADMIN_TEMPLATE_PATH;
		}
	}

	public String getLogadingImage() {
		return LOADING_IMAGE;
	}

	public String getSkin() {
		return TEMPLATE_SKIN;
	}

	public boolean isVerticalTopMenu() {
		return verticalTopMenu;
	}

	public void setVerticalTopMenu(boolean verticalTopMenu) {
		this.verticalTopMenu = verticalTopMenu;
	}

	public boolean isShowDatatableExport() {
		return showDatatableExport;
	}

	public void setShowDatatableExport(boolean showDatatableExport) {
		this.showDatatableExport = showDatatableExport;
	}

	public String getSYSTEM_NAME() {
		return SYSTEM_NAME;
	}

	public void setSYSTEM_NAME(String sYSTEM_NAME) {
		SYSTEM_NAME = sYSTEM_NAME;
	}

	public String getSYSTEM_REPLY_MAIL_ADDRESS() {
		return SYSTEM_REPLY_MAIL_ADDRESS;
	}

	public void setSYSTEM_REPLY_MAIL_ADDRESS(String sYSTEM_REPLY_MAIL_ADDRESS) {
		SYSTEM_REPLY_MAIL_ADDRESS = sYSTEM_REPLY_MAIL_ADDRESS;
	}

}
