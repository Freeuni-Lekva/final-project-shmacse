package schmacse.model;

public class WishList {

    private int userId;
    private int itemId;

    public WishList(int userId, int itemId) {
        this.userId = userId;
        this.itemId = itemId;
    }

    public int getUserId() {
        return userId;
    }

    public int getItemId() {
        return itemId;
    }

    @Override
    public String toString() {
        return "WishList{" +
                "userId=" + userId +
                ", itemId=" + itemId +
                '}';
    }
}
