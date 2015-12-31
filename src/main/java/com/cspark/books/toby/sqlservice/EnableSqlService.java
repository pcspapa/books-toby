package com.cspark.books.toby.sqlservice;

import org.springframework.context.annotation.Import;

/**
 * Created by cspark on 2015. 12. 31..
 */
@Import(value = SqlServiceContext.class)
public @interface EnableSqlService {
}
