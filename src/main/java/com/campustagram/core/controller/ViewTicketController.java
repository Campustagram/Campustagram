package com.campustagram.core.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.enums.TicketProcessType;
import com.campustagram.core.model.Ticket;
import com.campustagram.core.persistence.TicketRepository;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "viewTicketController")
@Scope(value = "session")
@Component(value = "viewTicketController")
@Join(path = "/viewticket", to = "/pages/core/support/viewticket.jsf")
public class ViewTicketController {
	@Autowired
	private ActiveUserService activeUserService;
	@Autowired
	private TicketRepository ticketRepository;
	@Autowired
	private LoggerService loggerService;
	
	private Ticket tmpTicket;
	private Ticket tmpReplyTicket;

	private List<Ticket> replyTickets = new ArrayList<>();

	private List<String> ticketPriorityList = new ArrayList<String>() {
		{
			add(TicketProcessType.LOW.toString());
			add(TicketProcessType.MEDIUM.toString());
			add(TicketProcessType.HIGH.toString());
		}
	};

	private static final String ACTIVE_CLASS_NAME = "ViewTicketController";

	public void refreshData() {
		try {
			replyTickets.clear();
			if (null != tmpReplyTicket) {
				replyTickets = ticketRepository.findAllReplyTicketByParentId(tmpReplyTicket.getId());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public String saveTicket() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "saveTicket", null, CommonConstants.START);

		try {
			if ((null == tmpTicket.getId()) && (null != tmpReplyTicket) && (null != tmpReplyTicket.getId())) {
				tmpTicket.setReplyTo(tmpReplyTicket.getId());
				tmpTicket.setSubject(tmpReplyTicket.getSubject());
				if (tmpReplyTicket.getUserId().equals(activeUserService.fetchActiveUser().getId())) {
					tmpTicket.setStatus(TicketProcessType.OPEN.toString());
					tmpReplyTicket.setStatus(TicketProcessType.OPEN.toString());
					ticketRepository.save(tmpReplyTicket);
				} else {
					tmpReplyTicket.setStatus(TicketProcessType.ANSWERED.toString());
					ticketRepository.save(tmpReplyTicket);
				}
			}
			ticketRepository.save(tmpTicket);
			if ((null != tmpTicket.getId()) && ((null == tmpReplyTicket) || (null == tmpReplyTicket.getId()))) {
				setTmpReplyTicket(tmpTicket);
			}

			CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "ticketSended", "whiteSpaceChar");
		} catch (Exception e) {
			// TODO: handle exception
			CommonFunctions.addMessage(FacesMessage.SEVERITY_WARN, "ticketCannotBeSended", "whiteSpaceChar");
			loggerService.writeError(ACTIVE_CLASS_NAME, "reset", e.toString(), CommonConstants.ERROR);
		}
		refreshData();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "saveTicket", null, CommonConstants.END);
		return null;
	}

	public String reply() {
		// setTmpReplyTicket(tmpTicket);
		Ticket ticket = new Ticket();
		ticket.setUserId(activeUserService.fetchActiveUser().getId());
		setTmpTicket(ticket);
		refreshData();
		return NavigationUtils.buildRedirectionString("/viewticket");
	}

	public String refresh() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "refresh", null, CommonConstants.START);
		try {
			tmpTicket = (Ticket) ticketRepository.findById(tmpTicket.getId()).get();
			// selectedTmpLanguage = tmpUser.getLanguage().getCode();
		} catch (Exception e) {
			// TODO: handle exception
			loggerService.writeError(ACTIVE_CLASS_NAME, "reset", e.toString(), CommonConstants.ERROR);
		}
		refreshData();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "refresh", null, CommonConstants.END);
		return null;
	}

	public String reset() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "reset", null, CommonConstants.START);
		try {
			setTmpTicket(new Ticket());
		} catch (Exception e) {
			// TODO: handle exception
			loggerService.writeError(ACTIVE_CLASS_NAME, "reset", e.toString(), CommonConstants.ERROR);
		}
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "reset", null, CommonConstants.END);
		return null;
	}

	public void cancel() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "cancel", null, CommonConstants.START);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "cancel", null, CommonConstants.END);
	}

	public String startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);

		if (null == this.tmpTicket || null == this.tmpTicket.getId()) {
			tmpTicket = new Ticket();

			tmpTicket.setUserId(activeUserService.fetchActiveUser().getId());
			tmpTicket.setPriority(TicketProcessType.LOW.toString());

		}
		refreshData();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
		return null;
	}

	public Ticket getTmpTicket() {
		return tmpTicket;
	}

	public void setTmpTicket(Ticket tmpTicket) {
		this.tmpTicket = tmpTicket;
	}

	public List<String> getTicketPriorityList() {
		return ticketPriorityList;
	}

	public void setTicketPriorityList(List<String> ticketPriorityList) {
		this.ticketPriorityList = ticketPriorityList;
	}

	public Ticket getTmpReplyTicket() {
		return tmpReplyTicket;
	}

	public void setTmpReplyTicket(Ticket tmpReplyTicket) {
		this.tmpReplyTicket = tmpReplyTicket;
	}

	public List<Ticket> getReplyTickets() {
		return replyTickets;
	}

	public void setReplyTickets(List<Ticket> replyTickets) {
		this.replyTickets = replyTickets;
	}

}
