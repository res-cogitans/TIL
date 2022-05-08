package org.programmers.kdtspringjdbc.repository;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public final class JdbcConnector {

    private JdbcConnector() {
    }

    public static Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(ConnectionConst.URL, ConnectionConst.USERNAME, ConnectionConst.PASSWORD);
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void close(Connection con, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                log.error("Got error while closing connection", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                log.error("Got error while closing connection", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (Exception e) {
                log.error("Got error while closing connection", e);
            }
        }
    }
}
