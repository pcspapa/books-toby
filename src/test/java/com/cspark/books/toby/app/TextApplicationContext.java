package com.cspark.books.toby.app;

import com.cspark.books.toby.dao.UserDao;
import com.cspark.books.toby.dao.UserDaoJdbc;
import com.cspark.books.toby.service.UserService;
import com.cspark.books.toby.service.UserServiceImpl;
import com.cspark.books.toby.service.UserServiceTest;
import com.cspark.books.toby.sqlservice.SqlService;
import org.h2.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by cspark on 2015. 12. 30..
 */
@Configuration
@ImportResource("/test-sqlmap-applicationContext.xml")
public class TextApplicationContext {

    // 1. <context:annotation-config/> 제거
    // 2. <bean>의 전환

    @Autowired
    SqlService sqlService;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:h2:tcp://localhost//projects/h2data/books/toby");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());

        return transactionManager;
    }

    @Bean
    public UserDao userDao() {
        UserDaoJdbc userDao = new UserDaoJdbc();
        userDao.setDataSource(dataSource());
        userDao.setSqlService(sqlService);  // error sqlSservice()

        return userDao;
    }

    @Bean
    public UserService userService() {
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(userDao());

        return userService;
    }

    @Bean
    public UserService testUserService() {
        UserServiceTest.TestUserService testUserService = new UserServiceTest.TestUserService();
        testUserService.setUserDao(userDao());

        return testUserService;
    }
}
