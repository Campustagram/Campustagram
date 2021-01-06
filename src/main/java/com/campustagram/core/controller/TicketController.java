package com.campustagram.core.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.enums.TicketProcessType;
import com.campustagram.core.model.Ticket;
import com.campustagram.core.persistence.TicketRepository;
import com.campustagram.core.security.enums.RoleType;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "ticketController")
@Scope(value = "session")
@Component(value = "ticketController")
@Join(path = "/ticket", to = "/pages/core/support/ticket.jsf")
public class TicketController {
	@Autowired
	private ActiveUserService activeUserService;
	@Autowired
	private TicketRepository ticketRepository;
	@Autowired
	private ViewTicketController viewTicketController;
	@Autowired
	private RoleRightController roleRightController;
	@Autowired
	private LoggerService loggerService;
	
	private List<Ticket> ticketList = new ArrayList<>();
	private List<Ticket> filteredTickets = new ArrayList<>();

	private String searchKeyword;
	private Ticket tmpTicket;

	private static final String ACTIVE_CLASS_NAME = "TicketController";

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.START);
		
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.END);
	}

	public void refreshData() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "refreshData", null, CommonConstants.START);
		if (null != activeUserService.fetchActiveUser()) {
			if (roleRightController.checkIfActiveUserHasRoleType(RoleType.ROLE_SYSTEM_MANAGEMENT)) {
				ticketList = ticketRepository.findAllNotDeleted();
			} else {
				ticketList = ticketRepository.findAllNotDeletedByUserId(activeUserService.fetchActiveUser().getId());
			}
		}

		loggerService.writeInfo(ACTIVE_CLASS_NAME, "refreshData", null, CommonConstants.END);
	}
	
	/**
	 * sayfa ilk açıldığında çalışır
	 * ilklendirmeleri yapar
	 */
	public void startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);
		searchKeyword = "";
		if (!FacesContext.getCurrentInstance().isPostback()) {
			filteredTickets.clear();
			filteredTickets.addAll(ticketList);
		}
		refreshData();
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
	}

	/**
	 * open new ticket butonuna basılınca tetiklenir.
	 * ticket objesi oluşturur. 
	 * gerekli field leri doldurur.
	 * view ticket sayfasındaki variable a atamasını yapar.
	 * view ticket sayfasına yönlendirme yapar.
	 * 
	 * @return
	 */
	public String openNewTicket() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "openNewTicket", null, CommonConstants.START);
		Ticket ticket = new Ticket();
		ticket.setUserId(activeUserService.fetchActiveUser().getId());
		viewTicketController.setTmpTicket(ticket);
		viewTicketController.setTmpReplyTicket(null);
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "openNewTicket", null, CommonConstants.END);
		return NavigationUtils.buildRedirectionString("/viewticket");
	}

	public String back() {
		viewTicketController.setTmpTicket(null);
		viewTicketController.setTmpReplyTicket(null);
		return NavigationUtils.buildRedirectionString("/ticket");
	}

	public String viewTicket(Ticket ticket) {
		viewTicketController.setTmpTicket(ticket);
		viewTicketController.setTmpReplyTicket(ticket);
		return NavigationUtils.buildRedirectionString("/viewticket");
	}

	public List<Ticket> getTicketList() {
		return ticketList;
	}

	public void setTicketList(List<Ticket> ticketList) {
		this.ticketList = ticketList;
	}

	public List<Ticket> getFilteredTickets() {
		return filteredTickets;
	}

	public void setFilteredTickets(List<Ticket> filteredTickets) {
		this.filteredTickets = filteredTickets;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public Ticket getTmpTicket() {
		return tmpTicket;
	}

	public void setTmpTicket(Ticket tmpTicket) {
		this.tmpTicket = tmpTicket;
	}

}
