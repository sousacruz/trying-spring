package com.github.sousacruz.resources;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.sousacruz.exceptions.CustomerNotFoundException;
import com.github.sousacruz.model.Customer;
import com.github.sousacruz.model.CustomerModelAssembler;
import com.github.sousacruz.repositories.CustomerRepository;

@RestController
public class CustomerController {

	private final CustomerRepository repository;
	private final CustomerModelAssembler modelAssembler;

	CustomerController(CustomerRepository repository, CustomerModelAssembler modelAssembler) {
		this.repository = repository;
		this.modelAssembler = modelAssembler;
	}

	@GetMapping("/customers/{id}")
	public EntityModel<Customer> one(@PathVariable Long id) {
		Customer customer = repository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException(id));
		
		return modelAssembler.toModel(customer);
	}

	@GetMapping("/customers")
	public CollectionModel<EntityModel<Customer>> all() {
		
		List<EntityModel<Customer>> customers = repository.findAll().stream()
			.map(modelAssembler::toModel)
			.collect(Collectors.toList());
			return CollectionModel.of(customers,
				linkTo(methodOn(CustomerController.class).all()).withSelfRel());
	}

	@PostMapping("/customers")
	public ResponseEntity<?> create(@RequestBody Customer customer) {
		EntityModel<Customer> entityModel = modelAssembler
			.toModel(repository.save(customer));
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
			.toUri())
			.body(entityModel);
	}


	@PutMapping("customers/{id}")
	public ResponseEntity<?> replace(@RequestBody Customer customerToUpdate, @PathVariable Long id) {
		
		Customer updatedCustomer = repository.findById(id)
			.map(customer -> {
				customer.setFirstName(customerToUpdate.getFirstName());
				customer.setLastName(customerToUpdate.getLastName());
				return repository.save(customer);
			}).orElseGet(() -> {
				customerToUpdate.setId(id);
				return repository.save(customerToUpdate);
			});
		
		EntityModel<Customer> entityModel = modelAssembler
			.toModel(updatedCustomer);
		
		return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF)
			.toUri())
			.body(entityModel);
		
	}

	@DeleteMapping("/customers/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		if (repository.existsById(id))
			repository.deleteById(id);
		else
			throw new CustomerNotFoundException(id);
		
		return ResponseEntity.noContent().build();
	}
}
