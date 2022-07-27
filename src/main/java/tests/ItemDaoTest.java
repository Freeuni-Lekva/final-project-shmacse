package tests;

import org.junit.jupiter.api.*;
import schmacse.daos.ItemDao;
import schmacse.databaseconnection.DBConnection;
import schmacse.model.Category;
import schmacse.model.Item;

import java.sql.*;
import java.util.List;

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

    @Test
    public void testGetItemsByUsername() throws SQLException {
        Connection connection = DBConnection.getConnection();

        ItemDao itemDao = new ItemDao(connection);

        Statement statement = connection.createStatement();

        statement.executeUpdate("INSERT INTO users (id, first_name, last_name, phone_number, username, password) " +
                "VALUES (1, 'name1', 'last_name1', 123, 'username1', 'password1')");

        statement.executeUpdate("INSERT INTO users (id, first_name, last_name, phone_number, username, password) " +
                "VALUES (2, 'name2', 'last_name2', 456, 'username2', 'password2')");

        statement.executeUpdate(String.format("INSERT INTO items (id, user_id, name, description, category) " +
                "VALUES (1, 2, '%s', '%s', '%s')", item_names[0][0], item_names[0][1], item_names[0][2]));

        statement.executeUpdate(String.format("INSERT INTO items (id, user_id, name, description, category) " +
                "VALUES (2, 2, '%s', '%s', '%s')", item_names[1][0], item_names[1][1], item_names[1][2]));

        statement.executeUpdate(String.format("INSERT INTO items (id, user_id, name, description, category) " +
                "VALUES (3, 1, '%s', '%s', '%s')", item_names[2][0], item_names[2][1], item_names[2][2]));

        statement.executeUpdate(String.format("INSERT INTO items (id, user_id, name, description, category) " +
                "VALUES (4, 2, '%s', '%s', '%s')", item_names[3][0], item_names[3][1], item_names[3][2]));

        statement.executeUpdate(String.format("INSERT INTO items (id, user_id, name, description, category) " +
                "VALUES (5, 1, '%s', '%s', '%s')", item_names[4][0], item_names[4][1], item_names[4][2]));

        List<Item> itemListForUsername1 = itemDao.getItemsByUsername("username1");
        List<Item> itemListForUsername2 = itemDao.getItemsByUsername("username2");

        Assertions.assertEquals(2, itemListForUsername1.size());
        Assertions.assertEquals(3, itemListForUsername2.size());

        Item item3 = itemListForUsername1.get(0);

        Assertions.assertEquals(3, item3.getId());
        Assertions.assertEquals(1, item3.getUserId());
        Assertions.assertEquals(item_names[2][0], item3.getName());
        Assertions.assertEquals(item_names[2][1], item3.getDescription());
        Assertions.assertEquals(Category.valueOf(item_names[2][2]), item3.getCategory());

        Item item5 = itemListForUsername1.get(1);

        Assertions.assertEquals(5, item5.getId());
        Assertions.assertEquals(1, item5.getUserId());
        Assertions.assertEquals(item_names[4][0], item5.getName());
        Assertions.assertEquals(item_names[4][1], item5.getDescription());
        Assertions.assertEquals(Category.valueOf(item_names[4][2]), item5.getCategory());

        Item item1 = itemListForUsername2.get(0);

        Assertions.assertEquals(1, item1.getId());
        Assertions.assertEquals(2, item1.getUserId());
        Assertions.assertEquals(item_names[0][0], item1.getName());
        Assertions.assertEquals(item_names[0][1], item1.getDescription());
        Assertions.assertEquals(Category.valueOf(item_names[0][2]), item1.getCategory());

        Item item2 = itemListForUsername2.get(1);

        Assertions.assertEquals(2, item2.getId());
        Assertions.assertEquals(2, item2.getUserId());
        Assertions.assertEquals(item_names[1][0], item2.getName());
        Assertions.assertEquals(item_names[1][1], item2.getDescription());
        Assertions.assertEquals(Category.valueOf(item_names[1][2]), item2.getCategory());

        Item item4 = itemListForUsername2.get(2);

        Assertions.assertEquals(4, item4.getId());
        Assertions.assertEquals(2, item4.getUserId());
        Assertions.assertEquals(item_names[3][0], item4.getName());
        Assertions.assertEquals(item_names[3][1], item4.getDescription());
        Assertions.assertEquals(Category.valueOf(item_names[3][2]), item4.getCategory());

        statement.executeUpdate("DELETE FROM items WHERE id = 1");
        statement.executeUpdate("DELETE FROM items WHERE id = 2");
        statement.executeUpdate("DELETE FROM items WHERE id = 3");
        statement.executeUpdate("DELETE FROM items WHERE id = 4");
        statement.executeUpdate("DELETE FROM items WHERE id = 5");

        statement.executeUpdate("DELETE FROM users WHERE id = 1");
        statement.executeUpdate("DELETE FROM users WHERE id = 2");


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

