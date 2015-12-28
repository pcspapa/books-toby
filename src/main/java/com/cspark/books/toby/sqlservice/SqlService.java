package com.cspark.books.toby.sqlservice;

/**
 * Created by cspark on 2015. 12. 28..
 */
public interface SqlService {

    String getSql(String key) throws SqlRetrievalFailureException;
}
