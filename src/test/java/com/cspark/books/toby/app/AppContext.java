package com.cspark.books.toby.app;

import com.cspark.books.toby.dao.UserDao;
import com.cspark.books.toby.service.DummyMailSender;
import com.cspark.books.toby.service.UserService;
import com.cspark.books.toby.service.UserServiceTest;
import com.cspark.books.toby.sqlservice.EnableSqlService;
import com.cspark.books.toby.sqlservice.SqlMapConfig;
import com.cspark.books.toby.sqlservice.SqlServiceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Driver;

/**
 * Created by cspark on 2015. 12. 30..
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.cspark.books.toby")
@EnableSqlService
@PropertySource("/database.properties")
public class AppContext implements SqlMapConfig {

    @Autowired
    Environment env;

    @Value("${db.driverClass}")
    Class<? extends Driver> driverClass;

    @Value("${db.url}")
    String url;

    @Value("${db.username}")
    String username;

    @Value("${db.password}")
    String password;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource());

        return transactionManager;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfiguer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public Resource getSqlMapResource() {
        return new ClassPathResource("sqlmap.xml", UserDao.class);
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
