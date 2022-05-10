package org.programmers.springdao.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "customers")
@Getter
@NoArgsConstructor
public class CustomerEntity {
    public CustomerEntity(long id) {
        this.id = id;
    }

    @Id
    private long id;
    private String firstName;
    private String lastName;

    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
