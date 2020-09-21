package com.github.sousacruz.resources;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.sousacruz.exceptions.TicketNotFoundException;
import com.github.sousacruz.model.Ticket;
import com.github.sousacruz.model.TicketModelAssembler;
import com.github.sousacruz.model.TicketStatus;
import com.github.sousacruz.repositories.TicketRepository;

@RestController
public class TicketController {

	private final TicketRepository repository;
	private final TicketModelAssembler modelAssembler;

	public TicketController(TicketRepository repository, TicketModelAssembler modelAssembler) {
		this.repository = repository;
		this.modelAssembler = modelAssembler;
	}
	
	@GetMapping("/tickets/{id}")
	public EntityModel<Ticket> one(@PathVariable Long id) {
		Ticket ticket = repository.findById(id)
				.orElseThrow(() -> new TicketNotFoundException(id));
		
		return modelAssembler.toModel(ticket);
	}

	@GetMapping("/tickets")
	public CollectionModel<EntityModel<Ticket>> all() {
		
		List<EntityModel<Ticket>> tickets = repository.findAll().stream()
			.map(modelAssembler::toModel)
			.collect(Collectors.toList());
			return CollectionModel.of(tickets,
				linkTo(methodOn(TicketController.class).all()).withSelfRel());
	}

	@PostMapping("/tickets")
	public ResponseEntity<?> create(@RequestBody Ticket ticket) {
		
		ticket.setStatus(TicketStatus.OPENED);
		EntityModel<Ticket> entityModel = modelAssembler
			.toModel(repository.save(ticket));
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
			.toUri())
			.body(entityModel);
	}

	@PutMapping("tickets/{id}")
	public ResponseEntity<?> update(@RequestBody Ticket ticketToUpdate, @PathVariable Long id) {
		
		Ticket updatedTicket = repository.findById(id)
			.map(ticket -> {
				ticket.setStatus(ticketToUpdate.getStatus());
				ticket.setDescription(ticketToUpdate.getDescription());
				return repository.save(ticket);
			}).orElseGet(() -> {
				ticketToUpdate.setId(id);
				return repository.save(ticketToUpdate);
			});
		
		EntityModel<Ticket> entityModel = modelAssembler
			.toModel(updatedTicket);
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
			.toUri())
			.body(entityModel);
		
	}
	
	@PutMapping("tickets/{id}/start")
	public ResponseEntity<?> start(@PathVariable Long id) {
		
		Ticket updatedTicket = repository.findById(id)
			.map(ticket -> {
				ticket.setStatus(TicketStatus.IN_PROGRESS);
				return repository.save(ticket);
			})
			.orElseThrow(() -> new TicketNotFoundException(id));
		
		EntityModel<Ticket> entityModel = modelAssembler
			.toModel(updatedTicket);
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
			.toUri())
			.body(entityModel);
		
	}
	
	@PutMapping("tickets/{id}/close")
	public ResponseEntity<?> close(@PathVariable Long id) {
		
		Ticket updatedTicket = repository.findById(id)
			.map(ticket -> {
				ticket.setStatus(TicketStatus.CLOSED);
				return repository.save(ticket);
			})
			.orElseThrow(() -> new TicketNotFoundException(id));
		
		EntityModel<Ticket> entityModel = modelAssembler
			.toModel(updatedTicket);
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
			.toUri())
			.body(entityModel);
		
	}
	
	@PutMapping("tickets/{id}/reopen")
	public ResponseEntity<?> reopen(@PathVariable Long id) {
		
		Ticket updatedTicket = repository.findById(id)
			.map(ticket -> {
				ticket.setStatus(TicketStatus.REOPENED);
				return repository.save(ticket);
			})
			.orElseThrow(() -> new TicketNotFoundException(id));
		
		EntityModel<Ticket> entityModel = modelAssembler
			.toModel(updatedTicket);
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
			.toUri())
			.body(entityModel);
		
	}
	
	@PutMapping("tickets/{id}/cancel")
	public ResponseEntity<?> cancel(@PathVariable Long id) {
		
		Ticket ticketToCancel = repository.findById(id)
			.orElseThrow(() -> new TicketNotFoundException(id));

		if (ticketToCancel.getStatus() == TicketStatus.IN_PROGRESS || ticketToCancel.getStatus() == TicketStatus.REOPENED) {
			ticketToCancel.setStatus(TicketStatus.CANCELLED);
			return ResponseEntity.ok(modelAssembler
				.toModel(repository.save(ticketToCancel)));
		}
		
		return ResponseEntity
			.status(HttpStatus.METHOD_NOT_ALLOWED)
			.header(HttpHeaders.CONTENT_TYPE, 
					MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
			.body(Problem.create()
				.withTitle("Method not allowed")
				.withDetail("Ticket in the "+ ticketToCancel.getStatus() +" status can not be cancelled"));
		
	}	

	@DeleteMapping("/tickets/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		if (repository.existsById(id))
			repository.deleteById(id);
		else
			throw new TicketNotFoundException(id);
		
		return ResponseEntity.noContent().build();
	}
}
