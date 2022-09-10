package schmacse.daos;

import schmacse.model.Category;
import schmacse.model.Item;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemDao {

    private static final String SELECT_ITEMS_OF_USER_TO_SELL = "SELECT * FROM items " +
            "JOIN users WHERE users.id = items.user_id AND users.username = ?";
    private static final String DELETE_ITEMS_WITH_USER_ID = "" +
            "DELETE FROM items WHERE user_id = ?";
    private static final String SELECT_ITEMS_WITH_ID = "SELECT * FROM items " +
            "WHERE id = ?";

    private static final String UPDATE_PRICE = "UPDATE items SET price = ? WHERE id = ?";
    private static final String UPDATE_NAME = "UPDATE items SET name = ? WHERE id = ?";
    private static final String UPDATE_DESCRIPTION = "UPDATE items SET description = ? WHERE id = ?";
    private static final String UPDATE_CATEGORY = "UPDATE items SET category = ? WHERE id = ?";
    private static final String UPDATE_IMAGE_ID = "UPDATE items SET image_id = ? WHERE id = ?";

    private static final String SELECT_ITEMS_FOR_USER_IN_WISHLIST = "SELECT DISTINCT " +
            "items.id, items.user_id, items.name, items.price, items.description, items.category, items.image_id FROM items " +
            "JOIN wishlist ON items.id = wishlist.item_id AND wishlist.user_id = ?";
    private static final String SELECT_FILTERED_ITEMS = "SELECT * FROM items " +
            "WHERE name LIKE CONCAT('%', ?, '%') AND category LIKE CONCAT('%', ?, '%')";

    private Connection connection;

    public ItemDao(Connection connection){
        this.connection = connection;
    }

    public void changeImageID(int itemId, int imageId) throws SQLException {

        PreparedStatement stm = connection.prepareStatement(UPDATE_IMAGE_ID);
        stm.setInt(1, imageId);
        stm.setInt(2, itemId);
        stm.executeUpdate();

    }

    public void add(Item item) throws SQLException {

        PreparedStatement stm =  connection.prepareStatement(
                "INSERT INTO items (user_id, name, price, description, category)" +
                        "VALUES (?, ?, ?, ?, ?)"
        );

        stm.setInt(1, item.getUserId());
        stm.setString(2, item.getName());
        stm.setInt(3, item.getPrice());
        stm.setString(4, item.getDescription());
        stm.setObject(5, item.getCategory().name());
        stm.executeUpdate();
    }
    public void add(Item item, byte[] image) throws SQLException {
        connection.createStatement().execute("SET FOREIGN_KEY_CHECKS=0");

        PreparedStatement stm =  connection.prepareStatement(
                "INSERT INTO items (user_id, name, price, description, category, image_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)"
        );

        stm.setInt(1, item.getUserId());
        stm.setString(2, item.getName());
        stm.setInt(3, item.getPrice());
        stm.setString(4, item.getDescription());
        stm.setObject(5, item.getCategory().name());
        stm.setInt(6, 0);
        stm.executeUpdate();
        stm.close();


        connection.createStatement().execute("SET FOREIGN_KEY_CHECKS=1");

        ResultSet rs = connection.createStatement().executeQuery("SELECT LAST_INSERT_ID()");
        rs.next();
        int itemId = rs.getInt(1);

        if (image != null) {
            ImageDao imageDao = new ImageDao(connection);
            int image_id = imageDao.addImage(itemId, image);

            PreparedStatement stm2 = connection.prepareStatement("UPDATE items SET image_id = ? WHERE id = ?");
            stm2.setInt(1, image_id);
            stm2.setInt(2, itemId);
            stm2.execute();
            stm2.close();
        }
    }

    public void remove(Item item) throws SQLException{
        int itemId = item.getId();

        ImageDao imageDao = new ImageDao(connection);
        imageDao.removeWithItemId(itemId);

        PreparedStatement stm = connection.prepareStatement(
                "DELETE FROM items WHERE id = ?"
        );

        stm.setInt(1,itemId);
        stm.executeUpdate();
    }
    public void remove(int itemId) throws SQLException{

        ImageDao imageDao = new ImageDao(connection);
        imageDao.removeWithItemId(itemId);

        PreparedStatement stm = connection.prepareStatement(
                "DELETE FROM items WHERE id = ?"
        );

        stm.setInt(1,itemId);
        stm.executeUpdate();

    }

    public void removeByUserID(int userID) throws SQLException{

        for (Item item : getItemsByUserId(userID)) {
            ImageDao imageDao = new ImageDao(connection);
            imageDao.removeWithItemId(item.getId());
        }

        PreparedStatement stm = connection.prepareStatement(DELETE_ITEMS_WITH_USER_ID);
        stm.setInt(1, userID);

        stm.executeUpdate();
    }

    public void updateName(int itemId, String newName) throws SQLException {

        PreparedStatement stm = connection.prepareStatement(UPDATE_NAME);
        stm.setString(1,newName);
        stm.setInt(2, itemId);
        stm.executeUpdate();

    }
    public void updateCategory(int itemId, Category newCategory) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(UPDATE_CATEGORY);
        stm.setString(1, newCategory.toString());
        stm.setInt(2, itemId);
        stm.executeUpdate();

    }
    public void updateDescription(int itemId, String newDescription) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(UPDATE_DESCRIPTION);
        stm.setString(1, newDescription);
        stm.setInt(2, itemId);
        stm.executeUpdate();

    }
    public void updatePrice(int itemId, int newPrice) throws SQLException {

        PreparedStatement stm = connection.prepareStatement(UPDATE_PRICE);
        stm.setInt(1,newPrice);
        stm.setInt(2, itemId);
        stm.executeUpdate();

    }

    public int getUserIDByItemID(int itemId) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(SELECT_ITEMS_WITH_ID);
        stm.setInt(1, itemId);
        ResultSet resultSet = stm.executeQuery();

        resultSet.next();
        return resultSet.getInt(2);
    }

    public int getUserIDByItem(Item item) throws SQLException{

        int itemId = item.getId();

        PreparedStatement stm = connection.prepareStatement(SELECT_ITEMS_WITH_ID);
        stm.setInt(1, itemId);
        ResultSet resultSet = stm.executeQuery();

        resultSet.next();
        return resultSet.getInt(2);
    }

    public Item getItemByItemID(int itemId) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(SELECT_ITEMS_WITH_ID);
        stm.setInt(1, itemId);
        ResultSet resultSet = stm.executeQuery();

        resultSet.next();
        return getItemFromRow(resultSet);
    }

    public List<Item> getItemsByUsername(String username) throws SQLException {

        List<Item> itemList = new ArrayList<>();

        PreparedStatement stm = connection.prepareStatement(SELECT_ITEMS_OF_USER_TO_SELL);

        stm.setString(1, username);

        ResultSet resultSet = stm.executeQuery();

        while (resultSet.next()) {
            itemList.add(getItemFromRow(resultSet));
        }

        return itemList;
    }


    public List<Item> getItemsByUserId(int userId) throws SQLException {
        UserDao userDao = new UserDao(connection);
        return getItemsByUsername(userDao.getUserById(userId).getUsername());
    }

    public List<Item> getItemsForUserInWishlist(int id) throws SQLException {
        List<Item> itemsInWishlist = new ArrayList<>();

        PreparedStatement stm = connection.prepareStatement(SELECT_ITEMS_FOR_USER_IN_WISHLIST);

        stm.setInt(1, id);

        ResultSet resultSet = stm.executeQuery();

        while (resultSet.next()) {
            itemsInWishlist.add(getItemFromRow(resultSet));
        }

        return itemsInWishlist;
    }

    public List<Item> getFilteredItems(String itemName, Category category) throws SQLException{

        List<Item> filteredList = new ArrayList<>();

        PreparedStatement stm = connection.prepareStatement(SELECT_FILTERED_ITEMS);

        if (itemName.equals("")){
            itemName = "%";
        }
        stm.setString(1,itemName);

        if (category == null){
            stm.setString(2, "");
        }else{
            stm.setString(2, category.name());
        }

        ResultSet resultSet = stm.executeQuery();
        while(resultSet.next()){
            filteredList.add(getItemFromRow(resultSet));
        }

        if (filteredList.size() == 0){
            return Collections.emptyList();
        }

        return filteredList;
    }

    private Item getItemFromRow(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int userId = resultSet.getInt("user_id");
        String name = resultSet.getString("name");
        int price = resultSet.getInt("price");
        String description = resultSet.getString("description");
        Category category = Category.valueOf(resultSet.getString("category"));
        int imageId = resultSet.getInt("image_id");

        return new Item(id, userId, name, price, description, category, imageId);
    }
}
