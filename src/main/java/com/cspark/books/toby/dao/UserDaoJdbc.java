package com.cspark.books.toby.dao;

import com.cspark.books.toby.domain.Level;
import com.cspark.books.toby.domain.User;
import com.cspark.books.toby.sqlservice.SqlService;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * Created by cspark on 2015. 11. 25..
 */
public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;

    private SqlService sqlService;

    public void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setSqlService(SqlService sqlService) {
        this.sqlService = sqlService;
    }

    @Override
    public void add(final User user) {
        jdbcTemplate.update(sqlService.getSql("userAdd")
                , user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend());
    }

    @Override
    public User get(String id) {
        return jdbcTemplate.queryForObject(sqlService.getSql("userGet"), new Object[]{id}, userRowMapper());
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update(sqlService.getSql("userDeleteAll"));
    }

    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject(sqlService.getSql("userGetCount"), Integer.class);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(sqlService.getSql("userGetAll"), userRowMapper());
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(sqlService.getSql("userUpdate")
                , user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId());
    }

    private RowMapper<User> userRowMapper() {
        return (resultSet, i) -> {
            User user = new User();
            user.setId(resultSet.getString("id"));
            user.setName(resultSet.getString("name"));
            user.setPassword(resultSet.getString("password"));
            user.setLevel(Level.valueOf(resultSet.getInt("level")));
            user.setLogin(resultSet.getInt("login"));
            user.setRecommend(resultSet.getInt("recommend"));
            return user;
        };
    }
}
