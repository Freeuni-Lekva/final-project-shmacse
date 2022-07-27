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

    private static final String SELECT_ITEMS_BY_USER_ID = "SELECT * FROM items WHERE user_id = ?";

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

    public List<Item> getItemsByUserId(int userId) throws SQLException {

        List<Item> itemList = new ArrayList<>();

        PreparedStatement stm = connection.prepareStatement(SELECT_ITEMS_BY_USER_ID);

        stm.setInt(1, userId);

        ResultSet resultSet = stm.executeQuery();

        while (resultSet.next()) {

            int id = resultSet.getInt("id");
            int currUserId = resultSet.getInt("user_id");
            String name = resultSet.getString("name");
            String description = resultSet.getString("description");
            Category category = getCategory(resultSet.getString("category"));

            Item newItem = new Item(id, currUserId, name, description, category);

            itemList.add(newItem);
        }

        return itemList;
    }

    private Category getCategory(String category) {

        switch (category.toLowerCase()) {
            case "trousers":
                return Category.TROUSERS;
            case "isgood":
                return Category.ISGOOD;
            case "saul":
                return Category.SAUL;
            case "anddrive":
                return Category.ANDDRIVE;
        }

        return null;
    }

}
