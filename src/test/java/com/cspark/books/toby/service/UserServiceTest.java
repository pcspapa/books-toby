package com.cspark.books.toby.service;

import com.cspark.books.toby.dao.TransactionHandler;
import com.cspark.books.toby.dao.UserDao;
import com.cspark.books.toby.domain.Level;
import com.cspark.books.toby.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by cspark on 2015. 12. 16..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserServiceTest {

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Autowired
    PlatformTransactionManager transactionManager;

    private List<User> users;

    @Before
    public void setUp() throws Exception {
        users = Arrays.asList(
                new User("id1", "name1", "password1", Level.BASIC, 49, 0),
                new User("id2", "name2", "password2", Level.BASIC, 50, 0),
                new User("id3", "name3", "password3", Level.SILVER, 60, 29),
                new User("id4", "name4", "password4", Level.SILVER, 60, 30),
                new User("id5", "name5", "password5", Level.GOLD, 100, 100)
        );

    }

    @Test
    public void upgradeLevels() throws Exception {
        userDao.deleteAll();

        for (User user : users)
            userDao.add(user);

        userService.upgradeLevels();

        checkLevel(users.get(0), false);
        checkLevel(users.get(1), true);
        checkLevel(users.get(2), false);
        checkLevel(users.get(3), true);
        checkLevel(users.get(4), false);
    }

    @Test
    public void add() throws Exception {
        userDao.deleteAll();

        users.get(0).setLevel(null);
        userService.add(users.get(0));

        assertThat(users.get(0).getLevel(), is(Level.BASIC));

    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);

        UserServiceTx txUserService = new UserServiceTx();
        txUserService.setTransactionManager(transactionManager);
        txUserService.setUserService(testUserService);

        userDao.deleteAll();

        for (User user : users)
            userDao.add(user);

        try {
            txUserService.upgradeLevels();
            fail("TestUserServiceException expected.");
        } catch (Exception e) {
        }

        checkLevel(users.get(1), false);
    }


    @Test
    public void upgradeAllOrNothingWithDynamicProxy() throws Exception {
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);

        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTarget(testUserService);
        txHandler.setTransactionManager(transactionManager);
        txHandler.setPattern("upgradeLevels");

        UserService txUserService = (UserService) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] {UserService.class},
                txHandler);

        userDao.deleteAll();

        for (User user : users)
            userDao.add(user);

        try {
            txUserService.upgradeLevels();
            fail("TestUserServiceException expected.");
        } catch (Exception e) {
        }

        checkLevel(users.get(1), false);
    }

    private void checkLevel(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if(upgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        }
        else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }

    static class TestUserService extends UserServiceImpl {
        private String id;

        private TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if(user.getId().equals(id))
                throw new TestUserServiceException();

            super.upgradeLevel(user);
        }

        private class TestUserServiceException extends RuntimeException {
        }
    }
}