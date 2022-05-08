package org.programmers.kdtspringjdbc.repository;

import lombok.extern.slf4j.Slf4j;
import org.programmers.kdtspringjdbc.customer.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Slf4j
public class JdbcCustomerRepository {

    private final String INSERT_SQL = "INSERT INTO customers(customer_id, name, email) VALUES (?, ?, ?)";
    private final String FIND_BY_NAME = "SELECT * FROM customers WHERE customer_id = ?";

    public Customer insertCustomer(Customer customer) {
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = JdbcConnector.getConnection();

            pstmt = con.prepareStatement(INSERT_SQL);
            pstmt.setBytes(1, customer.getCustomerId().toString().getBytes());
            pstmt.setString(2, customer.getName());
            pstmt.setString(3, customer.getEmail());

            pstmt.executeUpdate();
            return customer;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        } finally {
            JdbcConnector.close(con, pstmt, null);
        }
    }

    public Customer findCustomerByName(UUID customerId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = JdbcConnector.getConnection();

            pstmt = con.prepareStatement(FIND_BY_NAME);
            pstmt.setBytes(1, customerId.toString().getBytes());

            rs = pstmt.executeQuery();
            return new Customer(
                    UUID.nameUUIDFromBytes(rs.getBytes("customer_id")),
                    rs.getString("name"),
                    rs.getString("email")
            );
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        } finally {
            JdbcConnector.close(con, pstmt, rs);
        }
    }

}
