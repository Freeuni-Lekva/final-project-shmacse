package schmacse.daos;

import schmacse.model.Item;
import schmacse.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WishListDao {

    private final String INSERT_INTO_WISHLIST = "INSERT INTO wishlist (user_id, item_id) VALUES (?,?)";
    private final String DELETE_ROW = "DELETE FROM wishlist WHERE user_id = ? AND item_id = ?";
    private final String DELETE_ROW_WITH_USER = "DELETE FROM wishlist WHERE user_id = ?";
    private final String DELETE_ROW_WITH_ITEM = "DELETE FROM wishlist WHERE item_id = ?";
    private final String FIND_IN_WISHLIST = "SELECT * FROM wishlist WHERE user_id = ? AND item_id = ?";


    Connection connection;
    public WishListDao(Connection connection){
        this.connection = connection;
    }

    // returns true if added succesfully
    public boolean add(User user, Item item) throws SQLException {

        int userID = user.getId();
        int itemId = item.getId();

        if(checkValidity(userID, itemId)) {
            PreparedStatement stm = connection.prepareStatement(INSERT_INTO_WISHLIST);
            stm.setInt(1, userID);
            stm.setInt(2, itemId);

            stm.executeUpdate();

            return true;
        }

        return false;

    }
    public boolean add(int userID, int itemId) throws SQLException{

        if(checkValidity(userID, itemId)) {
            PreparedStatement stm = connection.prepareStatement(INSERT_INTO_WISHLIST);
            stm.setInt(1, userID);
            stm.setInt(2, itemId);

            stm.executeUpdate();

            return true;
        }

        return false;
    }

    // checks if user owns the item
    private boolean checkValidity(int userID, int itemId) throws SQLException {

        ItemDao itemDao = new ItemDao(connection);
        Item item = itemDao.getItemByItemID(itemId);

        return userID != item.getUserId();

    }

    public void remove(User user, Item item) throws SQLException{

        int userID = user.getId();
        int itemId = item.getId();

        PreparedStatement stm = connection.prepareStatement(DELETE_ROW);
        stm.setInt(1, userID);
        stm.setInt(2, itemId);
        stm.executeUpdate();

    }
    public void remove(int userID, int itemId) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(DELETE_ROW);
        stm.setInt(1, userID);
        stm.setInt(2, itemId);
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

    public boolean hasItemInWishlist(int userId, int itemId) throws SQLException{
        PreparedStatement stm = connection.prepareStatement(FIND_IN_WISHLIST);
        stm.setInt(1, userId);
        stm.setInt(2, itemId);
        return stm.executeQuery().next();
    }

    public void removeRowsOfItem(Item item) throws SQLException{

        int itemId = item.getId();

        PreparedStatement stm = connection.prepareStatement(DELETE_ROW_WITH_ITEM);
        stm.setInt(1, itemId);

        stm.executeUpdate();

    }
    public void removeRowsOfItem(int itemId) throws SQLException{

        PreparedStatement stm = connection.prepareStatement(DELETE_ROW_WITH_ITEM);
        stm.setInt(1, itemId);

        stm.executeUpdate();

    }
}
