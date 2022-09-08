package schmacse.daos;
import schmacse.model.Category;
import schmacse.model.Item;
import schmacse.model.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserReviewDao {
    private Connection connection;

    private static final String ADD_REVIEW = "INSERT INTO review" +
            "(user_id, review_id, review_str)" + " VALUES (?,?,?)";

    private static final String SELECT_REVIEWS_OF_USER = "SELECT * FROM review " +
            "JOIN users WHERE users.id = review.user_id AND users.id = ?";

    public UserReviewDao(Connection connection){
        this.connection = connection;
    }

    //TODO: change return type to boolean?
    public void addReview(Review review) throws SQLException {
        PreparedStatement stm = connection.prepareStatement(ADD_REVIEW);

        stm.setInt(1, review.getUser_id());
        stm.setInt(2, review.getReview_id());
        stm.setString(3,review.getComment());

        stm.executeUpdate();
    }

    public List<Review> getReviewsByUserId(int user_id) throws SQLException {
        List<Review> reviewsList = new ArrayList<>();

        PreparedStatement stm = connection.prepareStatement(SELECT_REVIEWS_OF_USER);

        stm.setInt(1, user_id);

        ResultSet resultSet = stm.executeQuery();

        while (resultSet.next()) {

            int id = resultSet.getInt("review_id");
            int userId = resultSet.getInt("user_id");
            String comment = resultSet.getString("review_str");
            reviewsList.add(new Review(id, userId, comment));
        }

        return reviewsList;
    }
}
