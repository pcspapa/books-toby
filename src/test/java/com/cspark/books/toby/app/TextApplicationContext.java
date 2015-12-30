package com.cspark.books.toby.app;

import com.cspark.books.toby.dao.UserDao;
import com.cspark.books.toby.dao.UserDaoJdbc;
import com.cspark.books.toby.service.UserService;
import com.cspark.books.toby.service.UserServiceImpl;
import com.cspark.books.toby.service.UserServiceTest;
import com.cspark.books.toby.sqlservice.EmbeddedDbSqlRegistry;
import com.cspark.books.toby.sqlservice.OxmSqlService;
import com.cspark.books.toby.sqlservice.SqlRegistry;
import com.cspark.books.toby.sqlservice.SqlService;
import org.h2.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by cspark on 2015. 12. 30..
 */
@Configuration
@ImportResource("/test-sqlmap-applicationContext.xml")
public class TextApplicationContext {

    // 1. <context:annotation-config/> 제거
    // 2. <bean>의 전환

    @Resource
    DataSource embeddedDatabase;

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
        userDao.setSqlService(sqlService());

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

    @Bean
    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller());
        sqlService.setSqlRegistry(sqlRegistry());

        return sqlService;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("com.cspark.books.toby.sqlservice.jaxb");

        return marshaller;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase);

        return sqlRegistry;
    }

}
