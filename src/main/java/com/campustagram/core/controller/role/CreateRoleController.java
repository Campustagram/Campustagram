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

@ManagedBean(name = "createRoleController")
@Scope(value = "session")
@Component(value = "createRoleController")
@Join(path = "/createrole", to = "/pages/core/roleManagement/createrole.jsf")
public class CreateRoleController {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private LoggerService loggerService;
	@Autowired
	private ActiveUserService activeUserService;
	@Autowired
	private NotificationController notificationController;
	@Autowired
	private EditRoleController editRoleController;

	private static final String ACTIVE_CLASS_NAME = "CreateRoleController";

	// Name of the new role
	private String newRoleName = CommonConstants.EMPTY_STRING;

	/*
	 * Role types and the permission types of new role Every role type has a set of
	 * permission types
	 */
	private Map<String, String[]> selectedPermissionsOfRole = new HashMap<>();

	public void startUpChecks() {
		final String ACTIVE_METHOD_NAME = "startUpChecks";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		/*
		 * Initialize role name to empty string
		 */
		setNewRoleName(CommonConstants.EMPTY_STRING);

		/*
		 * Initialize permissions of role types to empty set
		 */
		for (RoleType roleType : RoleType.values()) {
			selectedPermissionsOfRole.put(roleType.name(), null);
		}

		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
	}

	public String saveRole() {
		final String ACTIVE_METHOD_NAME = "saveRole";
		loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);

		try {

			/**
			 * check if role exist on db
			 */
			Role tmpRole = roleRepository.findByNameNotDeleted(getNewRoleName());
			if ((null != tmpRole) && (null != tmpRole.getId())) {
				CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "roleNameAlreadyExist",
						CommonConstants.WHITE_SPACE_CHAR);
			} else {

				// Create new role
				Role newRole = new Role();

				// This map is the privileges of the newly created role
				EnumMap<RoleType, Set<PermissionType>> selectedPrivilegesMap = new EnumMap<>(RoleType.class);

				if (null != selectedPermissionsOfRole && selectedPermissionsOfRole.size() > 0) {
					/*
					 * Iterate the selected role types and permissions from role creation page And
					 * if permissions is selected add it to privileges map of created role
					 */
					for (String roleTypeName : selectedPermissionsOfRole.keySet()) {
						RoleType roleType = RoleType.valueOf(roleTypeName);
						String[] perms = selectedPermissionsOfRole.get(roleTypeName);

						// If this role type has selected permissions than add them to the role
						if (null != perms && perms.length > 0) {
							Set<PermissionType> selectedPerms = Arrays.asList(perms).stream()
									.map(PermissionType::valueOf).collect(Collectors.toSet());

							selectedPrivilegesMap.put(roleType, selectedPerms);
						}
					}
				}

				/*
				 * Save the new role in database
				 */
				newRole.setName(getNewRoleName());
				newRole.setPrivileges(selectedPrivilegesMap);
				roleRepository.save(newRole);

				notificationController.sendNotification(activeUserService.fetchActiveUser(),
						"roleIsUpdated", CommonConstants.LOG_CREATE, null);

				CommonFunctions.addMessage(FacesMessage.SEVERITY_INFO, "roleSaved", CommonConstants.WHITE_SPACE_CHAR);

				/*
				 * Clean up fields for next page load
				 */
				setNewRoleName(CommonConstants.EMPTY_STRING);

				for (RoleType roleType : RoleType.values()) {
					selectedPermissionsOfRole.put(roleType.name(), null);
				}

				editRoleController.setRoleThatWillBeEdited(newRole);
				loggerService.writeInfo(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
				return NavigationUtils.buildRedirectionString("/editrole");
			}

		} catch (Exception e) {
			// TODO: handle exception
			loggerService.writeError(ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, CommonFunctions.getExceptionAsString(e),
					CommonConstants.ERROR);
		}

		return null;
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
