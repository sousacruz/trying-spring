---
tags: [rest, hateoas, hypermedia]
projects: [spring-framework, spring-hateoas]
---

= Learning about RESTful Services with Spring

So far, I've building web-based services, but I realized that's not enough to make my API "RESTful" and I was wrong by calling any HTTP-based interface a REST API.  

This simple side project is "Hello World" about REST using https://spring.io/projects/spring-boot[Spring Boot] and https://spring.io/projects/spring-hateoas[Spring HATEOAS], a library that give us the constructs to define a RESTful service and then render it in an acceptable format for client consumption. So, in this sample project you will find a little bit more that CRUD operations actions (`GET`, `POST`, `PUT`, `DELETE`, ...).

This project is based on Spring MVC and uses the static helper methods from `WebMvcLinkBuilder` to build these links.
I will try Spring WebFlux in other project later, when I must use `WebFluxLinkBuilder` instead.