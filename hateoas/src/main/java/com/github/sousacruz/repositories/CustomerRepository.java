package com.github.sousacruz.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.sousacruz.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	List<Customer> findByLastName(String lastName);
	
	Customer findById(long id);
	
}
