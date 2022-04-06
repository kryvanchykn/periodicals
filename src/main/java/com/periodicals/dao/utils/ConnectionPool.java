package com.periodicals.dao.utils;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool implements ConnectionBuilder{
    private static ConnectionPool instance;
    private final DataSource dataSource;

    private ConnectionPool() {
        try {
            Context ctx = new InitialContext();
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/periodicals");
        } catch (NamingException e) {
            e.printStackTrace();
            throw new IllegalStateException("Cannot init", e);
        }
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    /**
     * get connection from pool
     * @return connection
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * close AutoCloseable object and log error if it appears
     *
     * @param autoCloseable closeable object to close
     */
    public static void close(AutoCloseable autoCloseable) throws Exception {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            } catch (Exception ex) {
                throw new Exception("Cannot close", ex);
            }
        }
    }

    /**
     * rollback connection and log error if it appears
     *
     * @param con Connection to rollback
     */
    public static void rollback(Connection con) throws SQLException {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                throw new SQLException("Cannot rollback", ex);
            }
        }
    }

}
