package com.cspark.books.toby.service;

import com.cspark.books.toby.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

/**
 * Created by cspark on 2015. 12. 18..
 */
public class UserServiceTx implements UserService {

    UserService userService;

    PlatformTransactionManager transactionManager;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public void add(User user) {
        userService.add(user);
    }

    @Override
    public User get(String id) {
        return userService.get(id);
    }

    @Override
    public List<User> getAll() {
        return userService.getAll();
    }

    @Override
    public void deleteAll() {
        userService.deleteAll();
    }

    @Override
    public void update(User user) {
        userService.update(user);
    }

    @Override
    public void upgradeLevels() {
        TransactionStatus status = transactionManager
                .getTransaction(new DefaultTransactionDefinition());

        try {

            userService.upgradeLevels();

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw  e;
        }
    }
}
