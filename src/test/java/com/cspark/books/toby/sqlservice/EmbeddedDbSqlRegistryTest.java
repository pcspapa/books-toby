package com.cspark.books.toby.sqlservice;

import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

    EmbeddedDatabase db;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/com/cspark/books/toby/learningtest/embeddeddb/schma.sql")
                .build();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);

        return embeddedDbSqlRegistry;
    }

    @After
    public void tearDown() throws Exception {
        db.shutdown();
    }

    @Test
    public void transactionalUpdate() throws Exception {
        super.checkFindResult("SQL1", "SQL2", "SQL3");

        Map<String, String> sqlMap = new HashMap<>();
        sqlMap.put("KEY1", "Modifier1");
        sqlMap.put("SQL9999!@#$", "Modifier9999");

        try {
            sqlRegistry.updateSql(sqlMap);
            fail();
        } catch (SqlUpdateFailureException e) {
        }

        super.checkFindResult("SQL1", "SQL2", "SQL3");
    }
}
