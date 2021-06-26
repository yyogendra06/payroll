package in.demo.payroll.controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import in.demo.payroll.model.Order;
import in.demo.payroll.model.Status;

@Component
public class OrderModelAssemebler implements RepresentationModelAssembler<Order, EntityModel<Order>> {

	@Override
	public EntityModel<Order> toModel(Order order) {
		EntityModel<Order> orderEntity = EntityModel.of(order,
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).one(order.getId()))
						.withSelfRel(),
				WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).all()).withRel("orders"));

		if (order.getStatus() == Status.IN_PROGRESS) {
			orderEntity.add(WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));

			orderEntity.add(
					WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OrderController.class).complete(order.getId()))
							.withRel("complete"));
		}

		return orderEntity;
	}

}
