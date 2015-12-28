package com.cspark.books.toby.sqlservice;

/**
 * Created by cspark on 2015. 12. 28..
 */
public class DefaultSqlService extends BaseSqlService {

    public DefaultSqlService() {
        setSqlReader(new JaxbXmlSqlReader());
        setSqlRegistry(new HashMapSqlRegistry());
    }

}
