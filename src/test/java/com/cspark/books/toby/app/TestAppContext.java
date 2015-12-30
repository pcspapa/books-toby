package com.cspark.books.toby.app;

import com.cspark.books.toby.dao.UserDao;
import com.cspark.books.toby.service.DummyMailSender;
import com.cspark.books.toby.service.UserService;
import com.cspark.books.toby.service.UserServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;

/**
 * Created by cspark on 2015. 12. 30..
 */
@Configuration
@Profile("test")
public class TestAppContext {

    @Bean
    public UserService testUserService() {
        return new UserServiceTest.TestUserService();
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

}
