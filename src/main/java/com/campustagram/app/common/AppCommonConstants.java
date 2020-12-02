package com.campustagram.app.common;

import javax.faces.bean.ManagedBean;

import org.springframework.web.context.annotation.ApplicationScope;

@ManagedBean(name = "appCommonConstants")
@ApplicationScope
public class AppCommonConstants {
	// Machine Start
	public static final int MIN_MACHINE_NO = 2;
	public static final int MAX_MACHINE_NO = Integer.MAX_VALUE;// 2147483647

	public static final int MIN_MACHINE_NAME_LENGTH = 2;
	public static final int MAX_MACHINE_NAME_LENGTH = 32;

	public static final int MIN_MACHINE_MISSION_LENGTH = 2;
	public static final int MAX_MACHINE_MISSION_LENGTH = 32;

	public static final int MIN_MACHINE_HOURLY_STUDY_VALUE = 1;
	public static final int MAX_MACHINE_HOURLY_STUDY_VALUE = 20000;

	// sn cinsinden
	public static final int MIN_MACHINE_WARNING_INTERVAL = 1;
	public static final int MAX_MACHINE_WARNING_INTERVAL = 20000;

	// sn cinsinden
	public static final int MIN_MACHINE_CLOSING_INTERVAL = 2;
	public static final int MAX_MACHINE_CLOSING_INTERVAL = 20000;

	// sn cinsinden
	public static final int MIN_MACHINE_SYNCHRONIZATION_INTERVAL = 2;
	public static final int MAX_MACHINE_SYNCHRONIZATION_INTERVAL = 20000;

	public final static String[] MISSIONS = { "Yan Çatma", "Görev 1", "Görev 2", "Görev 3" };

	public int getMinMachineNo() {
		return MIN_MACHINE_NO;
	}

	public int getMaxMachineNo() {
		return MAX_MACHINE_NO;
	}

	public int getMinMachineNameLength() {
		return MIN_MACHINE_NAME_LENGTH;
	}

	public int getMaxMachineNameLength() {
		return MAX_MACHINE_NAME_LENGTH;
	}

	public int getMinMachineMissionLength() {
		return MIN_MACHINE_MISSION_LENGTH;
	}

	public int getMaxMachineMissionLength() {
		return MAX_MACHINE_MISSION_LENGTH;
	}

	public int getMinMachineHourlyStudyValue() {
		return MIN_MACHINE_HOURLY_STUDY_VALUE;
	}

	public int getMaxMachineHourlyStudyValue() {
		return MAX_MACHINE_HOURLY_STUDY_VALUE;
	}

	public int getMinMachineWarningInterval() {
		return MIN_MACHINE_WARNING_INTERVAL;
	}

	public int getMaxMachineWarningInterval() {
		return MAX_MACHINE_WARNING_INTERVAL;
	}

	public int getMinMachineClosingInterval() {
		return MIN_MACHINE_CLOSING_INTERVAL;
	}

	public int getMaxMachineClosingInterval() {
		return MAX_MACHINE_CLOSING_INTERVAL;
	}

	public int getMinMachineSynchronizationInterval() {
		return MIN_MACHINE_SYNCHRONIZATION_INTERVAL;
	}

	public int getMaxMachineSynchronizationInterval() {
		return MAX_MACHINE_SYNCHRONIZATION_INTERVAL;
	}

	public String[] getMissions() {
		return MISSIONS;
	}
	// Machine End

	// Rfid Start
	public static final int MIN_RFID_NO = 2;
	public static final int MAX_RFID_NO = Integer.MAX_VALUE;// 2147483647

	public int getMinRfidNo() {
		return MIN_RFID_NO;
	}

	public int getMaxRfidNo() {
		return MAX_RFID_NO;
	}
	// Rfid End

	// Worker Start
	public static final int MIN_WORKER_NAME_LENGHT = 2;
	public static final int MAX_WORKER_NAME_LENGHT = 32;

	public static final int MIN_WORKER_SURNAME_LENGHT = 2;
	public static final int MAX_WORKER_SURNAME_LENGHT = 32;

	public static final int MIN_WORKER_PHONENUMBER_LENGHT = 10;
	public static final int MAX_WORKER_PHONENUMBER_LENGHT = 14;

	public static final int MIN_WORKER_ADDRESS_LENGHT = 2;
	public static final int MAX_WORKER_ADDRESS_LENGHT = 512;

	public int getMinWorkerNameLenght() {
		return MIN_WORKER_NAME_LENGHT;
	}

	public int getMaxWorkerNameLenght() {
		return MAX_WORKER_NAME_LENGHT;
	}

	public int getMinWorkerSurnameLenght() {
		return MIN_WORKER_SURNAME_LENGHT;
	}

	public int getMaxWorkerSurnameLenght() {
		return MAX_WORKER_SURNAME_LENGHT;
	}

	public int getMinWorkerPhonenumberLenght() {
		return MIN_WORKER_PHONENUMBER_LENGHT;
	}

	public int getMaxWorkerPhonenumberLenght() {
		return MAX_WORKER_PHONENUMBER_LENGHT;
	}

	public int getMinWorkerAddressLenght() {
		return MIN_WORKER_ADDRESS_LENGHT;
	}

	public int getMaxWorkerAddressLenght() {
		return MAX_WORKER_ADDRESS_LENGHT;
	}

	// Worker End

	// Band Start
	public static final int MIN_BAND_NAME_LENGHT = 2;
	public static final int MAX_BAND_NAME_LENGHT = 256;

	public int getMinBandNameLenght() {
		return MIN_BAND_NAME_LENGHT;
	}

	public int getMaxBandNameLenght() {
		return MAX_BAND_NAME_LENGHT;
	}

	// Band End

	// WorkPlan Start
	public static final int MIN_WORK_PLAN_NAME_LENGHT = 2;
	public static final int MAX_WORK_PLAN_NAME_LENGHT = 256;

	public int getMinWorkPlanNameLenght() {
		return MIN_WORK_PLAN_NAME_LENGHT;
	}

	public int getMaxWorkPlanNameLenght() {
		return MAX_WORK_PLAN_NAME_LENGHT;
	}

	// WorkPlan End
	
	// Model Start
	public static final int MIN_MODEL_NAME_LENGHT = 2;
	public static final int MAX_MODEL_NAME_LENGHT = 256;

	public static final int MIN_MODEL_DESCRIPTION_LENGHT = 2;
	public static final int MAX_MODEL_DESCRIPTION_LENGHT = 2048;

	public int getMinModelNameLenght() {
		return MIN_MODEL_NAME_LENGHT;
	}

	public int getMaxModelNameLenght() {
		return MAX_MODEL_NAME_LENGHT;
	}

	public int getMinModelDescriptionLenght() {
		return MIN_MODEL_DESCRIPTION_LENGHT;
	}

	public int getMaxModelDescriptionLenght() {
		return MAX_MODEL_DESCRIPTION_LENGHT;
	}


	// Model End
}
