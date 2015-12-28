package com.cspark.books.toby.sqlservice;

/**
 * Created by cspark on 2015. 12. 28..
 */
public interface SqlRegistry {

    void registry(String key, String value);

    String findSql(String key);

}
