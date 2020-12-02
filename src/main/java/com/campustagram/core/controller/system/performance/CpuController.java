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
import com.campustagram.core.model.CpuInfo;
import com.campustagram.core.persistence.CpuInfoRepository;
import com.campustagram.core.service.LoggerService;

@ManagedBean(name = "cpuController")
@Scope(value = "session")
@Component(value = "cpuController")
@Join(path = "/performance/cpu", to = "/pages/core/systemManagement/performance/cpu.jsf")
public class CpuController {

	@Autowired
	private Server server;
	@Autowired
	private CpuInfoRepository cpuInfoRepository;
	@Autowired
	private LoggerService loggerService;
	
	private static final String ACTIVE_CLASS_NAME = "HardwareController";

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.START);

		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.END);
	}

	public String prepareGraphWithPart(String part) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "prepareGraphWithPart", null, CommonConstants.START);

		List<CpuInfo> cpuInfoList = cpuInfoRepository.findAllNotDeleted();
		StringBuilder sb = new StringBuilder();

		for (CpuInfo cpuInfo : cpuInfoList) {

			try {
				StringBuilder sbInner = new StringBuilder();
				sbInner.append("[");
				sbInner.append(cpuInfo.getCreateDate().getTime());
				sbInner.append(",");

				if (part.equals("ProcessCpuLoad")) {
					sbInner.append(cpuInfo.getProcessCpuLoad() * 100);
				} else if (part.equals("SystemCpuLoad")) {
					sbInner.append(cpuInfo.getSystemCpuLoad() * 100);
				} else if (part.equals("ProcessCpuTime")) {
					sbInner.append(cpuInfo.getProcessCpuTime());
				}

				sbInner.append("],");
				sb.append(sbInner);
				System.out.println(sb);
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
