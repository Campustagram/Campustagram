package com.campustagram.core.controller.log;

public interface AppLogger {

	void writeInfo(String className, String methodName, String info, String status);

	void writeWarn(String className, String methodName, String info, String status);

	void writeError(String className, String methodName, String info, String status);

	void writeDebug(String className, String methodName, String info, String status);

}
