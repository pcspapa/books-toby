package com.cspark.books.toby.sqlservice;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Created by cspark on 2015. 12. 29..
 */
public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {

    private JdbcTemplate template;

    TransactionTemplate transactionTemplate;

    public void setDataSource(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
        transactionTemplate = new TransactionTemplate(
                new DataSourceTransactionManager(dataSource)
        );
    }

    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        int affected = template.update("UPDATE sqlmap SET sql_ = ? WHERE key_ = ?", sql, key);
        if (affected == 0)
            throw new SqlUpdateFailureException(key + "에 해당하는 SQL을 찾을 수 없습니다");
    }

    @Override
    public void updateSql(final Map<String, String> sqlmap) throws SqlUpdateFailureException {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
                    updateSql(entry.getKey(), entry.getValue());
                }
            }
        });
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
