package com.github.sousacruz.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.sousacruz.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
