package com.campustagram.core.util;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;

import org.springframework.web.context.annotation.ApplicationScope;

import com.campustagram.core.security.enums.PermissionType;
import com.campustagram.core.security.enums.RoleType;

@ManagedBean(name = "rolePermissionEnumsUtils")
@ApplicationScope
public final class RolePermissionEnumsUtils {	
	
	private static final Set<PermissionType> userManagementPerms = new LinkedHashSet<>();
	
	private static final Set<PermissionType> roleManagementPerms = new LinkedHashSet<>();
	
	private static final Set<PermissionType> notificationManagementPerms = new LinkedHashSet<>();
	
	private static final Set<PermissionType> logManagementPerms = new LinkedHashSet<>();
	
	private static final Set<PermissionType> supportManagementPerms = new LinkedHashSet<>();
	
	private static final Set<PermissionType> systemManagementPerms = new LinkedHashSet<>();
	
	private static final Set<PermissionType> systemLogManagementPerms = new LinkedHashSet<>();
	
	private static final Set<PermissionType> systemSettingsManagementPerms = new LinkedHashSet<>();
	
	private static final Set<PermissionType> systemEmailManagementPerms = new LinkedHashSet<>();
	
	private static final Set<PermissionType> systemProxyListManagementPerms = new LinkedHashSet<>();
	
	private static final Set<PermissionType> systemProxyGroupManagementPerms = new LinkedHashSet<>();
	
	private static final Set<PermissionType> systemProxyCheckManagementPerms  = new LinkedHashSet<>();
	
	private static final Set<PermissionType> systemUserAgentManagementPerms = new LinkedHashSet<>();
	
	private static final Set<PermissionType> systemDBManagementPerms = new LinkedHashSet<>();
	
	private static final Set<PermissionType> systemPerformanceManagementPerms = new LinkedHashSet<>();
	
	private static final Set<PermissionType> systemPerformanceCpuManagementPerms = new LinkedHashSet<>();
	
	private static final Set<PermissionType> systemPerformanceMemoryManagementPerms  = new LinkedHashSet<>();
	
	private static final Set<PermissionType> systemPerformanceDiscManagementPerms = new LinkedHashSet<>();
	
	private static final Set<PermissionType> systemDocumentationPerms = new LinkedHashSet<>();
	
	static {
		userManagementPerms.add(PermissionType.PERMISSION_VIEW);
		userManagementPerms.add(PermissionType.PERMISSION_EDIT);
		userManagementPerms.add(PermissionType.PERMISSION_ADD);
		userManagementPerms.add(PermissionType.PERMISSION_REMOVE);
		userManagementPerms.add(PermissionType.PERMISSION_BLOCK);
		userManagementPerms.add(PermissionType.PERMISSION_UNBLOCK);
		
		roleManagementPerms.add(PermissionType.PERMISSION_VIEW);
		roleManagementPerms.add(PermissionType.PERMISSION_EDIT);
		roleManagementPerms.add(PermissionType.PERMISSION_ADD);
		roleManagementPerms.add(PermissionType.PERMISSION_REMOVE);
		
		notificationManagementPerms.add(PermissionType.PERMISSION_SYSTEM_NOTIFICATION);

		logManagementPerms.add(PermissionType.PERMISSION_VIEW);
		
		supportManagementPerms.add(PermissionType.PERMISSION_VIEW);
		supportManagementPerms.add(PermissionType.PERMISSION_TICKET_OPEN);
		supportManagementPerms.add(PermissionType.PERMISSION_TICKET_ANSWER);

		systemManagementPerms.add(PermissionType.PERMISSION_VIEW);
		systemManagementPerms.add(PermissionType.PERMISSION_EDIT);
		systemManagementPerms.add(PermissionType.PERMISSION_ADD);
		systemManagementPerms.add(PermissionType.PERMISSION_REMOVE);
		systemManagementPerms.add(PermissionType.PERMISSION_MAINTENANCE);
		
		systemLogManagementPerms.add(PermissionType.PERMISSION_VIEW);
		systemLogManagementPerms.add(PermissionType.PERMISSION_VIEW_PROCESS_LOGS);
		systemLogManagementPerms.add(PermissionType.PERMISSION_VIEW_PAGE_VISIT_LOGS);
		
		systemSettingsManagementPerms.add(PermissionType.PERMISSION_VIEW);
		systemSettingsManagementPerms.add(PermissionType.PERMISSION_EDIT);

		systemEmailManagementPerms.add(PermissionType.PERMISSION_VIEW);
		systemEmailManagementPerms.add(PermissionType.PERMISSION_EDIT);
		
		systemProxyListManagementPerms.add(PermissionType.PERMISSION_VIEW);
		systemProxyListManagementPerms.add(PermissionType.PERMISSION_EDIT);
		systemProxyListManagementPerms.add(PermissionType.PERMISSION_ADD);
		systemProxyListManagementPerms.add(PermissionType.PERMISSION_REMOVE);
		
		systemProxyGroupManagementPerms.add(PermissionType.PERMISSION_VIEW);
		systemProxyGroupManagementPerms.add(PermissionType.PERMISSION_EDIT);
		systemProxyGroupManagementPerms.add(PermissionType.PERMISSION_ADD);
		systemProxyGroupManagementPerms.add(PermissionType.PERMISSION_REMOVE);
		
		systemProxyCheckManagementPerms.add(PermissionType.PERMISSION_VIEW);
		
		systemUserAgentManagementPerms.add(PermissionType.PERMISSION_VIEW);
		systemUserAgentManagementPerms.add(PermissionType.PERMISSION_EDIT);
		systemUserAgentManagementPerms.add(PermissionType.PERMISSION_ADD);
		systemUserAgentManagementPerms.add(PermissionType.PERMISSION_REMOVE);
		
		systemDBManagementPerms.add(PermissionType.PERMISSION_VIEW);
		
		systemPerformanceManagementPerms.add(PermissionType.PERMISSION_VIEW);
		
		systemPerformanceCpuManagementPerms.add(PermissionType.PERMISSION_VIEW);
		systemPerformanceCpuManagementPerms.add(PermissionType.PERMISSION_SYSTEM_INFO_COPY);
		
		systemPerformanceMemoryManagementPerms.add(PermissionType.PERMISSION_VIEW);
		systemPerformanceMemoryManagementPerms.add(PermissionType.PERMISSION_SYSTEM_INFO_COPY);
		
		systemPerformanceDiscManagementPerms.add(PermissionType.PERMISSION_VIEW);
		systemPerformanceDiscManagementPerms.add(PermissionType.PERMISSION_SYSTEM_INFO_COPY);
		
		systemDocumentationPerms.add(PermissionType.PERMISSION_VIEW);
	}
	
