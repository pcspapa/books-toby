package com.cspark.books.toby.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by cspark on 2015. 11. 30..
 */
public interface ConnectionMaker {

    public Connection makeNewConnection() throws ClassNotFoundException, SQLException;

}
