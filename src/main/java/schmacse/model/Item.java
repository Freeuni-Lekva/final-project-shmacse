package schmacse.model;

public class Item {

    private int id;
    private int userId;
    private String name;
    private String description;
    private Category category;

    public Item(int id, int userId, String name, String description, Category category) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                '}';
    }
}
