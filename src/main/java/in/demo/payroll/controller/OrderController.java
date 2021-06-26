package in.demo.payroll.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

import in.demo.payroll.dao.OrderRepository;
import in.demo.payroll.model.Order;
import in.demo.payroll.model.Status;

@RestController
public class OrderController {

	private final OrderRepository repository;
	private final OrderModelAssemebler assembler;

	public OrderController(OrderRepository repository, OrderModelAssemebler assembler) {
		this.repository = repository;
		this.assembler = assembler;
	}

	@GetMapping("/orders")
	public CollectionModel<EntityModel<Order>> all() {
		List<EntityModel<Order>> orders = repository.findAll().stream().map(assembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(orders,
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).all()).withSelfRel());
	}

	@GetMapping("/orders/{id}")
	public EntityModel<Order> one(@PathVariable Long id) {
		Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

		return assembler.toModel(order);
	}

	@PostMapping("/orders")
	ResponseEntity<?> newOrder(@RequestBody Order order) {

		order.setStatus(Status.IN_PROGRESS);
		Order newOrder = repository.save(order);

		return ResponseEntity
				.created(WebMvcLinkBuilder
						.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).one(newOrder.getId())).toUri())
				.body(assembler.toModel(newOrder));
	}

	@DeleteMapping("/orders/{id}/cancel")
	ResponseEntity<?> cancel(@PathVariable Long id) {

		Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

		if (order.getStatus() == Status.IN_PROGRESS) {
			order.setStatus(Status.CANCELLED);
			return ResponseEntity.ok(assembler.toModel(repository.save(order)));
		}

		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
				.header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
				.body(Problem.create().withTitle("Method Not Allowed")
						.withDetail("You can't cancel order that is in " + order.getStatus() + " status"));
	}

	@PutMapping("/orders/{id}/complete")
	public ResponseEntity<?> complete(@PathVariable Long id) {

		Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

		if (order.getStatus() == Status.IN_PROGRESS) {
			order.setStatus(Status.COMPLETED);
			return ResponseEntity.ok(assembler.toModel(repository.save(order)));
		}

		return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
				.header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
				.body(Problem.create().withTitle("Method not allowed")
						.withDetail("you can't complete an order that is in " + order.getStatus() + " status"));
	}

}
