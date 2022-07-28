package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import schmacse.daos.UserDao;
import schmacse.databaseconnection.DBConnection;
import schmacse.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoTest {

    private Connection connection = DBConnection.getConnection();

    private final String[][] usersData = new String[][]{
            {"iosebi", "chkhikvadze", "xutixutishvidi", "ioska", "zamtrisusafrtxoeba"},
            {"nika", "sharmazanashvili", "xutiertierti", "sharma", "sharmalozi"},
            {"gocha", "gulua", "xuticxracxra", "gogulua", "raviaba"},
            {"daviti", "xarshiladze", "xutishvidierti", "xarshila", "kenwero"},
            {"rolandi", "pkhakadze", "xuticxrarva", "rolandiko", "paroliko"}
    };

    @Test
    void testGetUserByUsernameAndPassword() throws SQLException {
        UserDao userDao = new UserDao(connection);

        for(int i = 0; i < 5; i++){
            User user = new User(101 + i,usersData[i][0],usersData[i][1],usersData[i][2],usersData[i][3],usersData[i][4]);
            userDao.add(user);
        }

        for(int i = 0; i < usersData.length; i++){
            User user = userDao.getUserByUsernameAndPassword(usersData[i][3], usersData[i][4]);
            Assertions.assertNotNull(user);
            Assertions.assertEquals(user.getFirstName(), usersData[i][0]);
            Assertions.assertEquals(user.getLastName(), usersData[i][1]);
            Assertions.assertEquals(user.getPhoneNumber(), usersData[i][2]);
            Assertions.assertEquals(user.getUsername(), usersData[i][3]);
            Assertions.assertEquals(user.getPassword(), usersData[i][4]);
        }

        Assertions.assertNull(userDao.getUserByUsernameAndPassword("foo", "foo"));

        reset_db(connection);
    }

    @Test
    void testGetUserById() throws SQLException {
        UserDao userDao = new UserDao(connection);
        reset_db(connection);
        for(int i = 0; i < usersData.length; i++){
            User user = new User(i,usersData[i][0],usersData[i][1],usersData[i][2],usersData[i][3],usersData[i][4]);
            userDao.add(user);
        }

        for(int i = 0; i < usersData.length; i++){
            User user = userDao.getUserById(i+1);
            Assertions.assertNotNull(user);
            Assertions.assertEquals(user.getFirstName(), usersData[i][0]);
            Assertions.assertEquals(user.getLastName(), usersData[i][1]);
            Assertions.assertEquals(user.getPhoneNumber(), usersData[i][2]);
            Assertions.assertEquals(user.getUsername(), usersData[i][3]);
            Assertions.assertEquals(user.getPassword(), usersData[i][4]);
        }

        Assertions.assertNull(userDao.getUserById(1917));

       reset_db(connection);
    }


    private void reset_db(Connection connection) throws SQLException {

        // deletes rows in items
        Statement stm = connection.createStatement();

        stm.addBatch("DELETE FROM items"); // clear rows in items table
        stm.addBatch("DELETE FROM users"); // clear rows in users table
        stm.addBatch("ALTER TABLE items AUTO_INCREMENT = 1"); // reset auto_increment in items
        stm.addBatch("ALTER TABLE users AUTO_INCREMENT = 1"); // reset auto_increment in users

        stm.executeBatch();

    }
}
