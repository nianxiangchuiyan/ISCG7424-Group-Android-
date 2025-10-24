package com.dinecraft.app;

public class Food {
    private String id;
    private String name;
    private String category;
    private String description;
    private double price;

    public Food() {} // Firestore needs this

    public Food(String id, String name, String category, String description, double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
    }

    // getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
}
