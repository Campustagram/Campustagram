package com.campustagram.core.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonPerformance;
import com.campustagram.core.controller.log.ILogger;
import com.campustagram.core.enums.SystemProcessType;
import com.campustagram.core.model.EmailTemplate;
import com.campustagram.core.model.SystemInformation;
import com.campustagram.core.persistence.CpuInfoRepository;
import com.campustagram.core.persistence.DiscInfoRepository;
import com.campustagram.core.persistence.MemoryInfoRepository;
import com.campustagram.core.persistence.ObjectRepository;
import com.campustagram.core.persistence.SystemInformationRepository;
import com.campustagram.core.service.MailSenderService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Component
public class ScheduledTasks {
	@Autowired
	private Environment env;
	@Autowired
	private MailSenderService mailSenderService;
	@Autowired
	private SystemInformationRepository systemInformationRepository;
	@Autowired
	private ObjectRepository objectRepository;
	@Autowired
	private MemoryInfoRepository memoryInfoRepository;
	@Autowired
	private CpuInfoRepository cpuInfoRepository;
	@Autowired
	private DiscInfoRepository discInfoRepository;

	private ObjectMapper objectMapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
			.setDateFormat(new SimpleDateFormat("yyyy.MM.dd hh:mm:ss"));

	private boolean isEmailSenderWork = false;

	private ILogger logger = new ILogger();
	private static final String ACTIVE_CLASS_NAME = "ScheduledTasks";

	public ScheduledTasks() {
		super();
	}

	// second, minute, hour, day, month, weekday
	@Scheduled(cron = "0 0 2 */1 * *")
	public void checkDailySystemInfo() {
		final String ACTIVE_METHOD_NAME = "checkDailySystemInfo";
		//logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		try {
			// DB LOGGER
			List<Object[]> dbNameAndSizes = new ArrayList<>();

			try {
				String dbInfo = env.getProperty("spring.datasource.url");
				String[] splittedDbInfo = dbInfo.split("/");
				dbNameAndSizes = objectRepository.getDbNameAndSizes(splittedDbInfo[splittedDbInfo.length - 1]);
			} catch (Exception e) {
				logger.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
						com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
			}

			SystemInformation systemInformation = new SystemInformation();
			systemInformation.setProcessType(SystemProcessType.DBSIZE.toString());
			systemInformation.setInfo(dbNameAndSizes.get(0)[2].toString());
			systemInformationRepository.save(systemInformation);
		} catch (Exception e) {
			logger.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}

		try {
			discInfoRepository.save(CommonPerformance.generateDiscInfo());
		} catch (Exception e) {
			logger.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}
		//logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	@Scheduled(fixedRate = 25 * 60 * 1000)
	public void runPer25Minute() {
		final String ACTIVE_METHOD_NAME = "runPer10Minute";

		//logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		// CPU
		try {
			cpuInfoRepository.save(CommonPerformance.generateCpuInfo());
		} catch (Exception e) {
			logger.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}

		// MEMORY
		try {
			memoryInfoRepository.save(CommonPerformance.generateMemoryInfo());
		} catch (Exception e) {
			logger.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}

		// DISC
		try {
			discInfoRepository.save(CommonPerformance.generateDiscInfo());
		} catch (Exception e) {
			logger.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
					com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		}

		//logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);

	}

	@Scheduled(fixedRate = 20 * 1000)
	public void runPer20Second() {
		final String ACTIVE_METHOD_NAME = "runPer20Second";

		//logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		sendMailFromPool();
		//logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public void sendMailFromPool() {
		final String ACTIVE_METHOD_NAME = "sendMailFromPool";

		//logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		if ((null != env.getProperty("system.sendmail")) && (env.getProperty("system.sendmail").equals("true"))) {
			if (!isEmailSenderWork) {
				isEmailSenderWork = true;
				boolean isContinue = true;
				while (isContinue) {
					try {
						List<EmailTemplate> listEmailTemplates = mailSenderService.getEmailTemplates();
						if (!listEmailTemplates.isEmpty()) {
							mailSenderService.sendPoolTemplates(listEmailTemplates);
						} else {
							isContinue = false;
						}
					} catch (Exception e) {
						isContinue = false;
						logger.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME,
								com.campustagram.core.common.CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
					}
				}
				isEmailSenderWork = false;
			}
		}

		//logger.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

}