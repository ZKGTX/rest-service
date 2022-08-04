package com.example.payroll.util;

import com.example.payroll.controller.EmployeeController;
import com.example.payroll.entity.Employee;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

// класс преобразует немодельный объект в основанный на модели объект
// в результате модель вместе с объектом-ресурсом будет содержать также ссылку на него

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler <Employee, EntityModel<Employee>> {


    @Override
    public EntityModel<Employee> toModel(Employee employee) {
        return EntityModel.of(employee,
                linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
                linkTo(methodOn(EmployeeController.class).allEmployees()).withRel("employees"));
    }
}
