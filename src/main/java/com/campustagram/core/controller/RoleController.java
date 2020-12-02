package com.campustagram.core.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
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
import com.campustagram.core.controller.role.EditRoleController;
import com.campustagram.core.model.Role;
import com.campustagram.core.model.User;
import com.campustagram.core.persistence.RoleRepository;
import com.campustagram.core.persistence.SystemPropertiesRepository;
import com.campustagram.core.persistence.UserRepository;
import com.campustagram.core.security.enums.PermissionType;
import com.campustagram.core.security.enums.RoleType;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "roleController")
@Scope(value = "session")
@Component(value = "roleController")
@Join(path = "/rolelist", to = "/pages/core/roleManagement/rolelist.jsf")
public class RoleController {
	@Autowired
	private Server server;
	@Autowired
	private EditRoleController editRoleController;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SystemPropertiesRepository systemPropertiesRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private LoggerService loggerService;

	private String searchKeyword;

	private Role defaultNewUserRole;
	
	private Role selectedDetailsRole;
	
	private Set<Role> roles;
	
	private static final String ACTIVE_CLASS_NAME = "RoleController";

	@Deferred
	@RequestAction
	@IgnorePostback
	public void loadData() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.START);
		setRoles(new HashSet<>(roleRepository.findAllNotDeleted()));
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "loadData", null, CommonConstants.END);
	}

	public void startUpChecks() {
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.START);
		searchKeyword = "";
		setDefaultNewUserRole(server.getSystemProperties().getDefaultNewUserRole());
		setRoles(new HashSet<>(roleRepository.findAllNotDeleted()));
		loggerService.writeInfo(ACTIVE_CLASS_NAME, "startUpChecks", null, CommonConstants.END);
	}
	
	/*
	 * Change default role that will be displayed when a new user is created
	 */
	public void changeDefaultRole(Role newDefaultRole) {
		server.getSystemProperties().setDefaultNewUserRole(newDefaultRole);
		systemPropertiesRepository.save(server.getSystemProperties());
		
		setDefaultNewUserRole(server.getSystemProperties().getDefaultNewUserRole());
		
		CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "successfullyUpdated" , CommonConstants.WHITE_SPACE_CHAR);
	}
	
	/*
	 * Redirect to the role editing page
	 */
	public String editRole(Role roleToEdit) {
		editRoleController.setRoleThatWillBeEdited(roleToEdit);
		return NavigationUtils.buildRedirectionString("/editrole");
	}
	
	public String deleteRole(Role roleToDelete) {
		List<User> allUsers = userRepository.findAllNotDeleted();
		StringBuilder namesOfUsersThatHaveThisRole = new StringBuilder();
		
		for(User user : allUsers) {
			if(roleToDelete.getName().equals(user.getRole().getName())) {
				namesOfUsersThatHaveThisRole.append(user.getName() + "\n");
			}
		}
		
		if(namesOfUsersThatHaveThisRole.length() > 0) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR , "sameRoleError" , CommonConstants.WHITE_SPACE_CHAR);
		} else {
			roleRepository.delete(roleToDelete);
			CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO , "roleIsDeleted" , CommonConstants.WHITE_SPACE_CHAR);
		}
		
		return null;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public Role getDefaultNewUserRole() {
		return defaultNewUserRole;
	}

	public void setDefaultNewUserRole(Role defaultNewUserRole) {
		this.defaultNewUserRole = defaultNewUserRole;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Role getSelectedDetailsRole() {
		return selectedDetailsRole;
	}

	public void setSelectedDetailsRole(Role selectedDetailsRole) {
		this.selectedDetailsRole = selectedDetailsRole;
	}
}
