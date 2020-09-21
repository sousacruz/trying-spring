package com.github.sousacruz.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Ticket {
	
	@Id
	@GeneratedValue
	private Long id;
	private TicketStatus status;
	private String description;
	
	protected Ticket() {}
	
	public Ticket(TicketStatus status, String description) {
		this.status = status;
		this.description = description;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public TicketStatus getStatus() {
		return status;
	}
	
	public void setStatus(TicketStatus status) {
		this.status = status;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Ticket))
			return false;
		
		Ticket ticket = (Ticket) obj;
		return Objects.equals(this.id, ticket.id) &&
				Objects.equals(this.status, ticket.status)  &&
				Objects.equals(this.description, ticket.description);

	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.status, this.description);
	}
	
	@Override
	public String toString() {
		return String.format("Ticket[id=%d, status='%s', description='%s']",
			this.id, this.status, this.description);
	}	
}
