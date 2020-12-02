package com.campustagram.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SystemColor implements Serializable {
	@Column(length = 9)
	private String COLOR_INFO = "4b9ff1";
	@Column(length = 7)
	private String COLOR_ERROR = "af2f1c";
	@Column(length = 9)
	private String COLOR_WARN = "f1604b";
	@Column(length = 9)
	private String COLOR_SUCCESS = "33842d";

	@Column(length = 9)
	private String COLOR_EDIT = "dab249";
	@Column(length = 9)
	private String COLOR_ADD = "2196f3";
	@Column(length = 9)
	private String COLOR_DELETE = "af2f1c";
	@Column(length = 9)
	private String COLOR_BLOCK = "33842d";
	@Column(length = 9)
	private String COLOR_UNBLOCK = "f1604b";
	@Column(length = 9)
	private String COLOR_CANCEL = "f1604b";
	@Column(length = 9)
	private String COLOR_REFRESH = "2196f3";
	@Column(length = 9)
	private String COLOR_SAVE = "33842d";
	@Column(length = 9)
	private String COLOR_CLEARFORM = "2196f3";
	@Column(length = 9)
	private String COLOR_SHOW = "2196f3";
	@Column(length = 9)
	private String COLOR_ACTIVE = "2196f3";
	@Column(length = 9)
	private String COLOR_PASSIVE = "f1604b";

	@Column(length = 9)
	private String COLOR_MAINTENANCE_ON = "455f7e";
	@Column(length = 9)
	private String COLOR_MAINTENANCE_ON_OUT = "1d2a3e";

	@Override
	public String toString() {
		return "Color [COLOR_INFO=" + COLOR_INFO + ", COLOR_ERROR=" + COLOR_ERROR + ", COLOR_WARN=" + COLOR_WARN
				+ ", COLOR_SUCCESS=" + COLOR_SUCCESS + ", COLOR_EDIT=" + COLOR_EDIT + ", COLOR_ADD=" + COLOR_ADD
				+ ", COLOR_DELETE=" + COLOR_DELETE + ", COLOR_BLOCK=" + COLOR_BLOCK + ", COLOR_UNBLOCK=" + COLOR_UNBLOCK
				+ ", COLOR_CANCEL=" + COLOR_CANCEL + ", COLOR_REFRESH=" + COLOR_REFRESH + ", COLOR_SAVE=" + COLOR_SAVE
				+ ", COLOR_CLEARFORM=" + COLOR_CLEARFORM + ", COLOR_SHOW=" + COLOR_SHOW + ", COLOR_ACTIVE="
				+ COLOR_ACTIVE + ", COLOR_PASSIVE=" + COLOR_PASSIVE + ", COLOR_MAINTENANCE_ON=" + COLOR_MAINTENANCE_ON
				+ ", COLOR_MAINTENANCE_ON_OUT=" + COLOR_MAINTENANCE_ON_OUT + "]";
	}

	public String getCOLOR_INFO() {
		return COLOR_INFO;
	}

	public void setCOLOR_INFO(String cOLOR_INFO) {
		COLOR_INFO = cOLOR_INFO;
	}

	public String getCOLOR_ERROR() {
		return COLOR_ERROR;
	}

	public void setCOLOR_ERROR(String cOLOR_ERROR) {
		COLOR_ERROR = cOLOR_ERROR;
	}

	public String getCOLOR_WARN() {
		return COLOR_WARN;
	}

	public void setCOLOR_WARN(String cOLOR_WARN) {
		COLOR_WARN = cOLOR_WARN;
	}

	public String getCOLOR_SUCCESS() {
		return COLOR_SUCCESS;
	}

	public void setCOLOR_SUCCESS(String cOLOR_SUCCESS) {
		COLOR_SUCCESS = cOLOR_SUCCESS;
	}

	public String getCOLOR_EDIT() {
		return COLOR_EDIT;
	}

	public void setCOLOR_EDIT(String cOLOR_EDIT) {
		COLOR_EDIT = cOLOR_EDIT;
	}

	public String getCOLOR_CANCEL() {
		return COLOR_CANCEL;
	}

	public void setCOLOR_CANCEL(String cOLOR_CANCEL) {
		COLOR_CANCEL = cOLOR_CANCEL;
	}

	public String getCOLOR_REFRESH() {
		return COLOR_REFRESH;
	}

	public void setCOLOR_REFRESH(String cOLOR_REFRESH) {
		COLOR_REFRESH = cOLOR_REFRESH;
	}

	public String getCOLOR_SAVE() {
		return COLOR_SAVE;
	}

	public void setCOLOR_SAVE(String cOLOR_SAVE) {
		COLOR_SAVE = cOLOR_SAVE;
	}

	public String getCOLOR_CLEARFORM() {
		return COLOR_CLEARFORM;
	}

	public void setCOLOR_CLEARFORM(String cOLOR_CLEARFORM) {
		COLOR_CLEARFORM = cOLOR_CLEARFORM;
	}

	public String getCOLOR_SHOW() {
		return COLOR_SHOW;
	}

	public void setCOLOR_SHOW(String cOLOR_SHOW) {
		COLOR_SHOW = cOLOR_SHOW;
	}

	public String getCOLOR_ADD() {
		return COLOR_ADD;
	}

	public void setCOLOR_ADD(String cOLOR_ADD) {
		COLOR_ADD = cOLOR_ADD;
	}

	public String getCOLOR_DELETE() {
		return COLOR_DELETE;
	}

	public void setCOLOR_DELETE(String cOLOR_DELETE) {
		COLOR_DELETE = cOLOR_DELETE;
	}

	public String getCOLOR_BLOCK() {
		return COLOR_BLOCK;
	}

	public void setCOLOR_BLOCK(String cOLOR_BLOCK) {
		COLOR_BLOCK = cOLOR_BLOCK;
	}

	public String getCOLOR_UNBLOCK() {
		return COLOR_UNBLOCK;
	}

	public void setCOLOR_UNBLOCK(String cOLOR_UNBLOCK) {
		COLOR_UNBLOCK = cOLOR_UNBLOCK;
	}

	public String getCOLOR_ACTIVE() {
		return COLOR_ACTIVE;
	}

	public void setCOLOR_ACTIVE(String cOLOR_ACTIVE) {
		COLOR_ACTIVE = cOLOR_ACTIVE;
	}

	public String getCOLOR_PASSIVE() {
		return COLOR_PASSIVE;
	}

	public void setCOLOR_PASSIVE(String cOLOR_PASSIVE) {
		COLOR_PASSIVE = cOLOR_PASSIVE;
	}

	public String getCOLOR_MAINTENANCE_ON() {
		return COLOR_MAINTENANCE_ON;
	}

	public void setCOLOR_MAINTENANCE_ON(String cOLOR_MAINTENANCE_ON) {
		COLOR_MAINTENANCE_ON = cOLOR_MAINTENANCE_ON;
	}

	public String getCOLOR_MAINTENANCE_ON_OUT() {
		return COLOR_MAINTENANCE_ON_OUT;
	}

	public void setCOLOR_MAINTENANCE_ON_OUT(String cOLOR_MAINTENANCE_ON_OUT) {
		COLOR_MAINTENANCE_ON_OUT = cOLOR_MAINTENANCE_ON_OUT;
	}

}
