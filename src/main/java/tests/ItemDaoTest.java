package tests;

import org.junit.Assert;
import org.junit.jupiter.api.*;
import schmacse.daos.ItemDao;
import schmacse.databaseconnection.DBConnection;
import schmacse.model.Category;
import schmacse.model.Item;

import java.sql.*;

public class ItemDaoTest {

    private final String[][] item_names = new String[][]{
            {"kenweroshi", "gatkbildebis", "TROUSERS"},
            {"toilet", "paper", "ISGOOD"},
            {"dont", "drink", "ANDDRIVE"},
            {"better", "call", "SAUL"},
            {"watch", "your", "TROUSERS"}
    };

    private static final String viewItems = "SELECT * FROM items";
    private static final String viewUsers = "SELECT * FROM users";
    private static final String clearItems = "DELETE FROM items";
    private static final String clearUsers = "DELETE FROM users";
    private static final String resetIncrementItems = "ALTER TABLE items AUTO_INCREMENT = 1";
    private static final String resetIncrementUsers = "ALTER TABLE users AUTO_INCREMENT = 1";

    private void addDummyUser(Connection connection) throws SQLException {

        // create user, to satisfy foreign key constraint (in items table)
        Statement stm = connection.createStatement();
        stm.executeUpdate("INSERT INTO " +
                "users (first_name, last_name, phone_number, username, password) " +
                "VALUES ('test','test','test','test', 'test')");

    }
    private void addDummyItem(Connection connection) throws SQLException{

        Statement stm = connection.createStatement();
        stm.executeUpdate(
                "INSERT INTO items (user_id, name, description, category)" +
                        "VALUES (1, 'item', 'nice', 'TROUSERS')"
        );


    }

    @Test
    public void testAddIDs() throws SQLException{

        Connection connection = DBConnection.getConnection();
        reset_db(connection);

        addDummyUser(connection);
        ItemDao itemDao = new ItemDao(connection);
        for(int i = 0; i < 60; i++){
            Item firstItem = new Item(i,1, Integer.toString(i), Integer.toString(i),
                    Category.TROUSERS);
            itemDao.add(firstItem);
        }
        PreparedStatement stm = connection.prepareStatement(viewItems);
        ResultSet resultSet = stm.executeQuery();

        int i = 0;
        while (resultSet.next()){
            i++;
            Assertions.assertEquals(i, resultSet.getInt(1));
            Assertions.assertEquals(1, resultSet.getInt(2));
        }
        Assertions.assertEquals(i, 60);

        reset_db(connection);
    }

    @Test
    public void testAddScenario() throws SQLException {

        Connection connection = DBConnection.getConnection();
        reset_db(connection);

        addDummyUser(connection);

        ItemDao itemDao = new ItemDao(connection);

        Item[] items = new Item[item_names.length];
        for(int i = 0; i < items.length; i++){
            items[i] = new Item(1,1, item_names[i][0],
                    item_names[i][1], Category.valueOf(item_names[i][2]));
        }

        // add items
        for (Item item : items) {
            itemDao.add(item);
        }

        PreparedStatement stm = connection.prepareStatement(viewItems);
        ResultSet resultSet = stm.executeQuery();

        int i = 0;
        while(resultSet.next()){
            Assertions.assertEquals(item_names[i][0], resultSet.getString(3));
            Assertions.assertEquals(item_names[i][1], resultSet.getString(4));
            Assertions.assertEquals(item_names[i][2], resultSet.getString(5));
            i++;
        }

        reset_db(connection);

    }

    @Test // tests if successfully removes item from database
    public void testRemoveOne() throws SQLException{

        Connection connection = DBConnection.getConnection();
        reset_db(connection);

        addDummyUser(connection);

        ItemDao itemDao = new ItemDao(connection);

        addDummyItem(connection);
        itemDao.remove(1); // because its first item

        Item secondItem = new Item(2, 1, "second", "good", Category.TROUSERS);
        itemDao.remove(secondItem); // check both ways to remove

        PreparedStatement stm = connection.prepareStatement(viewItems);
        ResultSet resultSet = stm.executeQuery();

        Assertions.assertFalse(resultSet.next());

        reset_db(connection);

    }

    @Test // tests if does not reset autoincrement
    public void testRemoveIncrement() throws SQLException{

        Connection connection = DBConnection.getConnection();
        reset_db(connection);

        addDummyUser(connection);

        ItemDao itemDao = new ItemDao(connection);
        addDummyItem(connection);
        itemDao.remove(1);

        for(int i = 0; i < 5; i++){ addDummyItem(connection); } // should add id-s from 2 to 6

        PreparedStatement stm1 = connection.prepareStatement(viewItems);
        ResultSet resultSet = stm1.executeQuery();

        int j = 2;
        while (resultSet.next()){
            Assertions.assertEquals(j, resultSet.getInt(1));
            j++;
        }

        itemDao.remove(2);
        itemDao.remove(4);

        for(int i = 0; i < 3; i++){ addDummyItem(connection); }
        while (resultSet.next()){
            Assertions.assertEquals(j, resultSet.getInt(1));
            j++;
        }

        reset_db(connection);

    }

    private void reset_db(Connection connection) throws SQLException {

        // deletes rows in items
        Statement stm = connection.createStatement();

        stm.addBatch(clearItems); // clear rows in items table
        stm.addBatch(clearUsers); // clear rows in users table
        stm.addBatch(resetIncrementItems); // reset auto_increment in items
        stm.addBatch(resetIncrementUsers); // reset auto_increment in users

        stm.executeBatch();

    }

}

