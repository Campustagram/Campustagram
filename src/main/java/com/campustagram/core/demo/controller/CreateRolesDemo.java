package com.campustagram.core.demo.controller;

import com.campustagram.core.persistence.RoleRepository;

public class CreateRolesDemo {

	private RoleRepository roleRepository;

	/**
	 * 
	 * @param roleRepository
	 */
	public CreateRolesDemo(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}
/*
	@Bean
	public void createRole(String roleName, Short status) {
		if (null == roleRepository.findByNameNotDeleted(roleName)) {
			Roles role = new Roles();
			role.setName(roleName);
			role.setStatus(status);
			roleRepository.save(role);
			System.out.println("Rol oluşturuldu. (" + roleName + ")");
		} else {
			System.out.println("Rol zaten mevcut oluşturulmadı. (" + roleName + ")");
		}
	}*/
}
