package schmacse.daos;

import schmacse.model.Category;
import schmacse.model.Item;
import schmacse.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDao {

    private static final String SELECT_ITEMS_OF_USER_TO_SELL = "SELECT * FROM items " +
            "JOIN users WHERE users.id = items.user_id AND users.username = ?";
    private static final String DELETE_ITEMS_WITH_USER_ID = "" +
            "DELETE FROM items WHERE user_id = ?";
    private static final String SELECT_ITEMS_WITH_ID = "SELECT * FROM items " +
            "WHERE id = ?";
    private static final String UPDATE_PRICE = "UPDATE items SET price = ? WHERE id = ?";
    private static final String SELECT_FILTERED_ITEMS = "SELECT * FROM items " +
            "WHERE name LIKE CONCAT('%', ?, '%') AND category LIKE CONCAT('%', ?, '%')";

    private Connection connection;

    public ItemDao(Connection connection){
        this.connection = connection;
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

    public void remove(Item item) throws SQLException{

        int itemId = item.getId();

        PreparedStatement stm = connection.prepareStatement(
                "DELETE FROM items WHERE id = ?"
        );

        stm.setInt(1,itemId);
        stm.executeUpdate();

    }
    public void remove(int itemId) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(
                "DELETE FROM items WHERE id = ?"
        );

        stm.setInt(1,itemId);
        stm.executeUpdate();

    }

    public void removeByUserID(int userID) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(DELETE_ITEMS_WITH_USER_ID);
        stm.setInt(1, userID);

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
        return new Item(resultSet.getInt(1), resultSet.getInt(2),
                resultSet.getString(3), resultSet.getInt(4),
                resultSet.getString(5),Category.valueOf(resultSet.getString(6)));

    }

    public List<Item> getItemsByUsername(String username) throws SQLException {

        List<Item> itemList = new ArrayList<>();

        PreparedStatement stm = connection.prepareStatement(SELECT_ITEMS_OF_USER_TO_SELL);

        stm.setString(1, username);

        ResultSet resultSet = stm.executeQuery();

        while (resultSet.next()) {

            int id = resultSet.getInt("id");
            int userId = resultSet.getInt("user_id");
            String name = resultSet.getString("name");
            int price = resultSet.getInt("price");
            String description = resultSet.getString("description");
            Category category = Category.valueOf(resultSet.getString("category"));

            Item newItem = new Item(id, userId, name, price, description, category);

            itemList.add(newItem);
        }

        return itemList;
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
            int id = resultSet.getInt("id");
            int userId = resultSet.getInt("user_id");
            int price = resultSet.getInt("price");
            itemName = resultSet.getString("name");
            String description = resultSet.getString("description");
            category = Category.valueOf(resultSet.getString("category"));

            Item newItem = new Item(id, userId, itemName, price, description, category);
            filteredList.add(newItem);
        }

        return filteredList;
    }

}
