package com.cspark.books.toby;

import java.sql.*;

/**
 * Created by cspark on 2015. 11. 25..
 */
public class UserDao {

    public void add(User user) throws ClassNotFoundException, SQLException {
        Connection conn = getConnection();

        PreparedStatement ps = conn.prepareStatement("INSERT INTO users(id, name, password) VALUES (?, ?, ?)");
        ps.setString(1, user.getId      ());
        ps.setString(2, user.getName    ());
        ps.setString(3, user.getPassword());

        ps.execute();

        ps.close();
        conn.close();
    }

    public User get(String id) throws ClassNotFoundException, SQLException {
        Connection conn = getConnection();

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();
        rs.next();
        User user = new User();
        user.setId      (rs.getString("id"      ));
        user.setName    (rs.getString("name"    ));
        user.setPassword(rs.getString("password"));

        rs.close();
        ps.close();
        conn.close();

        return user;
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao dao = new UserDao();

        User user = new User();
        user.setId      ("cspark");
        user.setName    ("cs 박"  );
        user.setPassword("1111"  );

        dao.add(user);

        System.out.println("등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName    ());
        System.out.println(user2.getPassword());

        System.out.println(user2.getId() + " 조회 성공");
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost//projects/h2data/books/toby", "sa", "");

        return conn;
    }

}
