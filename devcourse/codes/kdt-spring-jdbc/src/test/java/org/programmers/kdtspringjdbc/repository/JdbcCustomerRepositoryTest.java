package org.programmers.kdtspringjdbc.repository;

import org.junit.jupiter.api.Test;
import org.programmers.kdtspringjdbc.customer.Customer;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JdbcCustomerRepositoryTest {

    @Test
    void insertCustomer() {
        var jdbcCustomerRepository = new JdbcCustomerRepository();
        Customer customer = new Customer(UUID.randomUUID(), "Tester", "sei@ya");

        jdbcCustomerRepository.insertCustomer(customer);
    }

    @Test
    void findCustomerByName() {
    }
}