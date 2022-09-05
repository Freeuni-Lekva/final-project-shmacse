package schmacse.model;


public class Item {

    private int id;
    private int userId;
    private int price;
    private String name;
    private String description;
    private Category category;

    public Item(int id, int userId, String name, int price, String description, Category category) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public static int comparePrice(Item a, Item b) {
        return a.getPrice() - b.getPrice();
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", category=" + category +
                '}';
    }
}
