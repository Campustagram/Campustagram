package com.campustagram.core.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campustagram.core.model.SystemInformation;

public interface SystemInformationRepository extends JpaRepository<SystemInformation, Long> {

}
