package org.programmers.springdao;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
@SpringBootTest
public class JDBCTest {

    static final String JDBC_DRIVER = "org.h2.Driver";
    static final String DB_URL = "jdbc:h2:~/test";
    static final String USER = "sa";
    static final String PASSWORD = "";

    static final String DROP_TABLE_SQL = "DROP TABLE customers IF EXISTS";
    static final String CREATE_TABLE_SQL = "CREATE TABLE customers(id SERIAL, first_name VARCHAR(255), last_name VARCHAR(255))";
    static final String INSERT_SQL = "INSERT INTO customers (id, first_name, last_name) VALUES(1, 'honggu', 'kang')";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void jdbc_sample() {
        try {
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            log.info("GET CONNECTION");

            Statement stmt = con.createStatement();
            stmt.executeUpdate(DROP_TABLE_SQL);
            stmt.executeUpdate(CREATE_TABLE_SQL);
            log.info("CREATED TABLE");

            stmt.executeUpdate(INSERT_SQL);
            log.info("INSERTED CUSTOMER INFORMATION");

            ResultSet rs = stmt.executeQuery("SELECT * FROM customers WHERE id = 1");

            while(rs.next()) {
                String fullName = rs.getString("first_name") + " " + rs.getString("last_name");
                log.info("CUSTOMER FULL_NAME = {}", fullName);
            }

            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void jdbcTemplate_sample() {
        jdbcTemplate.update(DROP_TABLE_SQL);
        jdbcTemplate.update(CREATE_TABLE_SQL);
        log.info("CREATED TABLE USING JDBC TEMPLATE");

        jdbcTemplate.update(INSERT_SQL);
        log.info("INSERTED CUSTOMER INFORMATION USING JDBC TEMPLATE");

        String fullName = jdbcTemplate.queryForObject(
                "SELECT * FROM customers WHERE id = 1",
                (rs, i) -> rs.getString("first_name") + " " + rs.getString("last_name")
        );
        log.info("FULL_NAME = {}", fullName);
    }
}
