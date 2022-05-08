package org.programmers.kdtspringjdbc.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class JdbcConnectorTest {

    @Test
    @DisplayName("Jdbc 연결")
    void getConnection() {
        Connection con = JdbcConnector.getConnection();
        assertNotNull(con);
    }

}