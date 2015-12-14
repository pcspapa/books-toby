package com.cspark.books.toby.dao;

import com.cspark.books.toby.domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Created by cspark on 2015. 11. 25..
 */
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    private JdbcContext jdbcContext;

    private DataSource dataSource;

    public void setJdbcContext(JdbcContext jdbcContext) {
        this.jdbcContext = jdbcContext;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user) throws SQLException {
        jdbcTemplate.update("INSERT INTO users(id, name, password) VALUES (?, ?, ?)", user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
            ps.setString(1, id);

            rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }

            if (user == null) throw new EmptyResultDataAccessException(1);

            return user;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public void deleteAll() throws SQLException {
        jdbcTemplate.update("DELETE FROM users");
    }

    public int getCount() throws SQLException {
        return jdbcTemplate.query(connection -> {
            return connection.prepareStatement("SELECT COUNT(*) FROM users");
        }, resultSet -> {
            resultSet.next();
            return resultSet.getInt(1);
        });
    }

}
