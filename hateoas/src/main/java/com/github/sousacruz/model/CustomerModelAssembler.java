package com.github.sousacruz.model;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.github.sousacruz.resources.CustomerController;

@Component
public class CustomerModelAssembler implements RepresentationModelAssembler<Customer, EntityModel<Customer>>{

	@Override
	public EntityModel<Customer> toModel(Customer customer) {
		
		return EntityModel.of(customer,
			linkTo(methodOn(CustomerController.class).one(customer.getId())).withSelfRel(),
			linkTo(methodOn(CustomerController.class).all()).withRel("customers"));
		
	}

}
