package schmacse.daos;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ImageDao {
    private final Connection connection;

    public ImageDao(Connection connection) {
        this.connection = connection;
    }

    public int addImage(int itemId, byte[] image) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("INSERT INTO item_images (item_id, image) VALUES (?, ?)");
        stm.setInt(1, itemId);
        stm.setBytes(2, image);
        stm.execute();
        stm.close();

        ResultSet rs = connection.createStatement().executeQuery("SELECT LAST_INSERT_ID()");
        rs.next();
        return rs.getInt(1);
    }

    public void removeWithItemId(int itemId) throws SQLException {
        connection.createStatement().execute("SET FOREIGN_KEY_CHECKS=0");

        PreparedStatement stm = connection.prepareStatement("DELETE FROM item_images WHERE item_id = ?");
        stm.setInt(1, itemId);
        stm.execute();
        stm.close();

        connection.createStatement().execute("SET FOREIGN_KEY_CHECKS=1");
    }

    public byte[] getImage(int itemId) throws SQLException {
        PreparedStatement stm = connection.prepareStatement("SELECT image FROM item_images WHERE id = ?");
        stm.setInt(1, itemId);
        ResultSet rs = stm.executeQuery();
        rs.next();

        return rs.getBytes("image");
    }
}
