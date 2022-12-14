package com.example.payroll.configuration;

import com.example.payroll.entity.Employee;
import com.example.payroll.entity.Order;
import com.example.payroll.entity.Status;
import com.example.payroll.repository.EmployeeRepository;
import com.example.payroll.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    // Spring загружает все бины CommandLineRunner при запуске приложения (сразу после инициилизации Spring-контекста)

    @Bean
    CommandLineRunner initDatabase (EmployeeRepository employeeRepository, OrderRepository orderRepository) {

        return args -> {
            employeeRepository.save(new Employee("Stepan", "Semenov", "gamer"));
            employeeRepository.save(new Employee("Bat" , "Man", "hero"));
            employeeRepository.findAll().forEach(employee -> log.info("Preloaded " + employee));

            orderRepository.save(new Order("Game console", Status.COMPLETED));
            orderRepository.save(new Order("Batmobile", Status.IN_PROGRESS));
            orderRepository.findAll().forEach(order -> log.info("Preloaded " + order));
        };
    }
}
