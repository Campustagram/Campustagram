package com.campustagram.core.model;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.campustagram.core.converters.RolesPermissionsConverter;
import com.campustagram.core.security.enums.PermissionType;
import com.campustagram.core.security.enums.RoleType;

@Entity
@Table(name = "role")
public class Role implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8305698095548680606L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(unique = true, name = "name", nullable = false, length = 64)
	private String name;

	private boolean isDeleted = false;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
	private Set<User> users = new HashSet<>();

	/*
	 * This property holds the role types and permission types of the role types
	 * Every role type in this map has a set of permission types
	 * Set of permission types may be null
	 */
	@Convert(converter = RolesPermissionsConverter.class)
	@Column(length = 2048)
	private EnumMap<RoleType, Set<PermissionType>> privileges = new EnumMap<>(RoleType.class);

	public Role() {
		// Necessary for JPA
	}

	/*
	 * Return role types of this role
	 */
	public Set<RoleType> retrieveRoleTypes() {
		return privileges.keySet();
	}

	/*
	 * Return role types of this role as string
	 */
	public Set<String> retrieveRoleTypesAsString() {
		return privileges.keySet().stream().map(RoleType::name).collect(Collectors.toSet());
	}
	
	public String allPrivilegesOfRole() {
		StringBuilder allPrivileges = new StringBuilder();
		
		for(Map.Entry<RoleType, Set<PermissionType>> entry : getPrivileges().entrySet()) {
			 allPrivileges.append(entry.getKey().name() + "\n");
			 
			 for(PermissionType permissionType : entry.getValue()) {
				 allPrivileges.append(entry.getKey().name() + "-" + permissionType.name() + "\n");
			 }
		}
		
		return allPrivileges.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + (isDeleted ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isDeleted != other.isDeleted)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", isDeleted=" + isDeleted + ", privileges=" + privileges + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public EnumMap<RoleType, Set<PermissionType>> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(EnumMap<RoleType, Set<PermissionType>> privileges) {
		this.privileges = privileges;
	}
}
