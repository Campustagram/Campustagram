package com.campustagram.core.model;

public class AppSystem {
	private String osName = System.getProperty("os.name");
	private String osVersion = System.getProperty("os.version");
	private String javaVersion = System.getProperty("java.version");

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}

}
