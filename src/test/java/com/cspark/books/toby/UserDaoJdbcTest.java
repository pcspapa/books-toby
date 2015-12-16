package com.cspark.books.toby;

import com.cspark.books.toby.dao.UserDao;
import com.cspark.books.toby.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
public class UserDaoJdbcTest {

    @Autowired
    private UserDao userDao;

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
    public void count() {
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.add(user1);
        assertThat(userDao.getCount(), is(1));

        userDao.add(user2);
        assertThat(userDao.getCount(), is(2));

        userDao.add(user3);
        assertThat(userDao.getCount(), is(3));
    }

    @Test
    public void addAndGet() {
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.add(user1);
        userDao.add(user2);
        assertThat(userDao.getCount(), is(2));

        User getUser1 = userDao.get(user1.getId());
        assertThat(getUser1.getName(), is(user1.getName()));
        assertThat(getUser1.getPassword(), is(user1.getPassword()));

        User getUser2 = userDao.get(user2.getId());
        assertThat(getUser2.getName(), is(user2.getName()));
        assertThat(getUser2.getPassword(), is(user2.getPassword()));
    }

    @Test
    public void testduplicateKey() throws Exception {
        userDao.deleteAll();

        userDao.add(user1);
        userDao.add(user1);

    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void getUserFailure() {
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.get("unknown_id");
    }


    @Test(expected = DuplicateKeyException.class)
    public void getAll() {
        userDao.deleteAll();

        userDao.add(user1);
        List<User> users1 = userDao.getAll();
        assertThat(users1.size(), is(1));

        userDao.add(user2);
        List<User> users2 = userDao.getAll();
        assertThat(users2.size(), is(2));

        userDao.add(user3);
        List<User> users3 = userDao.getAll();
        assertThat(users3.size(), is(3));
    }
}
