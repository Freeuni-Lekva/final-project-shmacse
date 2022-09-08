package tests;

import org.junit.jupiter.api.*;
import schmacse.daos.ItemDao;
import schmacse.daos.UserDao;
import schmacse.databaseconnection.DBConnection;
import schmacse.model.Category;
import schmacse.model.Item;
import schmacse.model.User;

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

    private final String[][] usersData = new String[][]{
            {"iosebi", "chkhikvadze", "xutixutishvidi", "ioska", "zamtrisusafrtxoeba"},
            {"nika", "sharmazanashvili", "xutiertierti", "sharma", "sharmalozi"},
            {"gocha", "gulua", "xuticxracxra", "gogulua", "raviaba"},
            {"daviti", "xarshiladze", "xutishvidierti", "xarshila", "kenwero"},
            {"rolandi", "pkhakadze", "xuticxrarva", "rolandiko", "paroliko"}
    };


    private static final String selectItems = "SELECT * FROM items";
    private static final String selectUsers = "SELECT * FROM users";
    private static final String clearItems = "DELETE FROM items";
    private static final String clearUsers = "DELETE FROM users";
    private static final String resetIncrementItems = "ALTER TABLE items AUTO_INCREMENT = 1";
    private static final String resetIncrementUsers = "ALTER TABLE users AUTO_INCREMENT = 1";
    private static final String clearWishlist = "DELETE FROM wishlist";

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
                "INSERT INTO items (user_id, name, price, description, category, image_id)" +
                        "VALUES (1, 'item', 1, 'nice', 'TROUSERS', 0)"
        );


    }
    private void addDummyItem(Connection connection, int userID) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(
                "INSERT INTO items (user_id, name, price, description, category, image_id)" +
                "VALUES (?, 'item', 1, 'nice', 'TROUSERS', 0)");
        stm.setInt(1, userID);
        stm.executeUpdate();

    }

    private void testAddIDs() throws SQLException{

        Connection connection = DBConnection.getConnection();
        reset_db(connection);

        addDummyUser(connection);
        ItemDao itemDao = new ItemDao(connection);
        for(int i = 0; i < 60; i++){
            Item firstItem = new Item(i,1, Integer.toString(i), 1, Integer.toString(i),
                    Category.TROUSERS, 0);
            itemDao.add(firstItem);
        }
        PreparedStatement stm = connection.prepareStatement(selectItems);
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
    private void testAddScenario() throws SQLException {

        Connection connection = DBConnection.getConnection();
        reset_db(connection);

        addDummyUser(connection);

        ItemDao itemDao = new ItemDao(connection);

        Item[] items = new Item[item_names.length];
        for(int i = 0; i < items.length; i++){
            items[i] = new Item(1,1, item_names[i][0], i,
                    item_names[i][1], Category.valueOf(item_names[i][2]), 0);
        }

        // add items
        for (Item item : items) {
            itemDao.add(item);
        }

        PreparedStatement stm = connection.prepareStatement(selectItems);
        ResultSet resultSet = stm.executeQuery();

        int i = 0;
        while(resultSet.next()){
            Assertions.assertEquals(item_names[i][0], resultSet.getString("name"));
            Assertions.assertEquals(i, resultSet.getInt("price"));
            Assertions.assertEquals(item_names[i][1], resultSet.getString("description"));
            Assertions.assertEquals(item_names[i][2], resultSet.getString("category"));
            i++;
        }

        reset_db(connection);

    }


    private void testRemoveOne() throws SQLException{
        // tests if successfully removes item from database
        Connection connection = DBConnection.getConnection();
        reset_db(connection);

        addDummyUser(connection);

        ItemDao itemDao = new ItemDao(connection);

        addDummyItem(connection);
        itemDao.remove(1); // because its first item

        Item secondItem = new Item(2, 1, "second", 2, "good", Category.TROUSERS, 0);
        itemDao.remove(secondItem); // check both ways to remove

        PreparedStatement stm = connection.prepareStatement(selectItems);
        ResultSet resultSet = stm.executeQuery();

        Assertions.assertFalse(resultSet.next());

        reset_db(connection);

    }
    private void testRemoveIncrement() throws SQLException{
        // tests if does not reset autoincrement
        Connection connection = DBConnection.getConnection();
        reset_db(connection);

        addDummyUser(connection);

        ItemDao itemDao = new ItemDao(connection);
        addDummyItem(connection);
        itemDao.remove(1);

        for(int i = 0; i < 5; i++){ addDummyItem(connection); } // should add id-s from 2 to 6

        PreparedStatement stm1 = connection.prepareStatement(selectItems);
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


    private void testRemoveByUserIDOne() throws SQLException{
        // remove single item by userID
        Connection connection = DBConnection.getConnection();
        ItemDao itemDao = new ItemDao(connection);

        reset_db(connection);

        addDummyUser(connection);
        addDummyItem(connection, 1);
        itemDao.removeByUserID(1);

        PreparedStatement stm = connection.prepareStatement(selectItems);
        ResultSet resultSet = stm.executeQuery();

        Assertions.assertFalse(resultSet.next());

        reset_db(connection);

    }
    private void testRemoveByUserIDMultiple() throws SQLException{
        // test if removes all of the items with certain userID
        Connection connection = DBConnection.getConnection();
        ItemDao itemDao = new ItemDao(connection);

        reset_db(connection);

        // create 3 different users with 1-3 userID-s
        addDummyUser(connection);
        addDummyUser(connection);
        addDummyUser(connection);

        for(int i = 0; i < 13; i++){addDummyItem(connection, 1);}
        for(int i = 0; i < 3; i++){addDummyItem(connection, 2);}
        for(int i = 0; i < 20; i++){addDummyItem(connection, 3);}

        itemDao.removeByUserID(2);

        PreparedStatement stm = connection.prepareStatement(selectItems);
        ResultSet resultSet = stm.executeQuery();

        int j = 0;
        while(resultSet.next()){
            j++;
            if(j <= 13) Assertions.assertEquals(resultSet.getInt(2),1);
            if(j > 13) Assertions.assertEquals(resultSet.getInt(2),3);
        }

        itemDao.removeByUserID(1);

        resultSet = stm.executeQuery();

        while(resultSet.next()){
            Assertions.assertEquals(resultSet.getInt(2), 3);
        }

        itemDao.removeByUserID(3);
        resultSet = stm.executeQuery();
        Assertions.assertFalse(resultSet.next());

        reset_db(connection);
    }

    @Test
    public void testgetItemByItemID() throws SQLException{

        Connection connection = DBConnection.getConnection();
        reset_db(connection);

        ItemDao itemDao = new ItemDao(connection);
        addDummyUser(connection);

        itemDao.add(new Item(1, 1, item_names[0][0], 1, item_names[0][1],
                Category.valueOf(item_names[0][2]), 0));
        itemDao.add(new Item(2, 1, item_names[1][0], 2, item_names[1][1],
                Category.valueOf(item_names[1][2]), 0));
        itemDao.add(new Item(3, 1, item_names[2][0], 3, item_names[2][1],
                Category.valueOf(item_names[2][2]), 0));
        itemDao.add(new Item(4, 1, item_names[3][0], 4, item_names[3][1],
                Category.valueOf(item_names[3][2]), 0));
        itemDao.add(new Item(5, 1, item_names[4][0], 5, item_names[4][1],
                Category.valueOf(item_names[4][2]), 0));

        PreparedStatement stm = connection.prepareStatement(selectItems);
        ResultSet resultSet = stm.executeQuery();

        for(int i = 1; i <= 5; i++) {
            resultSet.next();
            Item item = itemDao.getItemByItemID(i);
            Assertions.assertEquals(resultSet.getInt("user_id"), item.getUserId()); // userID
            Assertions.assertEquals(resultSet.getString("name"), item.getName()); // name
            Assertions.assertEquals(i, resultSet.getInt("price"));
            Assertions.assertEquals(resultSet.getString("description"), item.getDescription()); // description
            Assertions.assertEquals(Category.valueOf(resultSet.getString("category")), item.getCategory()); // category
        }

        reset_db(connection);
    }

    @Test
    public void testgetUserIDByItemID() throws SQLException{

        Connection connection = DBConnection.getConnection();
        reset_db(connection);

        UserDao userDao = new UserDao(connection);
        ItemDao itemDao = new ItemDao(connection);

        for (String[] usersDatum : usersData) {
            userDao.add(new User(0, usersDatum[0], usersDatum[1],
                    usersDatum[2], usersDatum[3], usersDatum[4]));
        }

        itemDao.add(new Item(0, 2, item_names[0][0], 4, item_names[0][1],
                Category.valueOf(item_names[0][2]), 0));
        itemDao.add(new Item(0, 4, item_names[1][0], 3, item_names[1][1],
                Category.valueOf(item_names[1][2]), 0));
        itemDao.add(new Item(0, 3, item_names[2][0], 6, item_names[2][1],
                Category.valueOf(item_names[2][2]), 0));
        itemDao.add(new Item(0, 1, item_names[3][0], 12, item_names[3][1],
                Category.valueOf(item_names[3][2]), 0));
        itemDao.add(new Item(0, 5, item_names[4][0], 5, item_names[4][1],
                Category.valueOf(item_names[4][2]), 0));

        Assertions.assertEquals(itemDao.getUserIDByItemID(1), 2);
        Assertions.assertEquals(itemDao.getUserIDByItemID(2), 4);
        Assertions.assertEquals(itemDao.getUserIDByItemID(3), 3);
        Assertions.assertEquals(itemDao.getUserIDByItemID(4), 1);
        Assertions.assertEquals(itemDao.getUserIDByItemID(5), 5);

        reset_db(connection);
    }

    @Test
    public void testAdd() throws SQLException {
        testAddIDs();
        testAddScenario();
    }

    @Test
    public void testRemove() throws SQLException{
        testRemoveOne();
        testRemoveIncrement();
    }

    @Test
    public void testRemoveByUserID() throws SQLException{
        testRemoveByUserIDOne();
        testRemoveByUserIDMultiple();
    }

    @Test
    public void testGetItemsByUsername() throws SQLException {

        Connection connection = DBConnection.getConnection();

        ItemDao itemDao = new ItemDao(connection);

        Statement statement = connection.createStatement();

        statement.executeUpdate("INSERT INTO users (id, first_name, last_name, phone_number, username, password) " +
                "VALUES (1, 'name1', 'lastname1', '123', 'username1', 'password1')");

        statement.executeUpdate("INSERT INTO users (id, first_name, last_name, phone_number, username, password) " +
                "VALUES (2, 'name2', 'lastname2', '456', 'username2', 'password2')");

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

        List<Item> itemList1 = itemDao.getItemsByUsername("username1");
        List<Item> itemList2 = itemDao.getItemsByUsername("username2");

        Item item3 = itemList1.get(0);

        Assertions.assertEquals(3, item3.getId());
        Assertions.assertEquals(1, item3.getUserId());
        Assertions.assertEquals(item_names[2][0], item3.getName());
        Assertions.assertEquals(item_names[2][1], item3.getDescription());
        Assertions.assertEquals(Category.valueOf(item_names[2][2]), item3.getCategory());

        Item item5 = itemList1.get(1);

        Assertions.assertEquals(5, item5.getId());
        Assertions.assertEquals(1, item5.getUserId());
        Assertions.assertEquals(item_names[4][0], item5.getName());
        Assertions.assertEquals(item_names[4][1], item5.getDescription());
        Assertions.assertEquals(Category.valueOf(item_names[4][2]), item5.getCategory());

        Item item1 = itemList2.get(0);

        Assertions.assertEquals(1, item1.getId());
        Assertions.assertEquals(2, item1.getUserId());
        Assertions.assertEquals(item_names[0][0], item1.getName());
        Assertions.assertEquals(item_names[0][1], item1.getDescription());
        Assertions.assertEquals(Category.valueOf(item_names[0][2]), item1.getCategory());

        Item item2 = itemList2.get(1);

        Assertions.assertEquals(2, item2.getId());
        Assertions.assertEquals(2, item2.getUserId());
        Assertions.assertEquals(item_names[1][0], item2.getName());
        Assertions.assertEquals(item_names[1][1], item2.getDescription());
        Assertions.assertEquals(Category.valueOf(item_names[1][2]), item2.getCategory());

        Item item4 = itemList2.get(2);

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

    @Test
    public void testGetItemsForUserInWishlist() throws SQLException {
        Connection connection = DBConnection.getConnection();

        ItemDao itemDao = new ItemDao(connection);

        Statement statement = connection.createStatement();

        statement.executeUpdate("INSERT INTO users (id, first_name, last_name, phone_number, username, password) " +
                "VALUES (1, 'name1', 'lastname1', '123', 'username1', 'password1')");
        statement.executeUpdate("INSERT INTO users (id, first_name, last_name, phone_number, username, password) " +
                "VALUES (2, 'name2', 'lastname2', '456', 'username2', 'password2')");

        statement.executeUpdate(String.format("INSERT INTO items (id, user_id, name, price, description, category, image_id) " +
                "VALUES (1, 2, '%s', 4, '%s', '%s', 0)", item_names[0][0], item_names[0][1], item_names[0][2]));
        statement.executeUpdate(String.format("INSERT INTO items (id, user_id, name, price, description, category, image_id) " +
                "VALUES (2, 2, '%s', 6, '%s', '%s', 0)", item_names[1][0], item_names[1][1], item_names[1][2]));
        statement.executeUpdate(String.format("INSERT INTO items (id, user_id, name, price, description, category, image_id) " +
                "VALUES (3, 1, '%s', 5, '%s', '%s', 0)", item_names[2][0], item_names[2][1], item_names[2][2]));
        statement.executeUpdate(String.format("INSERT INTO items (id, user_id, name, price, description, category, image_id) " +
                "VALUES (4, 2, '%s', 14, '%s', '%s', 0)", item_names[3][0], item_names[3][1], item_names[3][2]));
        statement.executeUpdate(String.format("INSERT INTO items (id, user_id, name, price, description, category, image_id) " +
                "VALUES (5, 1, '%s', 24, '%s', '%s', 0)", item_names[4][0], item_names[4][1], item_names[4][2]));

        statement.executeUpdate("INSERT INTO wishlist (user_id, item_id) " +
                "VALUES (1, 1)");
        statement.executeUpdate("INSERT INTO wishlist (user_id, item_id) " +
                "VALUES (1, 4)");
        statement.executeUpdate("INSERT INTO wishlist (user_id, item_id) " +
                "VALUES (2, 3)");

        List<Item> itemsInWishlist1 = itemDao.getItemsForUserInWishlist(1);
        List<Item> itemsInWishlist2 = itemDao.getItemsForUserInWishlist(2);

        Assertions.assertEquals(2, itemsInWishlist1.size());
        Assertions.assertEquals(2, itemsInWishlist1.get(0).getUserId());
        Assertions.assertEquals(2, itemsInWishlist1.get(1).getUserId());

        Assertions.assertEquals(1, itemsInWishlist2.size());
        Assertions.assertEquals(1, itemsInWishlist2.get(0).getUserId());

        reset_db(connection);
    }

    private void reset_db(Connection connection) throws SQLException {

        // deletes rows in items
        Statement stm = connection.createStatement();

        stm.addBatch(clearWishlist);
        stm.addBatch(clearItems); // clear rows in items table
        stm.addBatch(clearUsers); // clear rows in users table
        stm.addBatch(resetIncrementItems); // reset auto_increment in items
        stm.addBatch(resetIncrementUsers); // reset auto_increment in users

        stm.executeBatch();

    }

}

