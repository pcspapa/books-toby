package com.cspark.books.toby.service;

import com.cspark.books.toby.domain.User;

import java.sql.SQLException;

/**
 * Created by cspark on 2015. 12. 18..
 */
public interface UserService {

    void upgradeLevels();

    void add(User user);

}
