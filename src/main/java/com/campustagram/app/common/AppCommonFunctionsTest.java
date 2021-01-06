package com.campustagram.app.common;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AppCommonFunctionsTest {

	AppCommonFunctions appcommonfunctions = new AppCommonFunctions();
	
	@SuppressWarnings("static-access")
	@Test
	/**
	 * checkTheValidityOfTheIntegerVariableAndWriteErrorMessageValidVariable methodunun upperLimit değerinden daha büyük bir değer ile testi.
	 */
	void checkTheValidityOfTheIntegerVariableAndWriteErrorMessageGreaterUpperLimitVariableTest() {

		assertThrows(Exception.class,() -> appcommonfunctions.checkTheValidityOfTheIntegerVariableAndWriteErrorMessage(30, "testname", 3, 20, "lowerLimitErrorMessage", "upperLimitErrorMessage", true));

	}

	@SuppressWarnings("static-access")
	@Test
	/**
	 * checkTheValidityOfTheIntegerVariableAndWriteErrorMessageValidVariable methodunun lower limit değerinden daha küçük bir değer ile testi.
	 */
	void checkTheValidityOfTheIntegerVariableAndWriteErrorMessageLessLowerLimitVariableTest() {

		assertThrows(Exception.class,() -> appcommonfunctions.checkTheValidityOfTheIntegerVariableAndWriteErrorMessage(0, "testname", 3, 20, "lowerLimitErrorMessage", "upperLimitErrorMessage", true));

	}

	@SuppressWarnings("static-access")
	@Test
	/**
	 * checkTheValidityOfTheIntegerVariableAndWriteErrorMessageValidVariable methodunun doğru parametreler ile testi
	 */
	void checkTheValidityOfTheIntegerVariableAndWriteErrorMessageValidVariableTest() {

		boolean result = appcommonfunctions.checkTheValidityOfTheIntegerVariableAndWriteErrorMessage(5, "testname", 3, 20, "lowerLimitErrorMessage", "upperLimitErrorMessage", true);
		assertEquals(true,result);
	}

	@SuppressWarnings("static-access")
	@Test
	/**
	 * checkTheValidityOfTheIntegerVariableAndWriteErrorMessage methodunun null testi.
	 */
	void checkTheValidityOfTheIntegerVariableAndWriteErrorMessageNullVariableTest() {

		boolean result = appcommonfunctions.checkTheValidityOfTheIntegerVariableAndWriteErrorMessage(null, "testname", 3, 20, "lowerLimitErrorMessage", "upperLimitErrorMessage", false);
		assertEquals(true,result);
	}

	@SuppressWarnings("static-access")
	@Test
	/**
	 * checkTheValidityOfTheStringVariableAndWriteErrorMessage methodunun null input testi.
	 */
	void checkTheValidityOfTheStringVariableAndWriteErrorMessageNullVariableTest() {
		boolean result = appcommonfunctions.checkTheValidityOfTheStringVariableAndWriteErrorMessage(null, "variableName", 3, 20, "lowerLimitErrorMessage", "upperLimitErrorMessage", false);
		assertEquals(true,result);
	}

	@SuppressWarnings("static-access")
	@Test
	/**
	 * checkTheValidityOfTheStringVariableAndWriteErrorMessage methodunun doğru(geçerli) input testi.
	 */
	void checkTheValidityOfTheStringVariableAndWriteErrorMessageValidVariableTest() {
		boolean result = appcommonfunctions.checkTheValidityOfTheStringVariableAndWriteErrorMessage("deneme", "variableName", 3, 20, "lowerLimitErrorMessage", "upperLimitErrorMessage", false);
		assertEquals(true,result);
	}

	@SuppressWarnings("static-access")
	@Test
	/**
	 * checkTheValidityOfTheStringVariableAndWriteErrorMessage methodunun upper limit değerinden daha büyük değer ile testi.
	 */
	void checkTheValidityOfTheStringVariableAndWriteErrorMessageGreaterUpperLimitVariableTest() {
		assertThrows(Exception.class, () -> appcommonfunctions.checkTheValidityOfTheStringVariableAndWriteErrorMessage("uzunstringtesti", "variableName", 3, 5, "lowerLengthErrorMessage", "upperLengthErrorMessage", false));
	}
	@SuppressWarnings("static-access")
	@Test
	/**
	 * checkTheValidityOfTheStringVariableAndWriteErrorMessage methodunun upper limit değerinden daha büyük değer ile testi.
	 */
	void checkTheValidityOfTheStringVariableAndWriteErrorMessageLessThanLowerLimitVariableTest() {
		assertThrows(Exception.class, () -> appcommonfunctions.checkTheValidityOfTheStringVariableAndWriteErrorMessage("a", "variableName", 3, 5, "lowerLengthErrorMessage", "upperLengthErrorMessage", false));
	}






}
