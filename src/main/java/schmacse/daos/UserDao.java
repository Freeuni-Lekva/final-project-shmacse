package schmacse.daos;

import schmacse.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private static final String ADD_USER = "INSERT INTO users" +
            "(first_name, last_name, username, phone_number, password)" +
            " VALUES (?,?,?,?,?)";
    private static final String SELECT_USER_BY_USERNAME_AND_PASSWORD = "SELECT * FROM users " +
            "WHERE username = ? and password = ?";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE id = ?";


    private Connection connection;

    public UserDao(Connection connection){
        this.connection = connection;
    }

    public void add(User user) throws SQLException {

        PreparedStatement stm = connection.prepareStatement(ADD_USER);

        stm.setString(1, user.getFirstName());
        stm.setString(2, user.getLastName());
        stm.setString(3,user.getUsername());
        stm.setString(4,user.getPhoneNumber());
        stm.setString(5, user.getPassword());

        stm.executeUpdate();
    }

    public User getUserByUsernameAndPassword(String username, String password) throws SQLException {

        PreparedStatement stm = connection.prepareStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD);

        stm.setString(1,username);
        stm.setString(2,password);

        ResultSet rs = stm.executeQuery();
        if(!rs.next()) return null;

        return new User(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getString(6)
            );

    }

    public  User getUserById(int id) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(SELECT_USER_BY_ID);

        stm.setInt(1, id);

        ResultSet rs = stm.executeQuery();
        if(!rs.next()) return null;

        return new User(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getString(5),
                rs.getString(6)
        );
    }

}
