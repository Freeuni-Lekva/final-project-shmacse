package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import schmacse.daos.WishListDao;
import schmacse.databaseconnection.DBConnection;

import java.sql.*;

public class WishListDaoTest {

    private static final String SELECT_ITEMS = "SELECT * FROM items";
    private static final String SELECT_USERS = "SELECT * FROM users";
    private static final String SELECT_WISHLIST = "SELECT * FROM wishlist";
    private static final String CLEAR_ITEMS = "DELETE FROM items";
    private static final String CLEAR_USERS = "DELETE FROM users";
    private static final String CLEAR_WISHLIST = "DELETE FROM wishlist";
    private static final String RESET_INCREMENT_ITEMS = "ALTER TABLE items AUTO_INCREMENT = 1";
    private static final String RESET_INCREMENT_USERS = "ALTER TABLE users AUTO_INCREMENT = 1";


    private void addDummyUser(Connection connection) throws SQLException {
        // create user, to satisfy foreign key constraint (in items table)
        Statement stm = connection.createStatement();
        stm.executeUpdate("INSERT INTO " +
                "users (first_name, last_name, phone_number, username, password) " +
                "VALUES ('dummy','dummy','dummy','dummy', 'dummy')");
    }
    private void addDummyItem(Connection connection, int userID) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(
                "INSERT INTO items (user_id, name, price, description, category)" +
                        "VALUES (?, 'item', 1, 'nice', 'TROUSERS')");
        stm.setInt(1, userID);
        stm.executeUpdate();
    }

    @Test
    public void testAdd() throws SQLException {

        Connection connection = DBConnection.getConnection();
        reset_db(connection);

        // userIDs - 1, 2, 3 posses itemIds - 1, 2, 3
        addDummyUser(connection);
        addDummyUser(connection);
        addDummyUser(connection);

        addDummyItem(connection, 1);
        addDummyItem(connection, 2);
        addDummyItem(connection, 3);

        WishListDao wishListDao = new WishListDao(connection);
        Assertions.assertFalse(wishListDao.add(1,1));
        Assertions.assertFalse(wishListDao.add(2,2));
        Assertions.assertFalse(wishListDao.add(3,3));

        Assertions.assertTrue(wishListDao.add(1,3));
        Assertions.assertTrue(wishListDao.add(1,2));
        Assertions.assertTrue(wishListDao.add(2,3));
        Assertions.assertTrue(wishListDao.add(2,1));
        Assertions.assertTrue(wishListDao.add(3,2));
        Assertions.assertTrue(wishListDao.add(3,1));

        PreparedStatement stm = connection.prepareStatement(SELECT_WISHLIST);
        ResultSet resultSet = stm.executeQuery();

        resultSet.next();
        Assertions.assertEquals(1, resultSet.getInt(1));
        Assertions.assertEquals(3, resultSet.getInt(2));
        resultSet.next();
        Assertions.assertEquals(1, resultSet.getInt(1));
        Assertions.assertEquals(2, resultSet.getInt(2));
        resultSet.next();
        Assertions.assertEquals(2, resultSet.getInt(1));
        Assertions.assertEquals(3, resultSet.getInt(2));
        resultSet.next();
        Assertions.assertEquals(2, resultSet.getInt(1));
        Assertions.assertEquals(1, resultSet.getInt(2));
        resultSet.next();
        Assertions.assertEquals(3, resultSet.getInt(1));
        Assertions.assertEquals(2, resultSet.getInt(2));
        resultSet.next();
        Assertions.assertEquals(3, resultSet.getInt(1));
        Assertions.assertEquals(1, resultSet.getInt(2));


        reset_db(connection);

    }

    @Test
    public void testRemove() throws SQLException{

        Connection connection = DBConnection.getConnection();
        reset_db(connection);

        // userIDs - 1, 2, 3 posses itemIds - 3, 2, 1
        addDummyUser(connection);
        addDummyUser(connection);
        addDummyUser(connection);

        addDummyItem(connection, 3);
        addDummyItem(connection, 2);
        addDummyItem(connection, 1);

        WishListDao wishListDao = new WishListDao(connection);
        wishListDao.add(1,1); // add tests already passed
        wishListDao.add(1,2);
        wishListDao.add(3,3);
        wishListDao.add(3,2);

        wishListDao.remove(3,3);
        wishListDao.remove(1,1);

        PreparedStatement stm = connection.prepareStatement(SELECT_WISHLIST);
        ResultSet resultSet = stm.executeQuery();

        resultSet.next();
        Assertions.assertEquals(1, resultSet.getInt(1));
        Assertions.assertEquals(2, resultSet.getInt(2));
        resultSet.next();
        Assertions.assertEquals(3, resultSet.getInt(1));
        Assertions.assertEquals(2, resultSet.getInt(2));

        reset_db(connection);

    }

    @Test
    public void testRemoveRowOfUser() throws SQLException{

        Connection connection = DBConnection.getConnection();
        reset_db(connection);

        addDummyUser(connection); // #1
        addDummyUser(connection); // #2
        addDummyUser(connection); // #3
        addDummyUser(connection); // #4

        addDummyItem(connection, 4); // #1
        addDummyItem(connection, 1); // #2
        addDummyItem(connection, 2); // #3
        addDummyItem(connection, 1); // #4
        addDummyItem(connection, 2); // #5
        addDummyItem(connection, 3); // #6
        addDummyItem(connection, 2); // #7
        addDummyItem(connection, 1); // #8
        addDummyItem(connection, 3); // #9

        WishListDao wishListDao = new WishListDao(connection);

        wishListDao.add(3,1);
        wishListDao.add(4,2);
        wishListDao.add(4,3);
        wishListDao.add(2,4);
        wishListDao.add(4,5);
        wishListDao.add(4,2);
        wishListDao.add(3,7);
        wishListDao.add(4,8);
        wishListDao.add(1,9);

        wishListDao.removeRowsOfUser(4);

        PreparedStatement stm = connection.prepareStatement(SELECT_WISHLIST);
        ResultSet resultSet = stm.executeQuery();

        resultSet.next();
        Assertions.assertEquals(3, resultSet.getInt(1));
        Assertions.assertEquals(1, resultSet.getInt(2));
        resultSet.next();
        Assertions.assertEquals(2, resultSet.getInt(1));
        Assertions.assertEquals(4, resultSet.getInt(2));
        resultSet.next();
        Assertions.assertEquals(3, resultSet.getInt(1));
        Assertions.assertEquals(7, resultSet.getInt(2));
        resultSet.next();
        Assertions.assertEquals(1, resultSet.getInt(1));
        Assertions.assertEquals(9, resultSet.getInt(2));

        wishListDao.removeRowsOfUser(3);

        stm = connection.prepareStatement(SELECT_WISHLIST);
        resultSet = stm.executeQuery();

        resultSet.next();
        Assertions.assertEquals(2, resultSet.getInt(1));
        Assertions.assertEquals(4, resultSet.getInt(2));
        resultSet.next();
        Assertions.assertEquals(1, resultSet.getInt(1));
        Assertions.assertEquals(9, resultSet.getInt(2));

        wishListDao.removeRowsOfUser(2);

        stm = connection.prepareStatement(SELECT_WISHLIST);
        resultSet = stm.executeQuery();

        resultSet.next();
        Assertions.assertEquals(1, resultSet.getInt(1));
        Assertions.assertEquals(9, resultSet.getInt(2));

        wishListDao.removeRowsOfUser(1);

        stm = connection.prepareStatement(SELECT_WISHLIST);
        resultSet = stm.executeQuery();

        Assertions.assertFalse(resultSet.next());

        reset_db(connection);
    }

    @Test
    public void testRemoveRowOfItem() throws SQLException{

        Connection connection = DBConnection.getConnection();
        reset_db(connection);

        addDummyUser(connection); // #1
        addDummyUser(connection); // #2
        addDummyUser(connection); // #3
        addDummyUser(connection); // #4

        addDummyItem(connection, 4); // #1
        addDummyItem(connection, 1); // #2
        addDummyItem(connection, 2); // #3
        addDummyItem(connection, 1); // #4
        addDummyItem(connection, 2); // #5
        addDummyItem(connection, 3); // #6
        addDummyItem(connection, 2); // #7
        addDummyItem(connection, 1); // #8
        addDummyItem(connection, 3); // #9

        WishListDao wishListDao = new WishListDao(connection);

        wishListDao.add(1,5);
        wishListDao.add(3,5);
        wishListDao.add(4,5);
        wishListDao.add(4,2);
        wishListDao.add(3,7);
        wishListDao.add(4,9);
        wishListDao.add(1,9);

        wishListDao.removeRowsOfItem(5);

        PreparedStatement stm = connection.prepareStatement(SELECT_WISHLIST);
        ResultSet resultSet = stm.executeQuery();

        resultSet.next();
        Assertions.assertEquals(4, resultSet.getInt(1));
        Assertions.assertEquals(2, resultSet.getInt(2));
        resultSet.next();
        Assertions.assertEquals(3, resultSet.getInt(1));
        Assertions.assertEquals(7, resultSet.getInt(2));
        resultSet.next();
        Assertions.assertEquals(4, resultSet.getInt(1));
        Assertions.assertEquals(9, resultSet.getInt(2));
        resultSet.next();
        Assertions.assertEquals(1, resultSet.getInt(1));
        Assertions.assertEquals(9, resultSet.getInt(2));

        wishListDao.removeRowsOfItem(9);

        stm = connection.prepareStatement(SELECT_WISHLIST);
        resultSet = stm.executeQuery();

        resultSet.next();
        Assertions.assertEquals(4, resultSet.getInt(1));
        Assertions.assertEquals(2, resultSet.getInt(2));
        resultSet.next();
        Assertions.assertEquals(3, resultSet.getInt(1));
        Assertions.assertEquals(7, resultSet.getInt(2));

        wishListDao.removeRowsOfItem(7);

        stm = connection.prepareStatement(SELECT_WISHLIST);
        resultSet = stm.executeQuery();

        resultSet.next();
        Assertions.assertEquals(4, resultSet.getInt(1));
        Assertions.assertEquals(2, resultSet.getInt(2));

        wishListDao.removeRowsOfItem(2);

        stm = connection.prepareStatement(SELECT_WISHLIST);
        resultSet = stm.executeQuery();

        Assertions.assertFalse(resultSet.next());

        reset_db(connection);

    }


    private void reset_db(Connection connection) throws SQLException {

        // deletes rows in items
        Statement stm = connection.createStatement();

        stm.addBatch(CLEAR_WISHLIST); // clears wishlist

        stm.addBatch(CLEAR_ITEMS); // clear rows in items table
        stm.addBatch(CLEAR_USERS); // clear rows in users table
        stm.addBatch(RESET_INCREMENT_ITEMS); // reset auto_increment in items
        stm.addBatch(RESET_INCREMENT_USERS); // reset auto_increment in users

        stm.executeBatch();

    }

}
