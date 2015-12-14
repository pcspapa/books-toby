package com.cspark.books.toby;

import com.cspark.books.toby.dao.DeleteStatementStrategy;
import com.cspark.books.toby.dao.StatementStrategy;
import com.cspark.books.toby.dao.UserDao;
import com.cspark.books.toby.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by cspark on 2015. 11. 30..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext.xml")
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
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


    @Test
    public void getAll() throws Exception {
        dao.deleteAll();

        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertThat(users1.size(), is(1));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertThat(users2.size(), is(2));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertThat(users3.size(), is(3));
    }
}
