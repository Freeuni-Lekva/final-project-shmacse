package schmacse.model;

public class Rating {
    private int rating_id;
    private int user_id;
    private int rating;

    public Rating(int rating_id, int user_id, int rating) {};

    public int getUser_id() {
        return user_id;
    }

    public int getRating() {
        return rating;
    }

    public int getRating_id() {
        return rating_id;
    }

    public void setRating_id(int rating_id) {
        this.rating_id = rating_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}