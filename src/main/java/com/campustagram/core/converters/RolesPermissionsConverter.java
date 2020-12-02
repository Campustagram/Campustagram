package com.campustagram.core.converters;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.campustagram.core.common.CommonConstants;
import com.campustagram.core.security.enums.PermissionType;
import com.campustagram.core.security.enums.RoleType;

@Converter
public class RolesPermissionsConverter implements AttributeConverter<EnumMap<RoleType , Set<PermissionType>> , String> {
	
	private static final Logger logger = LoggerFactory.getLogger(RolesPermissionsConverter.class);
	
	private static final String LOG_REGEX = "User: {} => Class({}) Method({}) => info({}) => status({}) ";
	
	private static final String ACTIVE_CLASS_NAME = "RolesPermissionsConverter";
	
	private static final String NO_ROLES_STRING = "NO_ROLE";
	
	@Override
	public String convertToDatabaseColumn(EnumMap<RoleType, Set<PermissionType>> mapAttribute) {
		final String ACTIVE_METHOD_NAME = "convertToDatabaseColumn";
		
		logger.info(LOG_REGEX, "UNKNOWN", ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		StringBuilder sb = new StringBuilder();
		
		if(null == mapAttribute || mapAttribute.size() < 1) {
			return NO_ROLES_STRING;
		}
		
		for (RoleType roleType : mapAttribute.keySet()) {
			sb.append(roleType.name() + ':');
			
			for(PermissionType permissionType : mapAttribute.get(roleType)) {
				sb.append(permissionType.name() + ',');
			}
			
			if(sb.length() > 0) {
				sb.deleteCharAt(sb.length() - 1);
			}
			sb.append('/');
		}
		
		if(sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
	
		logger.info(LOG_REGEX, "UNKNOWN", ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return sb.toString();
	}

	@Override
	public EnumMap<RoleType, Set<PermissionType>> convertToEntityAttribute(String dbString) {
		final String ACTIVE_METHOD_NAME = "convertToEntityAttribute";
		logger.info(LOG_REGEX, "UNKNOWN", ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.START);
		EnumMap<RoleType, Set<PermissionType>> result = new EnumMap<>(RoleType.class);
		
		if(NO_ROLES_STRING.equals(dbString)) {
			return result;
		}
		
		String[] rolesArray = dbString.split("/");
		
		for(String roleString : rolesArray) {
			String[] roleAndPermissionsPart = roleString.split(":");
			
			if(roleAndPermissionsPart.length != 2) {
				result.put(RoleType.valueOf(roleAndPermissionsPart[0]) , new HashSet<>());
				continue;
			}
			
			String[] permissionsPart = roleAndPermissionsPart[1].split(",");
			
			Set<PermissionType> permissionTypes = new HashSet<>();
			for(String permissionString : permissionsPart) {
				permissionTypes.add(PermissionType.valueOf(permissionString));
			}
			
			result.put(RoleType.valueOf(roleAndPermissionsPart[0]) , permissionTypes);
		}
		
		logger.info(LOG_REGEX, "UNKNOWN", ACTIVE_CLASS_NAME, ACTIVE_METHOD_NAME, null, CommonConstants.END);
		return result;
	}
}
