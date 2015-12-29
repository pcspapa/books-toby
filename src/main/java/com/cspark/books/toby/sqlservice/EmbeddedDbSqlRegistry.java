package com.cspark.books.toby.sqlservice;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by cspark on 2015. 12. 29..
 */
public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {

    private JdbcTemplate template;

    public void setDataSource(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        int affected = template.update("UPDATE sqlmap SET sql_ = ? WHERE key_ = ?", sql, key);
        if (affected == 0)
            throw new SqlUpdateFailureException(key + "에 해당하는 SQL을 찾을 수 없습니다");
    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
        for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
            updateSql(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void registerSql(String key, String sql) {
        template.update("INSERT INTO sqlmap values(?, ?)", key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        try {
            return template.queryForObject("SELECT sql_ FROM sqlmap WHERE key_ = ?", String.class, key);
        } catch (EmptyResultDataAccessException e) {
            throw new SqlNotFoundException(key + "을 이용해서 찾을 수 없습니다");
        }

    }
}
