package com.campustagram.core.controller;

import java.util.HashSet;
import java.util.Set;

import javax.faces.bean.ManagedBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.campustagram.core.model.User;
import com.campustagram.core.security.enums.PermissionType;
import com.campustagram.core.security.enums.RoleType;
import com.campustagram.core.security.service.ActiveUserService;

@ManagedBean(name = "roleRightController")
@Service(value = "roleRightController")
public class RoleRightController { 
	
	@Autowired
	private ActiveUserService activeUserService;
	
	/*
	 * This method must be used in .xhtml pages
	 * In .java files below method should be used
	 * This method checks if active user the necessary role type
	 *
	 */
	public boolean checkIfActiveUserHasRoleTypeJSF(String roleTypeName) {
		return checkIfActiveUserHasRoleType(RoleType.valueOf(roleTypeName));
	}

	/*
	 * This method must be used in .java pages
	 * In .xhtml files above method should be used
	 * This method checks if active user the necessary role type
	 *
	 */
	public boolean checkIfActiveUserHasRoleType(RoleType roleTypeRequired) {
		User loggedInUser = activeUserService.fetchActiveUser();
		
		if(null == loggedInUser) {
			return false;
		}
		
		Set<RoleType> activeUserRoleTypes = loggedInUser.getRole().getPrivileges().keySet();

		for(RoleType roleType : activeUserRoleTypes) {
			if(roleType == roleTypeRequired) {
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * This method must be used in .xhtml pages
	 * In .java files below method should be used
	 * This method checks if active user the necessary role type and necessary permissions of that role type
	 *
	 */
	public boolean checkIfActiveUserHasPrivilegeJSF(String requiredRoleTypeName , String requiredPermissionTypesString) {
		Set<PermissionType> permissionTypesFromString = parsePageStringToPermissionSet(requiredPermissionTypesString);
		
		return checkIfActiveUserHasPrivilege(RoleType.valueOf(requiredRoleTypeName) , permissionTypesFromString);
	}
	
	/*
	 * This method must be used in .java pages
	 * In .xhtml files above method should be used
	 * This method checks if active user the necessary role type and necessary permissions of that role type
	 *
	 */
	public boolean checkIfActiveUserHasPrivilege(RoleType roleTypeRequired , Set<PermissionType> permissionTypesRequired) {
		User loggedInUser = activeUserService.fetchActiveUser();

		if(null == loggedInUser) {
			return false;
		}

		Set<RoleType> activeUserRoleTypes = loggedInUser.getRole().getPrivileges().keySet();
		
		for(RoleType roleType : activeUserRoleTypes) {
			if(roleType == roleTypeRequired && checkIfRoleTypeHasAllPermissions(loggedInUser.getRole().getPrivileges().get(roleType) , permissionTypesRequired)) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean checkIfActiveUserHasAnyPrivilegeJSF(String requiredRoleTypeName , String requiredPermissionTypesString) {
		Set<PermissionType> permissionTypesFromString = parsePageStringToPermissionSet(requiredPermissionTypesString);
		
		return checkIfActiveUserHasAnyPrivilege(RoleType.valueOf(requiredRoleTypeName) , permissionTypesFromString);
	}
	
	public boolean checkIfActiveUserHasAnyPrivilege(RoleType roleTypeRequired , Set<PermissionType> permissionTypesRequired) {
		User loggedInUser = activeUserService.fetchActiveUser();

		if(null == loggedInUser) {
			return false;
		}

		Set<RoleType> activeUserRoleTypes = loggedInUser.getRole().getPrivileges().keySet();
		
		for(RoleType roleType : activeUserRoleTypes) {
			if(roleType == roleTypeRequired && checkIfRoleTypeHasAnyPermission(loggedInUser.getRole().getPrivileges().get(roleType) , permissionTypesRequired)) {
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * This method must be used in .xhtml pages
	 * In .java files below method should be used
	 * This method checks if parameter user the necessary role type
	 *
	 */
	public boolean checkIfUserHasRoleTypeJSF(User user , String roleTypeName) {
		return checkIfUserHasRoleType(user , RoleType.valueOf(roleTypeName));
	}
	
	/*
	 * This method must be used in .java pages
	 * In .xhtml files above method should be used
	 * This method checks if parameter user the necessary role type
	 */
	public boolean checkIfUserHasRoleType(User user , RoleType requiredRoleType) {
		for(RoleType roleType : user.getRole().getPrivileges().keySet()) {
			if(roleType == requiredRoleType) {
				return true;
			}
		}
		
		return false;
	}

	/*
	 * This method must be used in .xhtml pages
	 * In .java files below method should be used
	 * This method checks if parameter user the necessary role type and necessary permissions of that role type
	 *
	 */
	public boolean checkIfUserHasPrivilegeJSF(User user , String roleTypeName , String permissionTypesString) {
		Set<PermissionType> permissionsFromJSFString = parsePageStringToPermissionSet(permissionTypesString);
		
		return checkIfUserHasPrivilege(user , RoleType.valueOf(roleTypeName) , permissionsFromJSFString);
	}

	/*
	 * This method must be used in .java pages
	 * In .xhtml files above method should be used
	 * This method checks if parameter user the necessary role type and necessary permissions of that role type
	 *
	 */
	public boolean checkIfUserHasPrivilege(User user , RoleType requiredRoleType , Set<PermissionType> requiredPermissionTypes) {
		
		for(RoleType roleType : user.getRole().getPrivileges().keySet()) {
			if(roleType == requiredRoleType && checkIfRoleTypeHasAllPermissions(user.getRole().getPrivileges().get(roleType) , requiredPermissionTypes)) {
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * This method is a utility method 
	 */
	private boolean checkIfRoleTypeHasAllPermissions(Set<PermissionType> permissionsOfRole , Set<PermissionType> permissionTypes) {
		return permissionsOfRole.containsAll(permissionTypes);
	}

	private boolean checkIfRoleTypeHasAnyPermission(Set<PermissionType> permissionsOfRole , Set<PermissionType> permissionTypes) {
		for(PermissionType permissionType : permissionTypes) {
			if(permissionsOfRole.contains(permissionType)) {
				return true;
			}
		}
		
		return false;
	}
	
	/*
	 * This method is a utility method 
	 */
	private Set<PermissionType> parsePageStringToPermissionSet(String pageString) {
		Set<PermissionType> permissionTypes = new HashSet<>();
		
		String[] parsedPermissions = pageString.split("[, ]+");
		
		for(String permString : parsedPermissions) {
			permissionTypes.add(PermissionType.valueOf(permString));
		}
		
		return permissionTypes;
	}
}
