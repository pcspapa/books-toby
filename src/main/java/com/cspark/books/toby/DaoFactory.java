package com.cspark.books.toby;

/**
 * Created by cspark on 2015. 12. 11..
 */
public class DaoFactory {

    public UserDao userDao() {
        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
        return userDao;
    }

}
