package schmacse.daos;

import schmacse.model.Category;
import schmacse.model.Item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ItemDao {

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

}
