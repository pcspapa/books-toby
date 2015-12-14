package com.cspark.books.toby;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by cspark on 2015. 11. 30..
 */
public class UserDaoTest {

    private UserDao dao;
    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        ApplicationContext context = new GenericXmlApplicationContext("applicationContext.xml");

        dao = context.getBean("userDao", UserDao.class);

        user1 = new User("cspark1", "비마중1", "1111");
        user2 = new User("cspark2", "비마중2", "1112");
        user3 = new User("cspark3", "비마중3", "1113");
    }

    @Test
    public void count() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        assertThat(dao.getCount(), is(1));

        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        dao.add(user3);
        assertThat(dao.getCount(), is(3));
    }

    @Test
    public void addAndGet() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount(), is(2));

        User getUser1 = dao.get(user1.getId());
        assertThat(getUser1.getName(), is(user1.getName()));
        assertThat(getUser1.getPassword(), is(user1.getPassword()));

        User getUser2 = dao.get(user2.getId());
        assertThat(getUser2.getName(), is(user2.getName()));
        assertThat(getUser2.getPassword(), is(user2.getPassword()));
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() throws Exception {
        dao.deleteAll();
        assertThat(dao.getCount(), is(0));

        dao.get("unknown_id");
    }
}
