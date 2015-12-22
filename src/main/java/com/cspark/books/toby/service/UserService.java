package com.cspark.books.toby.service;

import com.cspark.books.toby.domain.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by cspark on 2015. 12. 18..
 */
public interface UserService {

    void add(User user);

    User get(String id);

    List<User> getAll();

    void deleteAll();

    void update(User user);

    void upgradeLevels();

}
