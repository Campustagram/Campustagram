package com.campustagram.core.persistence;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.campustagram.core.model.WebAppProxy;

/**
 * created on 2018/09/28
 * 
 * @author Ahmet ŞEN
 *
 */
public interface WebProxyRepository extends JpaRepository<WebAppProxy, Long> {
	@Query(value = "SELECT * FROM web_app_proxy t WHERE host = :host AND port = :port ORDER BY id", nativeQuery = true)
	public List<WebAppProxy> getWebProxyByHostAndPort(@Param("host") String host, @Param("port") Integer port);

	@Query(value = "SELECT * FROM web_app_proxy t WHERE is_deleted = false ORDER BY id", nativeQuery = true)
	public List<WebAppProxy> getAllWebProxy();

	@Query(value = "SELECT wp.* FROM public.web_app_proxy_group AS wpg LEFT JOIN public.web_app_proxy AS wp ON wpg.id=wp.web_app_proxy_group_id WHERE wpg.is_active=true AND wp.is_deleted = false ORDER BY wp.active_worker_on_proxy , wp.usage_count_in_last10min  LIMIT 1", nativeQuery = true)
	public WebAppProxy getLeastUsedProxy();

	// TODO RANDOM() ifadesi kaldırılmalı. sistemi yavaşlatıyor
	@Query(value = "SELECT wp.* FROM public.web_app_proxy_group AS wpg LEFT JOIN public.web_app_proxy AS wp ON wpg.id=wp.web_app_proxy_group_id WHERE wpg.is_active=true AND wp.is_deleted = false ORDER BY RANDOM()  LIMIT 1", nativeQuery = true)
	public WebAppProxy getRandomProxy();

	@Query(value = "SELECT * FROM public.web_app_proxy WHERE id=:id", nativeQuery = true)
	public WebAppProxy getWebProxyWithId(@Param("id") Long id);

	@Modifying
	@Query("update WebAppProxy u set u.activeWorkerOnProxy = activeWorkerOnProxy + 1 WHERE u.id=:id")
	@Transactional
	int increaseProxyUsage(@Param("id") Long id);

	@Modifying
	@Query("update WebAppProxy u set u.activeWorkerOnProxy = activeWorkerOnProxy - 1 WHERE u.id=:id")
	@Transactional
	int decreaseProxyUsage(@Param("id") Long id);

	@Modifying
	@Query("update WebAppProxy u set u.usageCountInLast10Min = 0,u.errorCountInLast10Min = 0,u.lastResetDate=:lastResetDate")
	@Transactional
	int refreshProxyUsage(@Param("lastResetDate") Date lastResetDate);

	@Modifying
	@Query("update WebAppProxy u set u.lastErrorDate=:lastErrorDate, u.errorCount=errorCount + 1, u.errorCountInLast10Min=errorCountInLast10Min + 1 WHERE id=:id")
	@Transactional
	int updateProxyErrorCount(@Param("id") Long id, @Param("lastErrorDate") Date lastErrorDate);
}
