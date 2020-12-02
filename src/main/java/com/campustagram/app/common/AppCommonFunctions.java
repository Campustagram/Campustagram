package com.campustagram.app.common;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonFunctions;

@ManagedBean(name = "appCommonFunctions")
@ViewScoped
public class AppCommonFunctions {
	private static final String ACTIVE_CLASS_NAME = "AppCommonFunctions";

	/**
	 * errrorIfNull true ise: variable null sa hata mesajı verir, return false
	 * döner.<br>
	 * errrorIfNull false ise: variable null sa hata mesajı vermez, return true
	 * döner.<br>
	 * Alt ya da üst limit i sağlamıyorsa hata mesajı basar. return false döner.<br>
	 * 
	 * @param variable
	 * @param variableName
	 * @param lowerLimit
	 * @param upperLimit
	 * @param lowerLimitErrorMessage
	 * @param upperLimitErrorMessage
	 * @param errorIfNull
	 * @return
	 */
	public static boolean checkTheValidityOfTheIntegerVariableAndWriteErrorMessage(Integer variable,
			String variableName, int lowerLimit, int upperLimit, String lowerLimitErrorMessage,
			String upperLimitErrorMessage, boolean errorIfNull) {
		boolean result = true;
		if (null != variable) {
			if (variable < lowerLimit) {
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR,
						variableName + lowerLimit + lowerLimitErrorMessage, CommonConstants.WHITE_SPACE_CHAR);
				result = false;
			}
			if (variable > upperLimit) {
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR,
						variableName + upperLimit + upperLimitErrorMessage, CommonConstants.WHITE_SPACE_CHAR);
				result = false;
			}
			return result;
		} else {
			if (errorIfNull) {
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, variableName + " boş olamaz! ",
						CommonConstants.WHITE_SPACE_CHAR);
				return false;
			} else {
				return true;
			}
		}
	}

	public static boolean checkTheValidityOfTheStringVariableAndWriteErrorMessage(String variable, String variableName,
			int lowerLength, int upperLength, String lowerLengthErrorMessage, String upperLengthErrorMessage,
			boolean errorIfNull) {
		boolean result = true;

		if (null != variable) {
			if (variable.length() < lowerLength) {
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR,
						variableName + " uzunluğu " + lowerLength + lowerLengthErrorMessage,
						CommonConstants.WHITE_SPACE_CHAR);
				result = false;
			}
			if (variable.length() > upperLength) {
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR,
						variableName + " uzunluğu " + upperLength + upperLengthErrorMessage,
						CommonConstants.WHITE_SPACE_CHAR);
				result = false;
			}
			return result;
		} else {
			if (errorIfNull) {
				CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR, variableName + " boş olamaz! ",
						CommonConstants.WHITE_SPACE_CHAR);
				return false;
			} else {
				return true;
			}
		}
	}

}