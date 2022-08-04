package com.example.payroll.util;

import com.example.payroll.controller.OrderController;
import com.example.payroll.entity.Order;
import com.example.payroll.entity.Status;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssembler implements RepresentationModelAssembler <Order, EntityModel<Order>> {

    @Override
    public EntityModel<Order> toModel (Order order) {

        // данные ссылки будут предоставляться всегда
        EntityModel<Order> orderModel = EntityModel.of(order,
                linkTo(methodOn(OrderController.class).one(order.getId())).withSelfRel(),
                linkTo(methodOn(OrderController.class).allOrders()).withRel("orders"));

        // данные ссылки будут предоставляться в зависимости от статуса заказа
        if (order.getStatus() == Status.IN_PROGRESS) {
            orderModel.add(linkTo(methodOn(OrderController.class).cancel(order.getId())).withRel("cancel"));
            orderModel.add(linkTo(methodOn(OrderController.class).complete(order.getId())).withRel("complete"));
        }
        return orderModel;
    }
}
