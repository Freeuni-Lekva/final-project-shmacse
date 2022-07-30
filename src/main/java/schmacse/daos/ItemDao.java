package schmacse.daos;

import schmacse.model.Category;
import schmacse.model.Item;

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
    private static final String SELECT_ITEMS_WITH_USERID = "SELECT * FROM items " +
            "WHERE id = ?";

    private Connection connection;

    public ItemDao(Connection connection){
        this.connection = connection;
    }

    public void add(Item item) throws SQLException {

        PreparedStatement stm =  connection.prepareStatement(
                "INSERT INTO items (user_id, name, description, category)" +
                        "VALUES (?, ?, ?, ?)"
        );

        stm.setInt(1, item.getUserId());
        stm.setString(2, item.getName());
        stm.setString(3, item.getDescription());
        stm.setObject(4, item.getCategory().name());
        stm.executeUpdate();
    }

    public void remove(Item item) throws SQLException{

        int itemID = item.getId();

        PreparedStatement stm = connection.prepareStatement(
                "DELETE FROM items WHERE id = ?"
        );

        stm.setInt(1,itemID);
        stm.executeUpdate();

    }
    public void remove(int itemID) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(
                "DELETE FROM items WHERE id = ?"
        );

        stm.setInt(1,itemID);
        stm.executeUpdate();

    }

    public void removeByUserID(int userID) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(DELETE_ITEMS_WITH_USER_ID);
        stm.setInt(1, userID);

        stm.executeUpdate();
    }

    public int getUserIDByItemID(int itemID) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(SELECT_ITEMS_WITH_USERID);
        stm.setInt(1, itemID);
        ResultSet resultSet = stm.executeQuery();

        resultSet.next();
        return resultSet.getInt(2);

    }
    public int getUserIDByItem(Item item) throws SQLException{

        int itemID = item.getId();

        PreparedStatement stm = connection.prepareStatement(SELECT_ITEMS_WITH_USERID);
        stm.setInt(1, itemID);
        ResultSet resultSet = stm.executeQuery();

        resultSet.next();
        return resultSet.getInt(2);

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
            String description = resultSet.getString("description");
            Category category = Category.valueOf(resultSet.getString("category"));

            Item newItem = new Item(id, userId, name, description, category);

            itemList.add(newItem);
        }

        return itemList;
    }

}
