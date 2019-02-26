/*
 * Created by Luke DeRienzo on 2/25/19 9:34 PM
 * Copyright (c) 2019. All rights reserved
 *
 * Last modified: 2/25/19 5:49 PM
 */

package com.ef.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;


public final class JdbcMySqlConnectionTester {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/parser?useSSL=false";
    private static final String USERNAME = "parser";
    private static final String PASSWORD = "password";

    @Before
    public void setUp() {
        try {
            workAroundForSomeBrokenJavaImplementations();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void workAroundForSomeBrokenJavaImplementations()
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
    }

    @Test
    public void testDatabaseConnection() {
        System.out.println("Connecting database...");
        try (Connection connection = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD)) {
            System.out.println("Database connected!");
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
    }
}
