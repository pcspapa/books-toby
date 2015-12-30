package com.cspark.books.toby.app;

import com.cspark.books.toby.service.DummyMailSender;
import com.cspark.books.toby.service.UserService;
import com.cspark.books.toby.service.UserServiceTest;
import org.h2.Driver;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Created by cspark on 2015. 12. 30..
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.cspark.books.toby")
@Import({SqlServiceContext.class})
public class AppContext {

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

    /**
     * Created by cspark on 2015. 12. 30..
     */
    @Configuration
    @Profile("product")
    public static class ProductAppContext {

        @Bean
        public MailSender mailSender() {
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("localhost");
            return mailSender;
        }

    }

    /**
     * Created by cspark on 2015. 12. 30..
     */
    @Configuration
    @Profile("test")
    public static class TestAppContext {

        @Bean
        public UserService testUserService() {
            return new UserServiceTest.TestUserService();
        }

        @Bean
        public MailSender mailSender() {
            return new DummyMailSender();
        }

    }
}
