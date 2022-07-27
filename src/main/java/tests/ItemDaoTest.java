package tests;

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

    private void  addDummyUser(Connection connection) throws SQLException {

        // create user, to satisfy foreign key constraint (in items table)
        Statement stm = connection.createStatement();
        stm.executeUpdate("INSERT INTO " +
                "users (first_name, last_name, phone_number, username, password) " +
                "VALUES ('test','test','test','test', 'test')");

    }

    @Test
    public void testAddIDs() throws SQLException{

        Connection connection = DBConnection.getConnection();
        reset_db(connection);

        addDummyUser(connection);
        ItemDao itemDao = new ItemDao(connection);
        for(int i = 0; i < 60; i++){
            Item firstItem = new Item(i,1, item_names[i%5][0], item_names[i%5][1],
                    Category.valueOf(item_names[i%5][2]));
            itemDao.add(firstItem);
        }
        PreparedStatement stm = connection.prepareStatement("SELECT * FROM items");
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

        PreparedStatement stm = connection.prepareStatement("SELECT * FROM items");
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

