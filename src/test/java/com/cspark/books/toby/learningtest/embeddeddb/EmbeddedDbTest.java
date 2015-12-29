package com.cspark.books.toby.learningtest.embeddeddb;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by cspark on 2015. 12. 29..
 */
public class EmbeddedDbTest {

    EmbeddedDatabase db;

    JdbcTemplate template;

    @Before
    public void setUp() throws Exception {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:/com/cspark/books/toby/learningtest/embeddeddb/schma.sql")
                .addScript("classpath:/com/cspark/books/toby/learningtest/embeddeddb/data.sql")
                .build();

        template = new JdbcTemplate(db);
    }

    @After
    public void tearDown() throws Exception {
        db.shutdown();
    }

    @Test
    public void initData() throws Exception {
        checkCount(2);

        List<Map<String, Object>> list = template.queryForList("SELECT * FROM sqlmap");
        assertThat(list.get(0).get("key_"), is("KEY1"));
        assertThat(list.get(0).get("sql_"), is("SQL1"));
        assertThat(list.get(1).get("key_"), is("KEY2"));
        assertThat(list.get(1).get("sql_"), is("SQL2"));
    }

    @Test
    public void insert() throws Exception {
        template.update("INSERT INTO sqlmap values(?, ?)", "KEY3", "SQL3");

        checkCount(3);
    }

    private void checkCount(int count) {
        assertThat(
                template.queryForObject("SELECT COUNT(*) FROM sqlmap", Integer.class),
                is(count)
        );
    }
}
