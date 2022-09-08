package schmacse.daos;
import schmacse.model.Category;
import schmacse.model.Item;
import schmacse.model.Rating;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRatingDao {
    private Connection connection;

    private static final String ADD_RATING = "INSERT INTO rating" +
            "(user_id, rating_id, rating)" + " VALUES (?,?,?)";

    private static final String SELECT_RATINGS_OF_USER = "SELECT * FROM rating " +
            "JOIN users WHERE users.id = rating.user_id AND users.id = ?";

    public UserRatingDao(Connection connection){
        this.connection = connection;
    }

    //TODO: change return type to boolean?
    public void addReview(Rating rating) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(ADD_RATING);

        stm.setInt(1, rating.getUser_id());
        stm.setInt(2, rating.getRating_id());
        stm.setInt(3, rating.getRating());

        stm.executeUpdate();
    }

    public List<Rating> getReviewsByUserId(int user_id) throws SQLException {
        List<Rating> ratingList = new ArrayList<>();

        PreparedStatement stm = connection.prepareStatement(SELECT_RATINGS_OF_USER);

        stm.setInt(1, user_id);

        ResultSet resultSet = stm.executeQuery();

        while (resultSet.next()) {

            int id = resultSet.getInt("rating_id");
            int userId = resultSet.getInt("user_id");
            int rating = resultSet.getInt("rating");
            ratingList.add(new Rating(id, userId, rating));
        }

        return ratingList;
    }
}
