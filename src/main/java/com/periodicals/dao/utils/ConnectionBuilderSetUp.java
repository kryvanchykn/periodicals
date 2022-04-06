package com.periodicals.dao.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.SQLException;

public class ConnectionBuilderSetUp {
    private static final Logger log = LogManager.getLogger(ConnectionBuilderSetUp.class.getName());

    private ConnectionBuilder connectionBuilder;

    public void setConnectionBuilder(ConnectionBuilder connectionBuilder) {
        this.connectionBuilder = connectionBuilder;
    }

    public ConnectionBuilder getConnectionBuilder() throws SQLException {
        if (connectionBuilder == null){
           log.info("ConnectionPool");
            return ConnectionPool.getInstance();
        } else {
            log.info("Direct connection");
            return this.connectionBuilder;
        }
    }
}
