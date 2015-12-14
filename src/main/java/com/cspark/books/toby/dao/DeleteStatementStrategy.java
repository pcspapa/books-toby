package com.cspark.books.toby.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by cspark on 2015. 12. 14..
 */
public class DeleteStatementStrategy implements StatementStrategy {
    @Override
    public PreparedStatement makePreparedStatement(Connection conn) throws SQLException {
        return conn.prepareStatement("DELETE FROM users");
    }
}
