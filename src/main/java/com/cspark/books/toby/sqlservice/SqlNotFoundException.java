package com.cspark.books.toby.sqlservice;

/**
 * Created by cspark on 2015. 12. 28..
 */
public class SqlNotFoundException extends RuntimeException {
    public SqlNotFoundException(String message) {
        super(message);
    }
}
