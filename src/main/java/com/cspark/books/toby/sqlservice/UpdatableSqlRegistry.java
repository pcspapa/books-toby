package com.cspark.books.toby.sqlservice;

import java.util.Map;

/**
 * Created by cspark on 2015. 12. 29..
 */
public interface UpdatableSqlRegistry extends SqlRegistry {

    void updateSql(String key, String sql) throws SqlUpdateFailureException;

    void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException;

}
