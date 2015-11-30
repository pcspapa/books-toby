package com.cspark.books.toby;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by cspark on 2015. 11. 30..
 */
public class NConnectionMaker implements ConnectionMaker {

    @Override
    public Connection makeNewConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost//projects/h2data/books/toby", "sa", "");

        return conn;
    }

}
