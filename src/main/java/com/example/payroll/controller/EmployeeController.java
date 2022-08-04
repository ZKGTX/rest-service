package com.example.payroll.controller;

import com.example.payroll.entity.Employee;
import com.example.payroll.exception.EmployeeNotFoundException;
import com.example.payroll.repository.EmployeeRepository;
import com.example.payroll.util.EmployeeModelAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class EmployeeController {

private final EmployeeRepository employeeRepository;

private final EmployeeModelAssembler employeeModelAssembler;

    public EmployeeController(EmployeeRepository employeeRepository, EmployeeModelAssembler employeeModelAssembler) {
        this.employeeRepository = employeeRepository;
        this.employeeModelAssembler = employeeModelAssembler;
    }

    @GetMapping("/employees")
    public CollectionModel<EntityModel<Employee>> allEmployees() {
        List<EntityModel<Employee>> employees = employeeRepository.findAll().stream()
                .map(employeeModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(employees,
                linkTo(methodOn(EmployeeController.class).allEmployees()).withSelfRel());

    }

    @PostMapping("/employees")
    ResponseEntity<?> newEmployee (@RequestBody Employee employee) {

        EntityModel<Employee> entityModel = employeeModelAssembler.toModel(employeeRepository.save(employee));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @GetMapping("/employees/{id}")
    public EntityModel<Employee> one(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return employeeModelAssembler.toModel(employee);

    }

    @PutMapping("/employees/{id}")
    ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        Employee updatedEmployee = employeeRepository.findById(id).map(employee -> {
            employee.setName(newEmployee.getName());
            employee.setRole(newEmployee.getRole());
            return employeeRepository.save(employee);
        }).orElseGet(() -> {
            newEmployee.setId(id);
            return employeeRepository.save(newEmployee);
        });

        EntityModel<Employee> entityModel = employeeModelAssembler.toModel(updatedEmployee);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);

    }

    @DeleteMapping("/employees/{id}")
    ResponseEntity<?> deleteEmployee (@PathVariable Long id) {
        employeeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
