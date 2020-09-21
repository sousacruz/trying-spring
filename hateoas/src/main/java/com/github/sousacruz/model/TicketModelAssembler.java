package com.github.sousacruz.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.github.sousacruz.resources.TicketController;

@Component
public class TicketModelAssembler implements RepresentationModelAssembler<Ticket, EntityModel<Ticket>>{

	@Override
	public EntityModel<Ticket> toModel(Ticket ticket) {
		
		EntityModel<Ticket> ticketModel =  EntityModel.of(ticket,
			linkTo(methodOn(TicketController.class).one(ticket.getId())).withSelfRel(),
			linkTo(methodOn(TicketController.class).all()).withRel("tickets"));
		
		if (ticket.getStatus() == TicketStatus.OPENED) {
			ticketModel.add(
				linkTo(methodOn(TicketController.class).start(ticket.getId())).withRel("start"),
				linkTo(methodOn(TicketController.class).close(ticket.getId())).withRel("close"),
				linkTo(methodOn(TicketController.class).cancel(ticket.getId())).withRel("cancel"));
		} else if (ticket.getStatus() == TicketStatus.IN_PROGRESS || ticket.getStatus() == TicketStatus.REOPENED) {
			ticketModel.add(
				linkTo(methodOn(TicketController.class).close(ticket.getId())).withRel("close"),
				linkTo(methodOn(TicketController.class).cancel(ticket.getId())).withRel("cancel"));
		} else if (ticket.getStatus() == TicketStatus.CLOSED) {
			ticketModel.add(
				linkTo(methodOn(TicketController.class).reopen(ticket.getId())).withRel("reopen"));
		}
		
		return ticketModel;
	}

}
