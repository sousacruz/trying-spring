package com.github.sousacruz.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Customer {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	private String firstName;
	private String lastName;
	
	protected Customer() {}
	
	public Customer(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		this.name = this.firstName + " " + this.lastName;
		return this.name;
	}
	
	public void setName(String name) {
		if (name.contains(" ")) {
			String[] parts = name.split(" ");
			this.firstName = parts[0];
			this.lastName = parts[1];
		} 
		this.name = name;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Customer))
			return false;
		
		Customer customer = (Customer) obj;
		return Objects.equals(this.id, customer.id) &&
				Objects.equals(this.firstName, customer.firstName)  &&
				Objects.equals(this.lastName, customer.lastName);

	}

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.firstName, this.lastName);
	}
	
	@Override
	public String toString() {
		return String.format("Customer[id=%d, firtName='%s', lastName='%s']",
			id, firstName, lastName);
	}
}
