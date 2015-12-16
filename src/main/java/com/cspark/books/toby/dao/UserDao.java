package com.cspark.books.toby.dao;

import com.cspark.books.toby.domain.User;

import java.util.List;

/**
 * Created by cspark on 2015. 12. 16..
 */
public interface UserDao {
    void add(User user);

    User get(String id);

    void deleteAll();

    int getCount();

    List<User> getAll();

    void update(User user);
}
