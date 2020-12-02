package com.campustagram.core.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.campustagram.core.model.SystemInformation;

public interface ObjectRepository extends JpaRepository<SystemInformation, Long> {
	/*
	 * sample => "public" "wstb_product" "1069 MB" "1121001472" "1069 MB" "1567 MB"
	 * "498 MB"
	 */
	@Query(value = "SELECT \n" + "schema_name, \n" + "relname, 	\n" + "pg_size_pretty(table_size) AS size, \n"
			+ "table_size,\n"
			+ "pg_size_pretty (pg_relation_size(concat(schema_name,'.',relname))) AS pg_relation_size,\n"
			+ "pg_size_pretty (pg_total_relation_size (concat(schema_name,'.',relname))) AS pg_total_relation_size,\n"
			+ "pg_size_pretty (pg_indexes_size(concat(schema_name,'.',relname))) AS pg_indexes_size\n"
			+ "FROM (SELECT pg_catalog.pg_namespace.nspname AS schema_name, relname, pg_relation_size(pg_catalog.pg_class.oid) AS table_size\n"
			+ "	  FROM pg_catalog.pg_class JOIN pg_catalog.pg_namespace ON relnamespace = pg_catalog.pg_namespace.oid ) t\n"
			+ "	  WHERE schema_name NOT LIKE 'pg_%' AND schema_name <> 'information_schema'\n"
			+ "	  ORDER BY table_size DESC;", nativeQuery = true)
	public List<Object[]> getDbTableNameAndSizes();

	/*
	 * sample => "wstb_product" "857824" "298"
	 */
	@Query(value = "SELECT \n" + "	relname AS ObjectName\n" + "	,pg_stat_get_live_tuples(c.oid) AS LiveTuples\n"
			+ "	,pg_stat_get_dead_tuples(c.oid) AS DeadTuples\n" + "FROM pg_class c\n" + "	WHERE relname IN(\n"
			+ "		SELECT table_name\n" + "		FROM information_schema.tables\n"
			+ "		WHERE table_type='BASE TABLE'\n" + "	) OR relname LIKE '%wstb%'\n"
			+ "ORDER BY DeadTuples DESC;", nativeQuery = true)
	public List<Object[]> getDbDeadTuples();

	/*
	 * sample => "webscraper" "4374 MB"
	 */
	@Query(value = "SELECT :dbName AS table_name, pg_size_pretty( pg_database_size(:dbName) ), pg_database_size(:dbName)", nativeQuery = true)
	public List<Object[]> getDbNameAndSizes(@Param("dbName") String dbName);

	/*
	 * sample => 23976 "webscraper3" 14932 10 "postgres" "PostgreSQL JDBC Driver"
	 * "127.0.0.1" 62268 "2019-06-18 23:00:30.03677+03"
	 * "2019-06-18 23:00:30.044442+03" "2019-06-18 23:00:30.044463+03" "Client"
	 * "ClientRead" "idle" "SET application_name = 'PostgreSQL JDBC Driver'"
	 * "client backend"
	 */
	@Query(value = "SELECT * FROM pg_stat_activity", nativeQuery = true)
	public List<Object[]> getDbConnections();

}
