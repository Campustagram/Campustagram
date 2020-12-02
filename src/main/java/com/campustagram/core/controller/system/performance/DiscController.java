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
import com.campustagram.core.model.DiscInfo;
import com.campustagram.core.persistence.DiscInfoRepository;
import com.campustagram.core.service.LoggerService;

@ManagedBean(name = "discController")
@Scope(value = "session")
@Component(value = "discController")
@Join(path = "/performance/disc", to = "/pages/core/systemManagement/performance/disc.jsf")
public class DiscController {

	@Autowired
	private Server server;
	@Autowired
	private DiscInfoRepository discInfoRepository;
	@Autowired
	private LoggerService loggerService;
	
	private static final String ACTIVE_CLASS_NAME = "DiscController";

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.START);

		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.END);
	}

	public String prepareGraphWithPart(String part) {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "prepareGraphWithPart", null, CommonConstants.START);

		List<DiscInfo> discInfoList = discInfoRepository.findAllNotDeleted();
		StringBuilder sb = new StringBuilder();

		for (DiscInfo discInfo : discInfoList) {

			try {
				StringBuilder sbInner = new StringBuilder();
				sbInner.append("[");
				sbInner.append(discInfo.getCreateDate().getTime());
				sbInner.append(",");

				if (part.equals("TotalSpace1")) {
					sbInner.append(discInfo.getTotalSpace1());
				} else if (part.equals("UsableSpace1")) {
					sbInner.append(discInfo.getUsableSpace1());
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
