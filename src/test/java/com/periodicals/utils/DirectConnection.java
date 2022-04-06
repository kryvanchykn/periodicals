package com.periodicals.utils;

import com.periodicals.dao.utils.ConnectionBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static com.periodicals.utils.TestConstants.*;

public class DirectConnection implements ConnectionBuilder {
    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(FULL_URL);
    }
}
