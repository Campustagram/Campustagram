package com.campustagram.core.controller.system.performance;

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
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.model.MemoryInfo;
import com.campustagram.core.persistence.MemoryInfoRepository;
import com.campustagram.core.service.LoggerService;

@ManagedBean(name = "memoryController")
@Scope(value = "session")
@Component(value = "memoryController")
@Join(path = "/performance/memory", to = "/pages/core/systemManagement/performance/memory.jsf")
public class MemoryController {

	@Autowired
	private Server server;
	@Autowired
	private MemoryInfoRepository memoryInfoRepository;
	@Autowired
	private LoggerService loggerService;
	private static final String ACTIVE_CLASS_NAME = "MemoryController";

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.START);

		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.END);
	}

	public String prepareGraphWithPart(String part) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "prepareGraphWithPart", null, CommonConstants.START);

		List<MemoryInfo> memoryInfoList = memoryInfoRepository.findAllNotDeleted();
		StringBuilder sb = new StringBuilder();

		for (MemoryInfo memoryInfo : memoryInfoList) {

			try {
				StringBuilder sbInner = new StringBuilder();
				sbInner.append("[");
				sbInner.append(memoryInfo.getCreateDate().getTime());
				sbInner.append(",");

				if (part.equals("UsedPercentage")) {
					sbInner.append(memoryInfo.getUsedPercentage());
				} else if (part.equals("FreeMemoryMB")) {
					sbInner.append(memoryInfo.getFreeMemoryMB());
				} else if (part.equals("TotalMemoryMB")) {
					sbInner.append(memoryInfo.getTotalMemoryMB());
				} else if (part.equals("UsedMemoryMB")) {
					sbInner.append(memoryInfo.getUsedMemoryMB());
				}

				sbInner.append("],");
				sb.append(sbInner);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "prepareGraphWithPart", null, CommonConstants.END);
		return CommonFunctions.removeLastCharacterFromString(sb.toString());
	}

	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);

		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
		return null;
	}

}
