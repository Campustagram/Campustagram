package com.campustagram.core.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.campustagram.core.model.WebProxyGroup;

/**
 * created on 2018/09/28
 * 
 * @author Ahmet ŞEN
 *
 */
public interface WebProxyGroupRepository extends JpaRepository<WebProxyGroup, Long> {

	@Query(value = "SELECT * FROM web_app_proxy_group t WHERE is_deleted = false ORDER BY id", nativeQuery = true)
	public List<WebProxyGroup> getAllWebProxyGroup();

	@Query(value = "SELECT * FROM public.web_app_proxy_group WHERE id=:id", nativeQuery = true)
	public WebProxyGroup getWebProxyGroupWithId(@Param("id") Long id);

	@Query(value = "SELECT * FROM web_app_proxy_group t WHERE name = :name ORDER BY id", nativeQuery = true)
	public List<WebProxyGroup> getWebProxyGroupByName(@Param("name") String name);

}
