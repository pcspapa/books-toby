package com.cspark.books.toby.service;

import com.cspark.books.toby.dao.UserDao;
import com.cspark.books.toby.domain.Level;
import com.cspark.books.toby.domain.User;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
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
    UserService testUserService;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    ApplicationContext context;

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
        TestUserServiceImpl testUserServiceImpl = new TestUserServiceImpl(users.get(3).getId());
        testUserServiceImpl.setUserDao(userDao);

        UserServiceTx txUserService = new UserServiceTx();
        txUserService.setTransactionManager(transactionManager);
        txUserService.setUserService(testUserServiceImpl);

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
        TestUserServiceImpl testUserServiceImpl = new TestUserServiceImpl(users.get(3).getId());
        testUserServiceImpl.setUserDao(userDao);

        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTarget(testUserServiceImpl);
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

    @Test
    @DirtiesContext
    @Ignore
    public void upgradeAllOrNothingWithFactoryBean() throws Exception {
        TestUserServiceImpl testUserServiceImpl = new TestUserServiceImpl(users.get(3).getId());
        testUserServiceImpl.setUserDao(userDao);

        TxProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", TxProxyFactoryBean.class);
        txProxyFactoryBean.setTarget(testUserServiceImpl);

        UserService txUserService = (UserService) txProxyFactoryBean.getObject();

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
    @DirtiesContext
    @Ignore
    public void upgradeAllOrNothingWithProxyFactoryBean() throws Exception {
        TestUserServiceImpl testUserServiceImpl = new TestUserServiceImpl(users.get(3).getId());
        testUserServiceImpl.setUserDao(userDao);

        ProxyFactoryBean txProxyFactoryBean = context.getBean("&userService", ProxyFactoryBean.class);
        txProxyFactoryBean.setTarget(testUserServiceImpl);

        UserService txUserService = (UserService) txProxyFactoryBean.getObject();

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
    public void upgradeAllOrNothingWithAutoProxy() throws Exception {
        userDao.deleteAll();

        for (User user : users)
            userDao.add(user);

        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected.");
        } catch (Exception e) {
        }

        checkLevel(users.get(1), false);
    }

    @Test
    public void advisorAutoProxyCreator() throws Exception {
        assertThat(testUserService, is(instanceOf(Proxy.class)));

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

    static class TestUserServiceImpl extends UserServiceImpl {
        private String id;

        public TestUserServiceImpl() {
            this.id = "id4";
        }

        private TestUserServiceImpl(String id) {
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