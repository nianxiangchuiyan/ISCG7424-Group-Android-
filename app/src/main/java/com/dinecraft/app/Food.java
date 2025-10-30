package com.dinecraft.app;

public class Food {
    private String id;
    private String name;
    private String category;
    private String description;
    private double price;
    private String imageUrl;

    public Food() {} // Firestore requires empty

    public Food(String id, String name, String category, String description, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}
