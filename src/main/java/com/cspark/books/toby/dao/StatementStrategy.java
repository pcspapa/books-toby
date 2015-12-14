package com.cspark.books.toby.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by cspark on 2015. 12. 14..
 */
public interface StatementStrategy {

    PreparedStatement makePreparedStatement(Connection conn) throws SQLException;

}