	public RolePermissionEnumsUtils() {
		//Left blank on purpose
	}

	public static Set<String> availablePermissionsOfRoleJSF(String roleName) {
		Set<PermissionType> availablePermissions = availablePermissionsOfRole(RoleType.valueOf(roleName));

		return availablePermissions.stream().map(PermissionType :: name).collect(Collectors.toCollection(LinkedHashSet :: new));		
	}

	public static Set<PermissionType> availablePermissionsOfRole(RoleType roleType) {
		switch (roleType) {
			case ROLE_LOG_MANAGEMENT:
				return logManagementPerms;

			case ROLE_NOTIFICATION_MANAGEMENT:
				return notificationManagementPerms;
			
			case ROLE_USER_MANAGEMENT:
				return userManagementPerms;

			case ROLE_ROLE_MANAGEMENT:
				return roleManagementPerms;
			
			case ROLE_SYSTEM_MANAGEMENT:
				return systemManagementPerms;

			case ROLE_SUPPORT_MANAGEMENT:
				return supportManagementPerms;
			
			case ROLE_SYSTEM_DB_MANAGEMENT:
				return systemDBManagementPerms;
			
			case ROLE_SYSTEM_DOCUMENTATION_MANAGEMENT:
				return systemDocumentationPerms;
			
			case ROLE_SYSTEM_EMAIL_MANAGEMENT:
				return systemEmailManagementPerms;
			
			case ROLE_SYSTEM_LOG_MANAGEMENT:
				return systemLogManagementPerms;
			
			case ROLE_SYSTEM_PERFORMANCE_CPU_MANAGEMENT:
				return systemPerformanceCpuManagementPerms;
			
			case ROLE_SYSTEM_PERFORMANCE_DISC_MANAGEMENT:
				return systemPerformanceDiscManagementPerms;
			
			case ROLE_SYSTEM_PERFORMANCE_MEMORY_MANAGEMENT:
				return systemPerformanceMemoryManagementPerms;

			case ROLE_SYSTEM_PERFORMANCE_MANAGEMENT:
				return systemPerformanceManagementPerms;
			
			case ROLE_SYSTEM_PROXY_CHECK_MANAGEMENT:
				return systemProxyCheckManagementPerms;
			
			case ROLE_SYSTEM_PROXY_GROUP_MANAGEMENT:
				return systemProxyGroupManagementPerms;

			case ROLE_SYSTEM_PROXY_LIST_MANAGEMENT:
				return systemProxyListManagementPerms;

			case ROLE_SYSTEM_USER_AGENT_MANAGEMENT:
				return systemUserAgentManagementPerms;

			case ROLE_SYSTEM_SETTINGS_MANAGEMENT:
				return systemSettingsManagementPerms;
			
			default:
				throw new IllegalStateException("No match found for: " + roleType.name());
		}
	}

