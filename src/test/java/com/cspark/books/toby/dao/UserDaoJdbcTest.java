package com.cspark.books.toby.dao;

import com.cspark.books.toby.dao.UserDao;
import com.cspark.books.toby.domain.Level;
import com.cspark.books.toby.domain.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by cspark on 2015. 11. 30..
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserDaoJdbcTest {

    @Autowired
    private UserDao userDao;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        user1 = new User("cspark1", "비마중1", "1111", Level.BASIC, 1, 0);
        user2 = new User("cspark2", "비마중2", "1112", Level.SILVER, 55, 10);
        user3 = new User("cspark3", "비마중3", "1113", Level.GOLD, 100, 40);
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
        checkSameUser(user1, getUser1);

        User getUser2 = userDao.get(user2.getId());
        checkSameUser(user2, getUser2);
    }

    @Test(expected = DuplicateKeyException.class)
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

    @Test
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

    @Test
    public void update() throws Exception {
        userDao.deleteAll();

        userDao.add(user1);
        userDao.add(user2);

        user1.setName("변경자");
        user1.setPassword("password");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        userDao.update(user1);

        User getUser1 = userDao.get(user1.getId());
        checkSameUser(user1, getUser1);

        User getUser2 = userDao.get(user2.getId());
        checkSameUser(user2, getUser2);
    }

    private void checkSameUser(User user, User getUser) {
        assertThat(user.getName(), is(getUser.getName()));
        assertThat(user.getPassword(), is(getUser.getPassword()));
        assertThat(user.getName(), is(getUser.getName()));
        assertThat(user.getLevel(), is(getUser.getLevel()));
        assertThat(user.getLogin(), is(getUser.getLogin()));
        assertThat(user.getRecommend(), is(getUser.getRecommend()));
    }
}
