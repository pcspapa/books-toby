package com.cspark.books.toby.app;

import com.cspark.books.toby.dao.UserDao;
import com.cspark.books.toby.service.UserService;
import com.cspark.books.toby.service.UserServiceTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by cspark on 2015. 12. 30..
 */
@Configuration
public class TestAppContext {

    @Autowired
    UserDao userDao;

    @Bean
    public UserService testUserService() {
        return new UserServiceTest.TestUserService();
    }

}
