package schmacse.daos;

import schmacse.model.Item;
import schmacse.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class WishListDao {

    private final String INSERT_INTO_WISHLIST = "INSERT INTO wishlist (user_id, item_id) VALUES (?,?)";
    private final String DELETE_ROW = "DELETE FROM wishlist WHERE user_id = ? AND item_id = ?";
    private final String DELETE_ROW_WITH_USER = "DELETE FROM wishlist WHERE user_id = ?";
    private final String DELETE_ROW_WITH_ITEM = "DELETE FROM wishlist WHERE item_id = ?";


    Connection connection;
    public WishListDao(Connection connection){
        this.connection = connection;
    }

    public void add(User user, Item item) throws SQLException {

        int userID = user.getId();
        int itemID = item.getId();

        PreparedStatement stm = connection.prepareStatement(INSERT_INTO_WISHLIST);
        stm.setInt(1,userID);
        stm.setInt(2,itemID);

        stm.executeUpdate();

    }
    public void add(int userID, int itemID) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(INSERT_INTO_WISHLIST);
        stm.setInt(1,userID);
        stm.setInt(2,itemID);

        stm.executeUpdate();

    }

    public void remove(User user, Item item) throws SQLException{

        int userID = user.getId();
        int itemID = item.getId();

        PreparedStatement stm = connection.prepareStatement(DELETE_ROW);
        stm.setInt(1, userID);
        stm.setInt(2, itemID);
        stm.executeUpdate();

    }
    public void remove(int userID, int itemID) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(DELETE_ROW);
        stm.setInt(1, userID);
        stm.setInt(2, itemID);
        stm.executeUpdate();

    }

    public void removeRowsOfUser(User user) throws SQLException{

        int userID = user.getId();

        PreparedStatement stm = connection.prepareStatement(DELETE_ROW_WITH_USER);
        stm.setInt(1, userID);
        stm.executeUpdate();

    }
    public void removeRowsOfUser(int userID) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(DELETE_ROW_WITH_USER);
        stm.setInt(1, userID);
        stm.executeUpdate();

    }

    public void remvoeRowsOfItem(Item item) throws SQLException{

        int itemID = item.getId();

        PreparedStatement stm = connection.prepareStatement(DELETE_ROW_WITH_ITEM);
        stm.setInt(1, itemID);

        stm.executeUpdate();

    }
    public void remvoeRowsOfItem(int itemID) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(DELETE_ROW_WITH_ITEM);
        stm.setInt(1, itemID);

        stm.executeUpdate();

    }
}
