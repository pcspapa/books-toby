package com.cspark.books.toby.service;

import com.cspark.books.toby.dao.UserDao;
import com.cspark.books.toby.domain.Level;
import com.cspark.books.toby.domain.User;

import java.util.List;

/**
 * Created by cspark on 2015. 12. 16..
 */
public class UserService {

    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if(user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                userDao.update(user);
            }
            else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                userDao.update(user);
            }
        }

    }
}
