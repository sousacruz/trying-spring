package com.github.sousacruz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.sousacruz.model.Customer;
import com.github.sousacruz.model.Ticket;
import com.github.sousacruz.model.TicketStatus;
import com.github.sousacruz.repositories.CustomerRepository;
import com.github.sousacruz.repositories.TicketRepository;

@Configuration
public class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

	@Bean
	public CommandLineRunner createCustomers(CustomerRepository repo) {
		return (args) -> {
			// save a few customers
			repo.save(new Customer("Jack", "Bauer"));
			repo.save(new Customer("Chloe", "O'Brian"));
			repo.save(new Customer("Kim", "Bauer"));
			repo.save(new Customer("David", "Palmer"));
			repo.save(new Customer("Michelle", "Dessler"));
			repo.save(new Customer("Kristen", "Palmer"));

			// fetch all customers
			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Customer customer : repo.findAll()) {
				log.info(customer.toString());
			}
			log.info("");

			// fetch an individual customer by ID
			Customer customer = repo.findById(1L);
			log.info("Customer found with findById(1L):");
			log.info("--------------------------------");
			log.info(customer.toString());
			log.info("");

			// fetch customers by last name
			log.info("Customer found with findByLastName('Bauer'):");
			log.info("--------------------------------------------");
			repo.findByLastName("Bauer").forEach(bauer -> {
				log.info(bauer.toString());
			});
			log.info("");

			// fetch customers by last name
			log.info("Customer found with findByLastName('Palmer'):");
			log.info("--------------------------------------------");
			for (Customer palmer : repo.findByLastName("Palmer")) {
				log.info(palmer.toString());
			}
			log.info("");
		};
	}

	@Bean
	public CommandLineRunner createTickets(TicketRepository repo) {
		return (args) -> {		
			// save a few tickets
			repo.save(new Ticket(TicketStatus.OPENED, "Reset my password please"));
			repo.save(new Ticket(TicketStatus.IN_PROGRESS, "Restore application database backup from yesterday"));
			
			// fetch all tickets
			log.info("Tickets found with findAll():");
			log.info("-------------------------------");
			for (Ticket ticket : repo.findAll()) {
				log.info(ticket.toString());
			}
			log.info("");

		};
	}
}