package com.cspark.books.toby;

import java.sql.SQLException;

/**
 * Created by cspark on 2015. 11. 30..
 */
public class UserDaoTest {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao dao = new DaoFactory().userDao();

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
}