package com.cspark.books.toby.sqlservice;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cspark on 2015. 12. 28..
 */
public class HashMapSqlRegistry implements SqlRegistry {

    private Map<String, String> sqlMap = new HashMap<>();

    @Override
    public void registerSql(String key, String value) {
        sqlMap.put(key, value);
     }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);
        if (sql == null)
            throw new SqlNotFoundException(key + "를 이용해서 SQL을 찾을 수 없습니다");
        else
            return sql;
    }
}
