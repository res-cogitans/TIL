package org.programmers.springdao.repository;

import org.apache.ibatis.annotations.Mapper;
import org.programmers.springdao.domain.Customer;

@Mapper
public interface CustomerXmlMapper {
    void save(Customer customer);
    Customer findById(long id);
}
