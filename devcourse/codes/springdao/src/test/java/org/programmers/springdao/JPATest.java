package org.programmers.springdao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.programmers.springdao.domain.CustomerEntity;
import org.programmers.springdao.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
public class JPATest {

    @Autowired
    CustomerRepository repository;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void INSERT_TEST() {

        //Given
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setLastName("honggu");
        customer.setFirstName("kang");

        //When
        repository.save(customer);

        //Then
        CustomerEntity foundEntity = repository.findById(1L).get();
        log.info("{} {}", foundEntity.getFirstName(), foundEntity.getLastName());
    }

    @Test
    @Transactional
    void UPDATE_TEST() {

        //Given
        CustomerEntity customer = new CustomerEntity();
        customer.setId(1L);
        customer.setLastName("honggu");
        customer.setFirstName("kang");
        repository.save(customer);

        //When
        CustomerEntity foundEntity = repository.findById(1L).get();
        foundEntity.setFirstName("guppy");
        foundEntity.setLastName("hong");

        //Then
        CustomerEntity foundEntityAfterUpdate = repository.findById(1L).get();
        log.info("{} {}", foundEntityAfterUpdate.getFirstName(), foundEntityAfterUpdate.getLastName());
    }
}
