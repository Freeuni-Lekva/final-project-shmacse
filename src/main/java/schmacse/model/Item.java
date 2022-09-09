package schmacse.model;


import schmacse.daos.ImageDao;

import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Item {

    private int id;
    private int userId;
    private int price;
    private String name;
    private String description;
    private Category category;
    private int imageId;

    public Item(int id, int userId, String name, int price, String description, Category category) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.imageId = 0;
    }


    public Item(int id, int userId, String name, int price, String description, Category category, int imageId) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.imageId = imageId;
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

    public int getImageId() {
        return imageId;
    }


    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public byte[] getImage(Connection connection) {
        ImageDao imageDao = new ImageDao(connection);
        try {
            return imageDao.getImage(imageId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
