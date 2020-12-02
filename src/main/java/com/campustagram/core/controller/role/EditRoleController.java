package com.campustagram.core.controller.role;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import org.ocpsoft.rewrite.annotation.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.campustagram.core.app.Server;
import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.common.CommonFunctions;
import com.campustagram.core.controller.NotificationController;
import com.campustagram.core.model.Role;
import com.campustagram.core.persistence.RoleRepository;
import com.campustagram.core.security.enums.PermissionType;
import com.campustagram.core.security.enums.RoleType;
import com.campustagram.core.security.service.ActiveUserService;
import com.campustagram.core.service.LoggerService;
import com.campustagram.core.util.NavigationUtils;

@ManagedBean(name = "editRoleController")
@Scope(value = "session")
@Component(value = "editRoleController")
@Join(path = "/editrole", to = "/pages/core/roleManagement/editrole.jsf")
public class EditRoleController {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;
	@Autowired
	private Server server;
	@Autowired
	private NotificationController notificationController;
	
	private static final String ACTIVE_CLASS_NAME = "EditRoleController";
	
	/*
	 * Role that will be edited
	 */
	private Role roleThatWillBeEdited = null;

	/*
	 * Name of the role that we are editing
	 */
	private String newRoleName;

	/*
	 * Selected roles and their permission in the editing page
	 */
	private Map<String, String[]> selectedPermissionsOfRole = new HashMap<>();
	
	public String startUpChecks() {
		final String ACTIVE_METHOD_NAME = "startUpChecks";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		/*
		 * If no role is selected redirect to role list page
		 */
		if(null == getRoleThatWillBeEdited()) {
			CommonFunctions.addMessage(FacesMessage.SEVERITY_ERROR , "roleNotSelected" , CommonConstants.WHITE_SPACE_CHAR);
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
			return NavigationUtils.buildRedirectionString("/rolelist");
		} else {
			
			/*
			 * Populate the role editing page with properties of role that will be edited
			 */
			setNewRoleName(getRoleThatWillBeEdited().getName());
			
			EnumMap<RoleType , Set<PermissionType>> privilegesOfRole = getRoleThatWillBeEdited().getPrivileges();
			
			for(RoleType roleType : RoleType.values()) {
				
				if(null != privilegesOfRole.get(roleType)) {
					selectedPermissionsOfRole.put(roleType.name() , privilegesOfRole.get(roleType).stream().map(PermissionType :: name).toArray(String[] :: new));
				} else {
					selectedPermissionsOfRole.put(roleType.name() , null);
				}
			}
			
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
			return null;
		}
	}
	
	public String saveRole() {
		final String ACTIVE_METHOD_NAME = "saveRole";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		/*
		 * Privileges of the role that is being edited
		 */
		EnumMap<RoleType , Set<PermissionType>> selectedPrivilegesMap = new EnumMap<>(RoleType.class);
		
		/*
		 * If no change is made to the role
		 */
		if(null != selectedPermissionsOfRole && selectedPermissionsOfRole.size() > 0) {
			/*
			 * Iterate selected role types and permission types from role editing page
			 * And update the role object
			 */
			for(String roleTypeName : selectedPermissionsOfRole.keySet()) {
				RoleType roleType = RoleType.valueOf(roleTypeName);
				String[] perms = selectedPermissionsOfRole.get(roleTypeName);
				
				if(null != perms && perms.length > 0) {
					Set<PermissionType> selectedPerms = Arrays.asList(perms).stream().map(PermissionType :: valueOf).collect(Collectors.toSet());
					
					selectedPrivilegesMap.put(roleType , selectedPerms);
				}
			}
		}
		
		/*
		 * Save updated role to database
		 */
		getRoleThatWillBeEdited().setName(getNewRoleName());
		getRoleThatWillBeEdited().setPrivileges(selectedPrivilegesMap);
		roleRepository.save(getRoleThatWillBeEdited());
		
		notificationController.sendNotification(activeUserService.fetchActiveUser(), 	"roleIsUpdated", CommonConstants.LOG_UPDATE, null);
		
		CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO , "roleIsUpdated" , CommonConstants.WHITE_SPACE_CHAR);
		
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return null;
	}

	/*
	 * Is request is coming from edit button on the rolelist page
	 */
	public boolean isUrlAllowedForEditRoleVisibility() {
		final String ACTIVE_METHOD_NAME = "isUrlAllowedForEditRoleVisibility";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		try {
			return (null != getRoleThatWillBeEdited() && CommonFunctions.getClientUrl(server.getHttpServletRequest()).contains("editrole"));
		} catch (Exception e) {
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, CommonFunctions.getExceptionAsString(e), CommonConstants.ERROR);
		} finally {
			loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);			
		}
		
		return false;
	}

	public Role getRoleThatWillBeEdited() {
		return roleThatWillBeEdited;
	}

	public void setRoleThatWillBeEdited(Role roleThatWillBeEdited) {
		this.roleThatWillBeEdited = roleThatWillBeEdited;
	}
	
	public String getNewRoleName() {
		return newRoleName;
	}

	public void setNewRoleName(String newRoleName) {
		this.newRoleName = newRoleName;
	}

	public Map<String, String[]> getSelectedPermissionsOfRole() {
		return selectedPermissionsOfRole;
	}

	public void setSelectedPermissionsOfRole(Map<String, String[]> selectedPermissionsOfRole) {
		this.selectedPermissionsOfRole = selectedPermissionsOfRole;
	}
}