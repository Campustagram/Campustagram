package com.campustagram.core.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.campustagram.core.model.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {

	@Query(value = "SELECT * FROM language t WHERE is_deleted=false ORDER BY create_date DESC", nativeQuery = true)
	public List<Language> findAllNotDeleted();

	@Query(value = "SELECT * FROM language t WHERE is_deleted = false AND code= :code ", nativeQuery = true)
	public Language findByNameNotDeleted(@Param("code") String languageName);
}
