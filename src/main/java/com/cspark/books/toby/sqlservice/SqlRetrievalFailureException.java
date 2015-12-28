package com.cspark.books.toby.sqlservice;

/**
 * Created by cspark on 2015. 12. 28..
 */
public class SqlRetrievalFailureException extends RuntimeException {

    public SqlRetrievalFailureException(String message) {
        super(message);
    }

    public SqlRetrievalFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlRetrievalFailureException(Throwable cause) {
        super(cause);
    }

}
