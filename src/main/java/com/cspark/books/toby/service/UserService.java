package com.cspark.books.toby.service;

import com.cspark.books.toby.domain.User;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by cspark on 2015. 12. 18..
 */
@Transactional
public interface UserService {

    void add(User user);

    @Transactional(readOnly = true)
    User get(String id);

    @Transactional(readOnly = true)
    List<User> getAll();

    void deleteAll();

    void update(User user);

    void upgradeLevels();

}