	// ====================================================================================================================
	// ROLES AND PERMISSION PROPERTİES START:
	// ====================================================================================================================
	
	public String getRoleUserManagement() {
		return RoleType.ROLE_USER_MANAGEMENT.name();
	}

	public String getRoleRoleManagement() {
		return RoleType.ROLE_ROLE_MANAGEMENT.name();
	}

	public String getRoleNotificationManagement() {
		return RoleType.ROLE_NOTIFICATION_MANAGEMENT.name();
	}
	
	public String getRoleLogManagement() {
		return RoleType.ROLE_LOG_MANAGEMENT.name();
	}

	public String getRoleSupportManagement() {
		return RoleType.ROLE_SUPPORT_MANAGEMENT.name();
	}
	
	public String getRoleSystemManagement() {
		return RoleType.ROLE_SYSTEM_MANAGEMENT.name();
	}

	public String getRoleSystemLogManagement() {
		return RoleType.ROLE_SYSTEM_LOG_MANAGEMENT.name();
	}

	public String getRoleSystemSettingsManagement() {
		return RoleType.ROLE_SYSTEM_SETTINGS_MANAGEMENT.name();
	}
	
	public String getRoleSystemEmailManagement() {
		return RoleType.ROLE_SYSTEM_EMAIL_MANAGEMENT.name();
	}
	
	public String getRoleSystemProxyListManagement() {
		return RoleType.ROLE_SYSTEM_PROXY_LIST_MANAGEMENT.name();
	}
	
	public String getRoleSystemProxyGroupManagement() {
		return RoleType.ROLE_SYSTEM_PROXY_GROUP_MANAGEMENT.name();
	}
	
	public String getRoleSystemProxyCheckManagement() {
		return RoleType.ROLE_SYSTEM_PROXY_CHECK_MANAGEMENT.name();
	}
	
	public String getRoleSystemUserAgentManagement() {
		return RoleType.ROLE_SYSTEM_USER_AGENT_MANAGEMENT.name();
	}
	
	public String getRoleSystemDBManagement() {
		return RoleType.ROLE_SYSTEM_DB_MANAGEMENT.name();
	}
	
	public String getRoleSystemPerformanceManagement() {
		return RoleType.ROLE_SYSTEM_PERFORMANCE_MANAGEMENT.name();
	}
	
	public String getRoleSystemPerformanceCPUManagement() {
		return RoleType.ROLE_SYSTEM_PERFORMANCE_CPU_MANAGEMENT.name();
	}
	
	public String getRoleSystemPerformanceMemoryManagement() {
		return RoleType.ROLE_SYSTEM_PERFORMANCE_MEMORY_MANAGEMENT.name();
	}
	
	public String getRoleSystemPerformanceDiscManagement() {
		return RoleType.ROLE_SYSTEM_PERFORMANCE_DISC_MANAGEMENT.name();
	}
	
	public String getRoleSystemDocumentationManagement() {
		return RoleType.ROLE_SYSTEM_DOCUMENTATION_MANAGEMENT.name();
	}
	
	public String getPermissionView() {
		return PermissionType.PERMISSION_VIEW.name();
	}
	
	public String getPermissionEdit() {
		return PermissionType.PERMISSION_EDIT.name();
	}
	
	public String getPermissionAdd() {
		return PermissionType.PERMISSION_ADD.name();
	}

	public String getPermissionRemove() {
		return PermissionType.PERMISSION_REMOVE.name();
	}

	public String getPermissionBlock() {
		return PermissionType.PERMISSION_BLOCK.name();
	}

	public String getPermissionUnblock() {
		return PermissionType.PERMISSION_UNBLOCK.name();
	}

	public String getPermissionMaintenance() {
		return PermissionType.PERMISSION_MAINTENANCE.name();
	}

	public String getPermissionSystemNotification() {
		return PermissionType.PERMISSION_SYSTEM_NOTIFICATION.name();
	}
	
	public String getPermissionSystemInfoCopy() {
		return PermissionType.PERMISSION_SYSTEM_INFO_COPY.name();
	}
	
	public String getPermissionTicketOpen() {
		return PermissionType.PERMISSION_TICKET_OPEN.name();
	}
	
	public String getPermissionTicketAnswer() {
		return PermissionType.PERMISSION_TICKET_ANSWER.name();
	}
	
	public String getPermissionViewProcessLogs() {
		return PermissionType.PERMISSION_VIEW_PROCESS_LOGS.name();
	}
	
	public String getPermissionViewPageVisitLogs() {
		return PermissionType.PERMISSION_VIEW_PAGE_VISIT_LOGS.name();
	}
	// ====================================================================================================================
	// ROLES AND PERMISSION PROPERTİES END:
	// ====================================================================================================================
}
