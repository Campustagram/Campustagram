package com.campustagram.core.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.campustagram.core.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

	@Query(value = "SELECT * FROM ticket t WHERE reply_to IS NULL  ORDER BY update_date DESC", nativeQuery = true)
	public List<Ticket> findAllNotDeleted();
	
	@Query(value = "SELECT * FROM ticket t WHERE reply_to IS NULL AND user_id = :user_id ORDER BY update_date DESC", nativeQuery = true)
	public List<Ticket> findAllNotDeletedByUserId(@Param("user_id") Long user_id);

	@Query(value = "SELECT * FROM public.ticket WHERE reply_to = :id ORDER BY update_date DESC", nativeQuery = true)
	public List<Ticket> findAllReplyTicketByParentId(@Param("id") Long id);

}
