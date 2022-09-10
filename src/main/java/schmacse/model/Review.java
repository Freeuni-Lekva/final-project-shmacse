package schmacse.model;

public class Review {
    private int review_id;
    private int user_id;
    private String comment;

    public Review(int review_id, int user_id, String comment){
        this.review_id = review_id;
        this.user_id = user_id;
        this.comment = comment;
    }

    public Review(int user_id, String comment){
        this.user_id = user_id;
        this.comment = comment;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getReview_id(){
        return review_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setReview_id(int review_id){ this.review_id = review_id; }
}
